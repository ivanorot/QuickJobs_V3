package com.example.quickjobs.model.source;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.helper.ExceptionHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class LocationSource extends FusedLocationProviderClient {
    private final String TAG = "LocationSource";
    private static LocationSource Instance;

    public final int DEFAULT_INTERVAL = 15000;
    public final int DEFAULT_FASTEST_INTERVAL = 10000;
    public final int DEFAULT_DISPLACEMENT = 3000;

    private Observable<LocationResult> locationObservable;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private LocationSource(@NonNull Context context) {
        super(context);

        locationRequest = LocationRequest.create();

        locationRequest.setInterval(DEFAULT_INTERVAL);
        locationRequest.setFastestInterval(DEFAULT_FASTEST_INTERVAL);

        locationRequest.setSmallestDisplacement(DEFAULT_DISPLACEMENT);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


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

    public MutableLiveData<LocationAvailability> getLocationAvailabilityMutableLiveData(Context context) {
        MutableLiveData<LocationAvailability> locationAvailabilityMutableLiveData = new MutableLiveData<>();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new IllegalStateException();
        }

        getLocationAvailability().addOnCompleteListener(locAvailableTask -> {
            if (locAvailableTask.isSuccessful()) {
                locationAvailabilityMutableLiveData.postValue(locAvailableTask.getResult());
            } else {
                if (locAvailableTask.getException() != null) {
                    ExceptionHandler.consumeException(TAG, locAvailableTask.getException());
                }
            }
        });

        return locationAvailabilityMutableLiveData;
    }


    public void observeLocationUpdatesContinousStream(Context context, Observer<LocationResult> locationResultObserver) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationObservable = Observable.create(source -> {
                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        Location location = locationResult.getLastLocation();
                        Log.println(Log.ERROR, TAG, "latitude: " + location.getLatitude() + " longitude: " + location.getLongitude());
                        source.onNext(locationResult);
                    }
                };

                requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

            });

            locationObservable.subscribe(locationResultObserver);
        }

    }

    public MutableLiveData<Location> getSingleLocationMutableLiveData(Context context){
        MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationCallback = new LocationCallback();
            requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            getLastLocation().addOnCompleteListener(locationTask ->{
                if(locationTask.isSuccessful()){
                    Location location = locationTask.getResult();
                    locationMutableLiveData.setValue(location);
                }
            });
            removeLocationUpdates(locationCallback);
        }

        return locationMutableLiveData;
    }

    public void disableLocationUpdates(){
        if(locationCallback != null){
            removeLocationUpdates(locationCallback);
        }
    }
}
