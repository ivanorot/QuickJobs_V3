package com.example.quickjobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    NavController navController;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(findViewById(R.id.navigationHostFragment));
        bottomNavigationView.findViewById(R.id.BottonNavigation_ConstraintLayout);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigationView_home_button:
                        DestinationSelector_home_bottom();
                        break;
                    case R.id.navigationView_myProfile_button:
                        DestinationSelector_myProfile_bottom();
                        break;
                    case R.id.navigationView_myJobs_button:
                        DestinationSelector_myJobs_bottom();
                        break;
                    case R.id.navigationView_newPost_button:
                        DestinationSelector_newPost_bottom();
                        break;
                }

                return false;
            }
        });
    }

    public void DestinationSelector_home_bottom() {
        int CurrentDestination = navController.getCurrentDestination().getId();
        switch (CurrentDestination) {
            case R.id.navigationView_home_button:
                //Refresh Host

                break;

            case R.id.navigationView_myProfile_button:
                navController.navigate(R.id.action_myProfileFragment_to_homeFragment);

                break;

            case R.id.navigationView_myJobs_button:
                navController.navigate(R.id.action_myJobsFragment_to_homeFragment);

                break;
            case R.id.navigationView_newPost_button:
                navController.navigate(R.id.action_newPostFragment_to_homeFragment);

                break;
        }
    }

    public void DestinationSelector_myProfile_bottom() {
        int CurrentDestination = navController.getCurrentDestination().getId();
        switch (CurrentDestination) {
            case R.id.navigationView_home_button:
                navController.navigate(R.id.action_homeFragment_to_myProfileFragment);

                break;

            case R.id.navigationView_myProfile_button:
                //refresh host

                break;

            case R.id.navigationView_myJobs_button:
                navController.navigate(R.id.action_myJobsFragment_to_myProfileFragment);

                break;
            case R.id.navigationView_newPost_button:
                navController.navigate(R.id.action_newPostFragment_to_myProfileFragment);

                break;
        }
    }

    public void DestinationSelector_myJobs_bottom() {
        int CurrentDestination = navController.getCurrentDestination().getId();
        switch (CurrentDestination) {
            case R.id.navigationView_home_button:
                navController.navigate(R.id.action_homeFragment_to_myJobsFragment);

                break;

            case R.id.navigationView_myProfile_button:
                navController.navigate(R.id.action_myProfileFragment_to_myJobsFragment);

                break;

            case R.id.navigationView_myJobs_button:
                //Refresh Host

                break;
            case R.id.navigationView_newPost_button:
                navController.navigate(R.id.action_newPostFragment_to_myJobsFragment);

                break;
        }
    }

    public void DestinationSelector_newPost_bottom() {
        int CurrentDestination = navController.getCurrentDestination().getId();
        switch (CurrentDestination) {
            case R.id.navigationView_home_button:
                navController.navigate(R.id.action_homeFragment_to_myProfileFragment);

                break;

            case R.id.navigationView_myProfile_button:
                //refresh host

                break;

            case R.id.navigationView_myJobs_button:
                navController.navigate(R.id.action_myJobsFragment_to_myProfileFragment);

                break;
            case R.id.navigationView_newPost_button:
                navController.navigate(R.id.action_newPostFragment_to_myProfileFragment);

                break;
        }
    }


}
