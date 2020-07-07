package com.example.quickjobs.model.helper;

import android.util.Log;

public class ExceptionHandler {

    public static void consumeException(String TAG, Exception e){
        if(e.getMessage() != null){
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
    }
}
