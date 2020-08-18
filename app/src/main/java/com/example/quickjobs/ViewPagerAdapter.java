package com.example.quickjobs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    private static final String TAG = "ViewPagerAdapter";
    private Context context;
    private List<Bitmap> bitmaps;
    private int imagesCount;
    private static int IMAGE_CAPTURE_REQUEST;
    private Activity thisActivity;

    public ViewPagerAdapter(Context mContext, Activity activity, int image_capture_req) {
        context = mContext;
        thisActivity = activity;
        IMAGE_CAPTURE_REQUEST = image_capture_req;
        imagesCount = 0;
        loadNewData(new ArrayList<>());

    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.cardview_view_image, parent, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: position = "+ position + " bitmap size = " + bitmaps.size());
        if (bitmaps != null) {
            holder.image.setImageBitmap(bitmaps.get(position));
        }

    }

    public void loadNewData(List<Bitmap> newPhotos){
        bitmaps = newPhotos;
        notifyDataSetChanged();
    }




    @Override
    public int getItemCount() {
        if(bitmaps.isEmpty())
            return 0;
        else
            return bitmaps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
       // ImageButton addImage_Button;
        public ViewHolder(View itemView){
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.cameraItem_imageView);

        }

    }
    void startCamera(){
        Intent cameraSession = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        thisActivity.startActivityForResult(cameraSession, IMAGE_CAPTURE_REQUEST);
    }

}
