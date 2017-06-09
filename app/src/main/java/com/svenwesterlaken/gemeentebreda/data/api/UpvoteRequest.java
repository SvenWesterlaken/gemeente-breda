package com.svenwesterlaken.gemeentebreda.data.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.User;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lukab on 9-6-2017.
 */

public class UpvoteRequest {
	
	private Context context;
	public final String TAG = this.getClass().getSimpleName();
	private Gson gson;
	private final int MY_SOCKET_TIMEOUT_MS = 50000;
	
	// De aanroepende class implementeert deze interface.
	private DatabaseHandler handler;
	
	/**
	 * Constructor
	 *
	 * @param context
	 */
	public UpvoteRequest(Context context) {
		this.context = context;
		GsonBuilder gbuilder = new GsonBuilder();
		gson = gbuilder.create();
		handler = new DatabaseHandler(context);
	}
	
	public void addAUpvote(final Report report) {
		
		Log.i(TAG, "handlePostRequest");
		String url = "http://37.34.59.50/breda/CitySDK/upvoteRequest.json?service_request_id=" + report.getReportID() + "&extraDescription=upvote";
		
		HashMap<String, String> jsonParams = new HashMap<String, String>();
		JSONArray array = new JSONArray();
		JsonArrayRequest postRequest = new JsonArrayRequest(
				Request.Method.POST,
				url
				, array,
				new Response.Listener<JSONArray>() {
					@Override
					
					public void onResponse(JSONArray response) {
						Log.i("ADDUpvote" , response.toString());
						handler.addUpvote(report);
						
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Error - send back to caller
						error.getMessage();
					}
				})
		
		{
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Cache-Control", "no-cache");
				headers.put("Content-Type", "application/json; charset=utf-8");
				headers.put("Server", "Microsoft-ISS/8.5");
				
				return headers;
			}
			
			
		};
		
		postRequest.setRetryPolicy(new DefaultRetryPolicy(
				0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		// Access the RequestQueue through your singleton class.
		ReportRequestQueue.getInstance(context).addToRequestQueue(postRequest);
		
	}
	
}
