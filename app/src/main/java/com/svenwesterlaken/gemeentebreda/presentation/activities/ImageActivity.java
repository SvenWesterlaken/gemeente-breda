package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.svenwesterlaken.gemeentebreda.R;

/**
 * Created by Sven Westerlaken on 31-5-2017.
 */

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Bundle extras = getIntent().getExtras();
        ImageView image = (ImageView) findViewById(R.id.image_IV_content);
        Picasso.with(this).load(extras.getString("filepath")).into(image);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
