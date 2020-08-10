package com.example.quickjobs.view.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.quickjobs.R;
import com.example.quickjobs.model.User;
import com.example.quickjobs.view.auth.AuthActivity;
import com.example.quickjobs.viewmodel.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{
    private final String TAG = "MainActivity";
    private final String USER = "user";

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "checking MainViewModel", Toast.LENGTH_SHORT).show();
        initMainViewModel();

        Toast.makeText(this, "checking QuickJobsUser", Toast.LENGTH_SHORT).show();
        initQuickJobsUser();

        Toast.makeText(this, "checking NavController", Toast.LENGTH_SHORT).show();
        initNavController();

    }

    public void initMainViewModel(){
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    public void initQuickJobsUser(){
        mainViewModel.initializeCurrentUser();
        mainViewModel.currentUserMutableLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

            }


        });
    }

    public void initNavController(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.navigationHostFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    private void goToAuthActivity(){
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private class MyNavController extends NavController{
        public MyNavController(@NonNull Context context) {
            super(context);
        }


    }
}