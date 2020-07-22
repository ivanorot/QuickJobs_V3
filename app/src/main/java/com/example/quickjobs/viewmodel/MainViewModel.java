package com.example.quickjobs.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.repos.MainRepository;
import com.example.quickjobs.model.source.UserSource;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainViewModel extends AndroidViewModel {

    private MainRepository mainRepository;
    public LiveData<User> currentUserMutableLiveData;

    private UserSource userSource;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mainRepository = MainRepository.getInstance();
        currentUserMutableLiveData = new MutableLiveData<>();

    }

    public void initializeCurrentUser(){
        currentUserMutableLiveData = mainRepository.getCurrentUserMutableLiveData();
    }

    public void setCurrentUserAnonymous(User user){
        mainRepository.addAnonymousUserToSource(user);
    }

}
