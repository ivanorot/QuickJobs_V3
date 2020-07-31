package com.example.quickjobs.repos;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.source.JobSource;
import com.example.quickjobs.model.source.LocationSource;
import com.example.quickjobs.model.source.UserSource;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsRepository {
    private static SettingsRepository Instance;

    private UserSource userSource;
    private JobSource jobSource;
    private LocationSource locationSource;

    private SettingsRepository(Context context){
        userSource = UserSource.getInstance(FirebaseFirestore.getInstance());
        jobSource = JobSource.getInstance(FirebaseFirestore.getInstance());
//        locationSource = LocationSource.getInstance(context);

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

    public void updateUserEmailInFirestore(){
//        userSource.updateUserEmailInFirestore();
    }

}
