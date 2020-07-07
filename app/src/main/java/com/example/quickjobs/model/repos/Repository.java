package com.example.quickjobs.model.repos;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.QuickJob;
import com.example.quickjobs.model.source.JobSource;
import com.example.quickjobs.model.source.UserSource;
import com.example.quickjobs.model.beans.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Repository {
    private static Repository INSTANCE;

    private UserSource userSource;
    private JobSource jobSource;

    private Repository(){

        userSource = new UserSource(FirebaseFirestore.getInstance());
        jobSource = new JobSource(FirebaseFirestore.getInstance());

    }

    public static Repository getInstance()
    {
        if(INSTANCE == null)
        {
            synchronized (Repository.class)
            {
                if(INSTANCE == null)
                {
                    INSTANCE = new Repository();
                }
            }
        }
        return INSTANCE;
    }
}
