package com.svenwesterlaken.gemeentebreda.data.api;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.ServiceReport;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lukab on 6-6-2017.
 */

public class ReportRequest {
	private Context context;
	public final String TAG = this.getClass().getSimpleName();
	private static final String ENDPOINT2 = "http://37.34.59.50/breda/CitySDK/requests.json/?service_code=OV";
	private Gson gson;
	
	// De aanroepende class implementeert deze interface.
	private ReportRequest.ReportListener listener;
	private DatabaseHandler handler;
	
	/**
	 * Constructor
	 *
	 * @param context
	 */
	public ReportRequest(Context context, ReportListener listener) {
		this.context = context;
		this.listener = listener;
		GsonBuilder gbuilder = new GsonBuilder();
		gson = gbuilder.create();
		handler = new DatabaseHandler(context);
	}
	
	/**
	 * Verstuur een GET request om alle reports op te halen
	 */
	public void handleGetAllReports() {
		
		Log.i(TAG, "handleGetAllReports");
		StringRequest ReportsRequest = new StringRequest(Request.Method.GET, ENDPOINT2, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// Succesvol response
				Log.i(TAG, response.toString());
				
				List<ServiceReport> serviceReports = Arrays.asList(gson.fromJson(response, ServiceReport[].class));;
				Log.i("Reports", serviceReports.size() + "reports loaded");
				handler.deleteAllReports();
				handler.removeAllLocations();
				
				
				ArrayList<Report> reports = new ArrayList<>();
					for (ServiceReport report : serviceReports) {
						Log.i("Reports", report.address + report.serviceName);
						Report report1 = new Report();
						report1.setReportID(Integer.parseInt(report.id));
						Location location = new Location(report.address, report1.getReportID(), report.latitude, report.longitude);
						handler.addLocation(location);
						report1.setLocation(location);
						report1.setCategory(handler.getCategory(1)); //ff aanpassen nog
						report1.setDescription(report.description);
						report1.setStatus(report.statusGemeente);
						report1.setUpvotes( Integer.parseInt(report.upvotes.substring(0,1)));
						
						handler.addReport(report1);
						reports.add(report1);
						
				}
				listener.onReportsAvailable(reports);
			} } ,              new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							// handleErrorResponse(error);
							Log.e(TAG, error.toString());
						}
					});
		
		ReportRequestQueue.getInstance(context).addToRequestQueue(ReportsRequest);
	}
	
	

//	 Callback interface - implemented by the calling class (MainActivity in our case).

	public interface ReportListener {
		// Callback function to return a fresh list of ToDos
		void onReportsAvailable(ArrayList<Report> reports);

	}
	
	
	
}
