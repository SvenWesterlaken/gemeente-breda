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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewReportActivity;
import com.svenwesterlaken.gemeentebreda.presentation.activities.VideoActivity;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class NewReportMediaFragment extends Fragment implements View.OnClickListener {
    private MediaChangedListener mListener;
    private MediaManager mManager;

    private Media media;

    private ImageView image;
    private FloatingActionButton confirmFAB;
    private ConstraintLayout photoBTN, videoBTN, selectBTN, removeBTN;

    private Animation popupAnimation, popoutAnimation;


    private final static int CAMERA_REQUEST = 1;
    private final static int VIDEOCAMERA_REQUEST = 2;
    private final static int MEDIA_REQUEST = 3;

    private final static int MEDIA_PICTURE = 1;
    private final static int MEDIA_VIDEO = 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mManager = new MediaManager(this, getActivity());

        popupAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.popup_animation);
        popoutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.popout_animation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_report_media, container, false);

        photoBTN = (ConstraintLayout) rootView.findViewById(R.id.media_BTN_photo);
        photoBTN.setOnClickListener(this);

        videoBTN = (ConstraintLayout) rootView.findViewById(R.id.media_BTN_video);
        videoBTN.setOnClickListener(this);

        selectBTN = (ConstraintLayout) rootView.findViewById(R.id.media_BTN_mediaSelect);
        selectBTN.setOnClickListener(this);

        removeBTN = (ConstraintLayout) rootView.findViewById(R.id.media_BTN_delete);
        removeBTN.setOnClickListener(this);

        confirmFAB = (FloatingActionButton) rootView.findViewById(R.id.media_FAB_confirm);
        confirmFAB.setOnClickListener(this);

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
        } else if (v.getId() == R.id.media_BTN_delete) {

        } else if (v.getId() == R.id.media_IV_thumbnail) {
            if(media != null) {
                if(media.getType() == MEDIA_PICTURE) {
                    Intent intent = new Intent(getActivity(), ImageActivity.class);
                    intent.putExtra("filepath", media.getFilePath());
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), image, "media_preview");
                    startActivity(intent, options.toBundle());
                } else if (media.getType() == MEDIA_VIDEO) {
                    Intent intent = new Intent(getActivity(), VideoActivity.class);
                    intent.putExtra("uri", media.getUri());
                    startActivity(intent);
                }
            } else {
                Toast.makeText(getContext(), "Nog geen media toegevoegd", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.media_FAB_confirm) {
            mListener.setMedia(media);
            confirmFAB.setAnimation(popoutAnimation);
            confirmFAB.setVisibility(View.INVISIBLE);
            ((NewReportActivity) getActivity()).scrollToNext();


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(media != null) {
            if(media.getType() == MEDIA_PICTURE) {
                Glide.with(getContext()).load(media.getFilePath()).into(image);
            } else if (media.getType() == MEDIA_VIDEO) {
                Glide.with(getContext()).load(media.getUri()).into(image);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        media = new Media();
        Uri uri = null;
        int type = 0;
        String path = null;

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            path = data.getDataString();
            type = MEDIA_PICTURE;
            uri = Uri.fromFile( new File( path ));

        } else if (requestCode == VIDEOCAMERA_REQUEST && resultCode == RESULT_OK) {
            path = data.getDataString().substring(7);
            uri = Uri.fromFile( new File( path ));
            type = MEDIA_VIDEO;

        } else if (requestCode == MEDIA_REQUEST && resultCode == RESULT_OK) {

            if(mManager.isVideo(data)) {
                uri = mManager.processMedia(data);
                path = data.getDataString().substring(7);
                type = MEDIA_VIDEO;

            } else if (mManager.isImage(data)) {
                uri = mManager.processMedia(data);
                path = data.getDataString();
                type = MEDIA_PICTURE;
            }
        }

        if (type == MEDIA_PICTURE) {
            Glide.with(getContext()).load(path).into(image);
        } else if (type == MEDIA_VIDEO) {
            Glide.with(getContext()).load(uri).into(image);
        }

        media.setFilePath(path);
        media.setUri(uri);
        media.setType(type);
        enableConfirmButton();

    }

    private void enableConfirmButton() {
        confirmFAB.setVisibility(View.VISIBLE);
        confirmFAB.startAnimation(popupAnimation);
    }


    @Override
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
