package com.neeru.client.network;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by brajendra on 05/08/17.
 */

public class JsonRequestHandler extends JsonObjectRequest {
    private Map header;

    public JsonRequestHandler(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Map header) {
        super(method, url, jsonRequest, listener, errorListener);
        this.header = header;
        setShouldCache(false);
        setRetryPolicy(new DefaultRetryPolicy(0, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public JsonRequestHandler(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (header != null)
            return header;
        return super.getHeaders();
    }
}
