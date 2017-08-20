package com.neeru.client;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.neeru.client.callbacks.OTPListener;
import com.neeru.client.models.User;
import com.neeru.client.network.GsonRequest;
import com.neeru.client.network.JsonRequestHandler;
import com.neeru.client.network.NetworkHandler;
import com.neeru.client.prefs.AuthPreference;
import com.neeru.client.receiver.OtpReader;
import com.neeru.client.util.Constants;
import com.neeru.client.views.PinEntryView;
import com.neeru.client.views.ReaderFontTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.neeru.client.RegisterationActivity.INTENT_EXTRA_MOBILE;
import static com.neeru.client.RegisterationActivity.INTENT_EXTRA_OPERATION;
import static com.neeru.client.RegisterationActivity.INTENT_EXTRA_OTP;

public class OTPVarificationActivity extends AppCompatActivity implements OTPListener, Response.Listener<JSONObject>, Response.ErrorListener, PinEntryView.OnPinEnteredListener {

    String otp = null;
    String mobile = null;
    String operation = null;
    private PinEntryView mOTPView;
    private final Pattern p = Pattern.compile("(\\d{4})");
    private final OtpReader mybroadcast = new OtpReader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpvarification);
        VolleyLog.DEBUG = true;
        VolleyLog.setTag("Volley");
        Log.isLoggable("Volley", Log.VERBOSE);


        otp = getIntent().getStringExtra(INTENT_EXTRA_OTP);
        mobile = getIntent().getStringExtra(INTENT_EXTRA_MOBILE);
        operation = getIntent().getStringExtra(INTENT_EXTRA_OPERATION);
        ReaderFontTextView tvDesc = (ReaderFontTextView) findViewById(R.id.text_mobiledesc);

        tvDesc.setText(getResources().getString(R.string.text_verification_content) + " " + mobile);
        mOTPView = (PinEntryView) findViewById(R.id.pinview);
        mOTPView.setOnPinEnteredListener(this);

        List<String> mList = new ArrayList<>();
        mList.add("AM-NOTIFY");
        mList.add("HP-PLVSMS");

        mybroadcast.bind(this, mList);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mybroadcast, filter);


        keyboarsetUP();

        View v = findViewById(R.id.mRoot);

        if (v != null)
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    void keyboarsetUP() {
        KeyboardView keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        keyboardView.setPreviewEnabled(false);
        Keyboard keyboard = new Keyboard(OTPVarificationActivity.this, R.xml.keyboards);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(keyboardActionListener);

    }


    public KeyboardView.OnKeyboardActionListener keyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            long eventTime = System.currentTimeMillis();
            KeyEvent event = new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_DOWN, primaryCode, 0, 0, 0, 0, KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE);
            dispatchKeyEvent(event);
            if (primaryCode == KeyEvent.KEYCODE_NUMPAD_EQUALS) {
                //displayCalculatedResult();
            }
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeUp() {
        }
    };

    @Override
    public void otpReceived(String messageText) {
        Matcher m = p.matcher(messageText);

        if (m.find()) {
            String str = m.group(0);

            if (otp.equals(str)) {
                mOTPView.setText(str);
            }

        }
    }


    void onApiInit() {
        String url;

        if (operation == null) {
            url = Constants.URL + "auth/v1/signup";
        } else if (operation.equalsIgnoreCase("signup")) {
            url = Constants.URL + "auth/v1/signup";
        } else {
            url = Constants.URL + "auth/v1/signin";
        }


        JSONObject jsonObject = new JSONObject();


        try {

            jsonObject.put("contact", mobile);
            jsonObject.put("otp", Integer.parseInt(otp));
            jsonObject.put("deviceId", Settings.Secure.getString(getApplication().getContentResolver(),
                    Settings.Secure.ANDROID_ID));
            jsonObject.put("token", FirebaseInstanceId.getInstance().getToken());
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        JsonRequestHandler jsObjRequest = new JsonRequestHandler(Request.Method.POST, url, jsonObject, this, this, JsonRequestHandler.getFirebaseHeader(this));

        NetworkHandler.getInstance(this).addToRequestQueue(jsObjRequest);


    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.v("Error--------", new String(error.networkResponse.data));
    }

    @Override
    public void onPinEntered(String pin) {
        if (otp.equals(pin)) {
            onApiInit();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        //Log.v("Error--------", response.toString());


        Gson gson = new Gson();

        User user = gson.fromJson(response.toString(), User.class);

        AuthPreference mAuth = new AuthPreference(getApplicationContext());
        mAuth.setUser(user);


        Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        this.finish();


    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        try {
            unregisterReceiver(mybroadcast);

        } catch (Exception e) {

        }

    }


}


