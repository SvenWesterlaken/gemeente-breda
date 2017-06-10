package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.logic.adapters.ReportPagerAdapter;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.ReportListFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.ReportMapFragment;


public class ReportActivity extends MenuActivity  {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        super.onCreateDrawer(toolbar, this);

        PagerAdapter mSectionsPagerAdapter = new ReportPagerAdapter(getSupportFragmentManager(), 2, getApplicationContext(), new ReportMapFragment(),  new ReportListFragment());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), NewReportActivity.class);
                startActivity(i);
                finish();


            }
        });

        requestPermissions();


    }


    public void requestPermissions() {

        // Request ACCESS_FINE_LOCATION permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // sees the explanation, try again to request the permission.

                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.body_permission_alert)
                        .setTitle(R.string.title_permission_alert);

                // 3. Add button.
                builder.setNeutralButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestLocationPermission();
                    }
                });

                // 4. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();

                dialog.show();

            } else {

                // No explanation needed, we can request the permission.

                requestLocationPermission();

                // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }


    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        menu.findItem(R.id.action_filter);
        //TODO: Add listener to go to filter activity
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted.
                    // Do the task you need to do.
                    Log.i("LOCATION_PERMISSION", "GRANTED");
                    //      mapFragment.enableMyLocation();

                } else {

                    // permission denied. Disable the
                    // functionality that depends on this permission.
                    Log.i("LOCATION_PERMISSION", "DENIED");

                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}

