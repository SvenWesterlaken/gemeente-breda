package com.svenwesterlaken.gemeentebreda.logic.partials;

import android.os.Parcel;
import android.text.TextUtils;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by Sven Westerlaken on 3-6-2017.
 */

public class PlaceSearchSuggestion implements SearchSuggestion {
    private String placeID;
    private CharSequence primaryText;
    private CharSequence secondaryText;
    private CharSequence fullText;

    public PlaceSearchSuggestion() {}

    public PlaceSearchSuggestion(String placeID, CharSequence primaryText, CharSequence secondaryText, CharSequence fullText) {
        this.placeID = placeID;
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
        this.fullText = fullText;
    }

    @Override
    public String getBody() {
        return fullText.toString();
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public CharSequence getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(CharSequence primaryText) {
        this.primaryText = primaryText;
    }

    public CharSequence getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(CharSequence secondaryText) {
        this.secondaryText = secondaryText;
    }

    public CharSequence getFullText() {
        return fullText;
    }

    public void setFullText(CharSequence fullText) {
        this.fullText = fullText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.placeID);
        TextUtils.writeToParcel(this.primaryText, dest, flags);
        TextUtils.writeToParcel(this.secondaryText, dest, flags);
        TextUtils.writeToParcel(this.fullText, dest, flags);
    }

    protected PlaceSearchSuggestion(Parcel in) {
        this.placeID = in.readString();
        this.primaryText = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.secondaryText = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.fullText = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
    }

    public static final Creator<PlaceSearchSuggestion> CREATOR = new Creator<PlaceSearchSuggestion>() {
        @Override
        public PlaceSearchSuggestion createFromParcel(Parcel source) {
            return new PlaceSearchSuggestion(source);
        }

        @Override
        public PlaceSearchSuggestion[] newArray(int size) {
            return new PlaceSearchSuggestion[size];
        }
    };
}
