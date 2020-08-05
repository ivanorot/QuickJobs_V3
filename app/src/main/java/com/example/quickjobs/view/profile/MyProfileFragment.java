package com.example.quickjobs.view.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickjobs.R;
import com.example.quickjobs.view.auth.AuthActivity;
import com.example.quickjobs.view.settings.SettingsActivity;
import com.example.quickjobs.viewmodel.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {
    private final String TAG = "MyProfile";
    private MainViewModel mainViewModel;

    private FrameLayout signin_FrameLayout;
    private ScrollView myprofile_ScrollView;

    Button signin_Button;

    private final int SETTINGS_REQUEST_CODE = 101;
    private final int AUTH_REQUEST_CODE = 102;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment myProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
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

        initMainViewModel();
        initAuthentication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        initAuthentication();
        findInitViews(view);
        setHasOptionsMenu(true);
        //Toast.makeText(getActivity(), "onCreateView method", Toast.LENGTH_SHORT).show();

        signin_Button.setOnClickListener(ignore -> {
            Intent authIntent = new Intent(requireActivity(), AuthActivity.class);
            startActivityForResult(authIntent, AUTH_REQUEST_CODE);
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        initAuthentication();

    }

    public void initMainViewModel(){
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    //*****************************************************************************************************************
    private void initAuthentication(){
        Toast.makeText(getActivity(), "Authentication method", Toast.LENGTH_SHORT).show();
        mainViewModel.currentUserMutableLiveData.observeForever(currentUser->{
            if(!currentUser.isAnonymous()){
                Toast.makeText(getActivity(), "is signed in", Toast.LENGTH_SHORT).show();
                signin_FrameLayout.setVisibility(View.GONE);
                myprofile_ScrollView.setVisibility(View.VISIBLE);
                getProfileInfo();
            }
            else {
                Toast.makeText(getActivity(), "is not signed in", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //*******************************************************************************************************

    private void findInitViews(View v){
        signin_FrameLayout = (FrameLayout) v.findViewById(R.id.myProfile_signin_FrameLayout);
        myprofile_ScrollView = (ScrollView) v.findViewById(R.id.myProfile_ScrollView);
        signin_Button = (Button) v.findViewById(R.id.myProfile_signin_button);
    }

    private void getProfileInfo(){
        //TODO: Fill myProfile with user info
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.profile_action_bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.accountSettingsCog){
            goToAccountSettings();
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToAccountSettings(){
        Intent intent = new Intent(requireActivity(), SettingsActivity.class);
        startActivityForResult(intent, SETTINGS_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initAuthentication();

    }
}