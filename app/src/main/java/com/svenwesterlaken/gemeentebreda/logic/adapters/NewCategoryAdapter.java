package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewReportActivity;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.NewReportCategoryFragment;

import java.util.List;

/**
 * Created by lukab on 21-5-2017.
 */

public class NewCategoryAdapter extends RecyclerView.Adapter<NewCategoryAdapter.CategoryViewHolder> implements View.OnClickListener {
    private List<Category> categories;
    private NewReportActivity activity;
    private RecyclerView rv;
    private NewReportCategoryFragment.CategoryChangedListener mListener;


    public NewCategoryAdapter(List<Category> categories, Activity activity, RecyclerView rv, NewReportCategoryFragment.CategoryChangedListener mListener){
        this.categories = categories;
        this.activity = (NewReportActivity) activity;
        this.rv = rv;
        this.mListener = mListener;
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public NewCategoryAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new_category, parent, false);

        return new NewCategoryAdapter.CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder viewHolder, final int i) {
        Category category = categories.get(i);

        viewHolder.name.setText(category.getCategoryName());
        viewHolder.summary.setText(category.getCategoryName());
        viewHolder.icon.setImageResource(R.drawable.lightbulb);

        viewHolder.layout.setOnClickListener(this);



    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView summary;
        private ImageView icon;
        private View layout;

        public CategoryViewHolder(View v) {
            super(v);

            layout = v.findViewById(R.id.layout);
            name = (TextView) v.findViewById(R.id.newCategory_TV_name);
            summary = (TextView) v.findViewById(R.id.newCategory_TV_summary);
            icon = (ImageView) v.findViewById(R.id.newCategory_IV_icon);
        }

    }

    @Override
    public void onClick(View v) {
        Category categorySelected = categories.get(rv.getChildLayoutPosition(v));
        mListener.setCategory(categorySelected);
        Toast.makeText(activity.getApplicationContext(), "Categorie is toegevoegd", Toast.LENGTH_SHORT).show();
        activity.scrollToNext();
    }

}
