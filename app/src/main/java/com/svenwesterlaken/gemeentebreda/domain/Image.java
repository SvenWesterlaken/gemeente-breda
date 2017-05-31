package com.svenwesterlaken.gemeentebreda.domain;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Sven Westerlaken on 31-5-2017.
 */

public class Image implements Serializable{
    private Bitmap bitmap;

    public Image(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
