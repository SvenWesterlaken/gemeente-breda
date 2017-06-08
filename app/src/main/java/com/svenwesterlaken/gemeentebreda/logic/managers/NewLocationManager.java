package com.svenwesterlaken.gemeentebreda.logic.managers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Media;
import com.svenwesterlaken.gemeentebreda.logic.exceptions.NoLocationMetaException;
import com.svenwesterlaken.gemeentebreda.logic.handlers.MediaLocationHandler;
import com.svenwesterlaken.gemeentebreda.logic.services.FetchAddressIntentService;

/**
 * Created by Sven Westerlaken on 7-6-2017.
 */

public class NewLocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Activity activity;
    private Context context;
    private ResultReceiver mResultReceiver;
    private LocationManagerListener mListener;
    private SelectedLocationListener sListener;
    private GoogleApiClient mGoogleApiClient;
    private MediaLocationHandler mlHandler;

    public NewLocationManager(Activity a, Context c, LocationManagerListener mListener) {
        this.activity = a;
        this.context = c;
        this.mListener = mListener;
        this.mResultReceiver = new AddressResultReceiver(new Handler());
        this.mlHandler = new MediaLocationHandler(c);

        if (mGoogleApiClient == null) {
            newGoogleApiClient();
        }
    }

    public NewLocationManager(Activity a, Context c, SelectedLocationListener listener) {
        this.sListener = listener;
        this.activity = a;
        this.context = c;
        this.mResultReceiver = new AddressResultReceiver(new Handler());
        if (mGoogleApiClient == null) {
            newGoogleApiClient();
        }

    }

    public void getMediaLocation(Media m) {
        try {
            LatLng latlong = mlHandler.getLatLong(m.getUri());
            startGeoService(latlong.latitude, latlong.longitude, FetchAddressIntentService.MEDIA_LOCATION);
        } catch (NoLocationMetaException e) {
            mListener.disableButton(FetchAddressIntentService.MEDIA_LOCATION);
        }
    }

    public void getSelectedLocation(LatLng ll) {
        startGeoService(ll.latitude, ll.longitude, FetchAddressIntentService.CHOOSE_LOCATION);
    }

    private void newGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    public GoogleApiClient getApiClient() {
        return mGoogleApiClient;
    }

    private void startGeoService(double lat, double lon, int type) {
        Intent i = new Intent(context, FetchAddressIntentService.class);
        i.putExtra("RECEIVER", mResultReceiver);
        i.putExtra("TYPE", type);
        i.putExtra(FetchAddressIntentService.LATITUDE_DATA_EXTRA, lat);
        i.putExtra(FetchAddressIntentService.LONGITUDE_DATA_EXTRA, lon);
        activity.startService(i);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mListener.disableButton(FetchAddressIntentService.CURRENT_LOCATION);
        } else {
            android.location.Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                startGeoService(mLastLocation.getLatitude(), mLastLocation.getLongitude(), FetchAddressIntentService.CURRENT_LOCATION);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mListener.disableButton(FetchAddressIntentService.CURRENT_LOCATION);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mListener.disableButton(FetchAddressIntentService.CURRENT_LOCATION);
    }

    public interface LocationManagerListener {
        void setReceivedLocation(Location l, int type);
        void disableButton(int type);
    }

    public interface SelectedLocationListener {
        void setSelectedLocation(Location l);
    }


    public class AddressResultReceiver extends ResultReceiver {

        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                int type = resultData.getInt("TYPE");
                Location receivedLocation = resultData.getParcelable(FetchAddressIntentService.RESULT_DATA_KEY);

                if(receivedLocation != null) {
                    if (type == FetchAddressIntentService.CHOOSE_LOCATION) {
                        sListener.setSelectedLocation(receivedLocation);
                    } else {
                        if(mListener != null) {
                            mListener.setReceivedLocation(receivedLocation, type);
                        }
                    }
                }
            }
        }
    }

}
