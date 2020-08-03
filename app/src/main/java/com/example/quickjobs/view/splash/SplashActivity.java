package com.example.quickjobs.view.splash;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickjobs.R;
import com.example.quickjobs.interfaces.LocationChangeListener;
import com.example.quickjobs.model.User;
import com.example.quickjobs.helper.PermissionManager;
import com.example.quickjobs.view.auth.AuthActivity;
import com.example.quickjobs.view.main.MainActivity;
import com.example.quickjobs.viewmodel.SplashViewModel;
import com.google.android.gms.location.LocationResult;

public class SplashActivity extends AppCompatActivity implements LocationChangeListener {
    private TextView textView;
    private final int LOCATION_REQUEST_ID = 180;
    private final String TAG = "SplashActivity";

    private SplashViewModel splashViewModel;
    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView = findViewById(R.id.splashScreenTV);

        initSplashViewModel();
        initPermissionManager();
        checkIfUserIsAnonymousAndAuthenticated();

        splashViewModel.register(this);

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
                Log.println(Log.ERROR, TAG, "checkIfUserIsAnonymousAndAuthenticated -> setCurrentUserAsAnonymous");
                setCurrentUserAsAnonymous(user);
           }
//            registered user is returing to app
            else if(user.isAuthenticated() && !user.isAnonymous()){
                Log.println(Log.ERROR, TAG, "checkIfUserIsAnonymousAndAuthenticated -> getUserFromDatabase");
                getUserFromDatabase(user.getUid());
            }
