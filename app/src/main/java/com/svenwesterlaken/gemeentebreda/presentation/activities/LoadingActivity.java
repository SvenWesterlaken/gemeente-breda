package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;

import com.svenwesterlaken.gemeentebreda.R;

public class LoadingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new CountDownTimer(1500, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Intent i = null;
                String name = preferences.getString("pref_name", null);
                String email = preferences.getString("pref_email", null);
                String phone = preferences.getString("pref_phone", null);

                //Start login activity when no name, email and phonenumber is specified (First time using the app)
                //Otherwise start ReportActivity
                if (name != null && email != null && phone != null) {
                    if ( name.equals("Onbekend") && email.equals("Onbekend") && phone.equals("Onbekend")) {
                        i = new Intent(getApplicationContext(), LoginActivity.class);
                    } else {
                        i = new Intent(getApplicationContext(), ReportActivity.class);
                    }
                } else {
                    i = new Intent(getApplicationContext(), ReportActivity.class);
                }

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }.start();
    }
}
