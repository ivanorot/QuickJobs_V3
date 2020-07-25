package com.example.quickjobs.helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

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
        final String TAG = "checkPermission";
        if(shouldAskPermission(context, permission)){
            if(ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) context, permission)){
                Log.println(Log.ERROR, TAG, "onPermssionPreviouslyDenied");
                listnener.onPermssionPreviouslyDenied();
            }
            else{
                if(sessionManager.isFirstTimeAsking(permission)){
                    Log.println(Log.ERROR, TAG, "onNeedPermission");
                    sessionManager.firstTimeAsking(permission, false);
                    listnener.onNeedPermission();
                }
                else{
                    Log.println(Log.ERROR, TAG, "onPermissionPreviouslyDeniedWithNeverAskAgain");
                    listnener.onPermissionPreviouslyDeniedWithNeverAskAgain();
                }
            }
        }
        else{
            Log.println(Log.ERROR, TAG, "onPermissionGranted");
            listnener.onPermissionGranted();
        }
    }

    public void checkPermissionUpdate(Context context, String permission, PermissionUpdateListener lisener){
        String TAG = "checkPermissionUpdate";
        Log.println(Log.ERROR, TAG, "" + ContextCompat.checkSelfPermission(context, permission));

        if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED){
            lisener.onPermissionGranted();
        }
        else{
            lisener.onPermissionDenied();
        }

    }

    public interface PermissionAskListnener{
        void onNeedPermission();
        void onPermssionPreviouslyDenied();
        void onPermissionPreviouslyDeniedWithNeverAskAgain();
        void onPermissionGranted();
    }

    public interface PermissionUpdateListener{
        void onPermissionGranted();
        void onPermissionDenied();
    }
}
