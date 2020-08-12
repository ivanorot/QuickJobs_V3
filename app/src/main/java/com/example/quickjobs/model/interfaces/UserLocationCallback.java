package com.example.quickjobs.model.interfaces;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationResult;

public interface UserLocationCallback {
    void onLocationChange(LocationResult locationResults);
    void onLocationAvailability(LocationAvailability locationAvailability);
}
