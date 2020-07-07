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
    private final String TAG = "Quickjobs: UserSource: ";

    //Firebase Firestore
    private final String COLLECTION_NAME = "user";
    private CollectionReference collectionReference;
    private DocumentReference currentUserDocument;
    private FirebaseAuth firebaseAuth;

    private User user;


    public UserSource(FirebaseFirestore firebaseFirestore) {
        user = new User();
        firebaseAuth = FirebaseAuth.getInstance();
        collectionReference = firebaseFirestore.collection(COLLECTION_NAME);
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

        DocumentReference uidReference = collectionReference.document(authenticatedUser.getUid());

        uidReference.get().addOnCompleteListener(uidTask -> {
            if(uidTask.isSuccessful()){
                DocumentSnapshot snapshot = uidTask.getResult();
                if(snapshot != null && !snapshot.exists()){
                    uidReference.set(authenticatedUser).addOnCompleteListener(userCreationTask -> {
                        if(userCreationTask.isSuccessful()){
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
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null){
            user.setAuthenticated(false);
            isUserAuthenticatedInFirebaseMutableLiveData.setValue(user);
        }
        else{
            user.setUid(firebaseUser.getUid());
            user.setAuthenticated(true);
            isUserAuthenticatedInFirebaseMutableLiveData.setValue(user);
        }
        return isUserAuthenticatedInFirebaseMutableLiveData;
    }

    public MutableLiveData<User> addUserToLiveData(String inUid){
        Log.println(Log.ERROR, TAG, "addUserToLiveData");
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        collectionReference.document(inUid).get().addOnCompleteListener(userTask -> {
            Log.println(Log.ERROR, TAG, "userTask isSuccessful" + userTask.isSuccessful());
            if(userTask.isSuccessful()){
                DocumentSnapshot documentSnapshot = userTask.getResult();
                Log.println(Log.ERROR, TAG, "documentSnapshot exists: " + documentSnapshot.exists());
                if(documentSnapshot.exists()){
                    User user = documentSnapshot.toObject(User.class);
                    userMutableLiveData.setValue(user);
                }
                //todo snapshot.exists() is returning false;
            }
            else{
                if(userTask.getException() != null){
                    logErrorMessage(userTask.getException());
                }
            }
        });
        Log.println(Log.ERROR,TAG, "" + userMutableLiveData.getValue());
        return userMutableLiveData;
    }

    public void logErrorMessage(Exception e){
        if(e.getMessage() != null){
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
    }
}
