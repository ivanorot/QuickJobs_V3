package com.example.quickjobs.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.repos.SettingsRepository;

public class SettingsViewModel extends AndroidViewModel {
    private SettingsRepository settingsRepository;

    public LiveData<User> currentUserLiveData;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        settingsRepository = SettingsRepository.getInstance(application);
    }

    public void initCurrentUser(){
        currentUserLiveData = settingsRepository.getCurrentUserMutableLiveData();
    }

    public void resetCurrentUserEmailAndPersistToCloud(String Email){
//        settingsRepository.
    }
}
