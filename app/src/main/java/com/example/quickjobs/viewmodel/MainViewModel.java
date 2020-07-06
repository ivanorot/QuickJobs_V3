package com.example.quickjobs.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.repos.Repository;
import com.example.quickjobs.model.source.UserSource;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainViewModel extends AndroidViewModel {

    private Repository repository;
    private MutableLiveData<User> currentUserMutableLiveData;
    private MutableLiveData<Boolean> setRestrictionsMutableLiveData;

    private UserSource userSource;

    public MainViewModel(@NonNull Application application) {
        super(application);

        repository = Repository.getInstance();

        currentUserMutableLiveData = new MutableLiveData<>();
        setRestrictionsMutableLiveData = new MutableLiveData<>();

        userSource = new UserSource(FirebaseFirestore.getInstance());
    }

    public LiveData<User> observeCurrentUserLiveData(){
        return currentUserMutableLiveData;
    }
    public void setCurrentUserLiveData(User user){
        currentUserMutableLiveData.setValue(user);
    }

    public LiveData<Boolean> observeRestrictionsForAnonymousUser(){
        return setRestrictionsMutableLiveData;
    }
    public void setRestrictionsForAnonymousUser(boolean setRestrictions){
        setRestrictionsMutableLiveData.setValue(setRestrictions);
    }

}
