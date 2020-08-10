package com.example.quickjobs.repos;

import android.app.Application;
import android.content.Context;
import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.interfaces.LocationChangeListener;
import com.example.quickjobs.model.QuickJob;
import com.example.quickjobs.model.User;
import com.example.quickjobs.source.JobSource;
import com.example.quickjobs.source.LocationSource;
import com.example.quickjobs.source.UserSource;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SplashRepository{
    private static SplashRepository Instance;
    private final String TAG = "SplashRepository";

    private final int INITIAL_DISTNACE = 25;

    private UserSource userSource;

    private JobSource jobSource;
    private LocationSource locationSource;

    public SplashRepository(Application context){
        userSource = UserSource.getInstance(FirebaseFirestore.getInstance());
        jobSource = JobSource.getInstance(FirebaseFirestore.getInstance());
        locationSource = LocationSource.getInstance(context);
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

    public MutableLiveData<List<QuickJob>> getJobsBasedOnUserLocation(double longitude, double latitude, int maxDistance){
        return jobSource.getQuickJobsNearUserLocation(longitude, latitude, maxDistance);
    }

    public void enableSnapshotListeners(){
        userSource.enableCurrentUserDocumentSnapshotListener();
    }

    public void register(LocationChangeListener locationChangeListener){
        locationSource.registerLocationChangeListener(locationChangeListener);
    }

    public void unregister(LocationChangeListener locationChangeListener){
        locationSource.unregisterLocationChangeListener(locationChangeListener);
    }
}
