package com.example.quickjobs.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickjobs.R;
import com.example.quickjobs.model.User;
import com.example.quickjobs.view.main.MainActivity;
import com.example.quickjobs.view.splash.SplashActivity;
import com.example.quickjobs.viewmodel.AuthViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.util.Arrays;
import java.util.List;

public class AuthActivity extends AppCompatActivity{
    private final String TAG = "AuthActivity";
    private final String USER = "user";

    private GoogleSignInClient googleSignInClient;
    private AuthViewModel authViewModel;

    private final int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Log.println(Log.ERROR, TAG, "Auth ");

        initAuthViewModel();
        launchAuthentication();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.println(Log.ERROR, TAG, "initiated");
        Log.println(Log.ERROR, TAG, "\n" + RESULT_OK);
        Log.println(Log.ERROR, TAG, "\n" + resultCode);

        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(response != null){
                    signInWithUsingDefault(response);
                }
            }
            if(resultCode == RESULT_CANCELED){
                goToSplashActivity();
            }
        }
    }

    private void launchAuthentication()
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

    public void initAuthViewModel(){
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    public void signInWithUsingDefault(IdpResponse response){
        Log.println(Log.ERROR, TAG, "\n signInWithUsingDefault");
        authViewModel.signInDefault(response);
        authViewModel.authenticatedUserLiveData.observe(this, user ->{
            if(user.isNew()){
                Log.println(Log.ERROR, TAG, "\n  createNewUser");
                createNewUser(user);
            }
            else{
                Log.println(Log.ERROR, TAG, "\n goToMainActivity");
                goToMainActivity(user);
            }
        });
    }

    public void createNewUser(User authenticatedUser) {
        Log.println(Log.ERROR, TAG, "createNewUser()");
        authViewModel.createUser(authenticatedUser);
        authViewModel.createdUserLiveData.observe(this, this::goToMainActivity);
    }
    public void goToMainActivity(User user) {
        Log.println(Log.ERROR, TAG, "\n goToMain");
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void goToSplashActivity(){
        Intent intent = new Intent(AuthActivity.this, SplashActivity.class);
        startActivity(intent);
        Log.println(Log.ERROR, TAG, "\n intent launched");
        finish();
    }
}