package com.example.quickjobs.view.splash;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quickjobs.R;
import com.example.quickjobs.viewmodel.SplashViewModel;

public class EnableAppServicesFragment extends Fragment {

    SplashViewModel splashViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashViewModel = new ViewModelProvider(requireActivity()).get(SplashViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enable_app_services, container, false);


        CountDownTimer countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {
                Log.println(Log.ERROR, "EnableAppServices", "countDownTimer " + l);
            }

            @Override
            public void onFinish() {

            }
        };

        countDownTimer.start();

        return view;
    }
}