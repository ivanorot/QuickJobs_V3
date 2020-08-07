package com.example.quickjobs.view.settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.quickjobs.R;


public class HelpCenterFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        WebView webView = view.findViewById(R.id.about_us_web_view);

        webView.loadUrl("https://support.google.com/?hl=en");

        return view;
    }
}