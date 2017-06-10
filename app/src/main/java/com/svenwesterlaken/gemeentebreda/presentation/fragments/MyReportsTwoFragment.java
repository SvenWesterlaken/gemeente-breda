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

import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

/**
 * Created by lukab on 29-5-2017.
 */

public class MyReportsTwoFragment extends Fragment implements PullRefreshLayout.OnRefreshListener {

    private PullRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Select view & cardView to inflate
        View rootView = inflater.inflate(R.layout.fragment_my_report_two, container, false);
        RecyclerView reportList = (RecyclerView) rootView.findViewById(R.id.myreport_RV_two);
        reportList.setItemAnimator(new FadeInUpAnimator());
        reportList.getItemAnimator().setAddDuration(300);
        reportList.getItemAnimator().setRemoveDuration(0);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reportList.setLayoutManager(layoutManager);

        DatabaseHandler handler = new DatabaseHandler(this.getContext());
        User user = handler.getUser(1);
        List<Report> reports = handler.getFavourites(user);

        ReportAdapter reportAdapter = new ReportAdapter(reports, getContext());
        reportList.setAdapter(reportAdapter);

        handler.close();
        reportAdapter.notifyDataSetChanged();

        refreshLayout = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setColorSchemeColors(Color.argb(255, 217, 29, 73));
        refreshLayout.setOnRefreshListener(this);

        return rootView;
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
    }

}

