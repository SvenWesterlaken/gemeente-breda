package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.presentation.activities.NewReportActivity;
import com.svenwesterlaken.gemeentebreda.presentation.fragments.NewReportCategoryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukab on 19-6-2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
	
	private List<Category> categories;
	private RecyclerView rv;
	private Category categorySelected;
	
	
	public CategoryAdapter(List<Category> categories, RecyclerView rv) {
		this.categories = categories;
		this.rv = rv;
		
	}
	
	
	@Override
	public int getItemCount() {
		return categories.size();
	}
	
	@Override
	public CategoryAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_filter_category, parent, false);
		
		return new CategoryAdapter.CategoryViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(final CategoryAdapter.CategoryViewHolder viewHolder, final int i) {
		Category category = categories.get(i);
		
		viewHolder.name.setText(category.getCategoryName());
		viewHolder.summary.setText(category.getCategoryName());
		viewHolder.icon.setImageResource(R.drawable.lightbulb);
		viewHolder.checkBox.setChecked(true);
		
		viewHolder.layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (viewHolder.checkBox.isChecked()) {
					categorySelected = categories.get(rv.getChildLayoutPosition(v));
				} else {
					categorySelected = null;
				}
				
				
				viewHolder.checkBox.setChecked(!viewHolder.checkBox.isChecked());
			}
			
		});
		
		
	}
	
	public static class CategoryViewHolder extends RecyclerView.ViewHolder {
		private TextView name;
		private TextView summary;
		private ImageView icon;
		private View layout;
		private CheckBox checkBox;
		
		public CategoryViewHolder(View v) {
			super(v);
			
			layout = v.findViewById(R.id.layout);
			name = (TextView) v.findViewById(R.id.filter_TV_name);
			summary = (TextView) v.findViewById(R.id.filter_TV_summary);
			icon = (ImageView) v.findViewById(R.id.filter_IV_icon);
			checkBox = (CheckBox) v.findViewById(R.id.filter_CB_category);
			
		}
		
	}
	
	
	public Category getCategory() {
		return categorySelected;
	}
	
	
}
