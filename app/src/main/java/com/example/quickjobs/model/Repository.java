package com.example.quickjobs.model;

import com.example.quickjobs.model.persistence.UserSource;
import com.example.quickjobs.model.user.QuickJobsUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Repository {

    private static Repository INSTANCE;
    private static UserSource userSource;

    private Repository(){}

    public static Repository getInstance()
    {
        if(INSTANCE == null)
        {
            synchronized (Repository.class)
            {
                if(INSTANCE == null)
                {
                    INSTANCE = new Repository();

                    userSource = new UserSource(FirebaseFirestore.getInstance());
                }
            }
        }

        return INSTANCE;
    }
    /*
    CRUD: QuickJobUser
     */
    public void cacheUserToFirestore(QuickJobsUser quickJobsUser)
    {
        userSource.cacheUserToFirestore(quickJobsUser);
    }

    public QuickJobsUser readUserFromFirestore(String inUid)
    {
        return userSource.readUserFromFirestore(inUid);
    }
}
