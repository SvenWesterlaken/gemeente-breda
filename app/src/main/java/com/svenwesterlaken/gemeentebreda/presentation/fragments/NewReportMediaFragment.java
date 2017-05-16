package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.svenwesterlaken.gemeentebreda.R;

import static android.app.Activity.RESULT_OK;

public class NewReportMediaFragment extends Fragment {
    private Button mediaBTN, selectBTN;
    private ImageView image;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_report_media, container, false);

        image = (ImageView) rootView.findViewById(R.id.media_IV_image);

        mediaBTN = (Button) rootView.findViewById(R.id.media_btn_make);
        mediaBTN.setOnClickListener(new MediaClickListener());

        selectBTN = (Button) rootView.findViewById(R.id.media_btn_select);
        selectBTN.setOnClickListener(new SelectMediaClickListener());

        return rootView;
    }

    private class MediaClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            dispatchTakePictureIntent();
        }
    }

    private class SelectMediaClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
