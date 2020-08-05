package com.example.quickjobs.view.settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.quickjobs.R;

public class AboutUsFragment extends Fragment {

    private WebView webView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        initWebView(view);
        webView.loadUrl("https://about.google/");
        return view;
    }

    public void initWebView(View view){
        webView = view.findViewById(R.id.about_us_web_view);
    }

}