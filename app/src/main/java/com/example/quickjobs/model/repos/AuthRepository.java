package com.example.quickjobs.model.repos;

import com.firebase.ui.auth.IdpResponse;
import androidx.lifecycle.MutableLiveData;
import com.example.quickjobs.model.beans.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.quickjobs.model.source.UserSource;

public class AuthRepository {

    private static AuthRepository INSTANCE;
    private UserSource userSource;


    private AuthRepository(){
        userSource = new UserSource(FirebaseFirestore.getInstance());
    }

    public static AuthRepository getInstance() {
        if(INSTANCE == null)
        {
            synchronized (AuthRepository.class)
            {
                if(INSTANCE == null){
                    INSTANCE = new AuthRepository();
                }
            }
        }
        return INSTANCE;
    }

    public MutableLiveData<User> firebaseGenericSignIn(IdpResponse response){
        return userSource.firebaseGenericSignIn(response);
    }

    public MutableLiveData<User> createUserInFireBaseIfNotExists(User authenticatedUser){
        return userSource.createUserInFireBaseIfNotExists(authenticatedUser);
    }
}