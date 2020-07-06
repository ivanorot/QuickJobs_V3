package com.example.quickjobs.view.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quickjobs.R;
import com.example.quickjobs.viewmodel.MainViewModel;

public class HomeFragment extends Fragment {
    private final String TAG = "HomeFragment";
    MainViewModel mainViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Log.println(Log.ERROR, TAG, "Init");

        return view;
    }


    public void initMainViewModel(){
//        mainViewModel = new ViewModelProviders(this).get(
    }
}