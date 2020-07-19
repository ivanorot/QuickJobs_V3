package com.example.quickjobs.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.repos.AuthRepository;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.AuthCredential;

import io.reactivex.Observable;


public class AuthViewModel extends AndroidViewModel {
    private final String TAG = "AuthViewModel";
    private AuthRepository authRepository;

    public LiveData<User> authenticatedUserLiveData;
    public LiveData<User> createdUserLiveData;



    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = AuthRepository.getInstance();
    }

    public void signInDefault(IdpResponse firebaseResponse){
        Log.println(Log.ERROR, TAG, "signInWithGoogle()");
        authenticatedUserLiveData = authRepository.firebaseGenericSignIn(firebaseResponse);
    }

    public void createUser(User authenticatedUser){
        Log.println(Log.ERROR, TAG, "createUser()");
        createdUserLiveData = authRepository.createUserInFireBaseIfNotExists(authenticatedUser);
    }

}
