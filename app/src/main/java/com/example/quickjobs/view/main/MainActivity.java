package com.example.quickjobs.view.main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickjobs.R;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.viewmodel.MainViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private final String USER = "user";

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMainViewModel();
        initQuickJobsUser();
        mainViewModel.observeCurrentUserLiveData().observe(this, user ->{
            if(user.isAnonymous()){
//                todo set restrictions
            }
        });

    }

    public void initMainViewModel(){
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    public void initQuickJobsUser(){
        User user = (User) getIntent().getSerializableExtra(USER);
        mainViewModel.setCurrentUserLiveData(user);
    }
}