package com.example.quickjobs.model.concurrency;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserSourceHandler extends Handler {
    private final int LOAD_USER_SHARED_PREFERENCES = 0;
    private final int CHECK_USER_ANON_AUTH = 1;
    private final int SIGN_IN_ANON = 2;
    private final int SET_CURRENT_USER_ANON = 3;
    private final int GET_USER_FROM_DATABASE = 4;

    public UserSourceHandler(@NonNull Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);

        switch (msg.what){
            case LOAD_USER_SHARED_PREFERENCES:

            case CHECK_USER_ANON_AUTH:

            case SIGN_IN_ANON:

            case SET_CURRENT_USER_ANON:

            case GET_USER_FROM_DATABASE:
        }
    }
}
