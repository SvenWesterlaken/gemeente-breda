package com.svenwesterlaken.gemeentebreda.logic.managers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.afollestad.materialcamera.MaterialCamera;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Media;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.NewReportMediaFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Sven Westerlaken on 1-6-2017.
 */

public class MediaManager {
    private Fragment fragment;
    private NewReportMediaFragment.MediaChangedListener mListener;
    private Activity activity;

    static final int REQUEST_LOAD_MEDIA = 2;


    public MediaManager(Fragment f, NewReportMediaFragment.MediaChangedListener mListener, Activity activity) {
        this.activity = activity;
        this.fragment = f;
        this.mListener = mListener;
    }

    public File getSaveDir() {
        File saveDir = null;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            String dir = activity.getString(R.string.app_name).replaceAll("\\s","");
            saveDir = new File(Environment.getExternalStorageDirectory(), dir);
            saveDir.mkdirs();
        }

        return saveDir;
    }

    public File getFile(String path) {
        return new File(path);
    }

    public MaterialCamera createCamera() {

        return new MaterialCamera(fragment)
                .saveDir(getSaveDir())
                .showPortraitWarning(true)
                .allowRetry(true)
                .defaultToFrontFacing(true)
                .allowRetry(true)
                .autoSubmit(false)
                .labelRetry(R.string.camera_retry);
    }

    public MaterialCamera createStillShotCamera() {
        return createCamera().stillShot().labelConfirm(R.string.camera_stillshot_confirm);
    }

    public MaterialCamera createVideoCamera() {
        return createCamera().labelConfirm(R.string.camera_video_confirm);
    }

    public void dispatchChooseMediaIntent(int requestStatus) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("*/*");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String[] mimetypes = {"image/*", "video/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        }

        fragment.startActivityForResult(intent, requestStatus);
    }

    public void addMedia(Uri uri, String path){
        Media media = new Media(path, uri);
        media.setImage(ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
        mListener.setMedia(media);
    }

    public void addMedia(String path) {
        Media media = new Media();
        media.setFilePath(path);
        mListener.setMedia(media);
    }
}
