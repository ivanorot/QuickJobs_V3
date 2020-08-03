package com.example.quickjobs.repos;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.User;
import com.example.quickjobs.source.LocationSource;
import com.example.quickjobs.source.SharedPreferencesManager;
import com.example.quickjobs.source.UserSource;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsRepository {
    private static SettingsRepository Instance;

    private UserSource userSource;
    private LocationSource locationSource;

    private SharedPreferencesManager sharedPreferencesManager;

    private SettingsRepository(Context context){
        userSource = UserSource.getInstance(FirebaseFirestore.getInstance());
        locationSource = LocationSource.getInstance(context);

        sharedPreferencesManager = SharedPreferencesManager.getInstance(context);
    }


    public static SettingsRepository getInstance(Context context){
        if(Instance == null){
            synchronized (SettingsRepository.class){
                Instance = new SettingsRepository(context);
            }
        }
        return Instance;
    }

    public MutableLiveData<User> getCurrentUserMutableLiveData(){
        return userSource.getCurrentUserMutableLiveData();
    }

    public void loadDefaultSettings(Context context){
        locationSource.setDefaultSettings(context);
    }

    public void loadLowFrequencySettings(Context context){
        locationSource.setLowFrequencySettings(context);
    }

}
