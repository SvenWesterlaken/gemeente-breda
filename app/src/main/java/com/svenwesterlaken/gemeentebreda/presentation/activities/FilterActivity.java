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
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.logic.adapters.ReportAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

public class FilterActivity extends BaseActivity implements AdapterView.OnItemSelectedListener  {
	
	ArrayList reports = new ArrayList<>();
	DatabaseHandler handler;
	RecyclerView reportList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		reportList = (RecyclerView) findViewById(R.id.filter_RV);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.report_filter_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		handler = new DatabaseHandler(getApplicationContext());
		

		spinner.setOnItemSelectedListener(this);
		
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

		switch (position) {
			case 0:
				setAdapter(handler.getFilterReports("upvotes DESC"));
				break;
			case 1:
				setAdapter(handler.getFilterReports("upvotes ASC"));
			case 2:
				setAdapter(handler.getFilterReports("reportId DESC"));
				break;
			case 3:
				setAdapter(handler.getFilterReports("description ASC"));
				break;
			case 4:
				setAdapter(handler.getFilterReports("description DESC"));

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	
	public void setAdapter(ArrayList filterReports){
		
		ReportAdapter reportAdapter = new ReportAdapter(filterReports, getApplication());
		reportList.setAdapter(reportAdapter);
		reportAdapter.notifyDataSetChanged();
		reportList.setLayoutManager(new LinearLayoutManager(this));
	}
	
	@Override
	public void onBackPressed(){
		Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

}