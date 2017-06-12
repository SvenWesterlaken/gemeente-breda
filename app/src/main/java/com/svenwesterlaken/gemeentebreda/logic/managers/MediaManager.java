package com.svenwesterlaken.gemeentebreda.logic.managers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.afollestad.materialcamera.MaterialCamera;
import com.svenwesterlaken.gemeentebreda.R;

import java.io.File;

/**
 * Created by Sven Westerlaken on 1-6-2017.
 */

public class MediaManager {
    private Fragment fragment;
    private Activity activity;

    public MediaManager(Fragment f, Activity activity) {
        this.activity = activity;
        this.fragment = f;
    }

    public File getSaveDir() {
        File saveDir = null;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            String dir = activity.getString(R.string.app_name).replaceAll("\\s","");
            saveDir = new File(Environment.getExternalStorageDirectory(), dir);
        }

        return saveDir;
    }

    public MaterialCamera createCamera() {

        return new MaterialCamera(fragment)
                .saveDir(getSaveDir())
                .showPortraitWarning(true)
                .allowRetry(true)
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

    public boolean isVideo(Intent data) {
        boolean result = false;
        String mime = fragment.getContext().getContentResolver().getType(data.getData());
        if (mime != null && mime.contains("video")) {
            result = true;
        }

        return result;
    }

    public boolean isImage(Intent data) {
        boolean result = false;
        String mime = fragment.getContext().getContentResolver().getType(data.getData());
        if (mime != null && mime.contains("image")) {
            result = true;
        }
        return result;
    }

    public Uri processMedia(Intent data) {
        return data.getData();
    }
}
