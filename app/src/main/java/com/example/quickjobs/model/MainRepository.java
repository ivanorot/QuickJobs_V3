package com.example.quickjobs.model;

import android.app.Application;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.QuickJob;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.helper.ExceptionHandler;
import com.example.quickjobs.model.interfaces.UserLocationCallback;
import com.example.quickjobs.model.source.JobSource;
import com.example.quickjobs.model.source.LocationSource;
import com.example.quickjobs.model.source.SharedPreferencesManager;
import com.example.quickjobs.model.source.UserSource;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

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

    public void initializeBackgroundThread(){
        initDataHandlerThread_Background = new HandlerThread(BACKGROUND_THREAD_NAME);
        initDataHandlerThread_Background.start();
        initDataHandler_Background = new Handler(initDataHandlerThread_Background.getLooper());
    }

    public void terminateBackgroundThread(){
        initDataHandlerThread_Background.quitSafely();
        try{
            initDataHandlerThread_Background.join();
            initDataHandlerThread_Background = null;
            initDataHandler_Background = null;
        }catch (InterruptedException exception){
            ExceptionHandler.consumeException(TAG, exception);
        }
    }

    public MutableLiveData<User> getCurrentUserMutableLiveData(){
        Log.println(Log.ERROR, TAG, "getCurrentUserMutableLiveData");
        return userSource.getCurrentUserMutableLiveData();
    }

    public void addAnonymousUserToSource(User anonymousUser){
        initDataHandler_Background.post(() -> {
            userSource.addAnonymousUserToLiveData(anonymousUser);
        });
    }

    public MutableLiveData<List<QuickJob>> getJobsBasedOnUserLocation(double longitude, double latitude, int maxDistance){
        return jobSource.getQuickJobsNearUserLocation(longitude, latitude, maxDistance);
    }

    public void enableSnapshotListeners(){
        userSource.enableCurrentUserDocumentSnapshotListener();
    }

    public void register(UserLocationCallback userLocationCallback){
        locationSource.registerLocationChangeListener(userLocationCallback);
    }

    public void unregister(UserLocationCallback userLocationCallback){
        locationSource.unregisterLocationChangeListener(userLocationCallback);
    }
    public MutableLiveData<User> checkIfUserIsAnonymousAndAuthenticated(){
        return userSource.checkIfUserIsAnonymousAndAuthenticated();
    }

    public MutableLiveData<User> addAnonymousUserToLiveData(User anonymousUser){
        return userSource.addAnonymousUserToLiveData(anonymousUser);
    }

    public MutableLiveData<User> addAuthenticatedUserLiveData(String Uid){
        return userSource.addAuthenticatedUserToLiveData(Uid);
    }

    public MutableLiveData<User> signInAnonymously(){
        return userSource.firebaseAnonymousSignIn();
    }

    public MutableLiveData<User> checkIfCurrentUserHasLocationPersisted(){
        return userSource.getCurrentUserMutableLiveData();
    }

    public void enableLocationUpdates(){
        locationSource.run();
    }

    public MutableLiveData<User> updateUserLocationAndPersist(Location location){
        return userSource.updateUserLocationDataAndPersist(location);
    }

    public MutableLiveData<User>  updateUserWithMockLocationAndPersistToCloud(double latitude, double longitude){
        return userSource.updateUserWithMockLocationDataAndPersistToCloud(latitude, longitude);
    }

    public MutableLiveData<User> firebaseGenericSignIn(IdpResponse response){
        return  userSource.firebaseGenericSignIn(response);
    }

    public MutableLiveData<User> createUserInFireBaseIfNotExists(User authenticatedUser){
        return userSource.createUserInFireBaseIfNotExists(authenticatedUser);
    }

}
