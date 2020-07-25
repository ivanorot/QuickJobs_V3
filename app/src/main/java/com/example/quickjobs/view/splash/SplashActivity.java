package com.example.quickjobs.view.splash;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickjobs.R;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.helper.PermissionManager;
import com.example.quickjobs.view.auth.AuthActivity;
import com.example.quickjobs.view.main.MainActivity;
import com.example.quickjobs.viewmodel.SplashViewModel;

public class SplashActivity extends AppCompatActivity {

    private final int LOCATION_REQUEST_ID = 180;
    private final String TAG = "SplashActivity";

    private SplashViewModel splashViewModel;
    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initSplashViewModel();
        initPermissionManager();
        checkIfUserIsAnonymousAndAuthenticated();

    }

    private void initSplashViewModel(){
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    }

    private void initPermissionManager(){
        permissionManager = new PermissionManager(this);
    }


    /*
    authenticating the user either registerd or anonymous
     */
    private void checkIfUserIsAnonymousAndAuthenticated(){
        splashViewModel.checkIfUserIsAnonymousAndAuthenticated();
        splashViewModel.isUserAnonymousOrAuthenticatedLiveData.observe(this, user -> {
//            anonymous user registered with this phone is signed in and returning
            if(user.isAuthenticated() && user.isAnonymous()) {
               setCurrentUserAsAnonymous(user);
           }
//            registered user is returing to app
            else if(user.isAuthenticated() && !user.isAnonymous()){
                getUserFromDatabase(user.getUid());
            }
//            possible first time user
            else{
                signInAnonymously();
            }
        });
    }

    private void signInAnonymously(){
        splashViewModel.signInAnonymously();
        splashViewModel.authenticatedUserLiveData.observe(this, user -> {
            Log.println(Log.ERROR, TAG, "signInAnonymously");
            if(user == null){
                goToAuthActivity();
            }
            else{
                setCurrentUserAsAnonymous(user);
            }
        });

    }

    private void setCurrentUserAsAnonymous(User anonymousUser){
        splashViewModel.setAnonymousUser(anonymousUser);
        splashViewModel.authenticatedUserLiveData.observe(this, user -> {
            Log.println(Log.ERROR, TAG, "setCurrentUserAsAnonymous");
            if(user == null){
                dialogForFailedAuthentication("Authentication Failed", "Error while authenticating. Please try again.");
            }
            else{
                checkIfUserLocationIsAvailable();
            }
        });
    }

    private void getUserFromDatabase(String inUid){
        splashViewModel.setAuthenticatedUser(inUid);
        splashViewModel.authenticatedUserLiveData.observe(this, user ->{
            Log.println(Log.ERROR, TAG, "getUserFromDatabase");
            if(user == null){
                dialogForFailedAuthentication("Authentication Failed", "Error while authenticating. Please try again.");
            }
            else{
                checkIfUserLocationIsAvailable();
            }
        });
    }

    /*
    check if location permmision has been granted
     */
    private void checkIfUserLocationIsAvailable(){
        Log.println(Log.ERROR, TAG, "check If User Location Is Available");
        splashViewModel.checkIfCurrentUserHasLocationPersisted();
        splashViewModel.authenticatedUserLiveData.observe(this, user -> {
            if(user.hasLocationAvailable()){
                getQuickJobsBasedOnUserLocation();
            }
            else{
                checkLocationPermission();
            }
        });
    }

    private void checkLocationPermission(){
        permissionManager.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, new PermissionManager.PermissionAskListnener() {
            @Override
            public void onNeedPermission() {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_ID);
            }

            @Override
            public void onPermssionPreviouslyDenied() {
                showLocationRationale();
            }

            @Override
            public void onPermissionPreviouslyDeniedWithNeverAskAgain() {
                dialogForSettings("Permission Denied", "Now you must allow location access from settings.");
            }

            @Override
            public void onPermissionGranted() {
                splashViewModel.checkIfUserHasLocationAvailable(SplashActivity.this);
                splashViewModel.locationAvailabilityLiveData.observe(SplashActivity.this, locationAvailability -> {
                    if(locationAvailability.isLocationAvailable()){
                        Log.println(Log.ERROR, TAG, "location is available");
                        retrieveUserCurrentLocation();
                    }
                    else{
//                        todo handle location availability failed
                    }
                });
            }
        });
    }

    private void checkIfPermissionWasGranted(){
        permissionManager.checkPermissionUpdate(this, Manifest.permission.ACCESS_FINE_LOCATION, new PermissionManager.PermissionUpdateListener() {
            @Override
            public void onPermissionGranted() {
                Log.println(Log.ERROR, TAG, "Granted");
                retrieveUserCurrentLocation();
            }

            @Override
            public void onPermissionDenied() {
                Log.println(Log.ERROR, TAG, "Denied");
//                finish();
            }
        });
    }

    private void retrieveUserCurrentLocation(){
        splashViewModel.getCurrentLocationFromLocationSource(this);
        splashViewModel.locationResultLiveData.observe(this, this::updateUserLocationAndPersistToCloud);
    }

    public void updateUserLocationAndPersistToCloud(Location location){
        Log.println(Log.ERROR, TAG, "updateUserLocationAndPersistToCloud");
        splashViewModel.updateUserLocationAndPersistToCloud(location);
        splashViewModel.authenticatedUserLiveData.observe(this, updatedUser -> {
            Log.println(Log.ERROR, TAG, "Longitude: " + updatedUser.getLongitude());
            Log.println(Log.ERROR, TAG, "Latitude: " + updatedUser.getLatitude());
            getQuickJobsBasedOnUserLocation();
        });
    }

    public void getQuickJobsBasedOnUserLocation(){
//        todo refractor all code and get jobs testing
        goToMainActivity();
    }

    private void showLocationRationale(){
        new AlertDialog.Builder(this).setTitle("Permission Denied")
                .setMessage("Without this permission this app is unable to find jobs. Are you sure you want to Deny this message")
                .setCancelable(false)
                .setNegativeButton("IM SURE", (dialogInterface, i ) -> {
                    dialogInterface.dismiss();
                    finish();
                })
                .setPositiveButton("RETRY", (dialogInterface, i) -> {
                    ActivityCompat.requestPermissions(SplashActivity.this, new String[]
                            { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_ID);
                    checkIfPermissionWasGranted();
                }).show();
    }

    private void dialogForSettings(String title, String message){
        new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("NOT NOW", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                }))
                .setPositiveButton("SETTINGS", (dialogInterface, i) -> {
                    goToSettings();
                    dialogInterface.dismiss();
                }).show();
    }

    private void dialogForFailedAuthentication(String title, String message){
        new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("CONTINUE", (dialogInterface, i) -> {
                    signInAnonymously();
                    dialogInterface.dismiss();
                })
                .setPositiveButton("SIGN IN", (dialogInterface, i) -> {
                    goToAuthActivity();
                    dialogInterface.dismiss();
                }).show();
    }

    /*
    Navigation to next step
     */

    private void goToAuthActivity(){
        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainActivity(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToSettings(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + getPackageName());
        intent.setData(uri);
        startActivity(intent);
    }

}