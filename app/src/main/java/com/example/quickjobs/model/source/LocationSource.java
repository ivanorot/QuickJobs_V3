package com.example.quickjobs.model.source;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.quickjobs.model.helper.Constants;
import com.example.quickjobs.model.interfaces.UserLocationCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.util.ArrayList;
import java.util.List;

public class LocationSource extends FusedLocationProviderClient implements Runnable{
    private final String TAG = "LocationSource";
//    private static LocationSource Instance;

    public final int DEFAULT_INTERVAL = 150;
    public final int DEFAULT_FASTEST_INTERVAL = 10;
    public final int DEFAULT_DISPLACEMENT = 25;

    private LocationRequest locationRequest;
    private Application mApplication;

    private List<UserLocationCallback> userLocationCallbacks;
    private LocationCallback locationCallback;

    private Looper locationUpdateLooper;
    private Handler locationUpdateHandler;
    private volatile boolean stopThread = false;

    public LocationSource(@NonNull Application application) {
        super(application);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(DEFAULT_INTERVAL);
        locationRequest.setFastestInterval(DEFAULT_FASTEST_INTERVAL);
        locationRequest.setSmallestDisplacement(DEFAULT_DISPLACEMENT);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        userLocationCallbacks = new ArrayList<>();

        mApplication = application;

        locationUpdateHandler = new Handler(Looper.getMainLooper());
    }

//    public static LocationSource getInstance(Application context) {
//        if (Instance == null) {
//            synchronized (LocationSource.class) {
//                Instance = new LocationSource(context);
//            }
//        }
//        return Instance;
//    }

    private void initLooperAndHandler(){
        Looper.prepare();

        Looper.loop();

        locationUpdateLooper = Looper.myLooper();
    }

    public void registerLocationChangeListener(UserLocationCallback userLocationCallback) {
        userLocationCallbacks.add(userLocationCallback);
    }

    public void unregisterLocationChangeListener(UserLocationCallback userLocationCallback) {
        userLocationCallbacks.remove(userLocationCallback);
    }

    @Override
    public void run() {
        if(stopThread){
            return;
        }
        enableLocationUpdates();
    }

    private void enableLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(mApplication, Constants.FINE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(mApplication, Constants.COARSE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED){

            locationCallback = new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    locationUpdateHandler.post(() -> {
                        for(UserLocationCallback userLocationCallback : userLocationCallbacks){
                            userLocationCallback.onLocationChange(locationResult);
                        }
                    });
                }

                @Override
                public void onLocationAvailability(LocationAvailability locationAvailability) {
                    super.onLocationAvailability(locationAvailability);
                    locationUpdateHandler.post(() -> {
                        for(UserLocationCallback userLocationCallback : userLocationCallbacks){
                            userLocationCallback.onLocationAvailability(locationAvailability);
                        }
                    });
                }
            };

            requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
        }
    }

    private void disableLocationUpdates(){
        if(locationCallback != null){
            removeLocationUpdates(locationCallback);
        }
    }
}
