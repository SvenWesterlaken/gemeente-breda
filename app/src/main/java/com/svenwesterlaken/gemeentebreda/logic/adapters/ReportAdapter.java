package com.svenwesterlaken.gemeentebreda.logic.adapters;


import android.content.Context;
import android.content.Intent;
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
import java.util.Locale;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder>  {

    private List<Report> reports;
    private Context context;


    // Constructor
    public ReportAdapter(List<Report> reports, Context c) {
        this.reports = reports;
        this.context = c;
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_report, parent, false);

        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReportViewHolder viewholder, final int i) {
        Report report = reports.get(i);

        String description = report.getDescription();

        if(description == null || description.equals("null") || description.isEmpty() ) {
            description = context.getString(R.string.summary_missing_description);
        }

        viewholder.title.setText(report.getCategory().getCategoryName());
        viewholder.address.setText(report.getLocation().getStreet() );
        viewholder.description.setText(description);
        viewholder.upvotes.setText(String.format(Locale.GERMAN, "%d", report.getUpvotes()));


        viewholder.icon.setImageResource( R.drawable.lightbulb);
        if (report.getStatus().equals("open")) {
            viewholder.status.setImageResource(R.drawable.eye_off);
        } else {
            viewholder.status.setImageResource(R.drawable.check);
        }


        viewholder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailedReportActivity.class);
                intent.putExtra("REPORT", reports.get(i));
                v.getContext().startActivity(intent);
            }
        });

    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder  {
        private TextView title;
        private TextView address;
        private TextView description;
        private TextView upvotes;
        private ImageView icon, status;
        private View layout;


        public ReportViewHolder(View v) {
            super(v);


            title =  (TextView) v.findViewById(R.id.summary_TV_reportTitle);
            address = (TextView)  v.findViewById(R.id.summary_TV_address);
            description = (TextView)  v.findViewById(R.id.summary_TV_description);
            upvotes = (TextView) v.findViewById(R.id.report_TV_upvotes);

            icon = (ImageView) v.findViewById(R.id.summary_IV_icon);
            status = (ImageView) v.findViewById(R.id.report_IV_status);

            layout =  v.findViewById(R.id.layout);


        }

    }

}
