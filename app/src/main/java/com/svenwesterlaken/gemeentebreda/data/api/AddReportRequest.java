package com.svenwesterlaken.gemeentebreda.data.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.User;
import com.svenwesterlaken.gemeentebreda.logic.util.ApiUtil;

import org.json.JSONArray;

import java.util.Map;

/**
 * Created by lukab on 6-6-2017.
 */

public class AddReportRequest implements Response.Listener<JSONArray>, Response.ErrorListener{
	
    private Context context;
    private Report report;
    private DatabaseHandler handler;

    public AddReportRequest(Context context) {
        this.context = context;
        handler = new DatabaseHandler(context);
    }

    public void addAReport(Report report) {
        User user = handler.getUser(1);
        report.setUser(user);
        this.report = report;
        String url = ApiUtil.createReportAddURL(report);


        JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.POST, url, new JSONArray(), this, this) {
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
        Log.i("ADDREPORT" , response.toString());
        handler.addReport(report);
        handler.addReportUser(report);
    }
}
