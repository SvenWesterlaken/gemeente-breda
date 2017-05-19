package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.presentation.activities.DetailedReportActivity;

import java.util.List;

/**
 * Created by lukab on 16-5-2017.
 */

public class DetailedReportAdapter extends RecyclerView.Adapter<DetailedReportAdapter.DetailedReportViewHolder>  {

        private Context context;
        private Intent intent;
    private  Report report;
    private String title;

        // Constructor
    public DetailedReportAdapter(Context context, Report report) {
            this.context = context;
        this.report = report;
        }

    public int getItemCount() {
        return 1;
    }

    @Override
    public DetailedReportAdapter.DetailedReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detailed_report, parent, false);

        return new DetailedReportAdapter.DetailedReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailedReportAdapter.DetailedReportViewHolder contactViewHolder, int i) {


        contactViewHolder.category.setText(report.getCategory().getCategoryName());
        contactViewHolder.address.setText(report.getLocation().getStreet() + " " + report.getLocation().getHouseNumber());
        contactViewHolder.description.setText(report.getDescription());
        contactViewHolder.icon.setImageResource( R.drawable.lightbulb);
        contactViewHolder.status.setImageResource( R.drawable.eye_off);
        contactViewHolder.upvotes.setImageResource(R.drawable.star);





    }

    public static class DetailedReportViewHolder extends RecyclerView.ViewHolder {
        private TextView category, address, description;
        private ImageView icon, status, upvotes;

        public DetailedReportViewHolder(View v) {
            super(v);
            category =  (TextView) v.findViewById(R.id.report_TV_title);
            address = (TextView) v.findViewById(R.id.report_TV_address);
            description = (TextView) v.findViewById(R.id.report_TV_description);
            icon = (ImageView) v.findViewById(R.id.report_IV_icon);
            status = (ImageView) v.findViewById(R.id.report_IV_status);
            upvotes = (ImageView) v.findViewById(R.id.report_IV_upvotes);


        }


    }



}
