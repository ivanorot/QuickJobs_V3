package com.example.quickjobs.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.repos.AuthRepository;
import com.firebase.ui.auth.IdpResponse;


public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;

    public LiveData<User> authenticatedUserLiveData;
    public LiveData<User> createdUserLiveData;



    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = AuthRepository.getInstance();
    }

    public void signInDefault(IdpResponse firebaseResponse){
        authenticatedUserLiveData = authRepository.firebaseGenericSignIn(firebaseResponse);
    }

    public void createUser(User authenticatedUser){
        createdUserLiveData = authRepository.createUserInFireBaseIfNotExists(authenticatedUser);
    }

}
