package com.example.quickjobs.view.splash;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickjobs.R;
import com.example.quickjobs.view.main.MainActivity;
import com.example.quickjobs.viewmodel.SplashViewModel;

public class SplashActivity extends AppCompatActivity{
    private final String TAG = "SplashActivity";

    private ProgressBar loadingProgressBar;
    private SplashViewModel splashViewModel;

    private TextView textView;
    private ObjectAnimator animation;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initSplashViewModel();
        initUserPreferences();
        initViewAndAnimation();

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initSplashViewModel(){
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    }

    private void initUserPreferences(){
        splashViewModel.initUserPreference();
    }

    private void initViewAndAnimation(){
        textView = findViewById(R.id.quickJobsText);
        progressBar = findViewById(R.id.progress_bar);
        animation = ObjectAnimator.ofFloat(textView, "translationX", 0f, 100f, 0, -100f, 0f);
        animation.setDuration(3000);

    }


}