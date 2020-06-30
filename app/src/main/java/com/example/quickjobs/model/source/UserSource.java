package com.example.quickjobs.model.source;

import com.example.quickjobs.model.beans.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class UserSource {
    //user collection
    @SuppressWarnings("FieldCanBeLocal")
    private final String COLLECTION_NAME = "user";
    private CollectionReference userCollection;

//  Authentication
    private FirebaseAuth firebaseAuth;

//  Currently signed in user
    private FirebaseUser firebaseUser;
    private DocumentReference currentUserDocument;

    public UserSource(FirebaseFirestore firebaseFirestore) {
        userCollection = firebaseFirestore.collection(COLLECTION_NAME);

        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            setCurrentUser(firebaseUser.getUid());
        }
        else{
            //todo launch auth;
        }
    }

    private void setCurrentUser(String inUID){
        currentUserDocument = userCollection.document(inUID);
    }

    public void setCurrentUser(User currentUser) {
        currentUserDocument = userCollection.document(currentUser.getUid());

        //noinspection ConstantConditions
        if(currentUserDocument == null){
            createUser(currentUser);
            currentUserDocument = userCollection.document(currentUser.getUid());
        }
    }

    public void addCurrentUserListener(EventListener<DocumentSnapshot> currentUserListener){

        if(currentUserDocument != null){
            currentUserDocument.addSnapshotListener(currentUserListener);
        }
    }

    public void setCurrentUserAndListen(User currentUser, EventListener<DocumentSnapshot> currentUserListener) {
        currentUserDocument = userCollection.document(currentUser.getUid());

        //noinspection ConstantConditions
        if(currentUserDocument == null){
            createUser(currentUser);
            currentUserDocument = userCollection.document(currentUser.getUid());
        }

        currentUserDocument.addSnapshotListener(currentUserListener);
    }

    public void createUser(User user)
    {
        userCollection.document(user.getUid()).set(user);
    }

    public DocumentSnapshot readUser(User user){
        return userCollection.document(user.getUid())
                .get()
                .getResult();
    }

    public void updateUser(User user){
        userCollection.document(user.getUid())
                .set(user, SetOptions.merge());
    }

}
