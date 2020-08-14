package com.example.quickjobs.model.concurrency;


import android.os.HandlerThread;
import android.os.Process;

import com.example.quickjobs.model.interfaces.SplashScreenCallback;
import com.google.firebase.firestore.DocumentReference;

import java.lang.ref.WeakReference;

public class UserSourceHandlerThread extends HandlerThread {

     private WeakReference<SplashScreenCallback> mUserSourceCallback;
    private UserSourceHandler userSourceHandler;



    private DocumentReference currentUserDocument;

    public UserSourceHandlerThread() {
        super("User Source Background Thread", Process.THREAD_PRIORITY_DEFAULT);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        userSourceHandler = new UserSourceHandler(getLooper());
    }

    public void addMessage(int message){
        if(userSourceHandler != null){
            userSourceHandler.sendEmptyMessage(message);
        }
    }

    public void setLocationChangeCallback(SplashScreenCallback userSourceCallback){
        mUserSourceCallback = new WeakReference<>(userSourceCallback);
    }
}
