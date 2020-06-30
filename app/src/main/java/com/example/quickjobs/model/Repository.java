package com.example.quickjobs.model;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.beans.QuickJob;
import com.example.quickjobs.model.source.JobSource;
import com.example.quickjobs.model.source.LocationSource;
import com.example.quickjobs.model.source.UserSource;
import com.example.quickjobs.model.beans.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Repository {

    private static final String USER = "user";

    private static Repository INSTANCE;

    private static UserSource userSource;
    private static JobSource jobSource;

    private static FirebaseAuth firebaseAuth;
    private static FirebaseFirestore firebaseFirestore;
    private static CollectionReference collectionReference;

    private Repository(){}

    public static Repository getInstance(Context context)
    {
        if(INSTANCE == null)
        {
            synchronized (Repository.class)
            {
                if(INSTANCE == null)
                {
                    INSTANCE = new Repository();

//                    userSource = new UserSource(FirebaseFirestore.getInstance());
//                    jobSource = new JobSource(FirebaseFirestore.getInstance());

                    firebaseFirestore = FirebaseFirestore.getInstance();
                    collectionReference = firebaseFirestore.collection(USER);
                    firebaseAuth = FirebaseAuth.getInstance();
                }
            }
        }
        return INSTANCE;
    }

    public MutableLiveData<User> firebaseSignInWithGoogle(AuthCredential googleAuthCredential){
        MutableLiveData<User> authenticatedUserMutableLiveData = new MutableLiveData<>();

        firebaseAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener(task ->
        {
            if(task.isSuccessful() && task.getResult() != null){
                FirebaseUser firebaseUser = task.getResult().getUser();
                if(firebaseUser != null){

                    User user = new User(firebaseUser);
                    user.setNew(task.getResult().getAdditionalUserInfo().isNewUser());
                    authenticatedUserMutableLiveData.setValue(user);
                }
            }
            else {
                //todo: log error message
            }
        });

        return authenticatedUserMutableLiveData;
    }

    public MutableLiveData<User> createUserInFirestoreIfNotExist(User authenticatedUser)
    {
        MutableLiveData<User> newUserMutableLiveData = new MutableLiveData<>();
        DocumentReference uidRef = collectionReference.document(authenticatedUser.getUid());
        uidRef.get().addOnCompleteListener(uidTask -> {
            if(uidTask.isSuccessful())
            {
                DocumentSnapshot documentSnapshot = uidTask.getResult();
                if(!documentSnapshot.exists())
                {
                    uidRef.set(authenticatedUser).addOnCompleteListener(userCreationTask -> {

                        if(userCreationTask.isSuccessful()) {

                        }
                    });
                }
            }

        });

        return newUserMutableLiveData;
    }

    public void enableCurrentUser(Observer<User> currentUserObserver){

        Observable<User> temp = Observable.create(source ->{
            EventListener<DocumentSnapshot> tempSnapshot = new EventListener<DocumentSnapshot>()
            {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if(documentSnapshot != null)
                    {
                        source.onNext(documentSnapshot.toObject(User.class));
                    }
                }
            };

            userSource.addCurrentUserListener(tempSnapshot);

        });

        temp.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(currentUserObserver);

    }

    public void enableJobBoard(Observer<List<QuickJob>> jobBoardObserver){

    }
}
