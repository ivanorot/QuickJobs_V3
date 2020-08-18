package com.example.quickjobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

public class ViewImages extends DialogFragment {
    ViewPager2 viewPager2;

    public ViewImages() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_view_images, container, false);
        initFindViews(view);

        return view;
    }

    private void initFindViews(View v){
        viewPager2 = v.findViewById(R.id.viewImageDialog_ViewPager2);
    }

}
