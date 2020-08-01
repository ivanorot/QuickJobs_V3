package com.example.quickjobs.view.settings;

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
import com.example.quickjobs.viewmodel.SettingsViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class AccountSettingsFragment extends PreferenceFragmentCompat implements PreferenceManager.OnPreferenceTreeClickListener{
    private final String TAG = "AccountSettingsFragment";
    private SettingsViewModel settingsViewModel;

    Preference createAccount;
    Preference resetEmail;
    Preference resetPassword;
    Preference signOut;
    Preference deleteAccount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModelAndCurrentUser();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_account_preference, rootKey);
        getPreferenceManager().setOnPreferenceTreeClickListener(this);
        initPreferences();
    }

    public void initPreferences(){
        createAccount = findPreference(getString(R.string.account_settings_create_account_key));
        resetEmail = findPreference(getString(R.string.account_settings_reset_email_key));
        resetPassword = findPreference(getString(R.string.account_settings_reset_password_key));
        signOut = findPreference(getString(R.string.account_settings_sign_out_key));
        deleteAccount = findPreference(getString(R.string.account_settings_delete_account_key));
    }

    public void initViewModelAndCurrentUser(){
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        settingsViewModel.initCurrentUser();
        settingsViewModel.currentUserLiveData.observe(this, currentUser -> {

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
            if(preference.getKey().equals(getString(R.string.account_settings_sign_out_key))){
                Log.println(Log.ERROR, TAG, preference.getKey());
                signCurrentUserOut();
                return true;
            }else if(preference.getKey().equals(getString(R.string.account_settings_dark_mode_key))){
                Log.println(Log.ERROR, TAG, preference.getKey());
                SwitchPreference switchPreference = (SwitchPreference) preference;
                changeConfigurationMode(switchPreference);
            }
        }

        return super.onPreferenceTreeClick(preference);
    }

    public void resetUserEmailAndPersistToCloud(CharSequence email){

    }

    public void changeConfigurationMode(SwitchPreference switchPreference){
        if(switchPreference.isChecked()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void changeLocationUpdateFrequency(){

    }

    public void changeImageResolution(){

    }

    public void signCurrentUserOut(){
        FirebaseAuth.getInstance().signOut();
    }

    public void pauseCurrentUserAccount(){

    }
}