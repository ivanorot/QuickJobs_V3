package com.example.quickjobs.view.jobs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickjobs.R;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerHolder> {
    private Context context;
    private List<Bitmap> bitmaps;
    private int imagesCount;
    private static final int pic_id = 123;
    private Activity thisActivity;

    public ViewPagerAdapter(Context mContext, Activity activity){
        context = mContext;
        imagesCount = 0;
        //bitmaps = new Bitmap[9];
        thisActivity = activity;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewPagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(imagesCount<9) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.camera_item,parent, false);//View.inflate(context, R.layout.camera_item, parent);
            imagesCount++;
            return new ViewPagerHolder(view);
        }
        else
            return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewPagerHolder holder, int position) {
        holder.addImage_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraSession = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                thisActivity.startActivityForResult(cameraSession, pic_id);

            }


        });
    }

    void loadNewData(List<Bitmap> newPhotos){
        bitmaps = newPhotos;
        notifyDataSetChanged();
    }
    

    @Override
    public int getItemCount() {
        return imagesCount;
    }

    public class ViewPagerHolder extends RecyclerView.ViewHolder {
        ImageView image = null;
        ImageButton addImage_Button = null;
        public ViewPagerHolder(View itemView){
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.cameraItem_imageView);
            this.addImage_Button = (ImageButton) itemView.findViewById(R.id.camera_item_imageButton);

        }

    }

}
