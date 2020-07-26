package com.example.quickjobs.model.source;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.QuickJob;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.model.helper.ExceptionHandler;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import io.reactivex.Observable;

public class JobSource {
    private final String COLLECTION_NAME = "job_board";

    private CollectionReference jobBoard;

    private double oneMileOfLatitudeInDegrees = 0.0144927536231884;
    private double oneMileOfLongitudeInDegrees = 0.0181818181818182;


    public JobSource(FirebaseFirestore firebaseFirestore) {
        jobBoard = firebaseFirestore.collection(COLLECTION_NAME);
    }


    public MutableLiveData<List<QuickJob>> getQuickJobsNearUserLocation(double longitude, double latitude, int distance){
        MutableLiveData<List<QuickJob>> jobs = new MutableLiveData<>();

        jobBoard.whereGreaterThanOrEqualTo("longitude", getMinLongitudeByDistance(longitude, distance))
                .whereLessThanOrEqualTo("longitude", getMaxLongitudeByDistance(longitude, distance))
                .whereGreaterThanOrEqualTo("latitude", getMinLatitudeByDistance(latitude, distance))
                .whereLessThanOrEqualTo("latitude", getMaxLatitudeByDistance(latitude, distance))
                .addSnapshotListener((snapShot, exception) -> {
                    if(snapShot != null){
                        List<QuickJob> temp = snapShot.toObjects(QuickJob.class);
                        jobs.postValue(temp);
                    }
                    else{
                        final String TAG = "getQuickJobsNearUserLocation";
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