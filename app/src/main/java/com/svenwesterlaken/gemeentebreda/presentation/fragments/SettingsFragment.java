package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.v4.content.IntentCompat;

import com.svenwesterlaken.gemeentebreda.BuildConfig;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.presentation.activities.SettingsActivity;

/**
 * Created by Sven on 9-5-2017.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String KEY_PREF_THEME = "pref_theme";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        initSummary();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreference(findPreference(key), key);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    private void initSummary() {
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
            Preference preference = getPreferenceScreen().getPreference(i);
            if (preference instanceof PreferenceCategory) {
                PreferenceCategory preferenceGroup = (PreferenceCategory) preference;
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                    Preference singlePref = preferenceGroup.getPreference(j);
                    initPreference(singlePref, singlePref.getKey());
                }
            } else {
                initPreference(preference, preference.getKey());
            }
        }
    }

    private void initPreference(Preference preference, String key) {
        if(preference != null) {
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                listPreference.setSummary(listPreference.getEntry());
            } else {
                if (key.equals("pref_info")) {
                    preference.setSummary(getString(R.string.pref_value_info) + " " + BuildConfig.VERSION_NAME);
                } else if (key.equals("pref_reset")) {
                    preference.setSummary(preference.getSummary());
                } else {
                    SharedPreferences sharedPrefs = getPreferenceManager().getSharedPreferences();
                    preference.setSummary(sharedPrefs.getString(key, "default"));
                }
            }
        }
    }

    private void updatePreference(Preference preference, String key) {
        if(preference != null) {
            if (preference instanceof ListPreference) {
                if (key.equals("pref_theme")) {
                    SettingsActivity activity = (SettingsActivity) getActivity();
                    activity.finish();
                    final Intent intent = activity.getIntent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(intent);
                } else {
                    ListPreference listPreference = (ListPreference) preference;
                    listPreference.setSummary(listPreference.getEntry());
                }
            } else {
                SharedPreferences sharedPrefs = getPreferenceManager().getSharedPreferences();
                preference.setSummary(sharedPrefs.getString(key, "Default"));
            }
        }
    }


}
