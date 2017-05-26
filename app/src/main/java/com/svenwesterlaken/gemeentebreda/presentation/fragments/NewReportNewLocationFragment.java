package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.svenwesterlaken.gemeentebreda.R;

/**
 * Created by Adonis on 26-5-2017.
 */

public class NewReportNewLocationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_report_location_locationscreen, container, false);

        final FloatingSearchView mSearchView = (FloatingSearchView) rootView.findViewById(R.id.searchBar);

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
            }
        });
        return rootView;
    }

}