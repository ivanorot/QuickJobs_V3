package com.example.quickjobs.model.source;

import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.QuickJob;
import com.example.quickjobs.helper.ExceptionHandler;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class JobSource {
    private final String COLLECTION_NAME = "job_board";
    private static JobSource Instance;

    private CollectionReference jobBoard;

    private double oneMileOfLatitudeInDegrees = 0.0144927536231884;
    private double oneMileOfLongitudeInDegrees = 0.0181818181818182;


    private JobSource(FirebaseFirestore firebaseFirestore) {
        jobBoard = firebaseFirestore.collection(COLLECTION_NAME);
    }


    public static JobSource getInstance(FirebaseFirestore firebaseFirestore){
        if(Instance == null){
            synchronized (JobSource.class){
                Instance = new JobSource(firebaseFirestore);
            }
        }

        return Instance;
    }


    public MutableLiveData<List<QuickJob>> getQuickJobsNearUserLocation(double longitude, double latitude, int maxDistance){
        MutableLiveData<List<QuickJob>> jobs = new MutableLiveData<>();

        jobBoard.whereGreaterThanOrEqualTo("longitude", getMinLongitudeByDistance(longitude, maxDistance))
                .whereLessThanOrEqualTo("longitude", getMaxLongitudeByDistance(longitude, maxDistance))
                .addSnapshotListener((snapShot, exception) -> {
                    if(snapShot != null){
                        List<QuickJob> temp = snapShot.toObjects(QuickJob.class);
                        jobs.postValue(temp);
                    }
                    else{
                        final String TAG = "getQuickJobsNearUserLocation";
                        jobs.postValue(new ArrayList<>());
                        ExceptionHandler.consumeException(TAG, exception);
                    }
                });

        return jobs;
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
