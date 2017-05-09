package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.ReportDatabase;
import com.svenwesterlaken.gemeentebreda.logic.adapters.ReportAdapter;

import java.util.ArrayList;


public class ReportTab2 extends Fragment {

    ReportAdapter mReportAdapter;
    ArrayList reportList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_report_tab2, container, false);

        ListView reportListView = (ListView) rootView.findViewById(R.id.ReportListView);
        ReportDatabase database = new ReportDatabase(getContext());
        Cursor cursor = database.getReports();
        mReportAdapter = new ReportAdapter(getContext(), cursor, false);
        reportListView.setAdapter(mReportAdapter);


        this.mReportAdapter.notifyDataSetChanged();

        return rootView;
    }
}
