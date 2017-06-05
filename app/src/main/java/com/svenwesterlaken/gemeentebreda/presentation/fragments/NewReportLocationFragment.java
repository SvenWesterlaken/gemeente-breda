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
import com.svenwesterlaken.gemeentebreda.logic.services.FetchAddressIntentService;
import com.svenwesterlaken.gemeentebreda.logic.util.DoubleUtil;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewLocationActivity;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewReportActivity;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Random;

import static android.app.Activity.RESULT_OK;


public class NewReportLocationFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private LocationChangedListener mListener;
    private MediaAddressResultReceiver mediaResultReceiver;
    private CurLocAddressResultReceiver curLocResultReceiver;
    private GoogleApiClient mGoogleApiClient;

    private Location location, curLoc;
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

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }

        handler = new DatabaseHandler(getContext());

        mediaResultReceiver = new MediaAddressResultReceiver(new Handler());
        curLocResultReceiver = new CurLocAddressResultReceiver(new Handler());

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

                        setAddress(location);
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

    public void createLocationFromMedia(Uri uri) {
        String mime = getContext().getContentResolver().getType(uri);
        locationFromMedia = new Location();

        if (mime != null) {
            if (mime.contains("video")) {
                getVideoLocationMetadata(uri);
            } else if (mime.contains("image")) {
                getImageLocationMetadata(uri);
            }
        }

        if (locationFromMedia.getLongitude() != null && locationFromMedia.getLatitude() != null) {
            startMediaGeoIntentService(locationFromMedia);
            Log.i("SERVICE", "GeoService has started");
        }

    }

    private void getImageLocationMetadata(Uri imageUri) {

        try {
            File file = new File(getRealPathFromURI(getContext(), imageUri));
            // Read all metadata from the image
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            // See whether it has GPS data
            Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
            if (gpsDirectories.isEmpty()) {
                Log.i("GEOLOCATION", "Geolocation is not available for this image: " + file.getAbsolutePath());
            }
            for (GpsDirectory gpsDirectory : gpsDirectories) {
                // Try to read out the location, making sure it's non-zero
                GeoLocation geoLocation = gpsDirectory.getGeoLocation();
                if (geoLocation != null && !geoLocation.isZero()) {
                    Log.i("Photo Latitude", "Latitude: " + geoLocation.getLatitude());
                    Log.i("Photo Longitude", "Longitude: " + geoLocation.getLongitude());
                    locationFromMedia.setLatitude(geoLocation.getLatitude());
                    locationFromMedia.setLongitude(geoLocation.getLongitude());
                } else {
                    Log.i("GEOLOCATION", "Geolocation is not available for this image: " + file.getAbsolutePath());
                }
            }
        } catch (IOException | ImageProcessingException e) {
            e.printStackTrace();
        }
    }

    public void getVideoLocationMetadata(Uri videoUri) {
        ContentResolver videoResolver = getContext().getContentResolver();
        Cursor videoCursor = videoResolver.query(videoUri, null, null, null, null);

        if (videoCursor != null && videoCursor.moveToFirst()) {
            //get columns
            int latColumn = videoCursor.getColumnIndex
                    (MediaStore.Video.Media.LATITUDE);
            int lonColumn = videoCursor.getColumnIndex
                    (MediaStore.Video.Media.LONGITUDE);
            int resColumn = videoCursor.getColumnIndex
                    (MediaStore.Video.Media.RESOLUTION);
            int durationColumn = videoCursor.getColumnIndex
                    (MediaStore.Video.Media.DURATION);

            do {
                String thisLat = Double.toString(videoCursor.getDouble(latColumn));
                String thisLon = Double.toString(videoCursor.getDouble(lonColumn));

                Log.d("video", "------------------");
                Log.d("video Latitude", thisLat);
                Log.d("video Longitude", thisLon);

                locationFromMedia.setLatitude(videoCursor.getDouble(latColumn));
                locationFromMedia.setLongitude(videoCursor.getDouble(lonColumn));
            }
            while (videoCursor.moveToNext());
        }

        if (videoCursor != null) {
            videoCursor.close();
        }

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = 0;
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else {
                return null;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    protected void startMediaGeoIntentService(Location location) {
        Intent intent = new Intent(getContext(), FetchAddressIntentService.class);
        intent.putExtra("RECEIVER", mediaResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LATITUDE_DATA_EXTRA, location.getLatitude());
        intent.putExtra(FetchAddressIntentService.Constants.LONGITUDE_DATA_EXTRA, location.getLongitude());
        getActivity().startService(intent);
    }

    protected void startCurLocGeoIntentService(Location location) {
        Intent intent = new Intent(getContext(), FetchAddressIntentService.class);
        intent.putExtra("RECEIVER", curLocResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LATITUDE_DATA_EXTRA, location.getLatitude());
        intent.putExtra(FetchAddressIntentService.Constants.LONGITUDE_DATA_EXTRA, location.getLongitude());
        getActivity().startService(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            onConnectionSuspended(1);
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                TextView text = (TextView) rootView.findViewById(R.id.location_TV_currentMessage);
                text.setText(String.valueOf(mLastLocation.getLatitude()) + ", " + String.valueOf(mLastLocation.getLongitude()));

                location = new Location("Huidige straat", "Breda", new Random().nextInt(6), "4818CS", handler.getAllReports().size() + 1, mLastLocation.getLatitude(), mLastLocation.getLongitude());
                startCurLocGeoIntentService(location);
                Log.i("SERVICE", "GeoService has started");
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        currentBTN.setEnabled(false);
        currentBTN.setAlpha(alpha);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        currentBTN.setEnabled(false);
        currentBTN.setAlpha(alpha);
    }

    private class MediaAddressResultReceiver extends ResultReceiver {
        public MediaAddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Set address from service
            Location mAddressOutput = resultData.getParcelable(FetchAddressIntentService.Constants.RESULT_DATA_KEY);
            locationFromMedia.setStreet(mAddressOutput.getStreet());
            locationFromMedia.setPostalCode(mAddressOutput.getPostalCode());
            locationFromMedia.setCity(mAddressOutput.getCity());

            TextView text = (TextView) rootView.findViewById(R.id.location_TV_metaMessage);
            text.setText(locationFromMedia.getCity() + " " + locationFromMedia.getStreet() + " " + locationFromMedia.getPostalCode());

        }
    }

    private class CurLocAddressResultReceiver extends ResultReceiver {
        public CurLocAddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Set address from service
            Location mAddressOutput = resultData.getParcelable(FetchAddressIntentService.Constants.RESULT_DATA_KEY);
            curLoc = new Location();
            curLoc.setLatitude(mAddressOutput.getLatitude());
            curLoc.setLongitude(mAddressOutput.getLongitude());
            curLoc.setStreet(mAddressOutput.getStreet());
            curLoc.setPostalCode(mAddressOutput.getPostalCode());
            curLoc.setCity(mAddressOutput.getCity());

            TextView text = (TextView) rootView.findViewById(R.id.location_TV_currentMessage);
            text.setText(curLoc.getCity() + " " + curLoc.getStreet() + " " + curLoc.getPostalCode());

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
        locationTV.setText(l.getStreet() + " " + l.getHouseNumber() + ", " + l.getCity());
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

    public void setImageLocationTag(Media m) {
        TextView text = (TextView) rootView.findViewById(R.id.location_TV_metaMessage);
        createLocationFromMedia(m.getUri());
        lat = locationFromMedia.getLatitude();
        lon = locationFromMedia.getLongitude();

        if (lat != null) {
            if (DoubleUtil.isZero(lat, 0.00000001)) {
                lat = null;
                lon = null;
            }
        }


        if (lat == null) {
            text.setText(R.string.location_error);
            metaBTN.setEnabled(false);
            metaBTN.setAlpha(alpha);

        } else {

            text.setText(lat + ", " + lon);
        }

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
    public void onResume() {
        if (location != null) {
            setAddress(location);
        }
        super.onResume();
    }


}
