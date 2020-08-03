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

    public boolean shouldAskPermission(Context context, String[] permissions){
        if(shouldAskPermission()){
            for(String permission : permissions){
                int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
                if(permissionResult != PackageManager.PERMISSION_GRANTED){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean shouldShowRequestRational(Context context, String[] permissions){
        for(String permission: permissions){
            if(ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) context, permission)){
                return true;
            }
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

    public void checkPermission(Context context, String[] permissions, PermissionAskListnener listnener){
        if(shouldAskPermission(context, permissions)){
            if(shouldShowRequestRational(context, permissions)){
                listnener.onPermssionPreviouslyDenied();
            }
            else{
                for (String permission : permissions){
                    if(sessionManager.isFirstTimeAsking(permission)){
                        sessionManager.firstTimeAsking(permissions, false);
                        listnener.onNeedPermission();
                        return;
                    }
                    else{
                        listnener.onPermissionPreviouslyDeniedWithNeverAskAgain();
                    }
                }
            }
        }else{
            listnener.onPermissionGranted();
        }
    }

    public void handlePermissionRequestResults(Context context, String permission, PermissionRequestResultListener listener){
        if(shouldAskPermission(context, permission)){
            listener.onPermissionDenied();
        }else{
            listener.onPermissionGranted();
        }
    }

    public void handlePermissionRequestResults(Context context, String[] permissions, PermissionRequestResultListener listener){
        if(shouldAskPermission(context, permissions)){
            listener.onPermissionDenied();
        }else{
            listener.onPermissionGranted();
        }
    }

    public interface PermissionAskListnener{
        void onNeedPermission();
        void onPermssionPreviouslyDenied();
        void onPermissionPreviouslyDeniedWithNeverAskAgain();
        void onPermissionGranted();
    }

    public interface PermissionRequestResultListener{
        void onPermissionGranted();
        void onPermissionDenied();
    }
}
