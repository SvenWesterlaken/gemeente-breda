package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.svenwesterlaken.gemeentebreda.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static android.app.Activity.RESULT_OK;

public class NewReportMediaFragment extends Fragment {
    private Button mediaBTN, selectBTN;
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

        mediaBTN = (Button) rootView.findViewById(R.id.media_btn_make);
        mediaBTN.setOnClickListener(new MediaClickListener());

        selectBTN = (Button) rootView.findViewById(R.id.media_btn_select);
        selectBTN.setOnClickListener(new SelectMediaClickListener());

        return rootView;
    }

    private class MediaClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            dispatchTakeVideoIntent();
        }
    }

    private class SelectMediaClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //TODO: Video wordt niet weergegeven.
            intent.setType("image/* video/*");
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
        }

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOAD_IMG && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                image.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }

        }else {

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

}
