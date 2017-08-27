package com.neeru.client;

import android.content.Intent;
import android.net.LinkAddress;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.neeru.client.models.Address;
import com.neeru.client.network.ApiListener;
import com.neeru.client.network.ApiStatus;
import com.neeru.client.network.NetworkHelper;
import com.neeru.client.network.PostListener;
import com.neeru.client.prefs.AuthPreference;
import com.neeru.client.util.DialogHelper;
import com.neeru.client.views.ButtonView;

import java.util.List;

import okhttp3.Request;
import retrofit2.Response;

public class SelectAddressActivity extends AppCompatActivity implements View.OnClickListener, ApiListener, PostListener {
    public static String INTENT_ADDRESS = "intent_address";
    public static String INTENT_POSITION = "intent_position";
    public static int RESULT_ADDRESS = 1;
    private View mAddressView;
    private EditText mName;
    private EditText mAddress;
    private EditText mAddress1;
    private EditText mLandmark;
    private NestedScrollView mContent;
    private View mProgress;

    List<Address> addresses;

    DialogHelper dialogHelper = new DialogHelper();
    private View mRoot;
    private LinearLayout mAddressContainer;
    private NetworkHelper networkHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRoot = findViewById(R.id.root_view);
        mContent = (NestedScrollView) findViewById(R.id.content);
        mProgress = findViewById(R.id.progressBar);

        mName = (EditText) findViewById(R.id.input_name);
        mAddress = (EditText) findViewById(R.id.input_flat);
        mAddress1 = (EditText) findViewById(R.id.input_colony);
        mLandmark = (EditText) findViewById(R.id.input_landmark);

