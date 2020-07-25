package com.example.quickjobs.repos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.source.JobSource;
import com.example.quickjobs.model.source.LocationSource;
import com.example.quickjobs.model.source.UserSource;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainRepository {
    private final String TAG = "MainRepository";
    private static MainRepository INSTANCE;

    private UserSource userSource;
    private JobSource jobSource;
    private LocationSource locationSource;

    private MainRepository(){

        userSource = UserSource.getInstance(FirebaseFirestore.getInstance());
        jobSource = JobSource.getInstance(FirebaseFirestore.getInstance());

    }

    public static MainRepository getInstance()
    {
        if(INSTANCE == null)
        {
            synchronized (MainRepository.class)
            {
                if(INSTANCE == null)
                {
                    INSTANCE = new MainRepository();
                }
            }
        }
        return INSTANCE;
    }

    public MutableLiveData<User> getCurrentUserMutableLiveData(){
        Log.println(Log.ERROR, TAG, "getCurrentUserMutableLiveData");
        return userSource.getCurrentUserMutableLiveData();
    }

    public void addAnonymousUserToSource(User anonymousUser){
        userSource.addAnonymousUserToLiveData(anonymousUser);
    }
}
