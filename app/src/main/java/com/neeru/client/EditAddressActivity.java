package com.neeru.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.neeru.client.models.Address;
import com.neeru.client.network.ApiStatus;
import com.neeru.client.network.NetworkHandler;
import com.neeru.client.network.NetworkHelper;
import com.neeru.client.network.PostListener;
import com.neeru.client.prefs.AuthPreference;
import com.neeru.client.util.DialogHelper;
import com.neeru.client.views.ButtonView;

import okhttp3.Request;
import retrofit2.Response;

import static com.neeru.client.SelectAddressActivity.INTENT_ADDRESS;
import static com.neeru.client.SelectAddressActivity.INTENT_POSITION;

public class EditAddressActivity extends AppCompatActivity implements View.OnClickListener, PostListener {

    private Address address;
    private EditText mName;
    private EditText mAddress;
    private EditText mAddress1;
    private EditText mLandmark;

    DialogHelper dialogHelper = new DialogHelper();
    private View mRoot;
    private int position;
    private Address mBodyAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.text_add_address).setVisibility(View.GONE);
        mRoot = findViewById(R.id.root_view);

        address = getIntent().getParcelableExtra(INTENT_ADDRESS);
        position = getIntent().getIntExtra(INTENT_POSITION, -1);

        mName = (EditText) findViewById(R.id.input_name);
        mAddress = (EditText) findViewById(R.id.input_flat);
        mAddress1 = (EditText) findViewById(R.id.input_colony);
        mLandmark = (EditText) findViewById(R.id.input_landmark);

        findViewById(R.id.layout_address).setVisibility(View.VISIBLE);


        mName.setText(address.fullName);
        mAddress.setText(address.address);
        mAddress1.setText(address.addressLine1);
        mLandmark.setText(address.landmark);

        ButtonView btnSave = (ButtonView) findViewById(R.id.save);
        btnSave.setOnClickListener(this);
        btnSave.setText("Update");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:

                mBodyAddress = new Address();
                mBodyAddress.fullName = mName.getEditableText().toString();
                mBodyAddress.address = mAddress.getEditableText().toString();
                mBodyAddress.addressLine1 = mAddress1.getEditableText().toString();
                mBodyAddress.landmark = mLandmark.getEditableText().toString();


                NetworkHelper networkHandler = new NetworkHelper(this);
                networkHandler.updateAddress(new AuthPreference(getApplicationContext()).getAccessTocken(), this, mBodyAddress, this.address.id);


                break;
        }
    }

    @Override
    public void onResult(Integer position,Request request, Response response, boolean isError, Throwable throwable, ApiStatus status) {
        if (isError) {
            dialogHelper.showSnackBar("Something went wrong. Please try after some time.", mRoot);
        } else if (!response.isSuccessful()) {
            dialogHelper.showSnackBar("Unable to process this request. Please try after some time.", mRoot);
        } else {
            Intent intent = new Intent();
            intent.putExtra(INTENT_ADDRESS, mBodyAddress);
            intent.putExtra(INTENT_POSITION, this.position);

            setResult(RESULT_OK, intent);
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