        findViewById(R.id.text_add_address).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);

        mAddressView = findViewById(R.id.layout_address);

        mAddressContainer = (LinearLayout) findViewById(R.id.address_container);


        dialogHelper.showProgress(mProgress, mContent, true);

        networkHelper = new NetworkHelper(this, this);
        networkHelper.getAddress(new AuthPreference(getApplicationContext()).getAccessTocken(), this);

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

        switch (v.getId()) {
            case R.id.text_add_address:
                mAddressView.setVisibility(View.VISIBLE);
                hideButtonViews();
                mContent.post(new Runnable() {
                    @Override
                    public void run() {
                        mContent.scrollTo(0, mContent.getBottom());
                    }
                });


                break;
            case R.id.save:

                Address address = new Address();
                address.fullName = mName.getEditableText().toString();
                address.address = mAddress.getEditableText().toString();
                address.addressLine1 = mAddress1.getEditableText().toString();
                address.landmark = mLandmark.getEditableText().toString();

                networkHelper.createAddress(new AuthPreference(getApplicationContext()).getAccessTocken(), this, address);


                break;
            case R.id.address_layout:
                View view = (View) v.getParent();
                View card = (View) view.getParent();
                final View frame = (View) card.getParent();


                RadioButton mRadioButton = (RadioButton) view.findViewById(R.id.radioButton);
                boolean isChecked = mRadioButton.isChecked();
                if (!isChecked) {
                    hideButtonViews();
                    view.findViewById(R.id.button_layout).setVisibility(View.VISIBLE);
                    mRadioButton.setChecked(true);
                }


                mContent.post(new Runnable() {
                    @Override
                    public void run() {
                        mContent.scrollTo(0, frame.getTop());
                    }
                });
                mAddressView.setVisibility(View.GONE);

                break;
            case R.id.button_edit:
                int pos = (int) v.getTag();
                address = addresses.get(pos);


                Intent intent = new Intent(getApplicationContext(), EditAddressActivity.class);
                intent.putExtra(INTENT_ADDRESS, address);
                intent.putExtra(INTENT_POSITION, pos);
                startActivityForResult(intent, RESULT_ADDRESS);


                break;
            case R.id.button_delete:
                pos = (int) v.getTag();
                address = addresses.get(pos);

                dialogHelper.showProgressDialog(this, "Deleting");

                networkHelper.deleteAddress(pos, new AuthPreference(getApplicationContext()).getAccessTocken(), this, address.id);

                break;
            case R.id.button_deliver:
                pos = (int) v.getTag();
                address = addresses.get(pos);
                returnData(address);
                break;
        }
    }


    @Override
    public void onResult(Request request, Response response) {
        dialogHelper.showProgress(mProgress, mContent, false);
        if (response.isSuccessful()) {
            addresses = (List<Address>) response.body();
            addAddress();
        } else {
            dialogHelper.showSnackBar("Unable to get Address", mRoot);
        }

        showAddress();
    }

    @Override
    public void onFailure(Request request, Throwable t) {
        dialogHelper.showProgress(mProgress, mContent, false);
        dialogHelper.showSnackBar("Unable to get Address", mRoot);
        showAddress();
        dialogHelper.hideProgressDialog();
    }

    @Override
    public void onNetworkError(Throwable t) {
        dialogHelper.showProgress(mProgress, mContent, false);
        dialogHelper.showSnackBar("Network Error", mRoot);
        showAddress();

        dialogHelper.hideProgressDialog();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == RESULT_ADDRESS) {

            Address address = data.getParcelableExtra(INTENT_ADDRESS);
            int pos = data.getIntExtra(INTENT_POSITION, -1);


            Address mAddress = addresses.get(pos);
            mAddress.fullName = address.fullName;
            mAddress.address = address.address;
            mAddress.addressLine1 = address.addressLine1;
            mAddress.landmark = address.landmark;


            View view = mAddressContainer.getChildAt(pos);
            updateAddress(mAddress, pos, view);

        }

    }

    void addAddress() {
        mAddressContainer.removeAllViews();
        for (int i = 0; i < addresses.size(); i++) {
            Address address = addresses.get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.row_address, null, false);

            if (i == 0) {
                view.findViewById(R.id.button_layout).setVisibility(View.VISIBLE);
                ((RadioButton) view.findViewById(R.id.radioButton)).setChecked(true);
            }


            updateAddress(address, i, view);

            mAddressContainer.addView(view);

        }


    }


    void updateAddress(Address address, int position, View view) {

        view.setTag(position);

        TextView mTvAddressFullName = (TextView) view.findViewById(R.id.text_address_name);
        TextView mTvAddress = (TextView) view.findViewById(R.id.text_address);

        ButtonView btnEdit = (ButtonView) view.findViewById(R.id.button_edit);
        ButtonView btnDelete = (ButtonView) view.findViewById(R.id.button_delete);
        ButtonView btnDeliver = (ButtonView) view.findViewById(R.id.button_deliver);
        mTvAddressFullName.setText(address.fullName);


        String strAddress = address.address + "\n" + address.addressLine1;

        if (address.landmark != null && !address.landmark.isEmpty()) {
            strAddress += "(" + address.landmark + ")";
        }

        mTvAddress.setText(strAddress);

        btnDeliver.setTag(position);
        btnEdit.setTag(position);
        btnDelete.setTag(position);
        view.findViewById(R.id.address_layout).setTag(position);
        view.findViewById(R.id.address_layout).setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnDeliver.setOnClickListener(this);


    }


    void showAddress() {


        boolean isShow = false;

        if (addresses == null) {
            isShow = true;
        } else if (addresses.size() <= 0) {
            isShow = true;
        }

        if (isShow) {
            mAddressView.setVisibility(View.VISIBLE);
        }
    }


    void hideButtonViews() {

        for (int i = 0; i < mAddressContainer.getChildCount(); i++) {

            View view = mAddressContainer.getChildAt(i);

            view.findViewById(R.id.button_layout).setVisibility(View.GONE);
            RadioButton mRadioButton = (RadioButton) view.findViewById(R.id.radioButton);
            mRadioButton.setChecked(false);
        }
    }


    @Override
    public void onResult(Integer position, Request request, Response response, boolean isError, Throwable throwable, ApiStatus status) {

        if (isError) {
            dialogHelper.showSnackBar("Something went wrong. Please try after some time.", mRoot);
        } else if (!response.isSuccessful()) {
            dialogHelper.showSnackBar("Unable to process this request. Please try after some time.", mRoot);
        } else {
            if (status == ApiStatus.POST) {
                returnData((Address) response.body());
            } else if (status == ApiStatus.DELETE) {

                addresses.remove(position.intValue());

                addAddress();
                dialogHelper.hideProgressDialog();
            }

        }


    }


    void returnData(Address address) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_ADDRESS, address);

        setResult(RESULT_OK, intent);
        finish();
    }

}
