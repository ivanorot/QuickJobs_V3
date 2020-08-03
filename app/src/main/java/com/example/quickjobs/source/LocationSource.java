package com.example.quickjobs.source;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.quickjobs.interfaces.LocationChangeListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.util.ArrayList;
import java.util.List;

public class LocationSource extends FusedLocationProviderClient {
    private final String TAG = "LocationSource";
    private static LocationSource Instance;

    public final int DEFAULT_INTERVAL = 15000;
    public final int DEFAULT_FASTEST_INTERVAL = 10000;
    public final int DEFAULT_DISPLACEMENT = 3000;

    public final int LOW_FREQ_INTERVAL = 35000;
    public final int LOW_FREQ_FASTEST_INTERVAL = 25000;
    public final int LOW_FREQ_DISPLACEMENT = 5000;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    List<LocationChangeListener> locationChangeListeners;

    private LocationSource(@NonNull Context context) {
        super(context);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(DEFAULT_INTERVAL);
        locationRequest.setFastestInterval(DEFAULT_FASTEST_INTERVAL);
        locationRequest.setSmallestDisplacement(DEFAULT_DISPLACEMENT);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationChangeListeners = new ArrayList<>();

    }

    public void registerLocationChangeListener(LocationChangeListener locationChangeListener) {
        locationChangeListeners.add(locationChangeListener);
    }

    public void unregisterLocationChangeListener(LocationChangeListener locationChangeListener) {
        locationChangeListeners.remove(locationChangeListener);
    }

    public static LocationSource getInstance(Context context) {
        if (Instance == null) {
            synchronized (LocationSource.class) {
                Instance = new LocationSource(context);
            }
        }
        return Instance;
    }

    public void setDefaultSettings(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationRequest = LocationRequest.create();
            locationRequest.setInterval(DEFAULT_INTERVAL);
            locationRequest.setFastestInterval(DEFAULT_FASTEST_INTERVAL);
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            locationRequest.setSmallestDisplacement(DEFAULT_DISPLACEMENT);

            requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }

    }

    public void setLowFrequencySettings(Context context){
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            locationRequest = LocationRequest.create();
            locationRequest.setInterval(LOW_FREQ_INTERVAL);
            locationRequest.setFastestInterval(LOW_FREQ_FASTEST_INTERVAL);
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            locationRequest.setSmallestDisplacement(LOW_FREQ_DISPLACEMENT);

            requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
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

    public void enableLocationUpdates(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Log.println(Log.ERROR, TAG, locationResult.getLastLocation() + "");
                    for(LocationChangeListener temp : locationChangeListeners){
                        temp.onLocationChange(locationResult);
                    }
                }

                @Override
                public void onLocationAvailability(LocationAvailability locationAvailability) {
                    super.onLocationAvailability(locationAvailability);
                    Log.println(Log.ERROR, TAG, "Is Location Available" + locationAvailability.isLocationAvailable());
                }
            };
            requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }
}
