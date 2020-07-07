package com.example.quickjobs.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.repos.SplashRepository;

public class SplashViewModel extends AndroidViewModel {
    private SplashRepository splashRepository;

    public LiveData<User> isUserAuthenticatedLiveData;
    public LiveData<User> userLiveData;

    public SplashViewModel(@NonNull Application application) {
        super(application);

        splashRepository = SplashRepository.getInstance();

    }

    public void checkIfUserIsAuthenticated(){
        isUserAuthenticatedLiveData = splashRepository.checkIfUserIsAuthenticated();
    }

    public void setUid(String inUid){
        userLiveData = splashRepository.addUserToLiveData(inUid);
    }
}
