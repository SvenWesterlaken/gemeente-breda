package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.api.CategoryRequest;
import com.svenwesterlaken.gemeentebreda.data.api.ReportRequest;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        new CountDownTimer(1500, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                getCategories();
                getReports();
            }

            @Override
            public void onFinish() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String name = preferences.getString("pref_name", null);
                String email = preferences.getString("pref_email", null);
                String phone = preferences.getString("pref_phone", null);
                Intent i;

                //Start login activity when no name, email and phonenumber is specified (First time using the app)
                //Otherwise start ReportActivity
                if (name == null && email == null && phone == null) {
                    i = new Intent(getApplicationContext(), LoginActivity.class);
                } else if (name.equals("Onbekend") && email.equals("Onbekend") && phone.equals("Onbekend")){
                    i = new Intent(getApplicationContext(), LoginActivity.class);
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    
    private void getReports(){
    
        ReportRequest request = new ReportRequest(getApplicationContext());
        request.handleGetAllReports();
    }
    
    private void getCategories(){
        
        CategoryRequest request = new CategoryRequest(getApplicationContext());
        request.handleGetCategories();
    }
}
