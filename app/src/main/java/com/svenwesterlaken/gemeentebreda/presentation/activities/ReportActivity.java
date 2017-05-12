package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.view.PagerAdapter;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.logic.adapters.ReportPagerAdapter;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.ReportMapFragment;

import java.util.ArrayList;


public class ReportActivity extends MenuActivity {

    private PagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    ArrayList<Report> spotifyItemsList = new ArrayList<Report>();

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        super.onCreateDrawer(toolbar, this);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new ReportPagerAdapter(getSupportFragmentManager(), 2, getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
        menu.findItem(R.id.action_search).setOnMenuItemClickListener(new NotImplementedListener());
        menu.findItem(R.id.action_filter).setOnMenuItemClickListener(new NotImplementedListener());
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        ReportMapFragment mapFragment = (ReportMapFragment) f1;

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted.
                    // Do the task you need to do.
                    Log.i("LOCATION_PERMISSION", "GRANTED");
                    mapFragment.enableMyLocation();

                } else {

                    // permission denied. Disable the
                    // functionality that depends on this permission.
                    Log.i("LOCATION_PERMISSION", "DENIED");

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private class NotImplementedListener implements MenuItem.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Toast toast = Toast.makeText(getApplicationContext(), "Deze functie is nog niet geïmplementeerd", Toast.LENGTH_SHORT);
            toast.show();

            return true;
        }
    }
}
