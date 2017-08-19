package com.neeru.client;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dd.CircularProgressButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neeru.client.models.Location;
import com.neeru.client.network.GsonRequest;
import com.neeru.client.network.JsonRequestHandler;
import com.neeru.client.network.NetworkHandler;
import com.neeru.client.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LocationActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener {


    public static String INTENT_EXTRA_LOCATION = "intent_location";

    private Spinner mSpinner;
    private JsonRequestHandler jsObjRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        CircularProgressButton mButton = (CircularProgressButton) findViewById(R.id.circularButton);
        mSpinner = (Spinner) findViewById(R.id.spinner_location);

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/Klavika_Bold.otf");

        mButton.setTypeface(tf);


        mButton.setOnClickListener(this);


        String url = url = Constants.URL + "inventory/v1/location";


        jsObjRequest = new JsonRequestHandler(Request.Method.GET, url, null, this, this, JsonRequestHandler.getFirebaseHeader(this));

        NetworkHandler.getInstance(this).addToRequestQueue(jsObjRequest);


    }

    @Override
    public void onErrorResponse(VolleyError error) {

        // Log.v("Error--------", new String(error.networkResponse.data));
    }

    @Override
    public void onResponse(JSONObject response) {

        try {
            JSONArray rowArray = response.getJSONArray("rows");

            Type listType = new TypeToken<List<Location>>() {
            }.getType();
            List<Location> location = new Gson().fromJson(rowArray.toString(), listType);

            ArrayAdapter<Location> adapter =
                    new ArrayAdapter<Location>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, location);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mSpinner.setAdapter(adapter);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {


        Location location = (Location) mSpinner.getSelectedItem();


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(INTENT_EXTRA_LOCATION, location);
        startActivity(intent);
    }
}


