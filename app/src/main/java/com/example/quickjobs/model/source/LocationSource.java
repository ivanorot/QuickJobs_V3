package com.example.quickjobs.model.source;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LocationSource extends FusedLocationProviderClient{


    public final int DEFAULT_INTERVAL = 15000;
    public final int DEFAULT_FASTEST_INTERVAL = 10000;
    public final int DEFAULT_DISPLACEMENT = 3000;

    private Observable<Location> locationObservable;
    private LocationRequest locationRequest;

    public LocationSource(@NonNull Context context) {
        super(context);

        //hot observable
        locationObservable = Observable.create(source -> {
           new LocationCallback()
           {
               @Override
               public void onLocationResult(LocationResult locationResult) {
                   super.onLocationResult(locationResult);

                   if(locationResult != null)
                   {
                       source.onNext(locationResult.getLastLocation());
                   }
               }
           };

       });
    }

    public void setDefaultSettings() {
        locationRequest.setInterval(DEFAULT_INTERVAL);
        locationRequest.setFastestInterval(DEFAULT_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setSmallestDisplacement(DEFAULT_DISPLACEMENT);
    }

    public void setIntervalMaximun(int intervalMaximun){
        locationRequest.setInterval(intervalMaximun);
    }

    public void setIntervalMinimum(int intervalMinimum){
        locationRequest.setFastestInterval(intervalMinimum);
    }

    public void setDisplacement(int displacement){
        locationRequest.setSmallestDisplacement(displacement);
    }

    public void observeLocation(Observer<Location> locationObserver){
        locationObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(locationObserver);
    }

    public void enableLocationUpdates(){
        locationRequest = LocationRequest.create();
        setDefaultSettings();
    }

    public void disableLocationUpdates(){
        locationRequest = null;
    }
}
