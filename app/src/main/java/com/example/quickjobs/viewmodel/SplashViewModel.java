package com.example.quickjobs.viewmodel;

import android.app.Application;
import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.helper.PermissionManager;
import com.example.quickjobs.interfaces.LocationStateListener;
import com.example.quickjobs.model.beans.QuickJob;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.repos.SplashRepository;
import com.google.android.gms.location.LocationAvailability;

import java.util.List;

public class SplashViewModel extends AndroidViewModel {
    private final String TAG = "SplashViewModel";
    private SplashRepository splashRepository;
    private PermissionManager permissionManager;

    public LiveData<User> authenticatedUserLiveData;
    public LiveData<User> isUserAnonymousOrAuthenticatedLiveData;

    public LiveData<List<QuickJob>> jobsBasedOnUserLocation;

    private MutableLiveData<Boolean> shouldMakeLoadingScreenVisible;

    public SplashViewModel(@NonNull Application application) {
        super(application);

        splashRepository = new SplashRepository(application);
        permissionManager = new PermissionManager(application);

        shouldMakeLoadingScreenVisible = new MutableLiveData<>(false);

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

    public void checkIfUserHasLocationAvailable(Context context, LocationStateListener locationStateListener){
        splashRepository.checkIfLocationIsAvailable(context,locationStateListener);
    }

    public void checkIfCurrentUserHasLocationPersisted(){
        authenticatedUserLiveData = splashRepository.checkIfCurrentUserHasLocationPersisted();
    }

    public void enableLocationUpdates(Context context, LocationStateListener locationStateListener){
        splashRepository.enableLocationUpdates(context, locationStateListener);
    }

    public void getCurrentLocationFromLocationSource(Context context, LocationStateListener locationStateListener){
        splashRepository.getCurrentLocationFromLocationSource(context, locationStateListener);
    }

    public void updateUserLocationAndPersistToCloud(Location location){
        authenticatedUserLiveData = splashRepository.updateUserLocationAndPersist(location);
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

    public void checkPermission(){

    }

}
