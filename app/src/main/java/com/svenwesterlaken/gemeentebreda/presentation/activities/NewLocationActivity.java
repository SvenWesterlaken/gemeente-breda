package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.FloatingSearchView.OnHomeActionClickListener;
import com.arlib.floatingsearchview.FloatingSearchView.OnQueryChangeListener;
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
import com.svenwesterlaken.gemeentebreda.logic.managers.NewLocationManager;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.SettingsFragment;

import static com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import static com.svenwesterlaken.gemeentebreda.logic.managers.NewLocationManager.SelectedLocationListener;

public class NewLocationActivity extends BaseActivity implements OnQueryChangeListener, OnHomeActionClickListener, OnClickListener, OnMapReadyCallback, OnMapClickListener, SelectedLocationListener {
    
    private MapView mMapView;
    private NewLocationManager lManager;
    private GoogleMap map;
    private Marker placedMarker;
    private DatabaseHandler handler;
    private TextView streetTV, postalTV;
    private Location selectedLocation;
    private FloatingSearchView mSearchView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);
        
        handler = new DatabaseHandler(getApplicationContext());
        lManager = new NewLocationManager(this, getApplicationContext(), this);
        
        mSearchView = (FloatingSearchView) findViewById(R.id.newLocation_FSV_searchbar);
        FloatingActionButton confirmBTN = (FloatingActionButton) findViewById(R.id.newLocation_FAB_confirm);
        
        streetTV = (TextView) findViewById(R.id.newLocation_TV_adress);
        postalTV = (TextView) findViewById(R.id.newLocation_TV_postalCode);
        
        mSearchView.setOnQueryChangeListener(this);
        mSearchView.setOnHomeActionClickListener(this);
        confirmBTN.setOnClickListener(this);
        
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
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
        
        enableMyLocation();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        
        // Get Current Location
        android.location.Location myLocation = null;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myLocation = locationManager.getLastKnownLocation(provider);
        }
        
        if (myLocation != null){
            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
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
        
        lManager.getSelectedLocation(point);
        placedMarker = map.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }
    
    @Override
    public void setSelectedLocation(Location l) {
        selectedLocation = l;
        streetTV.setText(l.getStreet());
        postalTV.setText(l.getPostalCode() + " " + l.getCity());
    }
    
    @Override
    public void onClick(View v) {
        if(selectedLocation != null) {
            Intent intent = new Intent();
            intent.putExtra("location", selectedLocation);
            setResult(RESULT_OK, intent);
            finish();
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
        lManager.getApiClient().connect();
        super.onStart();
    }
    
    @Override
    public void onStop() {
        lManager.getApiClient().disconnect();
        super.onStop();
    }
}
