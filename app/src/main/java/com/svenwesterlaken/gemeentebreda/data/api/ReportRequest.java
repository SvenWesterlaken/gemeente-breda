package com.svenwesterlaken.gemeentebreda.data.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.ServiceReport;
import com.svenwesterlaken.gemeentebreda.logic.util.ApiUtil;

import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by lukab on 6-6-2017.
 */

public class ReportRequest implements Response.Listener<String>, Response.ErrorListener {
	private Context context;
	private Gson gson;
	private ReportRequest.ReportListener listener;
	private DatabaseHandler handler;

	public ReportRequest(Context context, ReportListener listener) {
		this.context = context.getApplicationContext();
		this.listener = listener;
		gson = new GsonBuilder().create();
		handler = new DatabaseHandler(context);
	}

	/**
	 * Verstuur een GET request om alle reports op te halen
	 */
	public void handleGetAllReports() {
		StringRequest reportsRequest = new StringRequest(Request.Method.GET, ApiUtil.getReportRequestURL(), this, this);
		ReportRequestQueue.getInstance(context).addToRequestQueue(reportsRequest);
	}

	public void handleGetAllReports(Location location, double radius) {
		StringRequest reportsRequest = new StringRequest(Request.Method.GET, ApiUtil.getReportRequestURL(location, radius), this, this);
		ReportRequestQueue.getInstance(context).addToRequestQueue(reportsRequest);
	}

	@Override
	public void onResponse(String response) {
		List<ServiceReport> serviceReports = Arrays.asList(gson.fromJson(response, ServiceReport[].class));
		Log.i("Reports", serviceReports.size() + " reports loaded");
		handler.deleteAllReports();
		handler.removeAllLocations();


		for (ServiceReport serviceReport : serviceReports) {
			Log.i("Reports", serviceReport.address + serviceReport.serviceName);

			Report report = new Report();
			report.setReportID(Integer.parseInt(serviceReport.id));

			Location location = new Location(serviceReport.address, report.getReportID(), serviceReport.latitude, serviceReport.longitude);
			handler.addLocation(location);

			report.setLocation(location);
			report.setCategory(handler.getCategory(1)); //ff aanpassen nog
			report.setDescription(serviceReport.description);
			report.setStatus(serviceReport.statusGemeente);
			report.setUpvotes( Integer.parseInt(serviceReport.upvotes.substring(0,1)));

			handler.addReport(report);
            listener.onReportsAvailable(report);
		}

		listener.onFinished();


	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Log.e(TAG, error.getMessage());
	}

	public interface ReportListener {
		void onReportsAvailable(Report report);
        void onFinished();

	}
	
	
	
}
