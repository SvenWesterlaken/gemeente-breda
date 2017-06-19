package com.svenwesterlaken.gemeentebreda.presentation.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.logic.adapters.CategoryAdapter;
import com.svenwesterlaken.gemeentebreda.logic.adapters.NewCategoryAdapter;
import com.svenwesterlaken.gemeentebreda.logic.adapters.ReportAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

public class FilterActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
	
	ArrayList reports = new ArrayList<>();
	DatabaseHandler handler;
	RecyclerView reportList;
	CategoryAdapter categoryAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Filteren");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		handler = new DatabaseHandler(getApplicationContext());
		
		reportList = (RecyclerView) findViewById(R.id.filter_RV);
		categoryAdapter = new CategoryAdapter(handler.getAllCategories(), reportList);
		reportList.setAdapter(categoryAdapter);
		
		categoryAdapter.notifyDataSetChanged();
		reportList.setLayoutManager(new LinearLayoutManager(this));
		
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.report_filter_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		
		spinner.setOnItemSelectedListener(this);
		
		
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		
		
		switch (position) {
			case 0:
				reports.clear();
				break;
			case 1:
				reports = handler.getFilterReports("upvotes DESC", categoryAdapter.getCategory());
				break;
			case 2:
				reports = handler.getFilterReports("upvotes ASC", categoryAdapter.getCategory());
				break;
			case 3:
				reports = handler.getFilterReports("reportId DESC", categoryAdapter.getCategory());
				break;
			case 4:
				reports = handler.getFilterReports("description ASC", categoryAdapter.getCategory());
				break;
			case 5:
				reports = handler.getFilterReports("description DESC", categoryAdapter.getCategory());
				break;
			case 6:
				reports = handler.getFilterReports("", categoryAdapter.getCategory());
				break;
			default:
		}
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
	
	@Override
	public void finish() {
		Intent intent = new Intent();
		
		if (reports.isEmpty()) {
			setResult(RESULT_CANCELED, intent);
		} else {
			intent.putExtra("new_list", reports);
			setResult(RESULT_OK, intent);
		}
		
		super.finish();
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
	
}