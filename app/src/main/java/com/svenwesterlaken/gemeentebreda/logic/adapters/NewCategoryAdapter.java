package com.svenwesterlaken.gemeentebreda.logic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.domain.Category;

import java.util.ArrayList;

/**
 * Created by lukab on 21-5-2017.
 */

public class NewCategoryAdapter extends ArrayAdapter<Category> {

    public  NewCategoryAdapter(Context context, ArrayList<Category> categories){
        super(context, 0, categories);

    }

    public View getView(int position, View convertView, ViewGroup parent){
        Category category = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_new_category, parent, false);
        }

        String name = category.getCategoryName();
        String summary = category.getCategorySummary();

        TextView nameTxt = (TextView) convertView.findViewById(R.id.newCategory_TV_name);
        TextView summaryTxt = (TextView) convertView.findViewById(R.id.newCategory_TV_summary);
        ImageView categoryIcon = (ImageView) convertView.findViewById(R.id.newCategory_IV_icon);

        nameTxt.setText(name);
        summaryTxt.setText(summary);
        categoryIcon.setImageResource(R.drawable.lightbulb); //verandert later per category type

        return convertView;
    }
}
