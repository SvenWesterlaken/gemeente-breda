package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.ServiceCategory;
import com.svenwesterlaken.gemeentebreda.domain.ServiceReport;
import com.svenwesterlaken.gemeentebreda.domain.User;
import com.svenwesterlaken.gemeentebreda.logic.adapters.ReportPagerAdapter;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.ReportListFragment;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.ReportMapFragment;
import com.svenwesterlaken.gemeentebreda.presentation.partials.NotImplementedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class ReportActivity extends MenuActivity {

    private PagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ReportMapFragment mapFragment;
    private ReportListFragment listFragment;
    private static final String ENDPOINT = "http://37.34.59.50/breda/CitySDK/services.json";
    private static final String ENDPOINT2 = "http://37.34.59.50/breda/CitySDK/requests.json/?service_code=OV";
    private RequestQueue categoryQueue;
    private Gson gson;


    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    DatabaseHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categoryQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gbuilder = new GsonBuilder();
        gson = gbuilder.create();
        fetchPosts();

        super.onCreateDrawer(toolbar, this);

        mapFragment = new ReportMapFragment();
        listFragment = new ReportListFragment();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new ReportPagerAdapter(getSupportFragmentManager(), 2, getApplicationContext(), mapFragment, listFragment);
        handler = new DatabaseHandler(getApplicationContext());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), NewReportActivity.class);
                startActivity(i);
//
//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
//        spinner.setVisibility(View.GONE);
        menu.findItem(R.id.action_filter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getSupportActionBar().getThemedContext(), R.array.report_filter_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String filter = parent.getItemAtPosition(position).toString();
//                String sPopular = getResources().getString(R.string.popular);
//                String sRecent = getResources().getString(R.string.recent);
//                String sAdult = getResources().getString(R.string.adult);
//                String sRating = getResources().getString(R.string.rating);
//                String sTitle = getResources().getString(R.string.title);
//
//                if (filter.equals(sPopular)) {
//                    fManager.findPopularMovies();
//                } else if (filter.equals(sRecent)) {
//                    fManager.findRecentMovies();
//                } else if (filter.equals(sAdult)) {
//                    fManager.findAdultMovies();
//                } else if (filter.equals(sRating)) {
//                    fManager.findRatedMovies();
//                } else if (filter.equals(sTitle)) {
//                    fManager.findMoviesByTitle();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

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
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void fetchPosts() {
        StringRequest categoryRequest = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
        StringRequest reportsRequest = new StringRequest(Request.Method.GET, ENDPOINT2, onPostsLoaded2, onPostsError);
        categoryQueue.add(categoryRequest);
        categoryQueue.add(reportsRequest);
    }


    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            List<ServiceCategory> categories = Arrays.asList(gson.fromJson(response, ServiceCategory[].class));
            Log.i("Categories", categories.size() + "categories loaded");
            if (!(handler.getAllCategories().size() == 0)) {
                handler.deleteCategory();
            }
            for (ServiceCategory category : categories) {
                Log.i("Categories", category.categoryName + category.description);
                String name = category.categoryName;
                String description = category.description;
                Category category1 = new Category(handler.getAllCategories().size() + 1, name, description);

                handler.addCategory(category1);
            }


        }
    };

    private final Response.Listener<String> onPostsLoaded2 = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            List<ServiceReport> serviceReports = Arrays.asList(gson.fromJson(response, ServiceReport[].class));
            Log.i("Reports", serviceReports.size() + "reports loaded");
            if (handler.getAllCategories().size() == 0) {

                for (ServiceReport report : serviceReports) {
                    Log.i("Reports", report.address + report.serviceName);
                    Report report1 = new Report();
                    Location location = new Location();
                    location.setStreet(report.address);
                    location.setLatitude(report.latitude);
                    location.setLocationID(handler.getAllReports().size() + 1);
                    handler.addLocation(location);
                    report1.setReportID(handler.getAllReports().size() + 1);
                    report1.setCategory(handler.getCategory(1)); //ff aanpassen nog
                    report1.setLocation(location);
                    report1.setDescription("Wachten op API");

                    handler.addReport(report1);
                }


            }
        }
    };

        private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PostActivity", error.toString());
            }
        };

}

