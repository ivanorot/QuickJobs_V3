package com.example.quickjobs.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.MainRepository;
import com.example.quickjobs.model.source.SharedPreferencesManager;

public class SettingsViewModel extends AndroidViewModel {
    private MainRepository mainRepository;
    private SharedPreferencesManager sharedPreferencesManager;

    public LiveData<User> currentUserLiveData;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        mainRepository = MainRepository.getInstance(application);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(application);
    }

    public void initCurrentUser(){
        currentUserLiveData = mainRepository.getCurrentUserMutableLiveData();
    }

    public void saveUserPreference(String preferenceKey, Boolean preferenceState){
        sharedPreferencesManager.updateUserPreferences(preferenceKey, preferenceState);
    }

    public void saveUserPreference(String preferenceKey, String preferenceState){
        sharedPreferencesManager.updateUserPreferences(preferenceKey, preferenceState);
    }

    public Boolean retrieveUserPreference(String preferenceKey, Boolean defaultValue){
        return sharedPreferencesManager.getValueFromUserPreferences(preferenceKey, defaultValue);
    }

}
