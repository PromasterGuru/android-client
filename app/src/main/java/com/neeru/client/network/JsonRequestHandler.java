package com.neeru.client.network;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.neeru.client.prefs.AuthPreference;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brajendra on 05/08/17.
 */

public class JsonRequestHandler extends JsonObjectRequest {


    public static String HEADER_AUTHORIZATION = "Authorization";
    public static String HEADER_DEVICE_ID = "device-id";
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


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.v("RESPONCE", jsonString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return super.parseNetworkResponse(response);
    }


    public static Map getFirebaseHeader(Context context) {
        Map<String, String> map = new HashMap<>();
        map.put(HEADER_DEVICE_ID, Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID) );
        return map;
    }

    public static Map getHeader(Context context) {
        Map<String, String> map = new HashMap<>();
        map.put(HEADER_AUTHORIZATION, new AuthPreference(context).getAccessTocken());
        map.put(HEADER_DEVICE_ID, Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID) );
        return map;
    }


}
