package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.logic.adapters.NewCategoryAdapter;

import java.util.ArrayList;

public class NewReportCategoryFragment extends Fragment {
    private CategoryChangedListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_new_report_category, container, false);
        RecyclerView categoryList = (RecyclerView) rootView.findViewById(R.id.category_RV_categories);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryList.setLayoutManager(layoutManager);

        DatabaseHandler handler = new DatabaseHandler(getContext());
        ArrayList<Category> categories = handler.getAllCategories();

        NewCategoryAdapter categoryAdapter = new NewCategoryAdapter(categories, getActivity(), categoryList, mListener);
        categoryList.setAdapter(categoryAdapter);

        categoryAdapter.notifyDataSetChanged();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a = null;

        if (context instanceof Activity){
            a= (Activity) context;
        }
        try {
            mListener = (CategoryChangedListener) a;
        } catch (ClassCastException e) {
            assert a != null;
            throw new ClassCastException(a.toString() + " must implement CategoryChangedListener");
        }
    }


    public interface CategoryChangedListener {
        void setCategory(Category c);
    }
}

