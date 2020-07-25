package com.example.quickjobs.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private final String MY_REF = "my_preferences";

    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences(MY_REF, Context.MODE_PRIVATE);
    }

    public void firstTimeAsking(String permission, boolean isFirstTimeAsking){
        doEdit();
        sharedPreferencesEditor.putBoolean(permission, isFirstTimeAsking);
        doCommit();
    }

    public boolean isFirstTimeAsking(String permission){
        return sharedPreferences.getBoolean(permission, true);
    }

    private void doEdit(){
        if(sharedPreferencesEditor == null){
            synchronized (SharedPreferences.Editor.class){
                sharedPreferencesEditor = sharedPreferences.edit();
            }
        }
    }

    private void doCommit(){
        if(sharedPreferencesEditor != null){
            sharedPreferencesEditor.commit();
            sharedPreferencesEditor = null;

        }
    }
}
