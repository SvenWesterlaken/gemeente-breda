package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.logic.partials.PlaceSearchSuggestion;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.SettingsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewLocationActivity extends BaseActivity implements FloatingSearchView.OnQueryChangeListener, FloatingSearchView.OnHomeActionClickListener, View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    MapView mMapView;
    private GoogleMap map;
    private Marker placedMarker;
    private DatabaseHandler handler;
    private TextView addressTV;
    private Double lat, lon;
    private FloatingSearchView mSearchView;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);

        handler = new DatabaseHandler(getApplicationContext());

        mSearchView = (FloatingSearchView) findViewById(R.id.newLocation_FSV_searchbar);
        FloatingActionButton confirmBTN = (FloatingActionButton) findViewById(R.id.newLocation_FAB_confirm);
        addressTV = (TextView) findViewById(R.id.newLocation_TV_adress);

        mSearchView.setOnQueryChangeListener(this);
        mSearchView.setOnHomeActionClickListener(this);
        confirmBTN.setOnClickListener(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).addApi(Places.GEO_DATA_API).build();
        }

        mMapView = (MapView) findViewById(R.id.newLocation_MV_map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
    }
    public void enableMyLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        Location testLocation = new Location("Geselecteerde straat", "Breda", new Random().nextInt(6) + 1, "4818RA", handler.getAllReports().size()+1, lat, lon);

        Intent intent = new Intent();
        intent.putExtra("location", testLocation);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onHomeClicked() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED,intent);
        finish();
    }

    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {


//        PendingResult<AutocompletePredictionBuffer> result = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, newQuery, null, null);
//        List<PlaceSearchSuggestion> suggestions = new ArrayList<>();
//        for(int i=0; i < 10; i++) {
//            AutocompletePrediction item = result.await().get(i);
//            PlaceSearchSuggestion suggestion = new PlaceSearchSuggestion(item.getPlaceId(), item.getPrimaryText(null), item.getSecondaryText(null), item.getFullText(null));
//            suggestions.add(suggestion);
//        }
//
//
//        mSearchView.swapSuggestions(suggestions);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        try {
            boolean success;

            if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(SettingsFragment.KEY_PREF_THEME, "").equals("light")) {
                success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map_light));
            } else {
                success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map_dark));
            }


            if (!success) {
                Log.e("GOOGLE MAP", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("GOOGLE MAP", "Can't find style. Error: ", e);
        }

        // Enable MyLocation Layer of Google Map
        enableMyLocation();

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        android.location.Location myLocation = null;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myLocation = locationManager.getLastKnownLocation(provider);
        }

        if (myLocation != null){
            // Get latitude of the current location
            double latitude = myLocation.getLatitude();

            // Get longitude of the current location
            double longitude = myLocation.getLongitude();

            // Create a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Add a marker at users initial position
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Current Location").snippet("You are here!"));

            // Move camera
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

        googleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng point) {
        if (placedMarker != null) {
            placedMarker.remove();
        }

        lat = point.latitude;
        lon = point.longitude;

        addressTV.setText(point.latitude + ", " + point.longitude);
        placedMarker = map.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}