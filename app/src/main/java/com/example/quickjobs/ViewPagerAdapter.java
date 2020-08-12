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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerHolder> {
    private static final String TAG = "ViewPagerAdapter";
    final int VIEW_TYPE_ONE = 0;
    final int VIEW_TYPE_TWO = 1;
    private Context context;
    private List<Bitmap> bitmaps;
    private int imagesCount;
    private static int IMAGE_CAPTURE_REQUEST;
    private Activity thisActivity;
    private ViewPager2 viewpager2;

    public ViewPagerAdapter(Context mContext, Activity activity, int image_capture_req) {
        context = mContext;
        thisActivity = activity;
        IMAGE_CAPTURE_REQUEST = image_capture_req;
        imagesCount = 0;
        //Toast.makeText(context, "constructor", Toast.LENGTH_SHORT).show();
        //viewpager2 = tempViewP;
        loadNewData(new ArrayList<>());
        //bindViewHolder(createViewHolder(viewpager2, VIEW_TYPE_ONE), 1);

    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewPagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Toast.makeText(context, "OnCreateViewHolder", Toast.LENGTH_SHORT).show();
        if (imagesCount < 9) {
            View view = LayoutInflater.from(context).inflate(R.layout.camera_item, parent, false);
            imagesCount++;
            Toast.makeText(context, "inflate", Toast.LENGTH_SHORT).show();
            return new ViewPagerHolder(view);
        } else {
            Toast.makeText(context, "no inflate", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewPagerHolder holder, int position) {
        Toast.makeText(context, "OnBindViewHolder", Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "position =" + position + "\n bitmaps size = " + bitmaps.size(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onBindViewHolder: position = "+ position + " bitmap size = " + bitmaps.size());
        if (bitmaps != null && bitmaps.size() == (position)) {
            holder.image.setImageBitmap(bitmaps.get(position-1));
            // Toast.makeText(context, "setOnClickListener", Toast.LENGTH_SHORT).show();
          /*  holder.addImage_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startCamera();
                }
            });*/
        }
       /* else if(position<bitmaps.size()){
            //holder.addImage_Button.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            //holder.image.setImageBitmap(bitmaps.get(position));
        }*/

    }

    public void loadNewData(List<Bitmap> newPhotos){
        bitmaps = newPhotos;
        Toast.makeText(context, "loadNewData", Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if(bitmaps.isEmpty())
            return 0;
        else
            return bitmaps.size();
    }

    public class ViewPagerHolder extends RecyclerView.ViewHolder {
        ImageView image;
       // ImageButton addImage_Button;
        public ViewPagerHolder(View itemView){
            super(itemView);
            Toast.makeText(context, "HolderConstructor", Toast.LENGTH_SHORT).show();
            this.image = (ImageView) itemView.findViewById(R.id.cameraItem_imageView);
            //this.addImage_Button = (ImageButton) itemView.findViewById(R.id.cameraItem_imageButton);

        }

    }
    void startCamera(){
        Intent cameraSession = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        thisActivity.startActivityForResult(cameraSession, IMAGE_CAPTURE_REQUEST);
    }

}
