package io.github.gianpamx.pdd.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import io.github.gianpamx.pdd.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }
}
