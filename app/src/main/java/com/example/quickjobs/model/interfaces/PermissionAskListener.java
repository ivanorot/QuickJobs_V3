package com.example.quickjobs.model.interfaces;

public interface PermissionAskListener{
    void onNeedPermission();
    void onPermssionPreviouslyDenied();
    void onPermissionPreviouslyDeniedWithNeverAskAgain();
    void onPermissionGranted();
}
