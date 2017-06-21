package com.svenwesterlaken.gemeentebreda.presentation.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.api.ReportRequest;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.presentation.activities.DetailedReportActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by Koen Kamman on 5-5-2017.
 */

public class ReportMapFragment extends Fragment implements ReportRequest.ReportListener, GoogleMap.OnInfoWindowClickListener {

    MapView mMapView;
    private GoogleMap map;
    private List<Marker> markers;
    private HashMap<Marker, Report> reportHashMap = new HashMap<>();
    private Location myLocation;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View v = layoutInflater.inflate(R.layout.fragment_reportmap, viewGroup, false);

        markers = new ArrayList<>();

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
                myLocation = null;
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    myLocation = locationManager.getLastKnownLocation(provider);
                }

                // Locations
                LatLng hogeschool = new LatLng(51.5843682, 4.795152);

                if (myLocation != null) {
                    // Get latitude of the current location
                    double latitude = myLocation.getLatitude();

                    // Get longitude of the current location
                    double longitude = myLocation.getLongitude();

                    // Create a LatLng object for the current location
                    LatLng latLng = new LatLng(latitude, longitude);

                    // Add a marker at users initial position
                    //mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location").snippet("You are here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

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

                map.setOnInfoWindowClickListener(ReportMapFragment.this);

                map.getUiSettings().setMapToolbarEnabled(false);

                getReports();
            }
        });

        return v;
    }

    public void getReports() {
        if (!markers.isEmpty()) {
            for (int i = 0; i < markers.size(); i++) {
                markers.get(i).remove();
            }
            markers.clear();
        }

        ReportRequest request = new ReportRequest(getContext(), this);
        com.svenwesterlaken.gemeentebreda.domain.Location location = new com.svenwesterlaken.gemeentebreda.domain.Location();

        if (myLocation != null) {
            LatLng hogeschool = new LatLng(51.5843682, 4.795152);
            myLocation.setLatitude(hogeschool.latitude);
            myLocation.setLongitude(hogeschool.longitude);
        }

        double lat = myLocation.getLatitude();
        double lng = myLocation.getLongitude();
        location.setLatitude(lat);
        location.setLongitude(lng);

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            String radius = sharedPrefs.getString("pref_radius", "");
            if (radius.isEmpty()){
                String[] testArray = getResources().getStringArray(R.array.radius_preference_array_values);
                radius = testArray[2];
            }
            Log.i("PREFERENCE_RADIUS", radius);
            request.handleGetAllReports(location, Double.parseDouble(radius));

    }

    public void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onReportsAvailable(Report report) {
        if (report.getLocation() != null) {
            com.svenwesterlaken.gemeentebreda.domain.Location location = report.getLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(report.getCategory().getCategoryName()).snippet(report.getDescription());
            Marker marker = map.addMarker(markerOptions);
            markers.add(marker);
            reportHashMap.put(marker, report);
        }
    }

    @Override
    public void onFinished() {
        //Not needed
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
    public void onInfoWindowClick(Marker marker) {
        Report report = reportHashMap.get(marker);
        Intent intent = new Intent(getContext(), DetailedReportActivity.class);
        intent.putExtra("REPORT", report);
        startActivity(intent);
    }
}
