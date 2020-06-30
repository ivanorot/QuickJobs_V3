package com.example.quickjobs.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.Repository;
import com.example.quickjobs.model.beans.QuickJob;
import com.example.quickjobs.model.beans.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private Repository repository;

    /*
    User
     */
    private MutableLiveData<User> currentUser;
    private MutableLiveData<List<QuickJob>> jobPostings;


    public MainViewModel(@NonNull Application application) {
        super(application);

        currentUser = new MutableLiveData<>();
        jobPostings = new MutableLiveData<>();

        repository = Repository.getInstance(application);
    }

    public LiveData<User> getCurrentUser()
    {
        return currentUser;
    }

}
