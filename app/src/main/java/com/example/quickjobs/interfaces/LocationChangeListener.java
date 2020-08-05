package com.example.quickjobs.interfaces;

import android.location.Location;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationResult;

public interface LocationChangeListener {
    void onLocationChange(LocationResult locationResults);
    void onLocationAvailability(LocationAvailability locationAvailability);
}
