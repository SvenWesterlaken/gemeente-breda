package com.svenwesterlaken.gemeentebreda.logic.managers;

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
import android.os.ResultReceiver;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationServices;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.logic.services.FetchAddressIntentService;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Random;

/**
 * Created by Sven Westerlaken on 7-6-2017.
 */

public class LocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Location curLoc, setLoc, medLoc;

    private Activity activity;
    private Context context;
    private ResultReceiver mResultReceiver;
    private DatabaseHandler handler;
    private LocationManagerListener mListener;

    public LocationManager(Activity a, Context c, DatabaseHandler handler, LocationManagerListener mListener) {
        this.activity = a;
        this.context = c;
        this.handler = handler;
        this.mListener = mListener;
    }

    public GoogleApiClient getApiClient(ConnectionCallbacks cb, GoogleApiClient.OnConnectionFailedListener fl) {
        return new GoogleApiClient.Builder(context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    public void startGeoService(double lat, double lon, int type) {
        Intent i = new Intent(context, FetchAddressIntentService.class);
        i.putExtra("RECEIVER", mResultReceiver);
        i.putExtra("TYPE", type);
        i.putExtra(FetchAddressIntentService.LATITUDE_DATA_EXTRA, lat);
        i.putExtra(FetchAddressIntentService.LONGITUDE_DATA_EXTRA, lon);
        activity.startService(i);
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

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver() {
            super(new Handler());
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                int type = resultData.getInt("TYPE");
                Location recievedLocation = resultData.getParcelable(FetchAddressIntentService.RESULT_DATA_KEY);
                mListener.setRecievedLocation(recievedLocation, type);
            }



            // Set address from service

            locationFromMedia.setStreet(mAddressOutput.getStreet());
            locationFromMedia.setPostalCode(mAddressOutput.getPostalCode());
            locationFromMedia.setCity(mAddressOutput.getCity());

            //setAddress(locationFromMedia);

        }

    }

    public void getVideoLocationMetadata(Uri videoUri) {
        ContentResolver videoResolver = context.getContentResolver();
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
            File file = new File(getRealPathFromURI(context, imageUri));
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

    public interface LocationManagerListener {
        void setReceivedLocation(Location l, int type);
        void disableButton(int type);
    }

}
