package com.example.quickjobs.source;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.quickjobs.helper.Constants;
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

    private List<LocationChangeListener> locationChangeListeners;
    private Thread locationUpdateThread;
    private LocationRunnable locationRunnable;

    private Handler locationUpdateHandler;

    private LocationCallback locationCallback;


    private LocationSource(@NonNull Application application) {
        super(application);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(DEFAULT_INTERVAL);
        locationRequest.setFastestInterval(DEFAULT_FASTEST_INTERVAL);
        locationRequest.setSmallestDisplacement(DEFAULT_DISPLACEMENT);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationChangeListeners = new ArrayList<>();


    }

    public static LocationSource getInstance(Application context) {
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

    @Override
    public void run() {
        enableLocationUpdates();
    }

    private void enableLocationUpdates(){

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for(LocationChangeListener locationChangeListener: locationChangeListeners){
                    locationChangeListener.onLocationChange(locationResult);
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

    }

    private void initThreadLooperAndHandler(){
        Looper.prepare();
        locationUpdateHandler = new Handler();
        locationUpdateHandler.post(locationRunnable);
        Looper.loop();
    }

    private class LocationRunnable implements Runnable{
        @Override
        public void run() {

        }
    }


    private class LocationThread extends Thread {

    }


    private class LocationHandler extends Handler{

    }
}
