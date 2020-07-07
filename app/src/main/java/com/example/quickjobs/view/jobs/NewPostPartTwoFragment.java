package com.example.quickjobs.view.jobs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.quickjobs.R;
import com.example.quickjobs.viewmodel.NewPostViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewPostPartTwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPostPartTwoFragment extends Fragment {

    NewPostViewModel newPostViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button previousButton;
    Button nextButton;


    public NewPostPartTwoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newPostPartTwoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewPostPartTwoFragment newInstance(String param1, String param2) {
        NewPostPartTwoFragment fragment = new NewPostPartTwoFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_post_part_two, container, false);



        previousButton = (Button) v.findViewById(R.id.newPostPartTwo_Previous_Button);
        previousButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.newPostFragment));


        return v;
    }
}