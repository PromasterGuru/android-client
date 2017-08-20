package com.neeru.client;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.neeru.client.callbacks.OrderActionListener;
import com.neeru.client.fragment.SuccessDialogFragment;
import com.neeru.client.models.Address;
import com.neeru.client.models.Product;
import com.neeru.client.network.JsonRequestHandler;
import com.neeru.client.network.NetworkHandler;
import com.neeru.client.prefs.AuthPreference;
import com.neeru.client.receiver.OtpReader;
import com.neeru.client.util.Constants;
import com.neeru.client.util.DialogHelper;
import com.neeru.client.util.Util;
import com.neeru.client.views.CansHorizontalView;
import com.neeru.client.views.TimeSlotHorizontalView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.neeru.client.LocationActivity.INTENT_EXTRA_LOCATION;
import static com.neeru.client.SelectAddressActivity.INTENT_ADDRESS;
import static com.neeru.client.fragment.ProductFragment.INTENT_EXTRA_PRODUCT;

public class FinalOrderActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, OrderActionListener, Response.Listener<JSONObject>, Response.ErrorListener {

    public static int RESULT_ADDRESS = 1;


    private EditText mEditTextDate;


    private Calendar calender;


    private int locationId;

    private int time;
    private int quantity;
    private TimeSlotHorizontalView mTimeSlot;
    private View mRoot;
    private Product product;
    private Address mAddress;
    private TextView mPrice;
    private AuthPreference mAuthPref;
    private View mPayment;
    boolean isSlotAvilable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VolleyLog.DEBUG = true;
        mAuthPref = new AuthPreference(getApplicationContext());

        mAddress = new Address();
        mAddress.line1 = "MVP Sector 8";
        mAddress.line2 = "";
        mAddress.landMark = "Satya sai vidya vihar";

        locationId = getIntent().getIntExtra(INTENT_EXTRA_LOCATION, -1);
        product = getIntent().getParcelableExtra(INTENT_EXTRA_PRODUCT);


        setContentView(R.layout.activity_final_order);


        mRoot = findViewById(R.id.root_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);


        getWindow().setEnterTransition(buildEnterTransition());


        TextView mTVTitle = (TextView) findViewById(R.id.title_product);

        mTVTitle.setText(product.name);

        mEditTextDate = (EditText) findViewById(R.id.editText_date);
        mEditTextDate.setOnClickListener(this);
        calender = Calendar.getInstance();


        mEditTextDate.setText(Util.formatDate(calender));


        mPayment = findViewById(R.id.payment);
        mPayment.setOnClickListener(this);


        mTimeSlot = (TimeSlotHorizontalView) findViewById(R.id.timeslotView);
        mTimeSlot.setActionListener(this);
        mTimeSlot.setCalender(calender);

        CansHorizontalView mQuantityView = (CansHorizontalView) findViewById(R.id.quantityView);


        mQuantityView.setActionListener(this);

        mPrice = (TextView) findViewById(R.id.text_price);

        findViewById(R.id.address_container).setOnClickListener(this);

    }


    private Transition buildEnterTransition() {
        Explode enterTransition = new Explode();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editText_date:

                DatePickerDialog datePicker = new DatePickerDialog(this, 0, this, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker.show();
                break;

            case R.id.payment:

                try {
                    payment();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case R.id.address_container:
                Intent intent = new Intent(this, SelectAddressActivity.class);
                startActivityForResult(intent, RESULT_ADDRESS);
                break;

        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calender.set(year, month, dayOfMonth);

        mEditTextDate.setText(Util.formatDate(calender));

        mTimeSlot.updateView();


    }


    DialogHelper dialogHelper = new DialogHelper();


    void payment() throws Exception {


        if (validation()) {


            JSONObject jsonObject = new JSONObject();

            jsonObject.put("slot", time);
            jsonObject.put("address", mAddress.fullName +" "+ mAddress.line1);
            jsonObject.put("addressLine1",mAddress.line1);
            jsonObject.put("paymentMethod", "COD");
            jsonObject.put("totalPrice", product.price * quantity);
            jsonObject.put("locationId", locationId);
            jsonObject.put("landmark", mAddress.landMark);

            JSONArray itemsArray = new JSONArray();

            JSONObject itmObject = new JSONObject();
            itmObject.put("productId", product.id);
            itmObject.put("sellerId", product.seller.id);
            itmObject.put("quantity", quantity);
            itmObject.put("price", product.price);

            itemsArray.put(itmObject);

            jsonObject.put("items", itemsArray);
            jsonObject.put("expectedDeliveryDate", calender.get(Calendar.YEAR) + "-" + (calender.get(Calendar.MONTH) + 1) + "-" + calender.get(Calendar.DAY_OF_MONTH));


            String url = Constants.URL + "inventory/v1/order";


            dialogHelper.showProgressDialog(this, "Ordering...");

            JsonRequestHandler jsObjRequest = new JsonRequestHandler(Request.Method.POST, url, jsonObject, this, this, JsonRequestHandler.getHeader(this));

            NetworkHandler.getInstance(this).addToRequestQueue(jsObjRequest);
        }


    }

    boolean validation() {


        if (!isSlotAvilable) {
            dialogHelper.showSnackBar("Please select other date.", mRoot);
            return false;
        } else if (time <= 0) {
            dialogHelper.showSnackBar("Please select time slot.", mRoot);
            return false;
        } else if (quantity <= 0) {
            dialogHelper.showSnackBar("Please select at least one Can.", mRoot);
            return false;
        }


        return true;
    }

    @Override
    public void onTimeSlotSelection(int time) {

        this.time = time;
    }


    @Override
    public void onCanSelected(int quantity) {
        this.quantity = quantity;


        mPrice.setText("" + (quantity * product.price));

    }

    @Override
    public void onSlotAvailable(boolean isSlotAvilable) {
        this.isSlotAvilable = isSlotAvilable;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        dialogHelper.hideProgressDialog();
        dialogHelper.showSnackBar("Error Placing order", mRoot);
    }

    @Override
    public void onResponse(JSONObject response) {
        dialogHelper.hideProgressDialog();

        SuccessDialogFragment mDialog = SuccessDialogFragment.newInstance();

        mDialog.show(getSupportFragmentManager(), "Success Dialog");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == RESULT_ADDRESS && data!=null) {
            mAddress = data.getParcelableExtra(INTENT_ADDRESS);
            updateAddress();

        }
    }

    void updateAddress(){

        TextView mName = (TextView) findViewById(R.id.text_address_name);
        TextView mtVAddress = (TextView) findViewById(R.id.text_address);

        mName.setText(mAddress.fullName);

        String address = "";

        if(mAddress.address !=null){
            address += mAddress.address +", ";
        }

        if(mAddress.line1 !=null){
            address += mAddress.line1+", ";
        }

        if(mAddress.line2 !=null){
            address += mAddress.line2+", ";
        }
        if(mAddress.landMark !=null){
            address += mAddress.landMark+", ";
        }

        address = address.substring(0,address.length()-2);

        mtVAddress.setText(address);
    }
}
