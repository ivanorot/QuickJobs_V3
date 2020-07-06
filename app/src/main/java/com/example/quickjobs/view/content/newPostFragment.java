package com.example.quickjobs.view.content;

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
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.quickjobs.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newPostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button nextButton;
    ImageButton cameraButton;
    ImageButton galleryButton;
    EditText newPostTitle;
    String mTitleText;
    ImageView pic1;
    private static final int pic_id = 123;


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

        nextButton = (Button) v.findViewById(R.id.newPost_Next_button);
        cameraButton = (ImageButton) v.findViewById(R.id.newPost_camera_imageButton);
        galleryButton = (ImageButton) v.findViewById((R.id.newPost_gallery_imageButton));
        newPostTitle = (EditText) v.findViewById(R.id.newPost_titletext_EditText);
        pic1 = (ImageView) v.findViewById(R.id.newPost_image1_imageView);
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
            pic1.setImageBitmap(pic);

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


}