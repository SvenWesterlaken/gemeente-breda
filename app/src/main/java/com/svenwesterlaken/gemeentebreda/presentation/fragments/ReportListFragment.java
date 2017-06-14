package com.svenwesterlaken.gemeentebreda.presentation.fragments;


import android.content.Intent;
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
import com.svenwesterlaken.gemeentebreda.data.api.ReportRequest;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.logic.adapters.ReportAdapter;
import com.svenwesterlaken.gemeentebreda.presentation.activities.ReportActivity;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

import static android.app.Activity.RESULT_OK;


public class ReportListFragment extends Fragment implements PullRefreshLayout.OnRefreshListener, ReportRequest.ReportListener {
    
    ReportAdapter reportAdapter;
    PullRefreshLayout prf;
    ArrayList<Report> reports;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Select view & cardView to inflate
        View rootView = inflater.inflate(R.layout.fragment_reportlist, container, false);
        RecyclerView reportList = (RecyclerView) rootView.findViewById(R.id.report_RV_reports);
        reportList.setItemAnimator(new FadeInUpAnimator());
        reportList.getItemAnimator().setAddDuration(300);
        reportList.getItemAnimator().setRemoveDuration(200);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reportList.setLayoutManager(layoutManager);
        
        ReportActivity activity = (ReportActivity) getActivity();
        reports = new ArrayList<>();
       
        if (activity.getList() != null) {
            reports = activity.getList();
        }
        
        else {
            getReports();
        }
        
        reportAdapter = new ReportAdapter(reports, getContext());
        reportList.setAdapter(reportAdapter);
        
        reportAdapter.notifyDataSetChanged();
        
        prf = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        prf.setColorSchemeColors(Color.argb(255, 217, 29, 73));
        prf.setOnRefreshListener(this);
        
        return rootView;
    }
    
    
    @Override
    public void onReportsAvailable(Report report) {
        reports.add(report);
        reportAdapter.notifyItemInserted(reports.size());
    }
    
    @Override
    public void onFinished() {
        prf.setRefreshing(false);
    }
    
    
    @Override
    public void onRefresh() {
        getReports();
    }
    
    
    public void getReports() {
        if (!reports.isEmpty()) {
            int size = reports.size();
            reports.clear();
            reportAdapter.notifyItemRangeRemoved(0, size);
        }
        ReportRequest request = new ReportRequest(getContext(), this);
        request.handleGetAllReports();
    }
    
    }

