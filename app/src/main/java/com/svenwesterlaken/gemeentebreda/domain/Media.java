package com.svenwesterlaken.gemeentebreda.domain;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by lukab on 9-5-2017.
 */

public class Media implements Parcelable {

    private int id;
    private String filePath;
    private int type;
    private Uri uri;

    private static int IMAGE = 1;
    private static int VIDEO = 2;

    public Media(String filePath, Uri uri, int type) {
        this.filePath = filePath;
        this.uri = uri;
        this.type = type;
    }

    public Media() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.filePath);
        dest.writeInt(this.type);
        dest.writeParcelable(this.uri, flags);
    }

    protected Media(Parcel in) {
        this.id = in.readInt();
        this.filePath = in.readString();
        this.type = in.readInt();
        this.uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Parcelable.Creator<Media> CREATOR = new Parcelable.Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
}
