package com.example.quickjobs.model;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.source.JobSource;
import com.example.quickjobs.model.source.LocationSource;
import com.example.quickjobs.model.source.SharedPreferencesManager;
import com.example.quickjobs.model.source.UserSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainRepository {
    private final String TAG = "MainRepository";
    private final String USER_COLLECTION_NAME = "users";
    private final String BACKGROUND_THREAD_NAME = "MAIN_REPOSITORY_THREAD";

    private static MainRepository INSTANCE;

    /*
    Concurrency
     */
    private HandlerThread initDataHandlerThread_Background;
    private Handler initDataHandler_Background;

    /*
    Data Sources
     */
    private UserSource userSource;
    private JobSource jobSource;
    private LocationSource locationSource;
    private SharedPreferencesManager sharedPreferencesManager;
    private CollectionReference collectionReference;

    private MainRepository(Application application){
        collectionReference = FirebaseFirestore.getInstance().collection(USER_COLLECTION_NAME);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(application);

        userSource = UserSource.getInstance();
        jobSource = JobSource.getInstance();
        locationSource = new LocationSource(application);
    }

    public static MainRepository getInstance(Application application)
    {
        if(INSTANCE == null)
        {
            synchronized (MainRepository.class)
            {
                if(INSTANCE == null)
                {
                    INSTANCE = new MainRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    public MutableLiveData<User> getCurrentUserFromUserSource(){
        return userSource.getCurrentUserMutableLiveData();
    }

    public void getUserPreferences(Observer<Map<String, ?>> observer){
        Observable<Map<String, ?>> userPrefObservable = Observable.create(source -> {
            Map<String, ?> preferenceMap = sharedPreferencesManager.getUsersCurrentPreferences();
            source.onNext(preferenceMap);
            source.onComplete();
        });

        userPrefObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }


}
