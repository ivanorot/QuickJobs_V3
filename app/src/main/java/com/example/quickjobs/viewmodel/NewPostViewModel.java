package com.example.quickjobs.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.QuickJob;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.MainRepository;

public class NewPostViewModel extends AndroidViewModel {

    MainRepository mainRepository;

    MutableLiveData<Boolean> isJobPostComplete = new MutableLiveData<>();

    MutableLiveData<QuickJob> quickJob = new MutableLiveData<>();
    MutableLiveData<QuickJob> completedJob = new MutableLiveData<>();

    public LiveData<User> currentUser;

    public NewPostViewModel(@NonNull Application application) {
        super(application);

        mainRepository = MainRepository.getInstance(application);
    }


}
