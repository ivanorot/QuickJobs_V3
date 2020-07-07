package com.example.quickjobs.model.source;

import com.example.quickjobs.model.beans.QuickJob;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import io.reactivex.Observable;

public class JobSource {
    private final String COLLECTION_NAME = "job_board";
    private CollectionReference jobBoard;

    public JobSource(FirebaseFirestore firebaseFirestore) {
        jobBoard = firebaseFirestore.collection(COLLECTION_NAME);
    }

    public void enableJobBoard(EventListener<QuerySnapshot> listener) {
        jobBoard.addSnapshotListener(listener);
    }

}
