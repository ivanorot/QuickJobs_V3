package com.example.quickjobs.model.helper;


import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

public class ExceptionHandler {

    public static void consumeException(String TAG, Exception e){
        if(e.getMessage() != null) {
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
    }

    public static boolean showDialogBox(Context context, String title, String message, String negativeButton, String positiveButton){
        AtomicBoolean temp = new AtomicBoolean(false);
        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(negativeButton, (dialogInterface, i ) -> {
                    temp.set(false);
                    dialogInterface.dismiss();
                })
                .setPositiveButton(positiveButton, (dialogInterface, i) -> {
                    temp.set(true);
                    dialogInterface.dismiss();
                }).show();

        return temp.get();
    }
}


