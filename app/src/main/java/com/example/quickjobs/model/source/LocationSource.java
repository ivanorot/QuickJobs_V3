package com.example.quickjobs.model.source;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.helper.ExceptionHandler;
import com.example.quickjobs.interfaces.LocationStateListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;

public class LocationSource extends FusedLocationProviderClient{
    private final String TAG = "LocationSource";
    private static LocationSource Instance;

    public final int DEFAULT_INTERVAL = 15000;
    public final int DEFAULT_FASTEST_INTERVAL = 10000;
    public final int DEFAULT_DISPLACEMENT = 3000;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    List<LocationStateListener> locationStateListeners;

    private LocationSource(@NonNull Context context) {
        super(context);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(DEFAULT_INTERVAL);
        locationRequest.setFastestInterval(DEFAULT_FASTEST_INTERVAL);
        locationRequest.setSmallestDisplacement(DEFAULT_DISPLACEMENT);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationStateListeners = new ArrayList<>();

    }

    public void register(LocationStateListener locationStateListener){
        locationStateListeners.add(locationStateListener);
    }

    public void unregister(LocationStateListener locationStateListener){
        locationStateListeners.remove(locationStateListener);
    }

    public static LocationSource getInstance(Context context) {
        if (Instance == null) {
            synchronized (LocationSource.class) {
                Instance = new LocationSource(context);
            }
        }
        return Instance;
    }

    public void setDefaultSettings() {
        locationRequest.setInterval(DEFAULT_INTERVAL);
        locationRequest.setFastestInterval(DEFAULT_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setSmallestDisplacement(DEFAULT_DISPLACEMENT);
    }

    public void setIntervalMaximun(int intervalMaximun) {
        locationRequest.setInterval(intervalMaximun);
    }

    public void setIntervalMinimum(int intervalMinimum) {
        locationRequest.setFastestInterval(intervalMinimum);
    }

    public void setDisplacement(int displacement) {
        locationRequest.setSmallestDisplacement(displacement);
    }

    public void enableLocationUpdates(Context context, LocationStateListener locationStateListener) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Log.println(Log.ERROR, TAG, locationResult.getLastLocation() + "");
                    for(LocationStateListener temp : locationStateListeners){
                        temp.onLocationChange(locationResult.getLastLocation());
                    }
                }

                @Override
                public void onLocationAvailability(LocationAvailability locationAvailability) {
                    super.onLocationAvailability(locationAvailability);
//                    locationStateListener.onLocationAvailable(locationAvailability);
                }
            };
            requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    public void getLocationAvailability(Context context, LocationStateListener locationStateListener) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getLocationAvailability().addOnCompleteListener(locationAvailability -> {
                if (locationAvailability.isSuccessful() && locationAvailability.getResult() != null) {
                    if (locationAvailability.getResult().isLocationAvailable()) {
                        locationStateListener.onLocationAvailable(locationAvailability.getResult());
                    }
                }
            });
        }
    }

    public void getSingleLocationMutableLiveData(Context context, LocationStateListener locationStateListener) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            getLastLocation().addOnCompleteListener(location -> locationStateListener.onLocationChange(location.getResult()));

        }
    }
}
