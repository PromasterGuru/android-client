package com.neeru.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.neeru.client.models.Address;

public class SelectAddressActivity extends AppCompatActivity implements View.OnClickListener {
    public static String INTENT_ADDRESS = "intent_address";
    private View mAddressView;
    private EditText mName;
    private EditText mAddress;
    private EditText mAddress1;
    private EditText mLandmark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);


        mName = (EditText) findViewById(R.id.input_name);
        mAddress = (EditText) findViewById(R.id.input_flat);
        mAddress1 = (EditText) findViewById(R.id.input_colony);
        mLandmark = (EditText) findViewById(R.id.input_landmark);

        findViewById(R.id.text_add_address).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);

        mAddressView = findViewById(R.id.layout_address);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.text_add_address:
                mAddressView.setVisibility(View.VISIBLE);
                break;
            case R.id.save:

                Address address = new Address();
                address.fullName = mName.getEditableText().toString();
                address.address = mAddress.getEditableText().toString();
                address.line1 = mAddress1.getEditableText().toString();
                address.landMark = mLandmark.getEditableText().toString();

                Intent intent = new Intent();
                intent.putExtra(INTENT_ADDRESS, address);

                setResult(RESULT_OK, intent);
                finish();

                break;
        }
    }
}
