package com.example.quickjobs.view.jobs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickjobs.R;

import java.net.URI;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerHolder> {
    private Context context;
    private URI[] uri;
    private int imagesCount;

    public ViewPagerAdapter(Context mContext){
        context = mContext;
        imagesCount = 0;
        uri = new URI[9];
    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewPagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.camera_item, parent);
        return new ViewPagerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewPagerHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return imagesCount;
    }

    public class ViewPagerHolder extends RecyclerView.ViewHolder {
        public ViewPagerHolder(View itemView){
            super(itemView);
        }

    }

}
