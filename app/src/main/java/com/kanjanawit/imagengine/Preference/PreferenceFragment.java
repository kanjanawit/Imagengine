package com.kanjanawit.imagengine.Preference;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.kanjanawit.imagengine.R;

class PreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference, rootKey);
    }
}
