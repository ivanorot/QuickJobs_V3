package com.example.quickjobs.view.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.quickjobs.R;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.view.auth.AuthActivity;
import com.example.quickjobs.view.main.MainActivity;
import com.example.quickjobs.viewmodel.SplashViewModel;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = "Quickjobs";
    private final String USER = "user";
    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initSplashViewModel();
        checkIfUserIsAuthenticated();
    }

    private void initSplashViewModel(){
        Log.println(Log.ERROR, TAG, "initSplashViewModel()");
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    }

    private void checkIfUserIsAuthenticated(){
        Log.println(Log.ERROR, TAG, "checkIfUserIsAuthenticated()");
        splashViewModel.checkIfUserIsAuthenticated();
        splashViewModel.isUserAuthenticatedLiveData.observe(this, user -> {
           if(!user.isAuthenticated()){
               goToAuthActivity();
               finish();
           }
           else {
               getUserFromDatabase(user.getUid());
           }
        });
    }

    private void getUserFromDatabase(String inUid){
        Log.println(Log.ERROR, TAG, "getUserFromDatabase()");
        splashViewModel.setUid(inUid);
        splashViewModel.userLiveData.observe(this, user ->{
           goToMainActivity(user);
           finish();
        });
    }


    private void goToAuthActivity(){
        Log.println(Log.ERROR, TAG, "goToAuthInActivity()");
        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainActivity(User user){
        Log.println(Log.ERROR, TAG, "goToMainActivity()");
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra(USER, user);
        startActivity(intent);
        finish();
    }
}