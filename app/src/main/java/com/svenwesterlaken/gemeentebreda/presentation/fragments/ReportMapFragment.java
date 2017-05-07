package com.svenwesterlaken.gemeentebreda.presentation.fragments;


import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.svenwesterlaken.gemeentebreda.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.svenwesterlaken.gemeentebreda.R;

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

                enableMyLocation();

                // Add a marker
                LatLng hogeschool = new LatLng(51.5843682,4.795152);
                mMap.addMarker(new MarkerOptions().position(hogeschool).title("Hogeschoollaan 1").snippet("Avans Locatie Hogeschoollaan"));

                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hogeschool, 17));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(hogeschool).zoom(17).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
