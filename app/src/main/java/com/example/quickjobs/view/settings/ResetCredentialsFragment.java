package com.example.quickjobs.view.settings;

import android.os.Bundle;
import com.example.quickjobs.R;
import androidx.preference.PreferenceFragmentCompat;

public class ResetCredentialsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_reset_credentials, rootKey);
    }
}