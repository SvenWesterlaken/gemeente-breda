package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.svenwesterlaken.gemeentebreda.R;

public class SettingsActivity extends MenuActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        super.onCreateDrawer(toolbar, this);
    }

    @Override
    public void onBackPressed() {
        //Go back to ReportActivity if the theme erased the activity stack
        if(isTaskRoot()) {
            Intent i = new Intent(getApplicationContext(), ReportActivity.class);
            startActivity(i);
        } else {
            super.onBackPressed();
        }
    }
}
