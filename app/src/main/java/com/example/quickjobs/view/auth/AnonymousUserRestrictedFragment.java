package com.example.quickjobs.view.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.quickjobs.R;

import java.util.Objects;

import com.example.quickjobs.R;
import com.example.quickjobs.view.auth.AuthActivity;


public class AnonymousUserRestrictedFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_anonymous_user_restricted, container, false);

        initSignInToContinueButton(view);

        return view;
    }

    private void initSignInToContinueButton(View view){
        Button signInToContinueButton = view.findViewById(R.id.signInToContinueButton);
        signInToContinueButton.setOnClickListener(ignore -> goToAuthActivity());
    }

    private void goToAuthActivity(){
        Intent intent = new Intent(requireActivity(), AuthActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}