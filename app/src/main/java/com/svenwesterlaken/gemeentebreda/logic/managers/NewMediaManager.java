package com.svenwesterlaken.gemeentebreda.logic.managers;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;

import com.svenwesterlaken.gemeentebreda.domain.Media;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.NewReportMediaFragment.MediaChangedListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Sven Westerlaken on 29-5-2017.
 */

public class NewMediaManager {
    private Fragment fragment;
    private MediaChangedListener mListener;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_LOAD_MEDIA = 2;
    static final int REQUEST_VIDEO_CAPTURE = 3;

    static final int MEDIA_REQUEST_ERROR = 0;
    static final int IMAGE_REQUEST_SUCCES = 1;
    static final int VIDEO_REQUEST_SUCCES = 2;
    static final int MEDIA_REQUEST_SUCCES = 3;

    public NewMediaManager(Fragment f, MediaChangedListener mListener) {
        this.fragment = f;
        this.mListener = mListener;
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(fragment.getContext().getPackageManager()) != null) {
            fragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(fragment.getContext().getPackageManager()) != null) {
            fragment.startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    public void dispatchChooseMediaIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("*/*");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String[] mimetypes = {"image/*", "video/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        }

        fragment.startActivityForResult(intent, REQUEST_LOAD_MEDIA);
    }

    public int getRequestStatus(int requestCode, int resultCode) {
        int status = MEDIA_REQUEST_ERROR;

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            status = IMAGE_REQUEST_SUCCES;
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            status = VIDEO_REQUEST_SUCCES;
        } else if (requestCode == REQUEST_LOAD_MEDIA && resultCode == RESULT_OK) {
            status = MEDIA_REQUEST_SUCCES;
        }

        return status;
    }

    public Bitmap processImage(Intent data) {
        Bundle extras = data.getExtras();
        Uri imageUri = data.getData();
        addMedia(imageUri);
        Bitmap bitmap = (Bitmap) extras.get("data");
        MediaStore.Images.Media.insertImage(fragment.getContext().getContentResolver(), bitmap, "testTitle", "testDescription");
        return bitmap;
    }

    public Uri processVideo(Intent data) {
        Uri videoUri = data.getData();
        addMedia(videoUri);
        File file = new File(videoUri.getPath());
        String filePath = file.getAbsolutePath();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.DATA, filePath);
        fragment.getContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

        return videoUri;
    }

    public boolean isVideo(Intent data) {
        boolean result = false;
        String mime = fragment.getContext().getContentResolver().getType(data.getData());

        if (mime != null) {
            if (mime.contains("video")) {
                result = true;
            }
        }

        return result;
    }

    public boolean isImage(Intent data) {
        boolean result = false;
        String mime = fragment.getContext().getContentResolver().getType(data.getData());

        if (mime != null) {
            if (mime.contains("image")) {
                result = true;
            }
        }

        return result;
    }

    public Uri processMediaVideo(Intent data) {
        Uri videoUri = data.getData();
        addMedia(videoUri);

        return videoUri;
    }

    public Bitmap processMediaImage(Intent data) {
        Uri imageUri = data.getData();
        addMedia(imageUri);

        try {
            final InputStream imageStream = fragment.getContext().getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            return selectedImage;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addMedia(Uri uri){
        //TODO: Correct media ID meegeven.
        Media media = new Media(0);
        media.setUri(uri);
        mListener.setMedia(media);
    }


}
