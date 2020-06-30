package com.example.quickjobs.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.Repository;
import com.example.quickjobs.model.beans.User;
import com.google.firebase.auth.AuthCredential;

public class AuthViewModel extends AndroidViewModel {
    private Repository repository;
    private MutableLiveData<User> authenticatedUser;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);
        authenticatedUser = new MutableLiveData<>();

    }

    public LiveData<User> authenticatedUserLiveData(){
        return authenticatedUser;
    }

    public void signInWithGoogle(AuthCredential googleAuthCredential){
        authenticatedUser = repository.firebaseSignInWithGoogle(googleAuthCredential);
    }

    public void createUser(User authenticatedUser){
        authenticatedUser = repository.
    }
}
