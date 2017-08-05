package com.neeru.client;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dd.CircularProgressButton;
import com.neeru.client.models.User;
import com.neeru.client.network.JsonRequestHandler;
import com.neeru.client.network.NetworkHandler;
import com.neeru.client.util.Constants;

import org.json.JSONObject;

public class RegisterationActivity extends BaseActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {


    public static String INTENT_EXTRA_OTP = "intent_otp";
    public static String INTENT_EXTRA_OPERATION = "intent_operation";
    public static String INTENT_EXTRA_MOBILE = "intent_mobile";


    private AppCompatEditText mContactView;
    private CircularProgressButton mCircularButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        CircularProgressButton mButton = (CircularProgressButton) findViewById(R.id.circularButton);

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/Klavika_Bold.otf");

        mButton.setTypeface(tf);

        mContactView = (AppCompatEditText) findViewById(R.id.contact);

        mContactView.setTypeface(tf);


        mCircularButton = (CircularProgressButton) findViewById(R.id.circularButton);

        mCircularButton.setOnClickListener(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        attemptRegister();
    }


    void attemptRegister() {


        String mobile = mContactView.getText().toString();


        if (validate(mobile)) {
            Log.v("REQUEST------->", "REQUEST");
            String url = Constants.URL + "auth/v1/otp/" + mobile;
            mCircularButton.setIndeterminateProgressMode(true);
            mCircularButton.setProgress(50);

            JsonRequestHandler jsObjRequest = new JsonRequestHandler
                    (Request.Method.POST, url, null, this, this,null);


            NetworkHandler.getInstance(this).addToRequestQueue(jsObjRequest);


        }

    }


    boolean validate(String mobile) {

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(mobile)) {
            mContactView.setError(getString(R.string.error_empty_mobile));
            focusView = mContactView;
            cancel = true;
        } else if (!isMobileValid(mobile)) {
            mContactView.setError(getString(R.string.error_invalid_contact));
            focusView = mContactView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            return false;
        } else {

            return true;
        }

    }


    private boolean isMobileValid(String phone) {
        boolean match = android.util.Patterns.PHONE.matcher(phone).matches();
        if (match) {
            if (phone.length() < 6 || phone.length() > 13)
                match = false;
        }

        return match;
    }


    @Override
    public void onResponse(JSONObject response) {

        Log.v("GET-RESPONSE------->", "GET-RESPONSE");
        mCircularButton.setProgress(0);
        String otp = null;
        String operation = null;

        try {
            otp = response.getString("data");
            operation = response.getString("operation");
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        if (otp == null)
            return;


        Intent intent = new Intent(getApplicationContext(), OTPVarificationActivity.class);
        intent.putExtra(INTENT_EXTRA_OTP, otp);
        intent.putExtra(INTENT_EXTRA_OPERATION, operation);
        intent.putExtra(INTENT_EXTRA_MOBILE, mContactView.getText().toString());
        startActivity(intent);


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.v("", "");
        mCircularButton.setProgress(-1);
    }
}
