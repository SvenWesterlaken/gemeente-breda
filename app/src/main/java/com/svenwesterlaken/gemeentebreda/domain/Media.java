package com.svenwesterlaken.gemeentebreda.domain;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

/**
 * Created by lukab on 9-5-2017.
 */

public class Media implements Serializable {

    private String filePath;
    private Bitmap image;
    private Uri uri;
    private int type;

    private static int IMAGE = 1;
    private static int VIDEO = 2;

    public Media(String filePath, Bitmap image) {
        this.filePath = filePath;
        this.image = image;
        this.type = 1;
    }

    public Media(String filePath, Uri uri) {
        this.filePath = filePath;
        this.uri = uri;
        this.type = 2;
    }

    public Media() {}

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
