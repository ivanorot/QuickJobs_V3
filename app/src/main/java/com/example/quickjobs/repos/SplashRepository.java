package com.example.quickjobs.repos;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.helper.PermissionManager;
import com.example.quickjobs.interfaces.LocationStateListener;
import com.example.quickjobs.model.beans.QuickJob;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.source.JobSource;
import com.example.quickjobs.model.source.LocationSource;
import com.example.quickjobs.model.source.UserSource;
import com.google.android.gms.location.LocationAvailability;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SplashRepository{
    private static SplashRepository Instance;
    private final String TAG = "SplashRepository";

    private final int INITIAL_DISTNACE = 25;

    private UserSource userSource;

    private JobSource jobSource;
    private LocationSource locationSource;

    public SplashRepository(Context context){
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

    public void checkIfLocationIsAvailable(Context context, LocationStateListener listener){
        locationSource.getLocationAvailability(context, listener);
    }

    public MutableLiveData<User> checkIfCurrentUserHasLocationPersisted(){
        return userSource.getCurrentUserMutableLiveData();
    }

    public void enableLocationUpdates(Context context, LocationStateListener locationStateListener){
        locationSource.enableLocationUpdates(context, locationStateListener);
    }

    public void getCurrentLocationFromLocationSource(Context context, LocationStateListener locationStateListener){
        locationSource.getSingleLocationMutableLiveData(context, locationStateListener);
    }

    public MutableLiveData<User> updateUserLocationAndPersist(Location location){
        return userSource.updateUserLocationDataAndPersist(location);
    }

    public MutableLiveData<List<QuickJob>> getJobsBasedOnUserLocation(double longitude, double latitude, int maxDistance){
        return jobSource.getQuickJobsNearUserLocation(longitude, latitude, maxDistance);
    }

    public void enableSnapshotListeners(){
        userSource.enableCurrentUserDocumentSnapshotListener();
    }
}
