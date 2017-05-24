package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import com.svenwesterlaken.gemeentebreda.R;


public class  NewReportDescriptionFragment extends Fragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_new_report_description, container, false);

        final EditText descriptionView = (EditText) rootView.findViewById(R.id.description_ET_description);
        Button descriptionBtn = (Button) rootView.findViewById(R.id.description_BTN_submit);

        descriptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionView.getText().toString();
                Log.i("DESCRIPTION", description);
            }
        });



        return rootView;
    }
}
