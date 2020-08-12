package com.example.quickjobs.model.concurrency;


import android.os.HandlerThread;

import com.example.quickjobs.model.interfaces.SplashScreenCallback;

import java.lang.ref.WeakReference;

public class SplashScreenHandlerThread extends HandlerThread {


    private SplashScreenHandler splashScreenHandler;

    private WeakReference<SplashScreenCallback> userLocationCallbackWeakReference;

    public SplashScreenHandlerThread(String name) {
        super(name);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        splashScreenHandler = new SplashScreenHandler(getLooper());
    }

    public void addMessage(int message){
        if(splashScreenHandler != null){
            splashScreenHandler.sendEmptyMessage(message);
        }
    }

    public void setLocationChangeCallback(SplashScreenCallback userLocationCallback){
        userLocationCallbackWeakReference = new WeakReference<>(userLocationCallback);
    }
}
