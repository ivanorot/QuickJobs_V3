package com.example.quickjobs.view.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.example.quickjobs.R;
import com.example.quickjobs.model.helper.Constants;
import com.example.quickjobs.view.auth.AuthActivity;
import com.example.quickjobs.viewmodel.SettingsViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class AccountSettingsFragment extends PreferenceFragmentCompat implements PreferenceManager.OnPreferenceTreeClickListener, Preference.OnPreferenceChangeListener {
    private final String TAG = "AccountSettingsFragment";
    private SettingsViewModel settingsViewModel;

    SwitchPreference darkMode;
    SwitchPreference locationUpdateSwitch;
    SwitchPreference imageResolution;
    ListPreference languages;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setOnPreferenceTreeClickListener(this);
        initViewModelAndCurrentUser(rootKey);
    }

    private void initPreferences(){
        darkMode = findPreference(getString(R.string.account_settings_dark_mode_key));
        locationUpdateSwitch = findPreference(getString(R.string.account_settings_location_updates_key));
        imageResolution = findPreference(getString(R.string.account_settings_image_resolution_key));
        languages = findPreference(getString(R.string.account_settings_language_key));

        boolean isDarkModeOn = settingsViewModel.retrieveUserPreference(Constants.USER_PREFERENCE_CONFIGURATION, false);
        boolean isLocationOn = settingsViewModel.retrieveUserPreference(Constants.USER_PREFERENCE_LOCATION, true);
        boolean isFullImageResolutionOn = settingsViewModel.retrieveUserPreference(Constants.USER_PREFERENCE_RESOLUTION, true);
        String currentLanguageValue = languages.getValue();

        darkMode.setChecked(isDarkModeOn);
        locationUpdateSwitch.setChecked(isLocationOn);
        imageResolution.setChecked(isFullImageResolutionOn);
        languages.setSummary(currentLanguageValue);
        languages.setOnPreferenceChangeListener(this);

    }

    private void initViewModelAndCurrentUser(String rootKey){
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        settingsViewModel.initCurrentUser();
        settingsViewModel.currentUserLiveData.observe(this, currentUser -> {
            if(currentUser.isAnonymous()){
                setPreferencesFromResource(R.xml.pref_account_preference_anonymous, rootKey);
            }
            else{
                setPreferencesFromResource(R.xml.pref_account_preference, rootKey);
            }
            initPreferences();
        });
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {

        if(preference.hasKey()){
            Log.println(Log.ERROR, TAG, preference.getKey());
            if(preference.getKey().equals(getString(R.string.account_settings_create_account_key))){
                goToAuthActivity();
                return true;
            }
            if(preference.getKey().equals(getString(R.string.account_settings_dark_mode_key))){
                Log.println(Log.ERROR, TAG, preference.getKey());
                SwitchPreference switchPreference = (SwitchPreference) preference;
                changeConfigurationMode(switchPreference);
                return true;
            }
            if(preference.getKey().equals(getString(R.string.account_settings_location_updates_key))){
                SwitchPreference switchPreference = (SwitchPreference) preference;
                changeLocationUpdateFrequency(switchPreference);
                return true;
            }
            if(preference.getKey().equals(getString(R.string.account_settings_image_resolution_key))){
                SwitchPreference switchPreference = (SwitchPreference) preference;
                changeImageResolution(switchPreference);
                return true;
            }
            if(preference.getKey().equals(getString(R.string.account_settings_sign_out_key))){
                Log.println(Log.ERROR, TAG, preference.getKey());
                signCurrentUserOut();
                return true;
            }

        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference.getKey().equals(getString(R.string.account_settings_language_key))){
            ListPreference listPreference = (ListPreference) preference;
            changeAppDefaultLanguage(listPreference, (String) newValue);
            return true;
        }
        return false;
    }

    public void changeConfigurationMode(SwitchPreference switchPreference){
        settingsViewModel.saveUserPreference(Constants.USER_PREFERENCE_CONFIGURATION, switchPreference.isChecked());
        if(switchPreference.isChecked()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void changeAppDefaultLanguage(ListPreference listPreference, String lanuage){
        settingsViewModel.saveUserPreference(Constants.USER_LANGUAGE_PREFERENCE_NAME, listPreference.getValue());
        listPreference.setSummary(lanuage);
    }

    public void changeLocationUpdateFrequency(SwitchPreference switchPreference){
        settingsViewModel.saveUserPreference(Constants.USER_PREFERENCE_LOCATION, switchPreference.isChecked());
        if(switchPreference.isChecked()){
        }else{
        }
    }

    public void changeImageResolution(SwitchPreference switchPreference){
        settingsViewModel.saveUserPreference(Constants.USER_PREFERENCE_RESOLUTION, switchPreference.isChecked());
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