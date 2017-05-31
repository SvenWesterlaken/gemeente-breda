package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.domain.Media;
import com.svenwesterlaken.gemeentebreda.logic.adapters.NewCategoryAdapter;

import java.util.ArrayList;

public class NewReportCategoryFragment extends Fragment {
    private CategoryChangedListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_new_report_category, container, false);
        final ListView reportList = (ListView) rootView.findViewById(R.id.category_LV_categories);

        final DatabaseHandler handler = new DatabaseHandler(this.getContext(),null, null, 1);

        final ArrayList<Category> categories = handler.getAllCategories();

        final NewCategoryAdapter categoryAdapter = new NewCategoryAdapter(this.getContext(), categories);
        reportList.setAdapter(categoryAdapter);

        categoryAdapter.notifyDataSetChanged();

        reportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category categorySelected = handler.getCategory(position + 1);
                categorySelected.getCategoryName();
                mListener.setCategory(categorySelected);
                Toast.makeText(getActivity().getApplicationContext(), "Categorie is toegevoegd", Toast.LENGTH_SHORT).show();


            }

        });



        return rootView;
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a = null;

        if (context instanceof Activity){
            a=(Activity) context;
        }
        try {
            mListener = (CategoryChangedListener) a;
        } catch (ClassCastException e) {
            throw new ClassCastException(a.toString() + " must implement CategoryChangedListener");
        }
    }


    public interface CategoryChangedListener {
        void setCategory(Category c);
    }
}

