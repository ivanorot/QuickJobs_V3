package com.example.quickjobs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ViewOrDeleteImageDialog extends BottomSheetDialogFragment {
    private static final String TAG = "ViewOrDeleteImageDialog";
    int mPosition;
    public Button deleteButton;
    public Button  viewImageButton;
    private  dialogListener listener;

    public ViewOrDeleteImageDialog(int position) {
        mPosition = position;
        Log.d(TAG, "ViewOrDeleteImageDialog: "+position);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_selected_bottom_dialog_layout, container,false);
        initFindViews(view);



        setOnClickListeners();
        return view;
    }


    private void setOnClickListeners() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listener.imageDeletion(mPosition);
               dismiss();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            Log.d(TAG, "onCreateView: setListener");
            listener = (dialogListener)this.getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassException" + e.getMessage());
        }
    }

    private void initFindViews(View v){
        deleteButton = v.findViewById(R.id.imageSelected_deleteImage_button);
        viewImageButton = v.findViewById(R.id.imageSelected_viewImage_button);
    }
    public interface dialogListener{
        void imageDeletion(int position);

    }

}
