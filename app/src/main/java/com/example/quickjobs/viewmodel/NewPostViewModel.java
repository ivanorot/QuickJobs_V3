package com.example.quickjobs.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.QuickJob;
import com.example.quickjobs.model.User;
import com.example.quickjobs.repos.MainRepository;

public class NewPostViewModel extends AndroidViewModel {

    MainRepository mainRepository;

    MutableLiveData<Boolean> isJobPostComplete = new MutableLiveData<>();

    MutableLiveData<QuickJob> quickJob = new MutableLiveData<>();
    MutableLiveData<QuickJob> completedJob = new MutableLiveData<>();

    public LiveData<User> currentUser;

    public NewPostViewModel(@NonNull Application application) {
        super(application);

        mainRepository = MainRepository.getInstance();
    }

    public void initCurrentUser(){
        currentUser = mainRepository.getCurrentUserMutableLiveData();
    }

    public LiveData<Boolean> getIsJobPostComplete() {
        return isJobPostComplete;
    }

    public void setIsJobPostComplete(Boolean inIsJobPostComplete) {
        isJobPostComplete.setValue(inIsJobPostComplete);
    }

}
