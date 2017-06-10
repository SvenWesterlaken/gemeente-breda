package com.svenwesterlaken.gemeentebreda.data.api;

/**
 * Created by lukab on 4-6-2017.
 */


import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

public class ReportRequestQueue {
        
    private static ReportRequestQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private ReportRequestQueue(Context context) {
        mCtx = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
    }
    
    /**
     * Static methode die één enkele instantie van deze class beheert.
     *
     * @param context
     * @return
     */
    public static synchronized ReportRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ReportRequestQueue(context);
        }
        return mInstance;
    }
    
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }
    
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
