package com.example.quickjobs.model.repos;

import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.source.UserSource;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashRepository {
    private static SplashRepository Instance;
    private UserSource userSource;

    private SplashRepository(){
        userSource = new UserSource(FirebaseFirestore.getInstance());
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
}
