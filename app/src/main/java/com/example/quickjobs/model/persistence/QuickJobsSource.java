package com.example.quickjobs.model.persistence;

import com.example.quickjobs.model.content.QuickJobsPost;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuickJobsSource {

    private FirebaseFirestore cloudPersistence;

    public QuickJobsSource(FirebaseFirestore firebaseFirestore) {
        cloudPersistence = firebaseFirestore;
    }

    public QuickJobsPost getQuickJobPost()
    {
        return new QuickJobsPost("email","name");
    }
}
