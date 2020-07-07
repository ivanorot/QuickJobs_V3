package com.example.quickjobs.view.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.quickjobs.R;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.view.main.MainActivity;
import com.example.quickjobs.viewmodel.AuthViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;

public class AuthActivity extends AppCompatActivity {
    private final String TAG = "AuthActivity";
    private final String USER = "user";

    private GoogleSignInClient googleSignInClient;
    private AuthViewModel authViewModel;

    private final int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initAuthViewModel();
        launchAuthentication();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.println(Log.ERROR, TAG, "initiated");

        if(requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (response != null) {
                signInWithUsingDefault(response);
            }
        }
    }


    private void launchAuthentication()
    {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.PhoneBuilder().build()
            );

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(false)
                            .setTheme(R.style.QuickJobsFirebaseAuth)
                            .build(),
                    RC_SIGN_IN
            );
    }

    public void initAuthViewModel(){
        Log.println(Log.ERROR, TAG, "initAuthViewModel()");
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    public void signInWithUsingDefault(IdpResponse response){
        authViewModel.signInDefault(response);
        authViewModel.authenticatedUserLiveData.observe(this, user ->{
            if(user.isNew()){
                Log.println(Log.ERROR, TAG,"persisting user");
                createNewUser(user);
            }
            else{
                Log.println(Log.ERROR, TAG, "reading user from persistence");
                goToMainActivity(user);
            }
        });
    }

    public void createNewUser(User authenticatedUser) {
        Log.println(Log.ERROR, TAG, "createNewUser()");
        authViewModel.createUser(authenticatedUser);
        authViewModel.createdUserLiveData.observe(this, user -> {
            if (user.isCreated()) {
                toastMessage(user.getDisplayName());
            }

            goToMainActivity(user);
        });
    }
    public void goToMainActivity(User user){
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        intent.putExtra(USER, user);
        startActivity(intent);
        finish();
    }

    public void toastMessage(String displaceName){
        Toast.makeText(this, "welcome " + displaceName, Toast.LENGTH_SHORT).show();
    }

    public void logErrorMessage(@NonNull Exception e){
        if(e.getMessage() != null){
            Log.println(Log.ERROR, TAG, "logErrorMessage()" + e.getMessage());
        }
    }

}