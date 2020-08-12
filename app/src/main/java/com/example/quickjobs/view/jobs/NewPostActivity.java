package com.example.quickjobs.view.jobs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.quickjobs.R;
import com.example.quickjobs.viewmodel.MainViewModel;
import com.example.quickjobs.viewmodel.NewPostViewModel;

public class NewPostActivity extends AppCompatActivity {
    MainViewModel mainViewModel;
    NewPostViewModel newPostViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        newPostViewModel.currentUser.observe(this, currentUser -> {

        });
   }

   private void initViewModels(){
       newPostViewModel = new ViewModelProvider(this).get(NewPostViewModel.class);
   }
}