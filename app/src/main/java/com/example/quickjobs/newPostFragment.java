package com.example.quickjobs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newPostFragment extends Fragment {
    private static final String TAG = "newPostFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private static String[] REQUIRED_PERSMISSIONS = {Manifest.permission.CAMERA};
    private static int REQUESTED_CODE_PERMISSIONS = 10;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView imagesRecyclerView;
    Button nextButton;
    ImageButton cameraButton;
    ImageButton galleryButton;
    ImageButton addPicButton;
    EditText newPostTitle;
    String mTitleText;
    List<Bitmap> images;
    RecyclerViewAdapter recyclerViewAdapter;




    public newPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static newPostFragment newInstance(String param1, String param2) {
        newPostFragment fragment = new newPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this

        View v = inflater.inflate(R.layout.fragment_new_post, container, false);
        images = new ArrayList<>();
        initfindViews(v);
        initRecyclerViewSetup();

        cameraButton.setOnClickListener(addPicAction);
        galleryButton.setOnClickListener(addPicActionGallery);
        newPostTitle.addTextChangedListener(newPostInputWatcher);

        return v;
    }
    View.OnClickListener addPicAction = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            cameraPermission();
        }
    };

    View.OnClickListener addPicActionGallery = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent gallerySession = new Intent(Intent.ACTION_GET_CONTENT);
            gallerySession.setType("image/*");
            startActivityForResult(gallerySession, REQUEST_IMAGE_CAPTURE);
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bitmap pic = (Bitmap) data.getExtras().get("data");
            images.add(pic);
            recyclerViewAdapter.loadNewData(images);
        }
    }

    private void initRecyclerViewSetup(){
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), getActivity(), REQUEST_IMAGE_CAPTURE);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        imagesRecyclerView.setAdapter(recyclerViewAdapter);
        imagesRecyclerView.setClipToPadding(false);
        imagesRecyclerView.setClipChildren(false);
        //imagesRecyclerView.setOffscreenPageLimit(4);
        //imagesRecyclerView.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

      /*  CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(4));
        compositePageTransformer.addTransformer((new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(1);
                }
        }));
          imagesRecyclerView.setPageTransformer(compositePageTransformer);*/
    }

    private TextWatcher newPostInputWatcher = new TextWatcher() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            mTitleText = newPostTitle.getText().toString();
            nextButton.setEnabled(!mTitleText.isEmpty());
            nextButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.newPostPartTwoFragment));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void initfindViews(View v){
        nextButton = (Button) v.findViewById(R.id.newPost_Next_button);
        cameraButton = (ImageButton) v.findViewById(R.id.newPost_camera_imageButton);
        galleryButton = (ImageButton) v.findViewById((R.id.newPost_gallery_imageButton));
        newPostTitle = (EditText) v.findViewById(R.id.newPost_titletext_EditText);
        imagesRecyclerView = v.findViewById(R.id.newPost_RecyclerView);
        //addPicButton = v.findViewById(R.id.newPost_addPic_imageButton);
    }


    private boolean allPermissionGranted() {
        int permissions_count = REQUIRED_PERSMISSIONS.length;
        for (int i = 0; i < permissions_count; i++) {
            if (ContextCompat.checkSelfPermission(getView().getContext(), REQUIRED_PERSMISSIONS[i]) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getView().getContext(), "Permissions failed.", Toast.LENGTH_SHORT).show();

                return false;
            }
        }

        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTED_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                Intent cameraSession = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraSession, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(getView().getContext(), "Permission not granted by user.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
    private void cameraPermission() {
        if (allPermissionGranted()) {
            Intent cameraSession = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraSession, REQUEST_IMAGE_CAPTURE);
            Toast.makeText(getContext(), "Permissions Granted.", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERSMISSIONS, REQUESTED_CODE_PERMISSIONS);
            Toast.makeText(getContext(), "Not all permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void delete(int position){
        images.remove(position);
        recyclerViewAdapter.loadNewData(images);
    }


}