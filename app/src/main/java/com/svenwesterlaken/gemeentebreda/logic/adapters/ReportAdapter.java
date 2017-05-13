package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Report;

import java.util.List;

/**
 * Created by lukab on 7-5-2017.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Report> reports;
    private Context context;

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

        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportViewHolder contactViewHolder, int i) {
        Report r = reports.get(i);
        contactViewHolder.title.setText(r.getCategory().getCategoryName());
        contactViewHolder.address.setText(r.getLocation().getStreet() + " " + r.getLocation().getHouseNumber());
        contactViewHolder.description.setText(r.getDescription());


        contactViewHolder.icon.setImageResource( R.drawable.lightbulb);
        contactViewHolder.status.setImageResource( R.drawable.eye_off);
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        private TextView title, address, description, upvotes;
        private ImageView icon, status;

        public ReportViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.report_TV_title);
            address = (TextView)  v.findViewById(R.id.report_TV_address);
            description = (TextView)  v.findViewById(R.id.report_TV_description);
            upvotes = (TextView) v.findViewById(R.id.report_TV_upvotes);

            icon = (ImageView) v.findViewById(R.id.report_IV_icon);
            status = (ImageView) v.findViewById(R.id.report_IV_status);
        }
    }

}
