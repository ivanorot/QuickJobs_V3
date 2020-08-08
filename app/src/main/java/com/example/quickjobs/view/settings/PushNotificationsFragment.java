package com.example.quickjobs.view.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.quickjobs.R;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class PushNotificationsFragment extends PreferenceFragmentCompat implements PreferenceManager.OnPreferenceTreeClickListener, SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_push_notifications, rootKey);

        getPreferenceManager().setOnPreferenceTreeClickListener(this);
//        getPreferenceManager().setSharedPreferencesName(Constants.USER_PREFERENCE_NAME);
//        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);

        getPreferenceManager().getSharedPreferences();

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        final String TAG = "onPreferenceTreeClick";
        Log.println(Log.ERROR, TAG, "here");

        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        final String TAG = "onSharedPrefChanged";
        Log.println(Log.ERROR, TAG, s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}