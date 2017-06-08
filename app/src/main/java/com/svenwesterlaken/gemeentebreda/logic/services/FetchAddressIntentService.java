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
    protected ResultReceiver mReceiver;

    public FetchAddressIntentService() {
        super("AddressService");
    }

    public final class Constants {
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME =
                "com.svenwesterlaken.gemeentebreda.logic.services";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME +
                ".RESULT_DATA_KEY";
        public static final String LATITUDE_DATA_EXTRA = PACKAGE_NAME + ".LATITUDE_DATA_EXTRA";
        public static final String LONGITUDE_DATA_EXTRA = PACKAGE_NAME + ".LONGITUDE_DATA_EXTRA";
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        Location location = new Location();

        double latitude = intent.getDoubleExtra(Constants.LATITUDE_DATA_EXTRA, 0);
        double longitude = intent.getDoubleExtra(Constants.LONGITUDE_DATA_EXTRA, 0);

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
//
//                location.setCity(city);

                //deliverResultToReceiver(Constants.SUCCESS_RESULT, city);
            } else {
                Log.e("ADDRESSES", "No addresses were returned by the geocoder.");
            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }

}
