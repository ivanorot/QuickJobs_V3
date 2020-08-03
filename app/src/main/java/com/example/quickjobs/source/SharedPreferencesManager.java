package com.example.quickjobs.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.AndroidResources;
import androidx.preference.Preference;

import com.example.quickjobs.R;
import com.example.quickjobs.helper.Constants;

import io.grpc.internal.SharedResourceHolder;

public class SharedPreferencesManager implements android.content.SharedPreferences.OnSharedPreferenceChangeListener {
    private final String TAG = "SharedPrefManager";

    private static SharedPreferencesManager Instance;

    private SharedPreferences userSettingsSharedPreferences;
    private SharedPreferences permissionStatePreferences;

    private SharedPreferences.Editor userSettingsSharedPreferencesEditor;
    private SharedPreferences.Editor permissionStateSharedPreferencesEditor;

    private SharedPreferencesManager(Context context){
        userSettingsSharedPreferences = context.getSharedPreferences(Constants.USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        permissionStatePreferences  = context.getSharedPreferences(Constants.APP_PERMISSION_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesManager getInstance(Context context){
        if(Instance == null){
            synchronized (SharedPreferencesManager.class){
                Instance = new SharedPreferencesManager(context);
            }
        }
        return Instance;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.println(Log.ERROR, TAG, "preference changed: " + s);
    }

    public void updateUserPreferences(String preferenceKey, Boolean preferenceState){
        if(userSettingsSharedPreferences == null){
            synchronized (SharedPreferences.Editor.class){
                userSettingsSharedPreferencesEditor = userSettingsSharedPreferences.edit();
            }
        }
        userSettingsSharedPreferencesEditor.putBoolean(preferenceKey, preferenceState);
        userSettingsSharedPreferencesEditor.apply();
    }

    public void updatePermissionPreferences(String permissionKey, Boolean permissionState){
        if(permissionStateSharedPreferencesEditor == null){
            synchronized (SharedPreferences.Editor.class){
                permissionStateSharedPreferencesEditor = permissionStatePreferences.edit();
            }
        }
        permissionStateSharedPreferencesEditor.putBoolean(permissionKey, permissionState);
        permissionStateSharedPreferencesEditor.apply();
    }

    public Boolean getValueFromUserPreferences(String preferenceKey, Boolean defaultValue){
        return userSettingsSharedPreferences.getBoolean(preferenceKey, defaultValue);
    }

    public Boolean getValueFromPermissionPreferences(String permission, Boolean defaultState){
        return permissionStatePreferences.getBoolean(permission, defaultState);
    }


}
