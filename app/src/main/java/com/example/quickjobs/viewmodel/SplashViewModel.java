package com.example.quickjobs.viewmodel;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.helper.Constants;
import com.example.quickjobs.helper.PermissionManager;
import com.example.quickjobs.interfaces.LocationChangeListener;
import com.example.quickjobs.model.QuickJob;
import com.example.quickjobs.model.User;
import com.example.quickjobs.repos.SplashRepository;
import com.example.quickjobs.source.SharedPreferencesManager;

import java.util.List;

public class SplashViewModel extends AndroidViewModel {
    private final String TAG = "SplashViewModel";
    private SplashRepository splashRepository;
    private SharedPreferencesManager sharedPreferencesManager;

    public LiveData<User> authenticatedUserLiveData;
    public LiveData<User> isUserAnonymousOrAuthenticatedLiveData;

    public LiveData<List<QuickJob>> jobsBasedOnUserLocation;

    private MutableLiveData<Boolean> shouldMakeLoadingScreenVisible;

    private PermissionManager permissionManager;

    public SplashViewModel(@NonNull Application application) {
        super(application);

        splashRepository = new SplashRepository(application);
        permissionManager = new PermissionManager(application);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(application);
        shouldMakeLoadingScreenVisible = new MutableLiveData<>(false);
        permissionManager = new PermissionManager(application);

    }

    public void checkIfUserIsAnonymousAndAuthenticated(){
        isUserAnonymousOrAuthenticatedLiveData = splashRepository.checkIfUserIsAnonymousAndAuthenticated();
    }

    public void setAnonymousUser(User anonymousUser){
        authenticatedUserLiveData = splashRepository.addAnonymousUserToLiveData(anonymousUser);
    }

    public void setAuthenticatedUser(String Uid){
        authenticatedUserLiveData = splashRepository.addAuthenticatedUserLiveData(Uid);
    }

    public void signInAnonymously(){
        authenticatedUserLiveData = splashRepository.signInAnonymously();
    }

    public void checkIfCurrentUserHasLocationPersisted(){
        authenticatedUserLiveData = splashRepository.checkIfCurrentUserHasLocationPersisted();
    }

    public void enableLocationUpdates(){
        splashRepository.enableLocationUpdates();
    }

    public void updateUserLocationAndPersistToCloud(Location location){
        authenticatedUserLiveData = splashRepository.updateUserLocationAndPersist(location);
    }

    public void updateUserWithMockLocationAndPersistToCloud(double latitude, double longitude){
        authenticatedUserLiveData = splashRepository.updateUserWithMockLocationAndPersistToCloud(latitude, longitude);
    }

    public void getJobsBasedOnUserLocation(double longitude, double latitude, int maxDistance){
        jobsBasedOnUserLocation = splashRepository.getJobsBasedOnUserLocation(longitude, latitude, maxDistance);
    }

    public void enableSnapshotListeners(){
        splashRepository.enableSnapshotListeners();
    }

    public LiveData<Boolean> shouldMakeLoadingScreenVisible(){
        return shouldMakeLoadingScreenVisible;
    }

    public void setShouldMakeLoadingScreenVisible(Boolean loadFirstAttempt){
        shouldMakeLoadingScreenVisible.setValue(loadFirstAttempt);
    }

    public void register(LocationChangeListener locationChangeListener){
        splashRepository.register(locationChangeListener);
    }

    public void unregister(LocationChangeListener locationChangeListener){
        splashRepository.unregister(locationChangeListener);
    }

    public PermissionManager getPermissionManager(){
        return permissionManager;
    }

    public void loadUserAccountPreferences(){
        boolean isDarkModeOn = sharedPreferencesManager.getValueFromUserPreferences(Constants.USER_PREFERENCE_CONFIGURATION, false);
        Log.println(Log.ERROR, TAG, "isDarkModeOn " + isDarkModeOn);
        if(isDarkModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

}
