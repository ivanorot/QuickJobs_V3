package com.example.quickjobs.view.jobs;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quickjobs.R;
import com.example.quickjobs.interfaces.LocationChangeListener;
import com.example.quickjobs.viewmodel.MainViewModel;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyJobFragment extends Fragment implements LocationChangeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyJobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyJobFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyJobFragment newInstance(String param1, String param2) {
        MyJobFragment fragment = new MyJobFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_job, container, false);

        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        mainViewModel.register(this);

        return view;
    }

    @Override
    public void onLocationChange(LocationResult locationResults) {

    }
}