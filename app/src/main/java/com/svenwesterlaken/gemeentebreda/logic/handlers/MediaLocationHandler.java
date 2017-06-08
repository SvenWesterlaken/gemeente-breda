package com.svenwesterlaken.gemeentebreda.logic.handlers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.google.android.gms.maps.model.LatLng;
import com.svenwesterlaken.gemeentebreda.logic.exceptions.NoLocationMetaException;
import com.svenwesterlaken.gemeentebreda.logic.util.DoubleUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by Sven Westerlaken on 7-6-2017.
 */

public class MediaLocationHandler {
    private Context c;
    
    public MediaLocationHandler(Context c) {
        this.c = c;
    }
    
    public LatLng getLatLong(Uri uri) throws NoLocationMetaException {
        LatLng latlong = null;
        
        String mime = c.getContentResolver().getType(uri);
        
        if (mime != null) {
            if (mime.contains("video")) {
                latlong = getVideoLocationMetadata(uri);
            } else if (mime.contains("image")) {
                latlong = getImageLocationMetadata(uri);
            }
        }
        
        if (latlong == null || DoubleUtil.isZero(latlong.latitude, 0.0001)) {
            throw new NoLocationMetaException();
        } else {
            return latlong;
        }
    }
    
    
    public LatLng getVideoLocationMetadata(Uri videoUri) {
        ContentResolver videoResolver = c.getContentResolver();
        Cursor videoCursor = videoResolver.query(videoUri, null, null, null, null);
        double lat = 0, lon = 0;
        
        if (videoCursor != null && videoCursor.moveToFirst()) {
            //get columns
            int latColumn = videoCursor.getColumnIndex
                    (MediaStore.Video.Media.LATITUDE);
            int lonColumn = videoCursor.getColumnIndex
                    (MediaStore.Video.Media.LONGITUDE);
//            int resColumn = videoCursor.getColumnIndex
//                    (MediaStore.Video.Media.RESOLUTION);
//            int durationColumn = videoCursor.getColumnIndex
//                    (MediaStore.Video.Media.DURATION);
            
            do {
                lat = videoCursor.getDouble(latColumn);
                lon = videoCursor.getDouble(lonColumn);
            }
            while (videoCursor.moveToNext());
        }
        
        if (videoCursor != null) {
            videoCursor.close();
        }
        
        return new LatLng(lat, lon);
        
    }
    
    private LatLng getImageLocationMetadata(Uri imageUri) {
        LatLng latlong = null;
        
        try {
            File file = new File(getRealPathFromURI(imageUri));
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
            for (GpsDirectory gpsDirectory : gpsDirectories) {
                GeoLocation geoLocation = gpsDirectory.getGeoLocation();
                if (geoLocation != null && !geoLocation.isZero()) {
                    latlong = new LatLng(geoLocation.getLatitude(), geoLocation.getLongitude());
                }
            }
        } catch (IOException | ImageProcessingException e) {
            e.printStackTrace();
        }
        
        return latlong;
    }
    
    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = c.getContentResolver().query(contentUri, proj, null, null, null);
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
    
}