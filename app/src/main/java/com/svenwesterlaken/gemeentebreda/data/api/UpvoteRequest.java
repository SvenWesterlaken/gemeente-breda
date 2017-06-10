package com.svenwesterlaken.gemeentebreda.data.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.logic.util.ApiUtil;

import org.json.JSONArray;

import java.util.Map;

/**
 * Created by lukab on 9-6-2017.
 */

public class UpvoteRequest implements Response.Listener<JSONArray>, Response.ErrorListener {
	
	private Context context;
	private DatabaseHandler handler;
	private Report report;

	public UpvoteRequest(Context context) {
		this.context = context;
		handler = new DatabaseHandler(context);
	}
	
	public void addAUpvote(Report report) {
		this.report = report;
		JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.POST, ApiUtil.getUpvoteRequestURL(report.getReportID()), new JSONArray(), this, this) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return ApiUtil.getPostHeaders();
			}
		};
		
		postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		ReportRequestQueue.getInstance(context).addToRequestQueue(postRequest);
		
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		error.getMessage();
	}

	@Override
	public void onResponse(JSONArray response) {
		handler.addUpvote(report);
	}
}
