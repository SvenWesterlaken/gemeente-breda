package com.svenwesterlaken.gemeentebreda.presentation.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.svenwesterlaken.gemeentebreda.domain.Report;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by Koen Kamman on 5-5-2017.
 */

public class ReportMapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap map;
    private Marker placedMarker;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View v = layoutInflater.inflate(R.layout.fragment_reportmap, viewGroup, false);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(bundle);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Log.e("MAPSINITIALIZER", e.getMessage());
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                map = mMap;

                try {
                    boolean success;

                    if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SettingsFragment.KEY_PREF_THEME, "").equals("light")) {
                        success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_light));
                    } else {
                        success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_dark));
                    }


                    if (!success) {
                        Log.e("GOOGLE MAP", "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e("GOOGLE MAP", "Can't find style. Error: ", e);
                }

                // Enable MyLocation Layer of Google Map
                enableMyLocation();

                // Get NewLocationManager object from System Service LOCATION_SERVICE
                LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

                // Create a criteria object to retrieve provider
                Criteria criteria = new Criteria();

                // Get the name of the best provider
                String provider = locationManager.getBestProvider(criteria, true);

                // Get Current Location
                Location myLocation = null;
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    myLocation = locationManager.getLastKnownLocation(provider);
                }

                // Locations
                LatLng hogeschool = new LatLng(51.5843682,4.795152);

                if (myLocation != null){
                    // Get latitude of the current location
                    double latitude = myLocation.getLatitude();

                    // Get longitude of the current location
                    double longitude = myLocation.getLongitude();

                    // Create a LatLng object for the current location
                    LatLng latLng = new LatLng(latitude, longitude);

                    // Add a marker at users initial position
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location").snippet("You are here!"));

                    // Move camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                } else {
                    // Move camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hogeschool, 17));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(hogeschool).zoom(17).build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }

                // Add markers
                mMap.addMarker(new MarkerOptions().position(hogeschool).title("Hogeschoollaan 1").snippet("Avans Locatie Hogeschoollaan"));

                placeMarkers(getAllReports());


                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
                        //Remove the previously placed marker
                        if (placedMarker != null) {
                            placedMarker.remove();
                        }
                        //Place a new marker at the location of the tap
                        placedMarker = map.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                });

            }
        });

        return v;
    }

    public List<Report> getAllReports() {
        DatabaseHandler handler = new DatabaseHandler(getContext());
        ArrayList<Report> reports;
        reports = handler.getAllReports();

        for (Report report : reports){
            com.svenwesterlaken.gemeentebreda.domain.Location location = handler.getLocation(report.getLocationID());
            report.setLocation(location);
        }

        handler.close();
        return reports;
    }

    public void placeMarkers(List<Report> reports) {

        for(Report report : reports) {
            if (report.getLocation() != null) {
                com.svenwesterlaken.gemeentebreda.domain.Location location = report.getLocation();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(report.getCategory().getCategoryName()).snippet(report.getDescription());
                map.addMarker(markerOptions);

            }
        }

    }

    public void enableMyLocation(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
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

}
