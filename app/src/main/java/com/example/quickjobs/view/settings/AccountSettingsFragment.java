package com.example.quickjobs.view.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.example.quickjobs.R;
import com.example.quickjobs.helper.Constants;
import com.example.quickjobs.view.auth.AuthActivity;
import com.example.quickjobs.viewmodel.SettingsViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class AccountSettingsFragment extends PreferenceFragmentCompat implements PreferenceManager.OnPreferenceTreeClickListener{
    private final String TAG = "AccountSettingsFragment";
    private SettingsViewModel settingsViewModel;

    Preference createAccount;
    Preference resetEmail;
    Preference resetPassword;
    SwitchPreference darkMode;
    SwitchPreference locationUpdateSwitch;
    SwitchPreference imageResolution;
    Preference signOut;
    Preference deleteAccount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_account_preference, rootKey);
        getPreferenceManager().setOnPreferenceTreeClickListener(this);
        initViewModelAndCurrentUser();
        initPreferences();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        settingsViewModel.saveUserPreference(Constants.USER_PREFERENCE_CONFIGURATION, darkMode.isChecked());
        settingsViewModel.saveUserPreference(Constants.USER_PREFERENCE_LOCATION, locationUpdateSwitch.isChecked());
        settingsViewModel.saveUserPreference(Constants.USER_PREFERENCE_RESOLUTION, imageResolution.isChecked());
    }

    private void initPreferences(){
        createAccount = findPreference(getString(R.string.account_settings_create_account_key));
        resetEmail = findPreference(getString(R.string.account_settings_reset_email_key));
        resetPassword = findPreference(getString(R.string.account_settings_reset_password_key));
        darkMode = findPreference(getString(R.string.account_settings_dark_mode_key));
        locationUpdateSwitch = findPreference(getString(R.string.account_settings_location_updates_key));
        imageResolution = findPreference(getString(R.string.account_settings_image_resolution_key));
        signOut = findPreference(getString(R.string.account_settings_sign_out_key));
        deleteAccount = findPreference(getString(R.string.account_settings_delete_account_key));

        boolean isDarkModeOn = settingsViewModel.retrieveUserPreference(Constants.USER_PREFERENCE_CONFIGURATION, false);
        boolean isLocationOn = settingsViewModel.retrieveUserPreference(Constants.USER_PREFERENCE_LOCATION, true);
        boolean isFullImageResolutionOn = settingsViewModel.retrieveUserPreference(Constants.USER_PREFERENCE_RESOLUTION, true);

        Log.println(Log.ERROR, TAG, "Is dark mode on " + isDarkModeOn);
        Log.println(Log.ERROR, TAG, "Is location on " + isLocationOn);
        Log.println(Log.ERROR, TAG, "Is full resolution on " + isLocationOn);

        darkMode.setChecked(isDarkModeOn);
        locationUpdateSwitch.setChecked(isLocationOn);
        imageResolution.setChecked(isFullImageResolutionOn);

    }

    private void initViewModelAndCurrentUser(){
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        settingsViewModel.initCurrentUser();
        settingsViewModel.currentUserLiveData.observe(this, currentUser -> {
            if(!currentUser.isAnonymous()){
                show();
            }
        });
    }

    private void show(){
        if(createAccount != null){
            createAccount.setVisible(false);
        }

        if(resetEmail != null){
            resetEmail.setVisible(true);
        }

        if(resetPassword != null){
            resetPassword.setVisible(true);
        }

        if(signOut != null){
            signOut.setVisible(true);
        }

        if(deleteAccount != null){
            deleteAccount.setVisible(true);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {

        if(preference.getKey()!= null){
            if(preference.getKey().equals(createAccount.getKey())){
                goToAuthActivity();
                return true;
            }
            if(preference.getKey().equals(darkMode.getKey())){
                Log.println(Log.ERROR, TAG, preference.getKey());
                SwitchPreference switchPreference = (SwitchPreference) preference;
                changeConfigurationMode(switchPreference);
                return true;
            }
            if(preference.getKey().equals(locationUpdateSwitch.getKey())){
                SwitchPreference switchPreference = (SwitchPreference) preference;
                changeLocationUpdateFrequency(switchPreference);
                return true;
            }
            if(preference.getKey().equals(imageResolution.getKey())){
                SwitchPreference switchPreference = (SwitchPreference) preference;
                changeImageResolution(switchPreference);
                return true;
            }
            if(preference.getKey().equals(signOut.getKey())){
                Log.println(Log.ERROR, TAG, preference.getKey());
                signCurrentUserOut();
                return true;
            }

        }

        return super.onPreferenceTreeClick(preference);
    }

    public void changeConfigurationMode(SwitchPreference switchPreference){
        if(switchPreference.isChecked()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void changeLocationUpdateFrequency(SwitchPreference switchPreference){
        if(switchPreference.isChecked()){
            settingsViewModel.loadDefaultSettings(requireActivity());
        }else{
            settingsViewModel.loadLowFrequencySettings(requireActivity());
        }
    }

    public void changeImageResolution(SwitchPreference switchPreference){
//        todo change image resolution in camera activiy
    }

    public void signCurrentUserOut(){
        FirebaseAuth.getInstance().signOut();
    }

    public void goToAuthActivity(){
        Intent intent = new Intent(requireActivity(), AuthActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}