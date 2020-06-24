package com.example.quickjobs;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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


        //Enable the button after there is a picture and a title
        if (v.findViewById(R.id.newPost_titletext_EditText) != null) {
            nextButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.newPostPartTwoFragment));
        }

        return v;
    }

}