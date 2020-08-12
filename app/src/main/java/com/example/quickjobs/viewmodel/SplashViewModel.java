package com.example.quickjobs.viewmodel;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.helper.Constants;
import com.example.quickjobs.model.helper.PermissionManager;
import com.example.quickjobs.model.interfaces.UserLocationCallback;
import com.example.quickjobs.model.beans.QuickJob;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.MainRepository;
import com.example.quickjobs.model.source.SharedPreferencesManager;

import java.util.List;

public class SplashViewModel extends AndroidViewModel {
    private final String TAG = "SplashViewModel";
    private MainRepository mainRepository;
    private SharedPreferencesManager sharedPreferencesManager;

    public LiveData<User> authenticatedUserLiveData;
    public LiveData<User> isUserAnonymousOrAuthenticatedLiveData;
    public LiveData<Boolean> isAllDataLoadedLiveData;
    public LiveData<Boolean> setLoadingScreenVisible;
    public LiveData<List<QuickJob>> jobsBasedOnUserLocation;

    private PermissionManager permissionManager;



    public SplashViewModel(@NonNull Application application) {
        super(application);

        mainRepository = MainRepository.getInstance(application);
        permissionManager = new PermissionManager(application);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(application);
        permissionManager = new PermissionManager(application);
        isAllDataLoadedLiveData = new MutableLiveData<>(false);
        setLoadingScreenVisible = new MutableLiveData<>(false);

    }

    public void checkIfUserIsAnonymousAndAuthenticated(){
        isUserAnonymousOrAuthenticatedLiveData = mainRepository.checkIfUserIsAnonymousAndAuthenticated();
    }

    public void setAnonymousUser(User anonymousUser){
        authenticatedUserLiveData = mainRepository.addAnonymousUserToLiveData(anonymousUser);
    }

    public void setAuthenticatedUser(String Uid){
        authenticatedUserLiveData = mainRepository.addAuthenticatedUserLiveData(Uid);
    }

    public void signInAnonymously(){
        authenticatedUserLiveData = mainRepository.signInAnonymously();
    }

    public void checkIfCurrentUserHasLocationPersisted(){
        authenticatedUserLiveData = mainRepository.checkIfCurrentUserHasLocationPersisted();
    }

    public void enableLocationUpdates(){
        mainRepository.enableLocationUpdates();
    }

    public void updateUserLocationAndPersistToCloud(Location location){
        authenticatedUserLiveData = mainRepository.updateUserLocationAndPersist(location);
    }

    public void updateUserWithMockLocationAndPersistToCloud(double latitude, double longitude){
        authenticatedUserLiveData = mainRepository.updateUserWithMockLocationAndPersistToCloud(latitude, longitude);
    }

    public void getJobsBasedOnUserLocation(double longitude, double latitude, int maxDistance){
        jobsBasedOnUserLocation = mainRepository.getJobsBasedOnUserLocation(longitude, latitude, maxDistance);
    }

    public void enableSnapshotListeners(){
        mainRepository.enableSnapshotListeners();
    }

    public void register(UserLocationCallback userLocationCallback){
        mainRepository.register(userLocationCallback);
    }

    public void unregister(UserLocationCallback userLocationCallback){
        mainRepository.unregister(userLocationCallback);
    }

    public PermissionManager getPermissionManager(){
        return permissionManager;
    }

    public void loadUserAccountPreferences(){
        boolean isDarkModeOn = sharedPreferencesManager.getValueFromUserPreferences(Constants.USER_PREFERENCE_CONFIGURATION, false);
        Log.println(Log.ERROR, TAG, "is dark mode on " + isDarkModeOn);
        if(isDarkModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

}
