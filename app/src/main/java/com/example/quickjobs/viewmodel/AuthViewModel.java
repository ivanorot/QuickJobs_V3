package com.example.quickjobs.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.MainRepository;
import com.firebase.ui.auth.IdpResponse;


public class AuthViewModel extends AndroidViewModel {
    private final String TAG = "AuthViewModel";
    private MainRepository mainRepository;

    public LiveData<User> authenticatedUserLiveData;
    public LiveData<User> createdUserLiveData;



    public AuthViewModel(@NonNull Application application) {
        super(application);
        mainRepository = MainRepository.getInstance(application);
    }

}
