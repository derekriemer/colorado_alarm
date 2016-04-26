package com.robobrandon.simpleweather;

/**
 * Created by brandon on 2/29/16.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

/**
 * Wrapper for drying up our JSON requests
 */
public class CustomJsonRequest extends JsonObjectRequest {

    public CustomJsonRequest(int method, String url, JSONObject jsonRequest,
                             Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    private Priority mPriority;

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    @Override
    public Priority getPriority() {
        return mPriority == null ? Priority.NORMAL : mPriority;
    }

}
