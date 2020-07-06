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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = "SplashActivity";
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
        splashViewModel.setUid(inUid);
        splashViewModel.userLiveData.observe(this, user ->{
           goToMainActivity(user);
           finish();
        });
    }

    private void signInAnonymously(){
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(anonTask ->
        {
           if(anonTask.isSuccessful()){
               FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
               if(firebaseUser != null){
                   Log.println(Log.ERROR, TAG, "anonymous sign in");
                   User user = new User(firebaseUser);
                   user.setAnonymous(true);
                   goToMainActivity(user);
               }
               else{
                   goToAuthActivity();
               }
           }
           else{
               goToAuthActivity();
           }
        });
    }

    private void goToAuthActivity(){
        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainActivity(User user){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra(USER, user);
        startActivity(intent);
        finish();
    }
}