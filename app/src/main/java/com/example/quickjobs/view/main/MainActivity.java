package com.example.quickjobs.view.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.quickjobs.R;
import com.example.quickjobs.model.user.QuickJobsUser;
import com.example.quickjobs.viewmodel.MainViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    private MainViewModel mainViewModel;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    private final int RC_SIGN_IN = 0;
    private final int RC_SUCCESS = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getCurrentUser().observe(this, this::printUserData);
        mainViewModel.getShouldLaunchAuthentication().observe(this, this::launchAuthentication);
        mainViewModel.getTranslateFireTOQJStatus().observe(this, shouldTranslate -> translateFirebaseToQJ(shouldTranslate, firebaseUser));


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null)
        {
            mainViewModel.setTranslateFireToQJStatus(true);
        }
        else
        {
            mainViewModel.setShouldLaunchAuthentication(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN && resultCode == RESULT_OK)
        {
            firebaseUser = firebaseAuth.getCurrentUser();

            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);

            if(idpResponse != null && idpResponse.isNewUser())
            {
                mainViewModel.createNewUserInFireStore(firebaseUser);
            }

            mainViewModel.setShouldLaunchAuthentication(false);
        }
    }

    private void launchAuthentication(boolean inShouldLaunch)
    {
        if(inShouldLaunch)
        {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.PhoneBuilder().build()
            );

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(false)
                            .setTheme(R.style.QuickJobsFirebaseAuth)
                            .build(),
                    RC_SIGN_IN
            );
        }
    }

    private void printUserData(QuickJobsUser quickJobsUser)
    {
        Log.println(Log.ERROR, TAG, quickJobsUser.getDisplayName());
    }

    private void translateFirebaseToQJ(Boolean inShouldTranslate, FirebaseUser firebaseUser)
    {
        if(inShouldTranslate)
        {
            mainViewModel.translateToQuickJobsUser(firebaseUser);
            mainViewModel.setTranslateFireToQJStatus(false);
        }
    }
}
