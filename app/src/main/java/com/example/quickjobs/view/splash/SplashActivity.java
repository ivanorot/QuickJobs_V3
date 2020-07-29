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
import com.example.quickjobs.interfaces.LocationStateListener;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.helper.PermissionManager;
import com.example.quickjobs.view.auth.AuthActivity;
import com.example.quickjobs.view.main.MainActivity;
import com.example.quickjobs.viewmodel.SplashViewModel;
import com.google.android.gms.location.LocationAvailability;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class SplashActivity extends AppCompatActivity implements LocationStateListener {
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
                Log.println(Log.ERROR, TAG, "Requesting location permission");
                ActivityCompat.requestPermissions(SplashActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_ID);
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
                splashViewModel.enableLocationUpdates(getApplicationContext(), SplashActivity.this);
                checkIfLocationIsAvailable();
            }
        });
    }

    private void checkPermissionResults(){
        Log.println(Log.ERROR, TAG, "Checking permission results");
        permissionManager.handlePermissionRequestResults(this, Manifest.permission.ACCESS_FINE_LOCATION, new PermissionManager.PermissionRequestResultListener() {
            @Override
            public void onPermissionGranted() {
//              todo set loading bar visible
                splashViewModel.enableLocationUpdates(getApplicationContext(), SplashActivity.this);
                checkIfLocationIsAvailable();
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

    private void checkIfLocationIsAvailable(){
//      TODO start loading screen until location is supplied exit the app after 5-10 seconds with error message.
        splashViewModel.checkIfUserHasLocationAvailable(SplashActivity.this, this);
    }

    @Override
    public void onLocationAvailable(LocationAvailability locationAvailability) {
        Log.println(Log.ERROR, TAG, "onLocationAvailable: locationAvailability");
        splashViewModel.getCurrentLocationFromLocationSource(this, this);
    }

    @Override
    public void onLocationNotAvailable() {
        Log.println(Log.ERROR, TAG, "onLocationNotAvailable: locationNotAvailable");
        if(textView.getVisibility() != View.GONE){
            textView.setVisibility(View.GONE);
            splashViewModel.setShouldMakeLoadingScreenVisible(true);
        }
        splashViewModel.checkIfUserHasLocationAvailable(SplashActivity.this, this);
    }

    @Override
    public void onLocationChange(Location locationResults) {
        getQuickJobsBasedOnUserLocation(locationResults.getLongitude(), locationResults.getLatitude());
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
            goToMainActivity();
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

    private void dialogForFailedJobs(){
        new AlertDialog.Builder(this).setTitle("Error finding jobs")
                .setMessage("retry later")
                .setCancelable(false)
                .setNegativeButton("LEAVE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                })
                .setPositiveButton("RETRY", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    splashViewModel.authenticatedUserLiveData.observe(this, user -> {
                        getQuickJobsBasedOnUserLocation(user.getLongitude(), user.getLatitude());
                    });
                })
                .show();
    }

    private void dialogBeforeForClose(){
        new AlertDialog.Builder(this).setTitle("Permission Denied")
                .setMessage("")
                .setCancelable(false)
                .setNegativeButton("RETRY", (dialogInterface, i) -> {
                    checkLocationPermission();
                })
                .setPositiveButton("CONTINUE", (dialogInterface, i) -> {
                    finish();
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