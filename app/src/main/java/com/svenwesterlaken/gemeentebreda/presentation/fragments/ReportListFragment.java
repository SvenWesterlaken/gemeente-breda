package com.svenwesterlaken.gemeentebreda.presentation.fragments;


import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.google.android.gms.maps.model.LatLng;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.api.ReportRequest;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.logic.adapters.ReportAdapter;
import com.svenwesterlaken.gemeentebreda.presentation.activities.ReportActivity;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


public class ReportListFragment extends Fragment implements PullRefreshLayout.OnRefreshListener, ReportRequest.ReportListener {
	
	private ReportAdapter reportAdapter;
	private PullRefreshLayout prf;
	private ArrayList<Report> reports;
	private ReportActivity activity;
	private RecyclerView reportList;
	private Location myLocation;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Select view & cardView to inflate
		View rootView = inflater.inflate(R.layout.fragment_reportlist, container, false);
		reportList = (RecyclerView) rootView.findViewById(R.id.report_RV_reports);
		reportList.setItemAnimator(new FadeInUpAnimator());
		reportList.getItemAnimator().setAddDuration(300);
		reportList.getItemAnimator().setRemoveDuration(200);
		
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		reportList.setLayoutManager(layoutManager);
		
		activity = (ReportActivity) getActivity();
		
		reports = new ArrayList<>();
		getReports();
		
		reportAdapter = new ReportAdapter(reports, getContext());
		reportList.setAdapter(reportAdapter);
		
		reportAdapter.notifyDataSetChanged();
		
		prf = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
		prf.setColorSchemeColors(Color.argb(255, 217, 29, 73));
		prf.setOnRefreshListener(this);

		// Get NewLocationManager object from System Service LOCATION_SERVICE
		LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

		// Create a criteria object to retrieve provider
		Criteria criteria = new Criteria();

		// Get the name of the best provider
		String provider = locationManager.getBestProvider(criteria, true);

		// Get Current Location
		myLocation = null;
		if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
				== PackageManager.PERMISSION_GRANTED) {
			myLocation = locationManager.getLastKnownLocation(provider);
		}
		
		return rootView;
	}
	
	
	@Override
	public void onReportsAvailable(Report report) {
		reports.add(report);
		reportAdapter.notifyItemInserted(reports.size());
	}
	
	@Override
	public void onFinished() {
		prf.setRefreshing(false);
	}
	
	
	@Override
	public void onRefresh() {
		getReports();
	}
	
	
	public void getReports() {
		
		if (!reports.isEmpty()) {
			int size = reports.size();
			reports.clear();
			reportAdapter.notifyItemRangeRemoved(0, size);
		}
		
		if (activity.getList() == null || activity.getList().isEmpty()) {
			ReportRequest request = new ReportRequest(getContext(), this);
			com.svenwesterlaken.gemeentebreda.domain.Location location = new com.svenwesterlaken.gemeentebreda.domain.Location();

			if (myLocation != null) {
				LatLng hogeschool = new LatLng(51.5843682, 4.795152);
				myLocation.setLatitude(hogeschool.latitude);
				myLocation.setLongitude(hogeschool.longitude);
			}

			double lat = myLocation.getLatitude();
			double lng = myLocation.getLongitude();
			location.setLatitude(lat);
			location.setLongitude(lng);

			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
			String radius = sharedPrefs.getString("pref_radius", "");
			if (radius.isEmpty()){
				String[] testArray = getResources().getStringArray(R.array.radius_preference_array_values);
				radius = testArray[2];
			}
			Log.i("PREFERENCE_RADIUS", radius);
			request.handleGetAllReports(location, Double.parseDouble(radius));
		}
		else {
			reports = activity.getList();
			reportAdapter = new ReportAdapter(reports, getContext());
			reportList.setAdapter(reportAdapter);
			reportAdapter.notifyDataSetChanged();
		}
	}
	
}

