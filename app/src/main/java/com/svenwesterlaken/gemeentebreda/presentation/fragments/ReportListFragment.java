package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.logic.adapters.ReportAdapter;

import java.util.ArrayList;


public class ReportListFragment extends Fragment {

    ReportAdapter mReportAdapter;
    ArrayList<Report> reports = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reportlist, container, false);

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
