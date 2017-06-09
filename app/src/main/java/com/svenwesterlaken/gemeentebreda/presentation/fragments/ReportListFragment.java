package com.svenwesterlaken.gemeentebreda.presentation.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.api.CategoryRequest;
import com.svenwesterlaken.gemeentebreda.data.api.ReportRequest;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.logic.adapters.ReportAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;


public class ReportListFragment extends Fragment implements PullRefreshLayout.OnRefreshListener, ReportRequest.ReportListener{

    ReportAdapter reportAdapter;
    DatabaseHandler handler;
    PullRefreshLayout prf;
    ArrayList<Report> reportlist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getReports();
     
        //Select view & cardView to inflate
        View rootView = inflater.inflate(R.layout.fragment_reportlist, container, false);
        RecyclerView reportList = (RecyclerView) rootView.findViewById(R.id.report_RV_reports);
        reportList.setHasFixedSize(true);
        reportList.setItemAnimator(new FadeInUpAnimator());
        reportList.getItemAnimator().setAddDuration(300);
        reportList.getItemAnimator().setRemoveDuration(0);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reportList.setLayoutManager(layoutManager);
        
        
        reportAdapter = new ReportAdapter(reportlist, getContext());
        reportList.setAdapter(reportAdapter);
        
        reportAdapter.notifyDataSetChanged();

        prf = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        prf.setColorSchemeColors(Color.argb(255, 217, 29, 73));
        prf.setOnRefreshListener(this);

        return rootView;
    }
    
    
    @Override
    public void onReportsAvailable(ArrayList<Report> reports) {
        Log.i("Arraylist", "We hebben " + reports.size() + " items in de lijst");
        
        reportlist.clear();
        for(int i = 0; i < reports.size(); i++) {
            reportlist.add(reports.get(i));
        }
        
        reportAdapter.notifyDataSetChanged();
        
    }
    

    @Override
    public void onRefresh() {

        getReports();
        reportAdapter.notifyDataSetChanged();
        prf.setRefreshing(false);

    }


//    public void updateList(ArrayList<Report> list){
//        reportAdapter.notifyItemRangeInserted(0, list.size());
//
//
//    }
    
    
    private void getReports(){
        
        ReportRequest request = new ReportRequest(this.getContext(), this);
        request.handleGetAllReports();
    }
}
