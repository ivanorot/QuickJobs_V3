package com.example.quickjobs.repos;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.source.JobSource;
import com.example.quickjobs.model.source.LocationSource;
import com.example.quickjobs.model.source.UserSource;
import com.google.android.gms.location.LocationAvailability;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public MutableLiveData<LocationAvailability> checkIfLocationIsAvailable(Context context){
        return locationSource.getLocationAvailabilityMutableLiveData(context);
    }

    public MutableLiveData<User> checkIfCurrentUserHasLocationPersisted(){
        return userSource.getCurrentUserMutableLiveData();
    }

    public MutableLiveData<Location> getCurrentLocationFromLocationSource(Context context){
        Log.println(Log.ERROR, TAG, "updateUserLocationAndPersistToCloud");

        return locationSource.getSingleLocationMutableLiveData(context);
    }

    public MutableLiveData<User> updateUserLocationAndPersist(Location location){
        return userSource.updateUserLocationDataAndPersist(location);
    }

}
