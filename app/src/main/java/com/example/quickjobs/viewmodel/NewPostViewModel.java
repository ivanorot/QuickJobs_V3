package com.example.quickjobs.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.QuickJob;
import com.example.quickjobs.repos.MainRepository;

public class NewPostViewModel extends AndroidViewModel {

    MainRepository mainRepository;

    MutableLiveData<Boolean> isJobPostComplete = new MutableLiveData<>();

    MutableLiveData<QuickJob> quickJob = new MutableLiveData<>();
    MutableLiveData<QuickJob> completedJob = new MutableLiveData<>();

    public NewPostViewModel(@NonNull Application application) {
        super(application);

        mainRepository = MainRepository.getInstance();
    }

    public MutableLiveData<Boolean> currentUser = new MutableLiveData<>();

    public LiveData<Boolean> shouldNavigate(){
        return currentUser;
    }

    public LiveData<Boolean> getIsJobPostComplete() {
        return isJobPostComplete;
    }

    public void setIsJobPostComplete(Boolean inIsJobPostComplete) {
        isJobPostComplete.setValue(inIsJobPostComplete);
    }

    public void setShouldNavigate(Boolean user){
        currentUser.setValue(user);
    }

}
