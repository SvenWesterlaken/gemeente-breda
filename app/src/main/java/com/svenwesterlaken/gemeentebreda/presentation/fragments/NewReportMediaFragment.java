package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialcamera.MaterialCamera;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Media;
import com.svenwesterlaken.gemeentebreda.logic.managers.MediaManager;
import com.svenwesterlaken.gemeentebreda.presentation.activities.ImageActivity;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class NewReportMediaFragment extends Fragment implements View.OnClickListener{
    private MediaChangedListener mListener;
    private MediaManager mManager;
    private ImageView image;
    private View rootView;
    private String filePath;

    private final static int CAMERA_REQUEST = 1;
    private final static int VIDEOCAMERA_REQUEST = 2;
    private final static int MEDIA_REQUEST = 3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mManager = new MediaManager(this, mListener, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_report_media, container, false);

        ConstraintLayout photoBTN = (ConstraintLayout) rootView.findViewById(R.id.media_BTN_photo);
        photoBTN.setOnClickListener(this);

        ConstraintLayout videoBTN = (ConstraintLayout) rootView.findViewById(R.id.media_BTN_video);
        videoBTN.setOnClickListener(this);

        ConstraintLayout selectBTN = (ConstraintLayout) rootView.findViewById(R.id.media_BTN_mediaSelect);
        selectBTN.setOnClickListener(this);

        image = (ImageView) rootView.findViewById(R.id.media_IV_thumbnail);
        image.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.media_BTN_photo) {
            mManager.createStillShotCamera().start(CAMERA_REQUEST);
        } else if (v.getId() == R.id.media_BTN_video){
            mManager.createVideoCamera().start(VIDEOCAMERA_REQUEST);
        } else if (v.getId() == R.id.media_BTN_mediaSelect) {
            mManager.dispatchChooseMediaIntent(MEDIA_REQUEST);
        } else if (v.getId() == R.id.media_IV_thumbnail) {
            Intent intent = new Intent(getActivity(), ImageActivity.class);
            intent.putExtra("filepath", filePath);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), image, "media_preview");
            startActivity(intent, options.toBundle());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                filePath = data.getDataString();
                Toast.makeText(getActivity(), "Afbeelding opgeslagen", Toast.LENGTH_SHORT).show();
                Glide.with(getContext()).load(filePath).into(image);
                mManager.addMedia(filePath);

            } else if(data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == VIDEOCAMERA_REQUEST && resultCode == RESULT_OK) {
            String pre = data.getDataString();
            filePath = pre.substring(7);

            Toast.makeText(getActivity(), "Video opgeslagen", Toast.LENGTH_SHORT).show();
            Glide.with(getContext()).load(Uri.fromFile( new File( filePath ))).into(image);

        } else if (requestCode == MEDIA_REQUEST && resultCode == RESULT_OK) {

            if(mManager.isVideo(data)) {

                videoUri = mManager.processMediaVideo(data);

            } else if (mManager.isImage(data)) {

                bitmap = mManager.processMediaImage(data);
                image.setImageBitmap(bitmap);

            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            // Sample was denied WRITE_EXTERNAL_STORAGE permission
            Toast.makeText(getActivity(), "Videos will be saved in a cache directory instead of an external storage directory since permission was denied.", Toast.LENGTH_LONG).show();
        }
    }

}
