package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.svenwesterlaken.gemeentebreda.R;

import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by Koen Kamman on 5-5-2017.
 */

public class ReportMapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap map;

    public ReportMapFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View v = layoutInflater.inflate(R.layout.fragment_reportmap, viewGroup, false);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(bundle);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                map = mMap;

                // Enable MyLocation Layer of Google Map
                enableMyLocation();

                // Get LocationManager object from System Service LOCATION_SERVICE
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

                    // Add a marker
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

            }
        });

        return v;
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
