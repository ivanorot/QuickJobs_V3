package com.example.quickjobs.model.repos;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.source.UserSource;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthRepository {
    private final String USER_COLLECTION_NAME = "users";

    private final static String TAG = "AuthRepository";

    private UserSource userSource;

    private FirebaseFirestore firebaseFirestore;

    private CollectionReference collectionReference;

    public AuthRepository(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection(USER_COLLECTION_NAME);
        userSource = UserSource.getInstance(firebaseFirestore);
    }

    public MutableLiveData<User> firebaseGenericSignIn(IdpResponse response){
        return  userSource.firebaseGenericSignIn(response);
    }

    public MutableLiveData<User> createUserInFireBaseIfNotExists(User authenticatedUser){
        return userSource.createUserInFireBaseIfNotExists(authenticatedUser);
    }
}