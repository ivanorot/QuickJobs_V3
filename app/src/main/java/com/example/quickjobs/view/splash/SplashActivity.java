package com.example.quickjobs.view.splash;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickjobs.R;
import com.example.quickjobs.model.helper.Constants;
import com.example.quickjobs.model.interfaces.UserLocationCallback;
import com.example.quickjobs.model.interfaces.PermissionAskListener;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.interfaces.PermissionResultsListener;
import com.example.quickjobs.view.main.MainActivity;
import com.example.quickjobs.viewmodel.SplashViewModel;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationResult;

public class SplashActivity extends AppCompatActivity implements UserLocationCallback, Animator.AnimatorListener {
    private final String TAG = "SplashActivity";
    private final String BACKGROUND_THREAD_NAME = "INIT_DATA_THREAD";

    private HandlerThread initDataHandlerThread_Background;
    private Handler initDataHandler_Background;

    private ProgressBar loadingProgressBar;
    private SplashViewModel splashViewModel;

    private TextView textView;
    private ObjectAnimator animation;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViewAndAnimation();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashViewModel.unregister(this);
    }

    private void initViewAndAnimation(){
        textView = findViewById(R.id.quickJobsText);
        progressBar = findViewById(R.id.progress_bar);
        animation = ObjectAnimator.ofFloat(textView, "translationX", 0f, 100f, -100f, 0f);
        animation.addListener(this);
        animation.setDuration(2000);

    }

    private void initSplashViewModel(){
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        splashViewModel.register(this);
        splashViewModel.loadUserAccountPreferences();

    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {
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
        Log.println(Log.ERROR, TAG, "check if user location is available");
        splashViewModel.checkIfCurrentUserHasLocationPersisted();
        splashViewModel.authenticatedUserLiveData.observe(this, user -> {
            if(user.hasLocationAvailable()){
                Log.println(Log.ERROR, TAG, "has location available");
                getQuickJobsBasedOnUserLocation(user.getLongitude(), user.getLatitude());
            }
            else{
                Log.println(Log.ERROR, TAG, "Does not have location available");
                checkLocationPermission();
            }
        });
    }

    private void checkLocationPermission(){
        splashViewModel.getPermissionManager().checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, new PermissionAskListener() {
                    @Override
                    public void onNeedPermission() {
                        Log.println(Log.ERROR, TAG, "requesting location permission");
                        ActivityCompat.requestPermissions(SplashActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_ID);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean wasPermissionGranted = true;


        for(int results : grantResults){
            Log.println(Log.ERROR, TAG, "results " + results);

            if(results == -1){
                wasPermissionGranted = false;
                break;
            }
        }

        checkPermissionRequestResults(wasPermissionGranted);

    }

    public void checkPermissionRequestResults(boolean wasGranted){
        splashViewModel.getPermissionManager().handlePermissionRequestResults(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION, new PermissionResultsListener() {
            @Override
            public void onPermissionGranted() {
                splashViewModel.enableLocationUpdates();
            }

            @Override
            public void onPermissionDenied() {
//                set mock location
                dialogForDeniedPermissionResults();
            }
        });
    }

    @Override
    public void onLocationChange(LocationResult locationResults) {

        for(Location location : locationResults.getLocations()){
            Log.println(Log.ERROR, TAG, "Location Accuracy: " + location.getAccuracy());
        }

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

    public void updateUserWithMockLocationAndPersistToCloud(){
        Log.println(Log.ERROR, TAG, "Setting Mock Location");
        splashViewModel.updateUserWithMockLocationAndPersistToCloud(Constants.MOCK_LATITUDE, Constants.MOCK_LONGITUDE);
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
                updateUserWithMockLocationAndPersistToCloud();
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
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_ID);
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

    private void dialogForDeniedPermissionResults(){
        new AlertDialog.Builder(this).setTitle("Permission Denied")
                .setMessage("To find jobs in your area we need your location. However, Trust is earned, ")
                .setCancelable(false)
                .setNegativeButton("NOT NOW", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    updateUserWithMockLocationAndPersistToCloud();
                }))
                .setPositiveButton("CONTINUE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_ID);
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