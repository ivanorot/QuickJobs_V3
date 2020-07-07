package com.example.quickjobs.view.main;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.quickjobs.R;
import com.example.quickjobs.model.beans.User;
import com.example.quickjobs.view.jobs.NewPostFragment;
import com.example.quickjobs.view.profile.MyProfileFragment;
import com.example.quickjobs.viewmodel.MainViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, FirebaseAuth.AuthStateListener {
    private final String TAG = "MainActivity";
    private final String USER = "user";

    private MainViewModel mainViewModel;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainScreen defaultScreen = MainScreen.HOME;

        initMainViewModel();
        initQuickJobsUser();
        initBottomNavigationView();

    }

    public void initMainViewModel(){
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    public void initQuickJobsUser(){
        User user = (User) getIntent().getSerializableExtra(USER);
        mainViewModel.setCurrentUserLiveData(user);
    }

    public void initBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    public void selectBottomNavigationViewMenuItem(@IdRes int inMenuItemId){
        bottomNavigationView.setOnNavigationItemSelectedListener(null);
        bottomNavigationView.setSelectedItemId(inMenuItemId);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        MainScreen temp = MainScreen.getMainScreenForMenuItem(menuItem.getItemId());
        if(temp != null){
            return true;
        }

        return false;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
    }
}

enum MainScreen{

    HOME(R.id.homeFragment, R.drawable.baseline_home_black_36, R.string.bottom_navigation_home, new HomeFragment()),
    NEWPOST(R.id.newPostFragment, R.drawable.baseline_add_black_36, R.string.bottom_navigation_new_post, new NewPostFragment()),
    MYJOBS(R.id.myJobsFragment, R.drawable.baseline_bookmarks_black_36, R.string.bottom_navigation_my_jobs, new HomeFragment()),
    PROFILE(R.id.myProfileFragment, R.drawable.baseline_perm_identity_black_36, R.string.bottom_navigation_my_profile, new MyProfileFragment());

    MainScreen(@IdRes int inMenuItem, @DrawableRes int inMenuItemIconId, @StringRes int inTitleStringId, Fragment inFragment) {
        menuItem = inMenuItem;
        menuItemIconId = inMenuItemIconId;
        titleStringId = inTitleStringId;
        fragment = inFragment;
    }

    @IdRes int menuItem;
    @DrawableRes int menuItemIconId;
    @StringRes int titleStringId;
    Fragment fragment;

    public static MainScreen getMainScreenForMenuItem(int menuItemId){
        for (MainScreen mainScreen : MainScreen.values()){
            if(mainScreen.menuItem == menuItemId){
                return mainScreen;
            }
        }
        return null;
    }
}