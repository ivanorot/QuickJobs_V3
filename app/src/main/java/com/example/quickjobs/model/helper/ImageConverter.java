package com.example.quickjobs.model.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageConverter {

    private static final int QUALITY = 100;
    private static final int OFFSET = 100;

    public static String BitmapToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,QUALITY, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap StringToBitmap(String string){
        try {
            byte[] bytes = Base64.decode(string,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, OFFSET, bytes.length);
        }catch (Exception e)
        {
            final String TAG = "StringToBitmap";
            ExceptionHandler.consumeException(TAG, e);
            return null;
        }
    }
}
