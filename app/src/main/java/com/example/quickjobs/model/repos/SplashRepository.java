package com.example.quickjobs.model.repos;

import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.QuickJob;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.source.JobSource;
import com.example.quickjobs.model.source.LocationSource;
import com.example.quickjobs.model.source.UserSource;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SplashRepository {
    private static SplashRepository Instance;

    private final int INITIAL_DISTNACE = 25;

    private UserSource userSource;
    private JobSource jobSource;

    private SplashRepository(){
        userSource = new UserSource(FirebaseFirestore.getInstance());
        jobSource = new JobSource(FirebaseFirestore.getInstance());
//        location = new LocationSource();
    }

    public static SplashRepository getInstance(){
        if(Instance == null){
            synchronized (SplashRepository.class){
                if(Instance == null){
                    Instance = new SplashRepository();
                }
            }
        }
        return Instance;
    }

    public MutableLiveData<User> checkIfUserIsAuthenticated(){
        return userSource.checkIfUserIsAuthenticated();
    }

    public MutableLiveData<User> addUserToLiveData(String inUid){
        return userSource.addUserToLiveData(inUid);
    }

    public MutableLiveData<List<QuickJob>> loadInitialJobsForUser(User user){
        return jobSource.getQuickJobsNearUserLocation(user.getLongitude(), user.getLatitude(), INITIAL_DISTNACE);
    }
}
