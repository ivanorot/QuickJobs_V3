package com.example.quickjobs.model.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.quickjobs.model.source.SharedPreferencesManager;

public class SessionManager {

    private SharedPreferences.Editor sharedPreferencesEditor;


    private SharedPreferencesManager sharedPreferencesManager;

    private final String MY_REF = "my_preferences";

    public SessionManager(Context context){
        sharedPreferencesManager = SharedPreferencesManager.getInstance(context);
    }

    public void firstTimeAsking(String permission, boolean isFirstTimeAsking){
        sharedPreferencesManager.updatePermissionPreferences(permission, isFirstTimeAsking);
    }

    public boolean isFirstTimeAsking(String permission){
        return sharedPreferencesManager.getValueFromPermissionPreferences(permission, true);
    }

    public void firstTimeAsking(String[] permissions, boolean isFirstTimeAsking){
        for(String permission: permissions){
            sharedPreferencesManager.updatePermissionPreferences(permission, isFirstTimeAsking);
        }
    }
}
