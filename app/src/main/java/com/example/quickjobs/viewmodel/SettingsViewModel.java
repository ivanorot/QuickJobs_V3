package com.example.quickjobs.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.preference.Preference;

import com.example.quickjobs.model.User;
import com.example.quickjobs.repos.SettingsRepository;
import com.example.quickjobs.source.SharedPreferencesManager;

public class SettingsViewModel extends AndroidViewModel {
    private SettingsRepository settingsRepository;
    private SharedPreferencesManager sharedPreferencesManager;

    public LiveData<User> currentUserLiveData;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        settingsRepository = SettingsRepository.getInstance(application);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(application);
    }

    public void initCurrentUser(){
        currentUserLiveData = settingsRepository.getCurrentUserMutableLiveData();
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
