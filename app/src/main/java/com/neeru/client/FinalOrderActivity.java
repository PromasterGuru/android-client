package com.neeru.client;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.neeru.client.callbacks.OrderActionListener;
import com.neeru.client.util.Util;
import com.neeru.client.views.CansHorizontalView;
import com.neeru.client.views.TimeSlotHorizontalView;

import java.util.Calendar;

import static com.neeru.client.LocationActivity.INTENT_EXTRA_LOCATION;
import static com.neeru.client.fragment.ProductFragment.INTENT_EXTRA_PRODUCT;

public class FinalOrderActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, OrderActionListener {


    public static final String INTENT_EXTRA_SELLER = "intent_seller";
    private EditText mEditTextDate;


    private Calendar calender;


    private int productId;
    private int sellerId;
    private int locationId;

    private int time;
    private int quantity;
    private TimeSlotHorizontalView mTimeSlot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationId = getIntent().getIntExtra(INTENT_EXTRA_LOCATION, -1);
        sellerId = getIntent().getIntExtra(INTENT_EXTRA_SELLER, -1);
        productId = getIntent().getIntExtra(INTENT_EXTRA_PRODUCT, -1);


        setContentView(R.layout.activity_final_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);


        getWindow().setEnterTransition(buildEnterTransition());


        mEditTextDate = (EditText) findViewById(R.id.editText_date);
        mEditTextDate.setOnClickListener(this);
        calender = Calendar.getInstance();


        mEditTextDate.setText(Util.formatDate(calender));

        findViewById(R.id.payment).setOnClickListener(this);
        mTimeSlot = (TimeSlotHorizontalView) findViewById(R.id.timeslotView);
        mTimeSlot.setCalender(calender);

        CansHorizontalView mQuantityView = (CansHorizontalView) findViewById(R.id.quantityView);

        mTimeSlot.setActionListener(this);
        mQuantityView.setActionListener(this);

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
                payment();
                break;

        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calender.set(year, month, dayOfMonth);

        mEditTextDate.setText(Util.formatDate(calender));


    }


    void payment() {


    }

    @Override
    public void onTimeSlotSelection(String time) {

    }

    @Override
    public void onCanSelected(String time) {

    }
}
