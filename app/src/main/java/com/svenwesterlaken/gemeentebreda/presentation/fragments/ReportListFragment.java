package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.logic.adapters.ReportAdapter;
import com.svenwesterlaken.gemeentebreda.presentation.activities.DetailedReportActivity;

import java.util.ArrayList;


public class ReportListFragment extends Fragment {

    ReportAdapter reportAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Select view & cardView to inflate
        View rootView = inflater.inflate(R.layout.fragment_reportlist, container, false);
        RecyclerView reportList = (RecyclerView) rootView.findViewById(R.id.report_RV_reports);
        reportList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reportList.setLayoutManager(layoutManager);



        DatabaseHandler handler = new DatabaseHandler(this.getContext(),null, null, 1);

        reportAdapter = new ReportAdapter(getContext(), handler.getAllReports());
        reportList.setAdapter(reportAdapter);

        handler.close();
        reportAdapter.notifyDataSetChanged();

        return rootView;
    }

    public void updateList(ArrayList<Report> list){
        reportAdapter.updateReports(list);
        reportAdapter.notifyDataSetChanged();
    }

}
