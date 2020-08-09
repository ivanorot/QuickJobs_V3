package com.example.quickjobs.view.splash;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.quickjobs.R;
import com.example.quickjobs.helper.PermissionManager;
import com.example.quickjobs.viewmodel.SplashViewModel;

public class EnableLocationServicesFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "EnableLocationServices";


    private SplashViewModel splashViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashViewModel = new ViewModelProvider(requireActivity()).get(SplashViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enable_location_services, container, false);

        initEnableLocationButton(view);

        return view;
    }

    @Override
    public void onClick(View view) {

    }

    public void initEnableLocationButton(View view){
        Button enableLocationServicesButton = view.findViewById(R.id.enableLocationButton);
        enableLocationServicesButton.setOnClickListener(this);
    }

    /*
    check if location permmision has been granted
     */

}