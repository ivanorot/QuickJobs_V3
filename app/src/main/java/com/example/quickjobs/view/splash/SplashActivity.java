package com.example.quickjobs.view.splash;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickjobs.R;
import com.example.quickjobs.helper.ExceptionHandler;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.helper.PermissionManager;
import com.example.quickjobs.view.auth.AuthActivity;
import com.example.quickjobs.view.main.MainActivity;
import com.example.quickjobs.viewmodel.SplashViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class SplashActivity extends AppCompatActivity {
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
        Log.println(Log.ERROR, TAG, "getting user from database");
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
                checkIfLocationIsAvailable();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkLocationPermission();
    }

    private void checkIfUserLocationIsAvailable(){
        Log.println(Log.ERROR, TAG, "check If User Location Is Available");
        splashViewModel.checkIfCurrentUserHasLocationPersisted();
        splashViewModel.authenticatedUserLiveData.observe(this, user -> {
            if(user.hasLocationAvailable()){
                getQuickJobsBasedOnUserLocation(user.getLongitude(), user.getLatitude());
                Log.println(Log.ERROR, TAG, "hasLocationAvailable");
            }
            else{
                checkLocationPermission();
                Log.println(Log.ERROR, TAG, "!hasLocationAvailable");
            }
        });
    }

    private void checkIfLocationIsAvailable(){
        splashViewModel.checkIfUserHasLocationAvailable(SplashActivity.this);
        splashViewModel.locationAvailabilityLiveData.observe(SplashActivity.this, locationAvailability -> {
            if(locationAvailability.isLocationAvailable()){
                Log.println(Log.ERROR, TAG, "location is available");
                retrieveUserCurrentLocation();
            }
            else{
//                TODO start loading screen until location is supplied exit the app after 5-10 seconds with error message
//                dialogForFailedLocation("Location Services", "Unable to extract location. You may try restarting.");
                final boolean IS_FIRST_ATTEMPT = true;
                migrateToLocationLoadingScreenUntilLocationIsAvailable(IS_FIRST_ATTEMPT);
            }
        });
    }

    private void retrieveUserCurrentLocation(){
        splashViewModel.getCurrentLocationFromLocationSource(this);
        splashViewModel.locationResultLiveData.observe(this, this::updateUserLocationAndPersistToCloud);
    }

    public void migrateToLocationLoadingScreenUntilLocationIsAvailable(boolean firstAttempt){
        textView.setVisibility(View.GONE);
        splashViewModel.setShouldMakeLoadingScreenVisible(true);
        splashViewModel.checkIfUserHasLocationAvailable(SplashActivity.this);

        splashViewModel.locationAvailabilityLiveData.observe(this, locationAvailability -> {
            if(locationAvailability.isLocationAvailable()){
                textView.setVisibility(View.VISIBLE);
                splashViewModel.setShouldMakeLoadingScreenVisible(false);
                retrieveUserCurrentLocation();
            }
            else{
                migrateToLocationLoadingScreenUntilLocationIsAvailable(false);
                if(!firstAttempt){
                    dialogForForceClose("Location Unavailable", "could not extract your location. Try Again Later");
                }
            }
        });
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
        final int NO_JOBS = 0;
        Log.println(Log.ERROR, TAG, "Longitude: " + longitude);
        Log.println(Log.ERROR, TAG, "Latitude: " + latitude);
        splashViewModel.getJobsBasedOnUserLocation(longitude, latitude, MAX_DISTANCE);
        splashViewModel.jobsBasedOnUserLocation.observe(this, jobs -> {
            if(jobs.size() != NO_JOBS){
                goToMainActivity();
            }else
            {
                dialogForFailedJobs("Error finding jobs", "retry later");
            }
        });
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
                    checkLocationPermission();
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

    private void dialogForFailedJobs(String title, String message){
        new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Exit", (dialogInterface, i) -> {
                    goToMainActivity();

                })
                .setPositiveButton("Retry", (dialogInterface, i) -> {
                    goToMainActivity();
                })
                .show();
    }

    private void dialogForFailedLocation(String title, String message){
        new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("RETRY", (dialogInterface, i) -> {

                })
                .setPositiveButton("ENTER", (dialogInterface, i) -> {

                }).show();
    }

    private void dialogForForceClose(String title , String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("CONTINUE", (dialogInterface, i) -> {
                    finish();
                });
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

    private void initializeDatabaseAsTestingEvironment(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setHost("9090")
                .build();

        Log.println(Log.ERROR, TAG, settings.getHost());

        FirebaseFirestore instance = FirebaseFirestore.getInstance();

        instance.setFirestoreSettings(settings);

        populateLocalizedFirestoreWithFakeData();
    }

    private void populateLocalizedFirestoreWithFakeData(){
        final String user_data_path = "/Users/mortonsworld/Documents/GitHub/QuickJobs_V3/firestore_testing/user_test_data.csv";
        final String EXCEPTION_TAG = "populateLocalizedFirestoreWithFakeData ";
        String line = "";
        try{
            File file = new File(user_data_path);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNext()){
                Log.println(Log.ERROR, TAG, scanner.next());
            }

        } catch (IOException exception){
            ExceptionHandler.consumeException(EXCEPTION_TAG, exception);
        }
    }

}