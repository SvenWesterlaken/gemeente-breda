package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Media;
import com.svenwesterlaken.gemeentebreda.logic.services.FetchAddressIntentService;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewReportActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static android.app.Activity.RESULT_OK;

public class NewReportMediaFragment extends Fragment {
    private MediaChangedListener mListener;

    private Button photoBTN, selectBTN, videoBTN;
    private VideoView video;
    private ImageView image;
    private Bitmap bitmap;
    private Uri videoUri;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_LOAD_MEDIA = 2;
    static final int REQUEST_VIDEO_CAPTURE = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_report_media, container, false);

        ConstraintLayout photoBTN = (ConstraintLayout) rootView.findViewById(R.id.media_BTN_photo);
        photoBTN.setOnClickListener(new PhotoClickListener());

        ConstraintLayout videoBTN = (ConstraintLayout) rootView.findViewById(R.id.media_BTN_video);
        videoBTN.setOnClickListener(new VideoClickListener());

        ConstraintLayout selectBTN = (ConstraintLayout) rootView.findViewById(R.id.media_BTN_mediaSelect);
        selectBTN.setOnClickListener(new SelectMediaClickListener());

        MediaController mediaController = new MediaController(getContext());
        video = (VideoView) rootView.findViewById(R.id.media_VV_video);
        video.setMediaController(mediaController);

        image = (ImageView) rootView.findViewById(R.id.media_IV_image);

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

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String[] mimetypes = {"image/*", "video/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            }

            startActivityForResult(intent, REQUEST_LOAD_MEDIA);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        image.setImageBitmap(bitmap);
        video.setVideoURI(videoUri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Uri imageUri = data.getData();
            addMedia(imageUri);
            bitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(bitmap);
            MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "testTitle", "testDescription");
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = data.getData();
            video.setVideoURI(videoUri);

            addMedia(videoUri);

            File file = new File(videoUri.getPath());
            String filePath = file.getAbsolutePath();
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.DATA, filePath);
            getContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        }

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOAD_MEDIA && resultCode == RESULT_OK) {

            String mime = getContext().getContentResolver().getType(data.getData());
            Log.d("DEBUG", mime);

            if (mime != null) {

                if (mime.contains("video")) {
                    final Uri videoUri = data.getData();
                    video.setVideoURI(videoUri);

                    addMedia(videoUri);

                    Log.d("DEBUG", "video loaded");
                } else if (mime.contains("image")) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        image.setImageBitmap(selectedImage);
                        Log.d("DEBUG", "image loaded");

                        addMedia(imageUri);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    }
                }

            }

        }
    }

    public void addMedia(Uri uri){
        //TODO: Correct media ID meegeven.
        Media media = new Media(0);
        media.setUri(uri);
        mListener.setMedia(media);
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

    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a = null;

        if (context instanceof Activity) {
            a = (Activity) context;
        }
        try {
            mListener = (MediaChangedListener) a;
        } catch (ClassCastException e) {
            throw new ClassCastException(a.toString() + " must implement MediaChangedListener");
        }
    }

    public interface MediaChangedListener {
        void setMedia(Media m);
    }

}
