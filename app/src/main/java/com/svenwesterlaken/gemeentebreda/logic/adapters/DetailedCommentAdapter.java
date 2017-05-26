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

/**
 * Created by lukab on 17-5-2017.
 */

public class DetailedCommentAdapter extends RecyclerView.Adapter<DetailedCommentAdapter.DetailedCommentViewHolder>  {

    private Context context;
    private Intent intent;
    private String title;

    // Constructor
    public DetailedCommentAdapter(Context context) {
        this.context = context;
    }

    public int getItemCount() {
        return 1;
    }

    @Override
    public DetailedCommentAdapter.DetailedCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detailed_comments, parent, false);

        return new DetailedCommentAdapter.DetailedCommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailedCommentAdapter.DetailedCommentViewHolder contactViewHolder, int i) {


//        contactViewHolder.category.setText(report.getCategory().getCategoryName());
//        contactViewHolder.address.setText(report.getLocation().getStreet() + " " + report.getLocation().getHouseNumber());
//        contactViewHolder.description.setText(report.getDescription());





    }

    public static class DetailedCommentViewHolder extends RecyclerView.ViewHolder {
        private TextView comment, user, date;
        private ImageView icon, status;

        public DetailedCommentViewHolder(View v) {
            super(v);
            comment = (TextView) v.findViewById(R.id.detailed_TV_comment);
            user = (TextView) v.findViewById(R.id.detailed_TV_username);
            date = (TextView) v.findViewById(R.id.detailed_TV_date);
        }


    }
}
