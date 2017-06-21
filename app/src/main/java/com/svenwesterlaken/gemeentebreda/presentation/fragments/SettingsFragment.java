package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.svenwesterlaken.gemeentebreda.BuildConfig;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.presentation.activities.SettingsActivity;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by Sven on 9-5-2017.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String KEY_PREF_THEME = "pref_theme";
    public static final String KEY_PREF_RADIUS = "pref_radius";

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

                if (key.equals(KEY_PREF_THEME) && listPreference.getEntry() == null) {
                    listPreference.setValueIndex(0);
                }

                if (key.equals(KEY_PREF_RADIUS) && listPreference.getEntry() == null) {
                    listPreference.setValueIndex(1);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("pref_radius", ((ListPreference) preference).getValue());
                    editor.apply();
                }

                listPreference.setSummary(listPreference.getEntry());

            } else {
                if (key.equals("pref_info")) {
                    preference.setSummary(getString(R.string.pref_value_info) + " " + BuildConfig.VERSION_NAME);
                    preference.setOnPreferenceClickListener(new InfoPreferenceListener());
                } else if (key.equals("pref_reset")) {
                    preference.setSummary(preference.getSummary());
                    preference.setOnPreferenceClickListener(new ResetPreferenceListener());
                } else {
                    SharedPreferences sharedPrefs = getPreferenceManager().getSharedPreferences();
                    preference.setSummary(sharedPrefs.getString(key, "Onbekend"));
                }
            }
        }
    }

    private void updatePreference(Preference preference, String key) {
        SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
        Log.i("PREFERENCE_RADIUS", prefs.getString("pref_radius", ""));

        if(preference != null) {
            if (preference instanceof ListPreference) {
                if (key.equals(KEY_PREF_THEME)) {
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

                if (key.equals("pref_name") || key.equals("pref_email")) {
                    NavigationView v = (NavigationView) getActivity().findViewById(R.id.nav_view);
                    View header = v.getHeaderView(0);
                    TextView name = (TextView) header.findViewById(R.id.drawerMenu_TV_username);
                    TextView email = (TextView) header.findViewById(R.id.drawerMenu_TV_useremail);

                    if (key.equals("pref_name")) {
                        name.setText(sharedPrefs.getString(key, ""));
                    }

                    if (key.equals("pref_email")) {
                        email.setText(sharedPrefs.getString(key, ""));
                    }
                }
            }
        }
    }

    private class ResetPreferenceListener implements Preference.OnPreferenceClickListener {

        @Override
        public boolean onPreferenceClick(Preference preference) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage("Weet u zeker dat u alle instellingen wilt herstellen?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Ja",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();

                            SharedPreferences sharedPrefs = getDefaultSharedPreferences(getActivity());
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.clear();
                            editor.commit();

                            SettingsActivity activity = (SettingsActivity) getActivity();
                            activity.finish();
                            final Intent intent = activity.getIntent();
                            activity.startActivity(intent);
                        }
                    });

            builder1.setNegativeButton(
                    "Nee",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder1.create();
            alert.show();

            return true;
        }
    }

    private class InfoPreferenceListener implements Preference.OnPreferenceClickListener {

        @Override
        public boolean onPreferenceClick(Preference preference) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Deze functie is nog niet geïmplementeerd", Toast.LENGTH_SHORT);
            toast.show();

            return true;
        }
    }


}