//            possible first time user
            else{
                Log.println(Log.ERROR, TAG, "checkIfUserIsAnonymousAndAuthenticated -> signInAnonymously");
                signInAnonymously();
            }
        });
    }

    private void signInAnonymously(){
        splashViewModel.signInAnonymously();
        splashViewModel.authenticatedUserLiveData.observe(this, user -> {
            Log.println(Log.ERROR, TAG, "signInAnonymously");
            if(user == null){
                Log.println(Log.ERROR, TAG, "signInAnonymously -> goToAuthActivity");
                goToAuthActivity();
            }
            else{
                Log.println(Log.ERROR, TAG, "signInAnonymously -> setCurrentUserAsAnonymous");
                setCurrentUserAsAnonymous(user);
            }
        });

    }

    private void setCurrentUserAsAnonymous(User anonymousUser){
        splashViewModel.setAnonymousUser(anonymousUser);
        splashViewModel.authenticatedUserLiveData.observe(this, user -> {
            if(user == null){
                Log.println(Log.ERROR, TAG, "setCurrentUserAsAnonymous -> dialogForFailedAuthentication");
                dialogForFailedAuthentication();
            }
            else{
                Log.println(Log.ERROR, TAG, "setCurrentUserAsAnonymous -> checkIfUserLocationIsAvailable");
                checkIfUserLocationIsAvailable();
            }
        });
    }

    private void getUserFromDatabase(String inUid){
        Log.println(Log.ERROR, TAG, "getting user from database");
        splashViewModel.setAuthenticatedUser(inUid);
        splashViewModel.authenticatedUserLiveData.observe(this, user ->{
            Log.println(Log.ERROR, TAG, "getUserFromDatabase");
            if(user == null){
                Log.println(Log.ERROR, TAG, "getUserFromDatabase -> dialogForFailedAuthentication");
                dialogForFailedAuthentication();
            }
            else{
                Log.println(Log.ERROR, TAG, "getUserFromDatabase -> checkIfUserLocationIsAvailable");
                enableSnapshotListeners();
                checkIfUserLocationIsAvailable();
            }
        });
    }

    /*
    check if location permmision has been granted
     */
    private void checkLocationPermission(){
        permissionManager.checkPermission(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
        , new PermissionManager.PermissionAskListnener() {
            @Override
            public void onNeedPermission() {
                Log.println(Log.ERROR, TAG, "Requesting location permission");
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_ID);
            }

            @Override
            public void onPermssionPreviouslyDenied() {
                Log.println(Log.ERROR, TAG, "informing user why we need location services");
                showLocationRationale();
            }

            @Override
            public void onPermissionPreviouslyDeniedWithNeverAskAgain() {
                Log.println(Log.ERROR, TAG, "sending user to settings to change location permission");
                dialogForSettings();
            }

            @Override
            public void onPermissionGranted() {
                Log.println(Log.ERROR, TAG, "checkLocationPermission -> onPermissionGranted -> checkIfLocationIsAvailable");
                splashViewModel.enableLocationUpdates(SplashActivity.this);
            }
        });
    }

    private void checkPermissionResults(){
        Log.println(Log.ERROR, TAG, "Checking permission results");

        permissionManager.handlePermissionRequestResults(this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                new PermissionManager.PermissionRequestResultListener() {
            @Override
            public void onPermissionGranted() {
                splashViewModel.enableLocationUpdates(SplashActivity.this);
            }

            @Override
            public void onPermissionDenied() {
                checkLocationPermission();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermissionResults();
    }

    private void checkIfUserLocationIsAvailable(){
        Log.println(Log.ERROR, TAG, "check If User Location Is Available");
        splashViewModel.checkIfCurrentUserHasLocationPersisted();
        splashViewModel.authenticatedUserLiveData.observe(this, user -> {
            if(user.hasLocationAvailable()){
                Log.println(Log.ERROR, TAG, "hasLocationAvailable");
                getQuickJobsBasedOnUserLocation(user.getLongitude(), user.getLatitude());
            }
            else{
                Log.println(Log.ERROR, TAG, "!hasLocationAvailable");
                checkLocationPermission();
            }
        });
    }

    @Override
    public void onLocationChange(LocationResult locationResults) {
        if(locationResults.getLastLocation() != null){
            updateUserLocationAndPersistToCloud(locationResults.getLastLocation());
        }
    }

    public void updateUserLocationAndPersistToCloud(Location location){
        Log.println(Log.ERROR, TAG, "updateUserLocationAndPersistToCloud");
        splashViewModel.updateUserLocationAndPersistToCloud(location);
        splashViewModel.authenticatedUserLiveData.observe(this, updatedUser -> {
            getQuickJobsBasedOnUserLocation(updatedUser.getLongitude(), updatedUser.getLatitude());
        });
    }

    public void getQuickJobsBasedOnUserLocation(double longitude, double latitude){
        final int MAX_DISTANCE = 25;
        splashViewModel.getJobsBasedOnUserLocation(longitude, latitude, MAX_DISTANCE);
        splashViewModel.jobsBasedOnUserLocation.observe(this, jobs -> {
            goToMainActivity();
        });
    }

    public void enableSnapshotListeners(){
        splashViewModel.enableSnapshotListeners();
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
                    dialogInterface.dismiss();
                    ActivityCompat.requestPermissions(SplashActivity.this, new String[]
                            { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_ID);
                }).show();
    }

    private void dialogForSettings(){
        new AlertDialog.Builder(this).setTitle("Permission Denied")
                .setMessage("Now you must allow location access from settings.")
                .setCancelable(false)
                .setNegativeButton("NOT NOW", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                }))
                .setPositiveButton("SETTINGS", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    goToSettings();
                }).show();
    }

    private void dialogForFailedAuthentication(){
        new AlertDialog.Builder(this).setTitle("Authentication Failed")
                .setMessage("Error while authenticating. Please try again.")
                .setCancelable(false)
                .setNegativeButton("CONTINUE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    signInAnonymously();
                })
                .setPositiveButton("SIGN IN", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    goToAuthActivity();
                }).show();
    }

    /*
    Navigation to next step
     */

    private void goToAuthActivity(){
        splashViewModel.unregister(this);
        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainActivity(){
        splashViewModel.unregister(this);
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