package com.robobrandon.simpleweather;

import android.app.Application;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * This class prevents memory bloat and increased activity on the main thread by instantiating a
 * single object and then passing each request to a thread that isn't being used by the UI.
 */
public class RestHelper extends Application {

    public static final String TAG = RestHelper.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static RestHelper mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static synchronized RestHelper getInstance() {
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancel() {
        mRequestQueue.cancelAll(TAG);
    }

}