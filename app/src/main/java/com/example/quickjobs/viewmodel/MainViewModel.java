package com.example.quickjobs.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quickjobs.model.Repository;
import com.example.quickjobs.model.user.QuickJobsUser;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainViewModel extends AndroidViewModel {
    private Repository repository;

    /*
    User
     */
    private MutableLiveData<QuickJobsUser> currentUser;

    /*
    Flags
     */
    MutableLiveData<Boolean> translateFireToQJStatus;
    MutableLiveData<Boolean> shouldLaunchAuthentication;

    public MainViewModel(@NonNull Application application) {
        super(application);

        currentUser = new MutableLiveData<>();
        repository = Repository.getInstance();

        //FirebaseUser To QuickJobsUser Flag
        translateFireToQJStatus = new MutableLiveData<>(false);
        shouldLaunchAuthentication = new MutableLiveData<>(false);
    }

    /*
     */

    public void translateToQuickJobsUser(FirebaseUser firebaseUser)
    {
        QuickJobsUser l_quickJobsUser = repository.readUserFromFirestore(firebaseUser.getUid());

        if(l_quickJobsUser != null)
        {
            currentUser.setValue(l_quickJobsUser);
            translateFireToQJStatus.setValue(true);
        }
        else
        {
            translateFireToQJStatus.setValue(false);
        }
    }

    public void createNewUserInFireStore(FirebaseUser firebaseUser)
    {
        QuickJobsUser l_quickJobUser = new QuickJobsUser(firebaseUser);
        repository.cacheUserToFirestore(l_quickJobUser);
    }

    public LiveData<QuickJobsUser> getCurrentUser()
    {
        return currentUser;
    }

    public LiveData<Boolean> getTranslateFireTOQJStatus()
    {
        return translateFireToQJStatus;
    }

    public LiveData<Boolean> getShouldLaunchAuthentication()
    {
        return shouldLaunchAuthentication;
    }

    public void setTranslateFireToQJStatus(Boolean inShouldTranslate)
    {
        translateFireToQJStatus.setValue(inShouldTranslate);
    }

    public void setShouldLaunchAuthentication(Boolean inShouldLaunch)
    {
        shouldLaunchAuthentication.setValue(inShouldLaunch);
    }

}
