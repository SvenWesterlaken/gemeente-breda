package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.svenwesterlaken.gemeentebreda.R;

/**
 * Created by lukab on 17-5-2017.
 */

public class DetailedCommentAdapter extends RecyclerView.Adapter<DetailedCommentAdapter.DetailedCommentViewHolder>  {

    public DetailedCommentAdapter() {}

    public int getItemCount() {
        return 1;
    }

    @Override
    public DetailedCommentAdapter.DetailedCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detailed_comments, parent, false);
        return new DetailedCommentAdapter.DetailedCommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailedCommentAdapter.DetailedCommentViewHolder contactViewHolder, int i) {}

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
