package com.example.quickjobs.view.main;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.quickjobs.R;
import com.example.quickjobs.interfaces.LocationStateListener;
import com.example.quickjobs.viewmodel.MainViewModel;
import com.google.android.gms.location.LocationAvailability;

public class HomeFragment extends Fragment implements LocationStateListener {
    private final String TAG = "HomeFragment";
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        return view;
    }


    public void initMainViewModel(){
        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.register(this);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_action_bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.filterJobsItem){

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationAvailable(LocationAvailability locationAvailability) {

    }

    @Override
    public void onLocationChange(Location location) {
        Log.println(Log.ERROR, TAG, location.getLatitude() + " ");
    }

    @Override
    public void onLocationNotAvailable() {

    }
}