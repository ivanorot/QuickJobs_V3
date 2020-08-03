package com.example.quickjobs.view.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.quickjobs.R;
import com.example.quickjobs.helper.Constants;
import com.example.quickjobs.helper.Enumerations;
import com.example.quickjobs.view.splash.SplashActivity;
import com.example.quickjobs.viewmodel.SettingsViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback, FirebaseAuth.AuthStateListener{
    private static String TITLE_TAG = "accountSettingsTitle";


    private SettingsViewModel settingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FirebaseAuth.getInstance().addAuthStateListener(this);
        initViewModel();
        initSettingsFragment(savedInstanceState);
        initActionBar();

    }

    private void initViewModel(){
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.initCurrentUser();
        settingsViewModel.currentUserLiveData.observe(this, currentUser ->{
        });;
    }

    private void initSettingsFragment(Bundle savedInstanceState){
        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.account_preferences, new AccountSettingsFragment())
                    .commit();
        } else
        {
            setTitle(savedInstanceState.getCharSequence(TITLE_TAG));
        }

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                setTitle(R.string.account_settings_title);
            }
        });
    }

    private void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(TITLE_TAG, getTitle());
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(getSupportFragmentManager().popBackStackImmediate()){
            return true;
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());

        fragment.setArguments(args);
        fragment.setTargetFragment(caller,0);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.account_preferences, fragment)
                .addToBackStack(null)
                .commit();
        setTitle(pref.getTitle());

        return true;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        Log.println(Log.ERROR, "onAuthStateChanged","signing user out");
        if(firebaseAuth.getCurrentUser() == null){
            goToSplashScreen();
        }
    }

    public void goToSplashScreen(){
        Intent intent = new Intent(SettingsActivity.this, SplashActivity.class);
        startActivity(intent);
        finish();
    }
}