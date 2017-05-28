package com.svenwesterlaken.gemeentebreda.domain;

import java.io.Serializable;

/**
 * Created by lukab on 9-5-2017.
 */

public class Media implements Serializable {

    public int getMediaID() {
        return mediaID;
    }

    public void setMediaID(int mediaID) {
        this.mediaID = mediaID;
    }

    int mediaID;

    public Media(int mediaID) {
        this.mediaID = mediaID;
    }
}
