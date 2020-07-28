package com.example.quickjobs.view.splash;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quickjobs.R;
import com.example.quickjobs.viewmodel.SplashViewModel;

public class LoadingDataFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_loading_data, container, false);


        splashViewModel.shouldMakeLoadingScreenVisible().observe(getViewLifecycleOwner(), makeVisible ->{
            if(makeVisible){
                view.setVisibility(View.VISIBLE);
            }
            else{
                view.setVisibility(View.GONE);
            }
        });

        return view;
    }
}