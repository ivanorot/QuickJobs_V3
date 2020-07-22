package com.example.quickjobs.model.helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionManager {
    private SessionManager sessionManager;

    public PermissionManager(Context context){
        sessionManager = new SessionManager(context);
    }

    public boolean shouldAskPermission(){
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public boolean shouldAskPermission(Context context, String permission){
        if(shouldAskPermission()){
            int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
            return permissionResult != PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void checkPermission(Context context, String permission, PermissionAskListnener listnener){
        if(shouldAskPermission(context, permission)){
            if(ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) context, permission)){
                listnener.onPermssionPreviouslyDenied();
            }
            else{
                if(sessionManager.isFirstTimeAsking(permission)){
                    sessionManager.firstTimeAsking(permission, false);
                    listnener.onNeedPermission();
                }
                else{
                    listnener.onPermissionPreviouslyDeniedWithNeverAskAgain();
                }
            }
        }
        else{
            listnener.onPermissionGranted();
        }
    }

    public interface PermissionAskListnener{
        void onNeedPermission();
        void onPermssionPreviouslyDenied();
        void onPermissionPreviouslyDeniedWithNeverAskAgain();
        void onPermissionGranted();
    }
}
