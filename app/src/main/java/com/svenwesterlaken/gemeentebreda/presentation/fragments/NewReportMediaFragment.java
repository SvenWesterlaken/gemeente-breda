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
import com.svenwesterlaken.gemeentebreda.logic.managers.NewMediaManager;
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
    private NewMediaManager mManager;

    static final int IMAGE_REQUEST_SUCCES = 1;
    static final int VIDEO_REQUEST_SUCCES = 2;
    static final int MEDIA_REQUEST_SUCCES = 3;

    private VideoView video;
    private ImageView image;
    private Bitmap bitmap;
    private Uri videoUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mManager = new NewMediaManager(this, mListener);

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
            mManager.dispatchTakePictureIntent();
        }
    }

    private class VideoClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mManager.dispatchTakeVideoIntent();
        }
    }

    private class SelectMediaClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mManager.dispatchChooseMediaIntent();
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
        int status = mManager.getRequestStatus(requestCode, resultCode);

        if (status == IMAGE_REQUEST_SUCCES) {

            bitmap = mManager.processImage(data);
            image.setImageBitmap(bitmap);

        } else if (status == VIDEO_REQUEST_SUCCES) {

            videoUri = mManager.processVideo(data);
            video.setVideoURI(videoUri);

        } else if (status == MEDIA_REQUEST_SUCCES) {

            if(mManager.isVideo(data)) {

                videoUri = mManager.processMediaVideo(data);
                video.setVideoURI(videoUri);

            } else if (mManager.isImage(data)) {

                bitmap = mManager.processMediaImage(data);
                image.setImageBitmap(bitmap);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
