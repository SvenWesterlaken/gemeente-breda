package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.svenwesterlaken.gemeentebreda.R;

public class NewReportMediaFragment extends Fragment {
    private Button mediaBTN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_report_media, container, false);


        mediaBTN = (Button) rootView.findViewById(R.id.media_btn_make);
        mediaBTN.setOnClickListener(new MediaClickListener());

        return rootView;
    }

    private class MediaClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }
}
