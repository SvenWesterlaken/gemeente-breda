package com.svenwesterlaken.gemeentebreda.data.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.Category;
import com.svenwesterlaken.gemeentebreda.domain.ServiceCategory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lukab on 6-6-2017.
 */

public class CategoryRequest {
	private Context context;
	public final String TAG = this.getClass().getSimpleName();
	private static final String ENDPOINT = "http://37.34.59.50/breda/CitySDK/services.json";
	private Gson gson;
	private DatabaseHandler handler;
	
	/**
	 * Constructor
	 *
	 * @param context
	 *
	 */
	public CategoryRequest(Context context) {
		this.context = context.getApplicationContext();
		GsonBuilder gbuilder = new GsonBuilder();
		gson = gbuilder.create();
		handler = new DatabaseHandler(context);
		
	}
	
	public void handleGetCategories() {
		
		Log.i(TAG, "handleGetAllReports");
		StringRequest CategoryRequest = new StringRequest(Request.Method.GET, ENDPOINT, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
	
				Log.i(TAG, response.toString());
				
				
				List<ServiceCategory> categories = Arrays.asList(gson.fromJson(response, ServiceCategory[].class));
				Log.i("Categories", categories.size() + "categories loaded");
				if (!(handler.getAllCategories().size() == 0)) {
					handler.deleteCategory();
				}
				for (ServiceCategory category : categories) {
					Log.i("Categories", category.categoryName + category.description);
					String name = category.categoryName;
					String description = category.description;
					Category category1 = new Category(handler.getAllCategories().size() + 1, name, description);
					
					handler.addCategory(category1);
				}
				
			} } ,              new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				
				Log.e(TAG, error.toString());
			}
		});
		
		ReportRequestQueue.getInstance(context).addToRequestQueue(CategoryRequest);
	}
	
	
}
