package com.example.quickjobs.view.auth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.quickjobs.R;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.view.main.MainActivity;
import com.example.quickjobs.viewmodel.AuthViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthActivity extends AppCompatActivity {
    private final String USER = "user";

    private GoogleSignInClient googleSignInClient;
    private AuthViewModel authViewModel;

    private final int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initSignButton();
        initAuthViewModel();
        initGoogleSignInClient();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);

                if (googleSignInAccount != null)
                {
                    getGoogleAuthCredential(googleSignInAccount);
                }

            } catch (ApiException exception)
            {
                logErrorMessage(exception);
            }
        }
    }

    public void initSignButton(){
        SignInButton signInButton = findViewById(R.id.google_sign_in_button);
        signInButton.setOnClickListener(v -> signIn());
    }

    public void initAuthViewModel(){
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    public void initGoogleSignInClient(){
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("todo: fill")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    public void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void getGoogleAuthCredential(GoogleSignInAccount googleSignInAccount){
        String googleSignInToken = googleSignInAccount.getIdToken();
        AuthCredential googleAuthCredential = GoogleAuthProvider.getCredential(googleSignInToken, null);
        signInWithGoogleAuthCredential(googleAuthCredential);
    }

    public void signInWithGoogleAuthCredential(AuthCredential googleAuthCredential){
        authViewModel.signInWithGoogle(googleAuthCredential);

        authViewModel.authenticatedUserLiveData().observe(this, authenticatedUser -> {
            if(authenticatedUser.isNew()){
                createNewUser(authenticatedUser);
            }
            else
            {
                goToMainActivity(authenticatedUser);
            }
        });
    }

    public void createNewUser(User authenticatedUser){
//        authViewModel.
    }

    public void goToMainActivity(User user){
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        intent.putExtra(USER, user);
        startActivity(intent);
        finish();
    }

    public void logErrorMessage(Exception e){

    }
}