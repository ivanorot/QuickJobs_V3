package com.example.quickjobs.model.helper;


import android.util.Log;

import androidx.annotation.Nullable;

public class ExceptionHandler {

    public static void consumeException(String TAG, Exception e){
        if(e.getMessage() != null) {
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
    }

}


