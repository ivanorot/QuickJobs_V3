package com.example.quickjobs.view.settings;

import android.os.Bundle;
import com.example.quickjobs.R;
import androidx.preference.PreferenceFragmentCompat;

public class LanguageFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_language, rootKey);
    }
}