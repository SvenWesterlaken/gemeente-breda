package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.GpsDirectory;
import com.svenwesterlaken.gemeentebreda.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static android.app.Activity.RESULT_OK;

public class NewReportMediaFragment extends Fragment {
    private Button photoBTN, selectBTN, videoBTN;
    private VideoView video;
    private ImageView image;
    private Bitmap bitmap;
    private Uri videoUri;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_LOAD_IMG = 2;
    static final int REQUEST_VIDEO_CAPTURE = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_report_media, container, false);

        image = (ImageView) rootView.findViewById(R.id.media_IV_image);

        MediaController mediaController = new MediaController(getContext());
        video = (VideoView) rootView.findViewById(R.id.media_VV_video);
        video.setMediaController(mediaController);

        photoBTN = (Button) rootView.findViewById(R.id.media_btn_photo);
        photoBTN.setOnClickListener(new PhotoClickListener());

        videoBTN = (Button) rootView.findViewById(R.id.media_btn_video);
        videoBTN.setOnClickListener(new VideoClickListener());

        selectBTN = (Button) rootView.findViewById(R.id.media_btn_select);
        selectBTN.setOnClickListener(new SelectMediaClickListener());

        return rootView;
    }

    private class PhotoClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            dispatchTakePictureIntent();
        }
    }
    private class VideoClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            dispatchTakeVideoIntent();
        }
    }

    private class SelectMediaClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("*/*");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){
                String[] mimetypes = {"image/*", "video/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            }

            startActivityForResult(intent, REQUEST_LOAD_IMG);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        image.setImageBitmap(bitmap);
        video.setVideoURI(videoUri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(bitmap);
            MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "testTitle" , "testDescription");
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = data.getData();
            video.setVideoURI(videoUri);

            File file = new File(videoUri.getPath());
            String filePath = file.getAbsolutePath();
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.DATA, filePath);
            getContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        }

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOAD_IMG && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                image.setImageBitmap(selectedImage);

                File file = new File(getRealPathFromURI(getContext(), imageUri));

                //Prints location data for all video files.
                //getLocalVideoFiles(getContext());

                //Prints all

                //Prints location metadata tags for selected image.
                getLocationMetadata(file);

            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }

        }
    }

    public void getLocalVideoFiles(Context context) {
        ContentResolver videoResolver = context.getContentResolver();
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String test = getRealPathFromURI(context, videoUri);
        Cursor videoCursor = videoResolver.query(videoUri, null, null, null, null);

        if(videoCursor!=null && videoCursor.moveToFirst()){
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
                String thisRes = Double.toString(videoCursor.getDouble(resColumn));
                String thisDuration = Double.toString(videoCursor.getDouble(durationColumn));

                Log.d("video","------------------");
                Log.d("video Duration",thisDuration);
                Log.d("video Resolution",thisRes);
                Log.d("video Latitude",thisLat);
                Log.d("video Longitude",thisLon);
            }
            while (videoCursor.moveToNext());
        }

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void printMetadata(File file) {
        try {

            Metadata metadata = ImageMetadataReader.readMetadata(file);

            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    System.out.println(tag);
                }
            }

        }
        catch (ImageProcessingException | IOException e){ e.printStackTrace();}
    }

    private void getLocationMetadata(File file) {

        try {
            // Read all metadata from the image
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            // See whether it has GPS data
            Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
            if (gpsDirectories.isEmpty()){
                Log.i("GEOLOCATION", "Geolocation is not available for this image: " + file.getAbsolutePath());
            }
            for (GpsDirectory gpsDirectory : gpsDirectories) {
                // Try to read out the location, making sure it's non-zero
                GeoLocation geoLocation = gpsDirectory.getGeoLocation();
                if (geoLocation != null && !geoLocation.isZero()) {
                    // Add to our collection for use below
                    Log.i("Photo Latitude", "Latitude: " + geoLocation.getLatitude());
                    Log.i("Photo Longitude", "Longitude: " + geoLocation.getLongitude());
                    break;
                } else {
                    Log.i("GEOLOCATION", "Geolocation is not available for this image: " + file.getAbsolutePath());
                }
            }
        } catch (IOException | ImageProcessingException e){
            e.printStackTrace();
        }
    }

}
