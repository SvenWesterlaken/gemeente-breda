package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.logic.adapters.NewCategoryAdapter;

import java.util.ArrayList;

public class NewReportCategoryFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_new_report_category, container, false);
        ListView reportList = (ListView) rootView.findViewById(R.id.category_LV_categories);

        DatabaseHandler handler = new DatabaseHandler(this.getContext(),null, null, 1);

        ArrayList<Category> categories = handler.getAllCategories();

        NewCategoryAdapter categoryAdapter = new NewCategoryAdapter(this.getContext(), categories);
        reportList.setAdapter(categoryAdapter);

        categoryAdapter.notifyDataSetChanged();
        return rootView;
    }
}
