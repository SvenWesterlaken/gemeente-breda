package com.svenwesterlaken.gemeentebreda.data.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Location;
import com.svenwesterlaken.gemeentebreda.domain.Report;
import com.svenwesterlaken.gemeentebreda.domain.ServiceReport;
import com.svenwesterlaken.gemeentebreda.domain.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lukab on 6-6-2017.
 */

public class AddReportRequest {
	
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
		public AddReportRequest(Context context) {
			this.context = context;
			GsonBuilder gbuilder = new GsonBuilder();
			gson = gbuilder.create();
			handler = new DatabaseHandler(context);
		}
		
		public void addAReport(final Report report) {
			
			String street = replace(report.getLocation().getStreet());
			String description = replace(report.getDescription());
			User user = handler.getUser(1);
			String name = user.getName();
			String[] split =  name.split(" ");
			String firstname =  split[0];
			String lastname = split[1];
			String phone = "";
			if (!(user.getMobileNumber() == null)){
				phone = "&phone=" + user.getMobileNumber();
			}
			Log.i(TAG, "handlePostRequest");
			String url = "http://37.34.59.50/breda/CitySDK/requests.json?service_code=OV&description=" + description +"&lat=" +
					report.getLocation().getLatitude() + "&long=" + report.getLocation().getLongitude() + "&address_string=" + street + "&address_id=1"
					+ "&first_name=" + firstname + "&last_name=" + lastname + phone;
					
			HashMap<String, String> jsonParams = new HashMap<String, String>();
			JSONArray array = new JSONArray();
			JsonArrayRequest postRequest = new JsonArrayRequest(
					Request.Method.POST,
					url
					, array,
					new Response.Listener<JSONArray>() {
						@Override
						
						public void onResponse(JSONArray response) {
							Log.i("ADDREPORT" , response.toString());
							handler.addReport(report);
							handler.addReportUser(report);
							
							
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
		
		public String replace(String str){
			return str.replaceAll(" ", "%20");
		}
	
}
