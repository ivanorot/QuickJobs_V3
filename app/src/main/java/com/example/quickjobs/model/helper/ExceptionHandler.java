package com.example.quickjobs.model.helper;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExceptionHandler {
    private static final String CAUSE = "CAUSE: ";

    public static void consumeException(final String OWNER, Exception e){
        if(e.getMessage() != null) {
            Log.println(Log.ERROR, OWNER, e.getMessage());
        }

        if(e.getCause() != null){
            Log.println(Log.ERROR, CAUSE, e.getCause().toString());
        }
    }

    public static void consumeThrowable(final String OWNER, Throwable e){
        if(e.getMessage() != null) {
            Log.println(Log.ERROR, OWNER, e.getMessage());
        }

        if(e.getCause() != null){
            Log.println(Log.ERROR, CAUSE, e.getCause().toString());
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


