package com.example.quickjobs.model.source;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.helper.ExceptionHandler;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserSource implements EventListener<DocumentSnapshot>{
    private final String USER_SOURCE_BACKGROUND_THREAD_NAME = "USER_SOURCE_BACKGROUND_THREAD";
    private final String USER_COLLECTION_NAME = "users";
    private final String TAG = "UserSource";

    private static UserSource Instance;

//  Threads
    private HandlerThread userSourceHandlerThread_Background;
    private Handler userSourceHandler_Background;

//  Authentication
    private DocumentReference currentUserDocument;
    private User currentUser;


//  LiveData
    public MutableLiveData<User> currentUserMutableLiveData;
    /*

     */
    private UserSource() {
        currentUserMutableLiveData = new MutableLiveData<>();
        initUserSourceThread();

    }

    public static UserSource getInstance() {
        if (Instance == null) {
            synchronized (UserSource.class) {
                Instance = new UserSource();
            }
        }
        return Instance;
    }

    public void initUserSourceThread(){
        userSourceHandlerThread_Background = new HandlerThread(USER_SOURCE_BACKGROUND_THREAD_NAME);
        userSourceHandlerThread_Background.start();
        userSourceHandler_Background = new Handler(userSourceHandlerThread_Background.getLooper());
    }

    public void terminateUserSourceThread(){
        userSourceHandlerThread_Background.quitSafely();
        try {

            userSourceHandlerThread_Background.join();
            userSourceHandlerThread_Background = null;
            userSourceHandler_Background = null;

        }catch (InterruptedException exception){
            ExceptionHandler.consumeException(TAG, exception);
        }
    }

    public void enableCurrentUserDocumentSnapshotListener(){
        if(currentUser != null && !currentUser.isAnonymous()){
            currentUserDocument.addSnapshotListener(this);
        }
    }

    public void disableCurrentUserDocumentSnapshotListener(){
        if(currentUser != null && !currentUser.isAnonymous()) {
            currentUserDocument.addSnapshotListener(this).remove();
        }
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
        if(error != null){
            ExceptionHandler.consumeException(TAG, error);
        }
        else{
            if(value != null){
                currentUserDocument = value.getReference();
                currentUser = value.toObject(User.class);
                currentUserMutableLiveData.postValue(currentUser);
            }
        }
    }

    /*

     */
    public MutableLiveData<User> checkIfUserIsAnonymousAndAuthenticated(){
        MutableLiveData<User> isUserAnonymousMutableLiveData = new MutableLiveData<>();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user;

        //nobody is signed in anonymously or authenticated
        if(firebaseUser == null){
            user = new User();
            user.setAuthenticated(false);
            user.setAnonymous(true);
        }
        //user is returnin to quick jobs and is therfore authenticated/ determines anonymity
        else{
            user = new User(firebaseUser);
            user.setAuthenticated(true);
            user.setAnonymous(firebaseUser.isAnonymous());
        }

        isUserAnonymousMutableLiveData.setValue(user);

        return isUserAnonymousMutableLiveData;
    }
    ////////



    /*

     */
    public MutableLiveData<User> getCurrentUserMutableLiveData(){
        return new MutableLiveData<>(currentUser);
    }

    public MutableLiveData<User> addAnonymousUserToLiveData(User anonymousUser){
        MutableLiveData<User> currentUserMutableLiveData = new MutableLiveData<>();

        if(anonymousUser != null){
            currentUser = anonymousUser;
            currentUserMutableLiveData.postValue(currentUser);
        }

        return currentUserMutableLiveData;
    }

    public MutableLiveData<User> addAuthenticatedUserToLiveData(String inUid){
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        currentUserDocument = FirebaseFirestore.getInstance().collection(USER_COLLECTION_NAME)
                .document(inUid);

        currentUserDocument.get().addOnCompleteListener(userTask -> {
            if(userTask.isSuccessful()){
                DocumentSnapshot documentSnapshot = userTask.getResult();
                if(documentSnapshot != null && documentSnapshot.exists()){
                    currentUser = documentSnapshot.toObject(User.class);
                    userMutableLiveData.postValue(currentUser);
                }
            }
            else{
                if(userTask.getException() != null){
                    ExceptionHandler.consumeException(TAG, userTask.getException());
                }
            }
        });
        return userMutableLiveData;
    }

    /*

     */
    @SuppressLint("RestrictedApi")
    public MutableLiveData<User> firebaseGenericSignIn(IdpResponse response){
        Log.println(Log.ERROR, TAG, "evaluating generic sign in response");
        MutableLiveData<User> authenicatedUserMutableLiveData = new MutableLiveData<>();
        if(response != null){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if(response.isSuccessful()){
                if(firebaseUser != null) {
                    currentUser = new User(firebaseUser);
                    currentUser.setNew(response.isNewUser());
                    currentUser.setCreated(true);
                    authenicatedUserMutableLiveData.postValue(currentUser);
                }
            }
            else{
                if(response.getError() != null){
                    ExceptionHandler.consumeException(TAG, response.getError());
                }
            }
        }

        return authenicatedUserMutableLiveData;
    }

    public MutableLiveData<User> firebaseAnonymousSignIn(){
        Log.println(Log.ERROR, TAG, "signing user in as anonymous");
        MutableLiveData<User> anonUserMutableLiveData = new MutableLiveData<>();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInAnonymously().addOnCompleteListener(anonTask -> {
            Log.println(Log.ERROR, TAG, "anonTask");
            if(anonTask.isSuccessful()){
                Log.println(Log.ERROR, TAG, "signed user in as anonymous");
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if(firebaseUser != null){
                    User user = new User(firebaseUser);
                    user.setAnonymous(true);
                    user.setAuthenticated(true);
                    anonUserMutableLiveData.postValue(user);
                }
            }else{
                User user = new User();
                user.setAnonymous(true);
                user.setAuthenticated(false);
                anonUserMutableLiveData.postValue(user);
            }
        });

        return anonUserMutableLiveData;
    }

    /*

     */
    public MutableLiveData<User> createUserInFireBaseIfNotExists(User authenticatedUser){
        MutableLiveData<User> newUserMutableLiveData = new MutableLiveData<>();

        CollectionReference userCollectionReference = FirebaseFirestore.getInstance().collection(USER_COLLECTION_NAME);
        DocumentReference uidReference = userCollectionReference.document(authenticatedUser.getUid());

        uidReference.get().addOnCompleteListener(uidTask -> {
            if(uidTask.isSuccessful()){
                DocumentSnapshot snapshot = uidTask.getResult();
                if(snapshot != null && !snapshot.exists()){
                    uidReference.set(authenticatedUser).addOnCompleteListener(userCreationTask -> {
                        if(userCreationTask.isSuccessful()){
                            authenticatedUser.setCreated(true);
                            newUserMutableLiveData.postValue(authenticatedUser);
                        }
                        else {
                            if(userCreationTask.getException() != null){
                                ExceptionHandler.consumeException(TAG, userCreationTask.getException());
                            }
                        }
                    });
                }
                else
                {
                    User user = snapshot.toObject(User.class);
                    if(user != null){
                        newUserMutableLiveData.setValue(user);
                    }
                }
            }
            else{
                if(uidTask.getException() != null){
                    ExceptionHandler.consumeException(TAG, uidTask.getException());
                }
            }
        });
        return newUserMutableLiveData;
    }

    public MutableLiveData<User> updateUserLocationDataAndPersist(Location location){

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        if(!currentUser.isAnonymous()){
            CollectionReference userCollectionReference = FirebaseFirestore.getInstance().collection(USER_COLLECTION_NAME);
            DocumentReference uidReference = userCollectionReference.document(currentUser.getUid());

            currentUser.setLongitude(longitude);
            currentUser.setLatitude(latitude);

            uidReference.update("longitude", longitude);
            uidReference.update("latitude", latitude);

        }
        else{
            currentUser.setLatitude(location.getLatitude());
            currentUser.setLongitude(location.getLongitude());
        }

        return new MutableLiveData<>(currentUser);
    }

    public MutableLiveData<User> updateUserWithMockLocationDataAndPersistToCloud(double latitude, double longitude){
        if(!currentUser.isAnonymous()){
            CollectionReference userCollectionReference = FirebaseFirestore.getInstance().collection(USER_COLLECTION_NAME);
            DocumentReference uidReference = userCollectionReference.document(currentUser.getUid());

            currentUser.setLongitude(longitude);
            currentUser.setLatitude(latitude);

            uidReference.update("longitude", longitude);
            uidReference.update("latitude", latitude);

        }
        else{
            currentUser.setLatitude(latitude);
            currentUser.setLongitude(longitude);
        }

        return new MutableLiveData<>(currentUser);
    }

    public void updateUserEmailInFirestore(String email){
        CollectionReference userCollectionReference = FirebaseFirestore.getInstance().collection(USER_COLLECTION_NAME);
        userCollectionReference.document(currentUser.getUid()).update("emailAddress", email).addOnCompleteListener(updateEmailTask -> {
           if(updateEmailTask.isSuccessful()){
               currentUser.setEmailAddress(email);
               currentUserMutableLiveData.setValue(currentUser);

               FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
               FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

               if(firebaseUser != null){
                   firebaseUser.updateEmail(email);
                   firebaseAuth.updateCurrentUser(firebaseUser);
               }
           }
        });
    }

}