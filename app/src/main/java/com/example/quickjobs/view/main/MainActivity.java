package com.example.quickjobs.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.quickjobs.R;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.view.auth.AuthActivity;
import com.example.quickjobs.viewmodel.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, FirebaseAuth.AuthStateListener {
    private final String USER = "user";

    private MainViewModel mainViewModel;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMainViewModel();
        initQuickJobsUser();
        initBottomNavigationView();

    }

    public void initMainViewModel(){
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    public void initQuickJobsUser(){
        mainViewModel.initializeCurrentUser();
        mainViewModel.currentUserMutableLiveData.observe(this, currentUser -> {
            user = currentUser;
            Log.println(Log.ERROR, USER, "Display Name: " + currentUser.getDisplayName());
            Log.println(Log.ERROR, USER, "Latitude: " + currentUser.getLatitude());
            Log.println(Log.ERROR, USER, "Longitude: " + currentUser.getLongitude());
        });
    }

    public void initBottomNavigationView(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Navigation.findNavController(this, R.id.navigationHostFragment).navigateUp();

        if(user.isAnonymous() && menuItem.getItemId() != R.id.homeFragment){
            Navigation.findNavController(this, R.id.navigationHostFragment).navigate(R.id.anonymousUserProfileFragment);
            return true;
        }

        switch (menuItem.getItemId()){
            case R.id.homeFragment:

                Navigation.findNavController(this, R.id.navigationHostFragment).navigate(R.id.homeFragment);
                return true;

            case R.id.newPostFragment:
                Navigation.findNavController(this, R.id.navigationHostFragment).navigate(R.id.newPostFragment);
                return true;

            case R.id.myJobsFragment:
                Navigation.findNavController(this, R.id.navigationHostFragment).navigate(R.id.myJobsFragment);
                return true;

            case R.id.myProfileFragment:
                Navigation.findNavController(this, R.id.navigationHostFragment).navigate(R.id.myProfileFragment);
                return true;

        }

        return false;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            signInAnonymously();
        }
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    private void signInAnonymously(){
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(anonTask ->
        {
            if(anonTask.isSuccessful()){
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null){
                    User user = new User(firebaseUser);
                    user.setDisplayName("anonymous user");
                    user.setAnonymous(true);
                    mainViewModel.setCurrentUserAnonymous(user);
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
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }


}