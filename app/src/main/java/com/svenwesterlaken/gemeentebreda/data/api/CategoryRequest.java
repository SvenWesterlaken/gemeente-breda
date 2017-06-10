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
import com.svenwesterlaken.gemeentebreda.logic.util.ApiUtil;

import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by lukab on 6-6-2017.
 */

public class CategoryRequest implements Response.Listener<String>, Response.ErrorListener{
	private Context context;
	private Gson gson;
	private DatabaseHandler handler;

	public CategoryRequest(Context context) {
		this.context = context.getApplicationContext();
		gson = new GsonBuilder().create();
		handler = new DatabaseHandler(context);
	}
	
	public void handleGetCategories() {
		StringRequest categoryRequest = new StringRequest(Request.Method.GET, ApiUtil.getCategoryRequestURL(), this, this);
		ReportRequestQueue.getInstance(context).addToRequestQueue(categoryRequest);
	}


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, error.getMessage());
    }

    @Override
    public void onResponse(String response) {
        List<ServiceCategory> categories = Arrays.asList(gson.fromJson(response, ServiceCategory[].class));

        if (!handler.getAllCategories().isEmpty()) {
            handler.deleteCategory();
        }

        for (ServiceCategory category : categories) {
            Log.i("Categories", category.categoryName + category.description);
            String name = category.categoryName;
            String description = category.description;
            Category category1 = new Category(handler.getAllCategories().size() + 1, name, description);

            handler.addCategory(category1);
        }
    }
}
