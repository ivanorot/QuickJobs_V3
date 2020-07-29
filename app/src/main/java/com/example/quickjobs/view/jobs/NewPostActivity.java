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
/*        Toast.makeText(this, "Atleast it reads this", Toast.LENGTH_SHORT);
        newPostViewModel.currentUser.observe(this, currentUser-> {
                    if (currentUser) {
                        Toast.makeText(this, "Creating Layout", Toast.LENGTH_SHORT);

                    }
                    else{
                        Intent authIntent = new Intent(this, AuthActivity.class);
                        startActivity(authIntent);
                        Toast.makeText(this, "NOPE", Toast.LENGTH_SHORT);
                    }

                });*/



   }
}