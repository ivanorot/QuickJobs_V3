package com.example.quickjobs.model.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.quickjobs.model.helper.Constants;

public class SharedPreferencesManager implements android.content.SharedPreferences.OnSharedPreferenceChangeListener {
    private final String TAG = "SharedPrefManager";

    private static SharedPreferencesManager Instance;

    private SharedPreferences userSettingsSharedPreferences;
    private SharedPreferences userLanguageSharedPreferences;
    private SharedPreferences permissionStatePreferences;

    private SharedPreferences.Editor userSettingsSharedPreferencesEditor;
    private SharedPreferences.Editor userLanguageSharedPreferencesEditor;
    private SharedPreferences.Editor permissionStateSharedPreferencesEditor;

    private SharedPreferencesManager(Context context){
        userSettingsSharedPreferences = context.getSharedPreferences(Constants.USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        userLanguageSharedPreferences = context.getSharedPreferences(Constants.USER_LANGUAGE_PREFERENCE_NAME, Context.MODE_PRIVATE);
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
        if(userSettingsSharedPreferencesEditor == null){
            synchronized (SharedPreferences.Editor.class){
                userSettingsSharedPreferencesEditor = userSettingsSharedPreferences.edit();
            }
        }
        userSettingsSharedPreferencesEditor.putBoolean(preferenceKey, preferenceState);
        userSettingsSharedPreferencesEditor.apply();
    }

    public void updateUserPreferences(String languageKey, String currentLanguage){
        if(userLanguageSharedPreferencesEditor == null){
            synchronized (SharedPreferences.Editor.class){
                userLanguageSharedPreferencesEditor = userLanguageSharedPreferences.edit();
            }
        }
        userLanguageSharedPreferencesEditor.putString(languageKey, currentLanguage);
        userLanguageSharedPreferencesEditor.apply();
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

    public String getValueFromUserLanguagePreferences(){
        final String DEFAULTVALUE = "English";
        return userLanguageSharedPreferences.getString(Constants.USER_LANGUAGE_PREFERENCE_NAME, DEFAULTVALUE);
    }

    public Boolean getValueFromPermissionPreferences(String permission, Boolean defaultState){
        return permissionStatePreferences.getBoolean(permission, defaultState);
    }


}
