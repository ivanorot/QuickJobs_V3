package com.example.quickjobs.view.jobs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickjobs.R;
import com.example.quickjobs.viewmodel.NewPostViewModel;

public class NewPostActivity extends AppCompatActivity {

    NewPostViewModel newPostViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        newPostViewModel = new ViewModelProvider(this).get(NewPostViewModel.class);
        newPostViewModel.currentUser.observe(this, currentUser -> {

        });
   }
}