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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newPostFragment extends Fragment implements ViewOrDeleteImageDialog.dialogListener {
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
    ImageButton addPicButton;
    EditText newPostTitle;
    String mTitleText;
    List<Bitmap> images;
    ImageView[] imageViews;
    ImageView[] blankViews;
    ImageView coverImageView;




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
        addPicButton.setOnClickListener(addPicAction);

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
            addImages(pic);

        }
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
        initFindBlankViews(v);
        initFindImageViews(v);

        coverImageView = v.findViewById(R.id.newPost_cover_imageView);
        nextButton = (Button) v.findViewById(R.id.newPost_Next_button);
        newPostTitle = (EditText) v.findViewById(R.id.newPost_titletext_EditText);
        addPicButton = v.findViewById(R.id.newPost_addPic_imageButton);
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
    }

    private void initFindBlankViews(View v){
        blankViews = new ImageView[9];
        blankViews[0] = v.findViewById(R.id.newPost_blank1_imageView);
        blankViews[1] = v.findViewById(R.id.newPost_blank2_imageView);
        blankViews[2] = v.findViewById(R.id.newPost_blank3_imageView);
        blankViews[3] = v.findViewById(R.id.newPost_blank4_imageView);
        blankViews[4] = v.findViewById(R.id.newPost_blank5_imageView);
        blankViews[5] = v.findViewById(R.id.newPost_blank6_imageView);
        blankViews[6] = v.findViewById(R.id.newPost_blank7_imageView);
        blankViews[7] = v.findViewById(R.id.newPost_blank8_imageView);
        blankViews[8] = v.findViewById(R.id.newPost_blank9_imageView);
    }

    private void initFindImageViews(View v){
        imageViews = new ImageView[9];
        imageViews[0] = v.findViewById(R.id.newPost_image1_imageView);
        imageViews[1] = v.findViewById(R.id.newPost_image2_imageView);
        imageViews[2] = v.findViewById(R.id.newPost_image3_imageView);
        imageViews[3] = v.findViewById(R.id.newPost_image4_imageView);
        imageViews[4] = v.findViewById(R.id.newPost_image5_imageView);
        imageViews[5] = v.findViewById(R.id.newPost_image6_imageView);
        imageViews[6] = v.findViewById(R.id.newPost_image7_imageView);
        imageViews[7] = v.findViewById(R.id.newPost_image8_imageView);
        imageViews[8] = v.findViewById(R.id.newPost_image9_imageView);
    }

    private void addImages(Bitmap pic){
        images.add(pic);
        rearrangeViews();
    }

    private void deleteImages(int imageDeleted){
        images.remove(imageDeleted);
        rearrangeViews();
    }

    private void rearrangeViews(){
        initViewSetting();
        setImages();
        setButton();
        if(images.size()==0){
            coverImageView.setVisibility(View.GONE);
        }
    }
    private void setButton(){
        if(images.size()>=9){
            addPicButton.setVisibility(View.GONE);
        }
        else
            addPicButton.setVisibility(View.VISIBLE);
    }


    private void initViewSetting(){
        for (int i = 0; i < 9; i++){
            blankViews[i].setVisibility(View.VISIBLE);
            imageViews[i].setVisibility(View.GONE);
        }
    }

    private void setImages(){
        if (images != null&&images.size()!=0) {
            coverImageView.setImageBitmap(images.get(0));
            coverImageView.setVisibility(View.VISIBLE);
            for (int i = 0; i < images.size(); i++){
                blankViews[i].setVisibility(View.GONE);
                imageViews[i].setVisibility(View.VISIBLE);
                imageViews[i].setImageBitmap(images.get(i));
                imageViews[i].setOnClickListener(onClickImage);
            }
        }
    }

   View.OnClickListener onClickImage = new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           int position = 10;
           for (int i = 0; i < images.size(); i++){
               if (v.getId() == imageViews[i].getId()){
                   position = i;
                   break;
               }
           }
           Log.d(TAG, "onClick:"+ position);
           ViewOrDeleteImageDialog viewOrDeleteImageDialog = new ViewOrDeleteImageDialog(position);
           viewOrDeleteImageDialog.setTargetFragment(newPostFragment.this, 1);
           Log.d(TAG, "onClick: initialized VieworDelete");
           viewOrDeleteImageDialog.show(getParentFragmentManager(), "TAG");

           


       }
   };


    @Override
    public void imageDeletion(int position) {
        deleteImages(position);
    }
}