package com.example.quickjobs.model.source;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.QuickJob;
import com.example.quickjobs.model.helper.ExceptionHandler;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class JobSource implements EventListener<QuerySnapshot> {
    private static final String TAG = "JobSource";

    private final String COLLECTION_NAME = "job_board";
    private final String JOB_SOURCE_HANDLER_THREAD_NAME = "JOB_SOURCE_THEAD";
    private static JobSource Instance;

    private CollectionReference jobBoard;

    private double oneMileOfLatitudeInDegrees = 0.0144927536231884;
    private double oneMileOfLongitudeInDegrees = 0.0181818181818182;

    private HandlerThread jobSourceHandlerThread_Background;
    private Handler jobSourceHandler_Background;


    private JobSource() {
        jobBoard = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }


    public static JobSource getInstance(){
        if(Instance == null){
            synchronized (JobSource.class){
                Instance = new JobSource();
            }
        }

        return Instance;
    }

    public void initBackgroundThread(){
        jobSourceHandlerThread_Background = new HandlerThread(JOB_SOURCE_HANDLER_THREAD_NAME);
        jobSourceHandlerThread_Background.start();
        jobSourceHandler_Background = new Handler(jobSourceHandlerThread_Background.getLooper());
    }

    public void terminateBackgroundThread(){
        jobSourceHandlerThread_Background.quitSafely();
        try{
             jobSourceHandlerThread_Background.join();
             jobSourceHandlerThread_Background = null;
             jobSourceHandler_Background = null;

        }catch (InterruptedException exception){
            ExceptionHandler.consumeException(TAG, exception);
        }
    }


    public MutableLiveData<List<QuickJob>> getQuickJobsNearUserLocation(double longitude, double latitude, int maxDistance){
        MutableLiveData<List<QuickJob>> jobs = new MutableLiveData<>();

        jobBoard.whereGreaterThanOrEqualTo("longitude", getMinLongitudeByDistance(longitude, maxDistance))
                .whereLessThanOrEqualTo("longitude", getMaxLongitudeByDistance(longitude, maxDistance))
                .addSnapshotListener(this);

        return jobs;
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

    }

    private double getMaxLatitudeByDistance(double latitude, int distance){
        return (latitude + (oneMileOfLatitudeInDegrees * distance));
    }

    private double getMinLatitudeByDistance(double latitude, int distance){
        return (latitude - (oneMileOfLatitudeInDegrees * distance));
    }

    private double getMaxLongitudeByDistance(double longitude, int distance){
        return (longitude + (oneMileOfLongitudeInDegrees * distance));
    }

    private double getMinLongitudeByDistance(double longitude, int distance){
        return (longitude - (oneMileOfLongitudeInDegrees * distance));
    }

}
