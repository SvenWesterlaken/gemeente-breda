package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.User;
import com.svenwesterlaken.gemeentebreda.logic.adapters.ReportAdapter;

import java.util.ArrayList;


public class ReportTab2 extends Fragment {

    ReportAdapter mReportAdapter;
    ArrayList<Report> reports = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_report_tab2, container, false);

        ListView reportListView = (ListView) rootView.findViewById(R.id.ReportListView);
//        ReportDatabase database = new ReportDatabase(getContext());

        DatabaseHandler handler = new DatabaseHandler(this.getContext(),null, null, 1);


        mReportAdapter = new ReportAdapter(getContext(), handler.getAllReports());
        reportListView.setAdapter(mReportAdapter);

        handler.close();

        this.mReportAdapter.notifyDataSetChanged();

        return rootView;
    }
}
