package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Media;
import com.svenwesterlaken.gemeentebreda.logic.managers.LocationManager;
import com.svenwesterlaken.gemeentebreda.logic.services.FetchAddressIntentService;
import com.svenwesterlaken.gemeentebreda.logic.util.DoubleUtil;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewLocationActivity;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewReportActivity;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Random;

import static android.app.Activity.RESULT_OK;


public class NewReportLocationFragment extends Fragment implements LocationManager.LocationManagerListener {
    private LocationChangedListener mListener;
    private LocationManager lManager;
    private GoogleApiClient mGoogleApiClient;

    private Location location, mediaLocation, currentLocation, chosenLocation;
    private android.location.Location mLastLocation;

    private Double lat, lon;
    private DatabaseHandler handler;

    private ConstraintLayout chooseBTN, metaBTN, currentBTN;
    private Location locationFromMedia;
    private View rootView;
    private TextView locationTV;
    private FloatingActionButton confirmFAB;
    private Animation popupAnimation, popoutAnimation;

    private float alpha;
    private static int NEW_LOCATION_REQUEST = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        popupAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.popup_animation);
        popoutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.popout_animation);
        handler = new DatabaseHandler(getContext());
        lManager = new LocationManager(getActivity(), getContext(), handler, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_report_location, container, false);
        alpha = rootView.findViewById(R.id.location_BTN_delete).getAlpha();

        chooseBTN = (ConstraintLayout) rootView.findViewById(R.id.location_BTN_choose);
        metaBTN = (ConstraintLayout) rootView.findViewById(R.id.location_BTN_meta);
        currentBTN = (ConstraintLayout) rootView.findViewById(R.id.location_BTN_current);

        locationTV = (TextView) rootView.findViewById(R.id.location_TV_location);


        metaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = new Location("Metadata straat", "Breda", new Random().nextInt(6), "4818MD", handler.getAllReports().size() + 1, lat, lon);
                setAddress(location);
                if (locationFromMedia != null){
                    setAddress(locationFromMedia);
                }
                enableConfirmButton();
            }
        });
        currentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    onConnectionSuspended(1);
                } else {
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (mLastLocation != null) {
                        location = new Location("Huidige straat", "Breda", new Random().nextInt(6), "4818CS", handler.getAllReports().size() + 1, mLastLocation.getLatitude(), mLastLocation.getLongitude());

                        if (location.getLongitude() != null && location.getLatitude() != null) {
                            startCurLocGeoIntentService(location);
                            Log.i("SERVICE", "GeoService has started");
                        }

                        if (curLoc != null){
                            setAddress(curLoc);
                        }
                        enableConfirmButton();
                    }
                }
            }
        });
        chooseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewLocationActivity.class);
                startActivityForResult(i, NEW_LOCATION_REQUEST);
            }


        });

        confirmFAB = (FloatingActionButton) rootView.findViewById(R.id.location_FAB_confirm);
        confirmFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setLocation(location);
                confirmFAB.setAnimation(popoutAnimation);
                confirmFAB.setVisibility(View.INVISIBLE);
                ((NewReportActivity) getActivity()).scrollToNext();
            }
        });

        return rootView;
    }

    @Override
    public void setReceivedLocation(Location l, int type) {
        TextView text = null;

        if(type == FetchAddressIntentService.CURRENT_LOCATION) {
            text = (TextView) rootView.findViewById(R.id.location_TV_currentMessage);
        }


        if(text != null && l != null) {
            text.setText(l.getCity() + " " + l.getStreet() + " " + l.getPostalCode());
        }

    }

    @Override
    public void disableButton(int type) {
        if(type == FetchAddressIntentService.CURRENT_LOCATION) {
            currentBTN.setEnabled(false);
            currentBTN.setAlpha(alpha);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_LOCATION_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Location location = extras.getParcelable("location");

                if (location != null) {
                    setAddress(location);
                    enableConfirmButton();
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void enableConfirmButton() {
        confirmFAB.setVisibility(View.VISIBLE);
        confirmFAB.startAnimation(popupAnimation);
    }

    private void setAddress(Location l) {
        this.location = l;
        locationTV.setText(l.getStreet() + " " + l.getCity() + ", " + l.getPostalCode());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a = null;

        if (context instanceof Activity) {
            a = (Activity) context;
        }
        try {
            mListener = (LocationChangedListener) a;
        } catch (ClassCastException e) {
            assert a != null;
            throw new ClassCastException(a.toString() + " must implement LocationChangedListener");
        }
    }

    public interface LocationChangedListener {
        void setLocation(Location t);
    }

//    public void setImageLocationTag(Media m) {
//        TextView text = (TextView) rootView.findViewById(R.id.location_TV_metaMessage);
//        lat = locationFromMedia.getLatitude();
//        lon = locationFromMedia.getLongitude();
//
//        if (lat != null) {
//            if (DoubleUtil.isZero(lat, 0.00000001)) {
//                lat = null;
//                lon = null;
//            }
//        }
//
//
//        if (lat == null) {
//            text.setText(R.string.location_error);
//            metaBTN.setEnabled(false);
//            metaBTN.setAlpha(alpha);
//
//        } else {
//
//            text.setText(lat + ", " + lon);
//        }
//
//    }

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
    public void onResume() {
        if (location != null) {
            setAddress(location);
        }
        super.onResume();
    }


}
