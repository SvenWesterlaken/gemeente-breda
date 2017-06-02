package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.User;
import com.svenwesterlaken.gemeentebreda.logic.adapters.ReportAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

/**
 * Created by lukab on 29-5-2017.
 */

public class MyReportsOneFragment extends Fragment implements PullRefreshLayout.OnRefreshListener{

    DatabaseHandler handler;
    ArrayList reports;
    ReportAdapter reportAdapter;
    PullRefreshLayout prf;

    @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    //Select view & cardView to inflate
    View rootView = inflater.inflate(R.layout.fragment_my_report_one, container, false);
    RecyclerView reportList = (RecyclerView) rootView.findViewById(R.id.myreport_RV_one);
    reportList.setHasFixedSize(true);
    reportList.setItemAnimator(new FadeInUpAnimator());
    reportList.getItemAnimator().setAddDuration(300);
    reportList.getItemAnimator().setRemoveDuration(0);

    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    reportList.setLayoutManager(layoutManager);

    handler = new DatabaseHandler(this.getContext());
  //  reports = handler.getAllReports(); --> get all added reports

        User user = handler.getUser(1);

        reports = handler.getReportUser(user);

    reportAdapter = new ReportAdapter(reports);
    reportList.setAdapter(reportAdapter);

    handler.close();
    reportAdapter.notifyDataSetChanged();

    prf = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
    prf.setColorSchemeColors(Color.argb(255, 217, 29, 73));
    prf.setOnRefreshListener(this);

    return rootView;
}

    @Override
    public void onRefresh() {
        reportAdapter.notifyItemRangeRemoved(0, reports.size() );
        updateList(handler.getAllReports());
    }


    public void updateList(ArrayList<Report> list){
        reportAdapter.notifyItemRangeInserted(0, list.size());
        prf.setRefreshing(false);

    }

}