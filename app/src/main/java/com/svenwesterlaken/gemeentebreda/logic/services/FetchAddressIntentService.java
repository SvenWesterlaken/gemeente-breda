package com.svenwesterlaken.gemeentebreda.logic.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.svenwesterlaken.gemeentebreda.domain.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Koen Kamman on 26-5-2017.
 */

public class FetchAddressIntentService extends IntentService {
    private ResultReceiver mReceiver;
    private int type;

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "com.svenwesterlaken.gemeentebreda.logic.services";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LATITUDE_DATA_EXTRA = PACKAGE_NAME + ".LATITUDE_DATA_EXTRA";
    public static final String LONGITUDE_DATA_EXTRA = PACKAGE_NAME + ".LONGITUDE_DATA_EXTRA";

    public static final int CURRENT_LOCATION = 1;
    public static final int MEDIA_LOCATION = 2;
    public static final int CHOOSE_LOCATION = 3;

    public FetchAddressIntentService() {
        super("AddressService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        Location location = new Location();

        assert intent != null;
        mReceiver = intent.getParcelableExtra("RECEIVER");
        type = intent.getIntExtra("TYPE", 0);

        double latitude = intent.getDoubleExtra(LATITUDE_DATA_EXTRA, 0);
        double longitude = intent.getDoubleExtra(LONGITUDE_DATA_EXTRA, 0);

        Log.d("GEOCODER", "" + "Geocoder is present: " + Geocoder.isPresent());

        try {

            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (!addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                location.setCity(city);
                location.setStreet(address);
                location.setPostalCode(postalCode);
                location.setLatitude(latitude);
                location.setLongitude(longitude);

                deliverResultToReceiver(SUCCESS_RESULT, location);
            } else {
                Log.e("ADDRESSES", "No addresses were returned by the geocoder.");
                deliverResultToReceiver(FAILURE_RESULT, location);
            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private void deliverResultToReceiver(int resultCode, Location location) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_DATA_KEY, location);
        bundle.putInt("TYPE", type);

        mReceiver.send(resultCode, bundle);
    }

}
