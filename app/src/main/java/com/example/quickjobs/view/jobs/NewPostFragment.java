package com.example.quickjobs.view.jobs;

import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.quickjobs.R;
import com.example.quickjobs.viewmodel.NewPostViewModel;


public class NewPostFragment extends Fragment {

    NewPostViewModel newPostViewModel;

    Button nextButton;
    ImageButton cameraButton;
    ImageButton galleryButton;
    EditText newPostTitle;
    String mTitleText;
    private static final int pic_id = 123;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this



        View v = inflater.inflate(R.layout.fragment_new_post, container, false);

        newPostViewModel = new ViewModelProvider(requireActivity()).get(NewPostViewModel.class);

      //  newPostViewModel.shouldNavigate().observe(getViewLifecycleOwner(), user -> navigate(user, v));

        InitfindViews(v);

        newPostTitle.addTextChangedListener(newPostInputWatcher);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraSession = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraSession, pic_id);
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            Bitmap pic = (Bitmap) data.getExtras().get("data");
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

    private void InitfindViews(View v){
        nextButton = (Button) v.findViewById(R.id.newPost_Next_button);
        cameraButton = (ImageButton) v.findViewById(R.id.newPost_camera_imageButton);
        galleryButton = (ImageButton) v.findViewById((R.id.newPost_gallery_imageButton));
        newPostTitle = (EditText) v.findViewById(R.id.newPost_titletext_EditText);
        //pic1 = (ImageView) v.findViewById(R.id.newPost_image1_imageView);
    }


}