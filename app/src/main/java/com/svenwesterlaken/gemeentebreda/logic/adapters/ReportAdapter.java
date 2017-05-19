package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.presentation.activities.DetailedReportActivity;

import java.util.List;

import static com.svenwesterlaken.gemeentebreda.R.id.constraintLayout;
import static com.svenwesterlaken.gemeentebreda.R.id.view;

/**
 * Created by lukab on 7-5-2017.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder>  {

    private List<Report> reports;
    private Context context;
    private Intent intent;
    private Report report;

    // Constructor
    public ReportAdapter(Context context, List<Report> reports) {
        this.reports = reports;
        this.context = context;
    }

    public int getItemCount() {
        return reports.size();
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_report, parent, false);



        return new ReportViewHolder(itemView, reports);
    }

    @Override
    public void onBindViewHolder(final ReportViewHolder contactViewHolder, final int i) {
         report = reports.get(i);
        contactViewHolder.title.setText(report.getCategory().getCategoryName());
        contactViewHolder.address.setText(report.getLocation().getStreet() + " " + report.getLocation().getHouseNumber());
        contactViewHolder.description.setText(report.getDescription());


        contactViewHolder.icon.setImageResource( R.drawable.lightbulb);
        contactViewHolder.status.setImageResource( R.drawable.eye_off);


        contactViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, DetailedReportActivity.class);

                intent.putExtra("REPORT", reports.get(i));

                v.getContext().startActivity(intent);
            }
        });





    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder  {
        private TextView title, address, description, upvotes;
        private ImageView icon, status;
        private View layout;


        public ReportViewHolder(View v, final List<Report> reports) {
            super(v);
            title = (TextView) v.findViewById(R.id.report_TV_title);
            address = (TextView) v.findViewById(R.id.report_TV_address);
            description = (TextView) v.findViewById(R.id.report_TV_description);
            upvotes = (TextView) v.findViewById(R.id.report_TV_upvotes);


            icon = (ImageView) v.findViewById(R.id.report_IV_icon);
            status = (ImageView) v.findViewById(R.id.report_IV_status);

            layout =  v.findViewById(R.id.layout);


        }

    }

    public void updateReports(List<Report> list){
        reports = list;
    }

}
