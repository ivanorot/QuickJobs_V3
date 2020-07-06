package com.example.quickjobs.model.source;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.User;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserSource {
    private final String USER_COLLECTION_NAME = "users";
    private final String TAG = "UserSource";


    @SuppressWarnings("FieldCanBeLocal")
    private CollectionReference userCollectionReference;
    private DocumentReference currentUserDocument;

//  Authentication
    private User currentUser;





    public UserSource(FirebaseFirestore firebaseFirestore) {
        currentUser = new User();
        userCollectionReference = firebaseFirestore.collection(USER_COLLECTION_NAME);
    }

    public MutableLiveData<User> getCurrentUser(){
        return new MutableLiveData<>(currentUser);
    }

    public MutableLiveData<User> firebaseAnonymousSignIn(IdpResponse response){
        MutableLiveData<User> anonymousUserMutableLiveData = new MutableLiveData<>();
        if(response != null){

        }
        return anonymousUserMutableLiveData;
    }

    public MutableLiveData<User> firebaseGenericSignIn(IdpResponse response){
        MutableLiveData<User> authenicatedUserMutableLiveData = new MutableLiveData<>();
        if(response != null){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if(response.isSuccessful()){
                if(firebaseUser != null) {
                    User user = new User(firebaseUser);
                    user.setNew(response.isNewUser());
                    user.setCreated(true);
                    authenicatedUserMutableLiveData.setValue(user);
                }
            }
            else{
                if(response.getError() != null){
                    logErrorMessage(response.getError());
                }
            }
        }

        return authenicatedUserMutableLiveData;
    }

    public MutableLiveData<User> createUserInFireBaseIfNotExists(User authenticatedUser){
        MutableLiveData<User> newUserMutableLiveData = new MutableLiveData<>();

        DocumentReference uidReference = userCollectionReference.document(authenticatedUser.getUid());

        uidReference.get().addOnCompleteListener(uidTask -> {
            if(uidTask.isSuccessful()){
                DocumentSnapshot snapshot = uidTask.getResult();
                if(snapshot != null && !snapshot.exists()){
                    uidReference.set(authenticatedUser).addOnCompleteListener(userCreationTask -> {
                        if(userCreationTask.isSuccessful()){

                            Log.println(Log.ERROR, TAG, authenticatedUser.getDisplayName());
                            authenticatedUser.setCreated(true);
                            newUserMutableLiveData.setValue(authenticatedUser);
                        }
                        else {
                            if(userCreationTask.getException() != null){
                                logErrorMessage(userCreationTask.getException());
                            }
                        }
                    });
                }
                else
                {
                    Log.println(Log.ERROR, TAG, authenticatedUser.getDisplayName());
                    User user = snapshot.toObject(User.class);
                    if(user != null){
                        newUserMutableLiveData.setValue(user);
                    }
                }
            }
            else{
                if(uidTask.getException() != null){
                    logErrorMessage(uidTask.getException());
                }
            }
        });
        return newUserMutableLiveData;
    }

    public MutableLiveData<User> checkIfUserIsAuthenticated(){
        MutableLiveData<User> isUserAuthenticatedInFirebaseMutableLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser == null){
            currentUser.setAuthenticated(false);
            isUserAuthenticatedInFirebaseMutableLiveData.setValue(currentUser);
        }
        else{
            currentUser.setUid(firebaseUser.getUid());
            currentUser.setAuthenticated(true);
            isUserAuthenticatedInFirebaseMutableLiveData.setValue(currentUser);
        }
        return isUserAuthenticatedInFirebaseMutableLiveData;
    }

    public MutableLiveData<User> addUserToLiveData(String inUid){
        Log.println(Log.ERROR, TAG, "addUserToLiveData()");
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        userCollectionReference.document(inUid).get().addOnCompleteListener(userTask -> {
            Log.println(Log.ERROR, TAG, "userTask isSuccessful : " + userTask.isSuccessful());
            if(userTask.isSuccessful()){
                DocumentSnapshot documentSnapshot = userTask.getResult();
                Log.println(Log.ERROR, TAG, "documentSnapshot exists: " + documentSnapshot.exists());
                if(documentSnapshot.exists()){
                    User user = documentSnapshot.toObject(User.class);
                    userMutableLiveData.setValue(user);
                }
            }
            else{
                if(userTask.getException() != null){
                    logErrorMessage(userTask.getException());
                }
            }
        });
        return userMutableLiveData;
    }

    public void logErrorMessage(Exception e){
        if(e.getMessage() != null){
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
    }
}
