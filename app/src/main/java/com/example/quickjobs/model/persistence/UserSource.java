package com.example.quickjobs.model.persistence;

import com.example.quickjobs.model.user.QuickJobsUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class UserSource {
    private final String COLLECTION_NAME = "user";
    private CollectionReference userCollection;
    private DocumentReference currentUserDocument;

    public UserSource(FirebaseFirestore firebaseFirestore)
    {
        userCollection = firebaseFirestore.collection(COLLECTION_NAME);
    }

    public void setCurrentUser(QuickJobsUser currentUser) {
        currentUserDocument = userCollection.document(currentUser.getUid());
    }

    public void listenToUserCollection(EventListener<QuerySnapshot> userDocumentListener)
    {
        userCollection.addSnapshotListener(userDocumentListener);
    }

    public QuickJobsUser readUserFromFirestore(String inUid)
    {
        AtomicReference<QuickJobsUser> documentSnapshot = new AtomicReference<>();

        userCollection.document(inUid)
                .get()
                .addOnCompleteListener( task ->
                        {
                            QuickJobsUser L_quickJobUser = Objects.requireNonNull(task.getResult()).toObject(QuickJobsUser.class);
                            documentSnapshot.set(L_quickJobUser);
                        }
                );

        return documentSnapshot.get();
    }

    public boolean cacheUserToFirestore(QuickJobsUser quickJobsUser)
    {
        AtomicBoolean L_wasCacheSuccess = new AtomicBoolean();

        userCollection.document(quickJobsUser.getUid())
                .set(quickJobsUser, SetOptions.merge())
                .addOnSuccessListener(value ->
                {
                    L_wasCacheSuccess.set(true);
                })
                .addOnFailureListener(exception ->
                {
                    L_wasCacheSuccess.set(false);
                });

        return L_wasCacheSuccess.get();
    }
}
