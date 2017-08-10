package com.neeru.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neeru.client.adapter.OrderAdapter;
import com.neeru.client.models.Location;
import com.neeru.client.models.Order;
import com.neeru.client.network.JsonRequestHandler;
import com.neeru.client.network.NetworkHandler;
import com.neeru.client.prefs.AuthPreference;
import com.neeru.client.util.Constants;
import com.neeru.client.views.EmptyRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private EmptyRecyclerView mRecyclerView;
    private OrderAdapter mAdapter;
    private JsonRequestHandler jsObjRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recyclerview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRecyclerView = (EmptyRecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(null);


        String url  = Constants.URL + "inventory/v1/order/user";
        AuthPreference mPrefs = new AuthPreference(getApplicationContext());
        Map headers = new HashMap<String, String>();

        headers.put("authorization", "Bearer " + mPrefs.getAccessTocken());

        jsObjRequest = new JsonRequestHandler(Request.Method.GET, url, null, this, this, headers);
        NetworkHandler.getInstance(this).addToRequestQueue(jsObjRequest);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.v("Error--------", new String(error.networkResponse.data));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(JSONObject response) {


        try {
            JSONArray rowArray = response.getJSONArray("rows");

            Type listType = new TypeToken<List<Order>>() {
            }.getType();
            List<Order> orders = new Gson().fromJson(rowArray.toString(), listType);

            mAdapter = new OrderAdapter(this, orders);
            mRecyclerView.setAdapter(mAdapter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
