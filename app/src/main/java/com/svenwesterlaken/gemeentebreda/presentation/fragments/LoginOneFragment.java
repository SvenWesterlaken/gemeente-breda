package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svenwesterlaken.gemeentebreda.R;

/**
 * Created by Sven Westerlaken on 12-5-2017.
 */

public class LoginOneFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        return rootView;
    }
}
