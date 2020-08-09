package com.example.quickjobs.source;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Parcel;
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

public class LocationSource extends FusedLocationProviderClient implements Runnable{
    private final String TAG = "LocationSource";
    private static LocationSource Instance;

    public final int DEFAULT_INTERVAL = 150;
    public final int DEFAULT_FASTEST_INTERVAL = 10;
    public final int DEFAULT_DISPLACEMENT = 25;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    List<LocationChangeListener> locationChangeListeners;

    Context mContext;
    Thread locationUpdateThread;
    private final String THREAD_NAME = "LocationUpdateThread";
    private LocationSource(@NonNull Context context) {
        super(context);
        mContext = context;

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(DEFAULT_INTERVAL);
        locationRequest.setFastestInterval(DEFAULT_FASTEST_INTERVAL);
        locationRequest.setSmallestDisplacement(DEFAULT_DISPLACEMENT);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationChangeListeners = new ArrayList<>();

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
                for(LocationChangeListener locationChangeListener: locationChangeListeners){
                    locationChangeListener.onLocationAvailability(locationAvailability);
                }
            }
        };

        locationUpdateThread = new Thread(this);
        locationUpdateThread.setName(THREAD_NAME);
        locationUpdateThread.setPriority(Thread.NORM_PRIORITY);
    }

    public static LocationSource getInstance(Context context) {
        if (Instance == null) {
            synchronized (LocationSource.class) {
                Instance = new LocationSource(context);
            }
        }
        return Instance;
    }

    public void registerLocationChangeListener(LocationChangeListener locationChangeListener) {
        locationChangeListeners.add(locationChangeListener);
    }

    public void unregisterLocationChangeListener(LocationChangeListener locationChangeListener) {
        locationChangeListeners.remove(locationChangeListener);
    }

    public void enableLocationUpdates(){
        locationUpdateThread.start();
    }

    @Override
    public void run() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    public void disableLocationUpdates(){
        locationUpdateThread.stop();
        removeLocationUpdates(locationCallback);
    }
}
