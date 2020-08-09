package com.example.quickjobs.view.splash;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.AsyncTaskLoader;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.quickjobs.R;
import com.example.quickjobs.helper.Constants;
import com.example.quickjobs.interfaces.LocationChangeListener;
import com.example.quickjobs.model.User;
import com.example.quickjobs.helper.PermissionManager;
import com.example.quickjobs.source.SharedPreferencesManager;
import com.example.quickjobs.view.auth.AuthActivity;
import com.example.quickjobs.view.main.MainActivity;
import com.example.quickjobs.viewmodel.SplashViewModel;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationResult;

import java.security.Permission;

public class SplashActivity extends AppCompatActivity implements LocationChangeListener {
    private final String TAG = "SplashActivity";

    private ProgressBar loadingProgressBar;
    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initSplashViewModel();
        checkIfUserIsAnonymousAndAuthenticated();

    }

    private void initSplashViewModel(){
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        splashViewModel.register(this);
        splashViewModel.loadUserAccountPreferences();
    }

    private void checkIfUserIsAnonymousAndAuthenticated(){
        splashViewModel.checkIfUserIsAnonymousAndAuthenticated();
        splashViewModel.isUserAnonymousOrAuthenticatedLiveData.observe(this, user -> {
            if(user.isAuthenticated() && user.isAnonymous()) {
                setCurrentUserAsAnonymous(user);
           }
            else if(user.isAuthenticated() && !user.isAnonymous()){
                getUserFromDatabase(user.getUid());
            }
            else{
                signInAnonymously();
            }
        });
    }

    private void signInAnonymously(){
        splashViewModel.signInAnonymously();
        splashViewModel.authenticatedUserLiveData.observe(this, user -> {
            if(user != null){
                setCurrentUserAsAnonymous(user);
            }
            else{
                dialogForFailedAuth();
                finish();
            }
        });
    }

    private void setCurrentUserAsAnonymous(User anonymousUser){
        splashViewModel.setAnonymousUser(anonymousUser);
        splashViewModel.authenticatedUserLiveData.observe(this, user -> {
            if(user != null){
                checkIfUserLocationIsAvailable();
            }
            else{
                dialogForFailedAuth();
                finish();
            }
        });
    }

    private void getUserFromDatabase(String inUid){
        Log.println(Log.ERROR, TAG, "getting user from database");
        splashViewModel.setAuthenticatedUser(inUid);
        splashViewModel.authenticatedUserLiveData.observe(this, user ->{
            Log.println(Log.ERROR, TAG, "getUserFromDatabase");
            if(user != null){
                enableSnapshotListeners();
                checkIfUserLocationIsAvailable();
            }
            else{
                Toast.makeText(this, "Authetification Error, Signing in Anonymously.", Toast.LENGTH_LONG).show();
                signInAnonymously();
            }
        });
    }

    private void dialogForFailedAuth(){
            new AlertDialog.Builder(this).setTitle("Authentification Failure")
                    .setMessage("This is embarrasing. For some odd reason we cannot authenticate you. Would you like to try again?")
                    .setCancelable(false)
                    .setNegativeButton("NOT NOW", ((dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        finish();
                    }))
                    .setPositiveButton("TRY AGAIN", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        checkIfUserIsAnonymousAndAuthenticated();
                    }).show();
    }

    /*
    authenticating the user either registerd or anonymous
     */

    private void checkIfUserLocationIsAvailable(){
        Log.println(Log.ERROR, TAG, "check If User Location Is Available");
        splashViewModel.checkIfCurrentUserHasLocationPersisted();
        splashViewModel.authenticatedUserLiveData.observe(this, user -> {
            if(user.hasLocationAvailable()){
                Log.println(Log.ERROR, TAG, "hasLocationAvailable");
                getQuickJobsBasedOnUserLocation(user.getLongitude(), user.getLatitude());
            }
            else{
                Log.println(Log.ERROR, TAG, "hasLocationAvailable");
                checkLocationPermission();
            }
        });
    }

    private void checkLocationPermission(){
        splashViewModel.getPermissionManager().checkPermission(this, Manifest.permission_group.LOCATION
                , new PermissionManager.PermissionAskListnener() {
                    @Override
                    public void onNeedPermission() {
                        Log.println(Log.ERROR, TAG, "Requesting location permission");
                        ActivityCompat.requestPermissions(SplashActivity.this, new String[] {Manifest.permission_group.LOCATION}, LOCATION_REQUEST_ID);
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
                        splashViewModel.enableLocationUpdates();
                    }
                });
    }


    @Override
    public void onLocationChange(LocationResult locationResults) {
        if(locationResults.getLastLocation() != null){
            updateUserLocationAndPersistToCloud(locationResults.getLastLocation());
        }
    }

    @Override
    public void onLocationAvailability(LocationAvailability locationAvailability) {
        if(!locationAvailability.isLocationAvailable()){
            makeProgressBarVisibleAndWaitForLocation();
        }
    }

    public void updateUserLocationAndPersistToCloud(Location location){
        Log.println(Log.ERROR, TAG, "updateUserLocationAndPersistToCloud");
        splashViewModel.updateUserLocationAndPersistToCloud(location);
        splashViewModel.authenticatedUserLiveData.observe(this, updatedUser -> {
            getQuickJobsBasedOnUserLocation(updatedUser.getLongitude(), updatedUser.getLatitude());
        });
    }

    public void updateUserWithMockLocationAndPersistToCloud(double latitude, double longitude){
        Log.println(Log.ERROR, TAG, "updateUserLocationAndPersistToCloud");
        splashViewModel.updateUserWithMockLocationAndPersistToCloud(latitude, longitude);
        splashViewModel.authenticatedUserLiveData.observe(this, updatedUser -> {
            getQuickJobsBasedOnUserLocation(updatedUser.getLongitude(), updatedUser.getLatitude());
        });
    }

    public void getQuickJobsBasedOnUserLocation(double longitude, double latitude){
        final int MAX_DISTANCE = 25;
        splashViewModel.getJobsBasedOnUserLocation(longitude, latitude, MAX_DISTANCE);
        splashViewModel.jobsBasedOnUserLocation.observe(this, jobs ->
            goToMainActivity());
    }

    public void enableSnapshotListeners(){
        splashViewModel.enableSnapshotListeners();
    }

    public void checkIfUserLocationHasBeenFound(){
        splashViewModel.authenticatedUserLiveData.observe(this, user->{
            if(!user.hasLocationAvailable()){
                updateUserWithMockLocationAndPersistToCloud(Constants.MOCK_LATITUDE, Constants.MOCK_LONGITUDE);
            }
        });
    }

    public void makeProgressBarVisibleAndWaitForLocation(){

        loadingProgressBar = findViewById(R.id.contentLoadingProgressBar);
        loadingProgressBar.setProgress(0);
        loadingProgressBar.setVisibility(View.VISIBLE);

        CountDownTimer countDownTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                loadingProgressBar.setVisibility(View.GONE);
                checkIfUserLocationHasBeenFound();
            }
        };

        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashViewModel.unregister(this);
    }

    private final int LOCATION_REQUEST_ID = 180;

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
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission_group.LOCATION}, LOCATION_REQUEST_ID);
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

    private void goToSettings(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + getPackageName());
        intent.setData(uri);
        startActivity(intent);
    }


    private void goToMainActivity(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}