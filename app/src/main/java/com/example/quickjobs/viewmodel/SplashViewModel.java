package com.example.quickjobs.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.AndroidViewModel;

import com.example.quickjobs.model.constants.Constants;
import com.example.quickjobs.model.constants.Language;
import com.example.quickjobs.model.helper.ExceptionHandler;
import com.example.quickjobs.model.helper.PermissionManager;
import com.example.quickjobs.model.MainRepository;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SplashViewModel extends AndroidViewModel {
    private final String TAG = "SplashViewModel";
    private MainRepository mainRepository;
    private PermissionManager permissionManager;



    public SplashViewModel(@NonNull Application application) {
        super(application);

        mainRepository = MainRepository.getInstance(application);
        permissionManager = new PermissionManager(application);

    }

    public void initUserPreference(){
        mainRepository.getUserPreferences(new Observer<Map<String, ?>>() {
            Disposable disposable;
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Map<String, ?> stringBooleanMap) {
                configureUserPreferences(stringBooleanMap);
            }

            @Override
            public void onError(Throwable e) {
                ExceptionHandler.consumeThrowable(TAG, e);
            }

            @Override
            public void onComplete() {
                disposable.dispose();
            }
        });
    }

    private void configureUserPreferences(Map<String, ?> userPreferenceKeys){
        if(userPreferenceKeys.isEmpty()){
            configureDefaultSettings();
        }else{
            for(Map.Entry<String ,?> userPreference: userPreferenceKeys.entrySet()){
                switch(userPreference.getKey()) {
                    case Constants.USER_PREFERENCE_CONFIGURATION:
                        configureUserInterfaceConfiguration((Boolean) userPreference.getValue());
                        break;

                    case Constants.USER_PREFERENCE_LANGUAGE:
                        configureUserLanguage((Language) userPreference.getValue());
                        break;
                }
            }
        }
    }

    private void configureUserInterfaceConfiguration(Boolean isDarkModeOn){
        if(isDarkModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void configureUserLanguage(Language language){
        switch (language){
            case ENGLISH:
//                TODO configure app for english;
            case SPANISH:
//                TODO configure app for spanish;
        }
    }

    private void configureDefaultSettings(){
        configureUserInterfaceConfiguration(false);
        configureUserLanguage(Language.ENGLISH);
    }
}
