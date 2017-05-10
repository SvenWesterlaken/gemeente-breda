package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.svenwesterlaken.gemeentebreda.R;

public class SettingsActivity extends MenuActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        super.onCreateDrawer(toolbar, this);
    }
}
