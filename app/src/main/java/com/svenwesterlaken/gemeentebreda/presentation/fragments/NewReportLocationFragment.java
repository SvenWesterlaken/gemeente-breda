package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.GpsDirectory;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Media;
import com.svenwesterlaken.gemeentebreda.logic.services.FetchAddressIntentService;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewLocationActivity;
import com.svenwesterlaken.gemeentebreda.presentation.partials.NotImplementedListener;

import java.io.File;
import java.io.IOException;
import java.util.Collection;


public class NewReportLocationFragment extends Fragment {
    private LocationChangedListener mListener;
    private AddressResultReceiver mResultReceiver;
    private Location locationFromMedia;
    private View rootView;

    private static int NEW_LOCATION_REQUEST = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_report_location, container, false);

        ConstraintLayout chooseBTN = (ConstraintLayout) rootView.findViewById(R.id.location_BTN_choose);
        ConstraintLayout metaBTN = (ConstraintLayout) rootView.findViewById(R.id.location_BTN_meta);
        ConstraintLayout currentBTN = (ConstraintLayout) rootView.findViewById(R.id.location_BTN_current);

        metaBTN.setOnClickListener(new NotImplementedListener(getActivity().getApplicationContext()));
        currentBTN.setOnClickListener(new NotImplementedListener(getActivity().getApplicationContext()));
        chooseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewLocationActivity.class);
                startActivityForResult(i, NEW_LOCATION_REQUEST);
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
            //TODO: Geocoder doesn't return any addresses, should use Google API (not actually an option)
            startGeoIntentService(locationFromMedia);
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

    private void printMetadata(Uri imageUri) {

        try {
            File file = new File(getRealPathFromURI(getContext(), imageUri));
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    System.out.println(tag);
                }
            }

        } catch (ImageProcessingException | IOException e) {
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

    protected void startGeoIntentService(Location location) {
        Intent intent = new Intent(getContext(), FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LATITUDE_DATA_EXTRA, location.getLatitude());
        intent.putExtra(FetchAddressIntentService.Constants.LONGITUDE_DATA_EXTRA, location.getLongitude());
        getActivity().startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            String mAddressOutput = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);
            Log.i("ADDRESS", mAddressOutput);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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
            throw new ClassCastException(a.toString() + " must implement LocationChangedListener");
        }
    }

    public interface LocationChangedListener {
        void setLocation(Location t);
    }

    public void setImageLocationTag(Media m) {
        TextView text = (TextView) rootView.findViewById(R.id.location_TV_metaMessage);
        createLocationFromMedia(m.getUri());
        Double lat = locationFromMedia.getLatitude();
        Double lon = locationFromMedia.getLongitude();


        text.setText(lat + ", " + lon);
    }


}
