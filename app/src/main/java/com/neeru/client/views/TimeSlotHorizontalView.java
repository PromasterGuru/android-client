package com.neeru.client.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.neeru.client.R;
import com.neeru.client.callbacks.OrderActionListener;

import java.util.Calendar;

/**
 * Created by brajendra on 05/08/17.
 */

public class TimeSlotHorizontalView extends HorizontalScrollView implements View.OnClickListener {
    private View mView;
    private OrderActionListener listener;
    private Calendar calender;


    public TimeSlotHorizontalView(Context context) {
        super(context);
        init(context);
    }

    public TimeSlotHorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimeSlotHorizontalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    void init(Context context) {


        mView = LayoutInflater.from(getContext()).inflate(R.layout.horizontal_time_slot_list, null);


        mView.findViewById(R.id.time_68am).setOnClickListener(this);
        mView.findViewById(R.id.time_1012am).setOnClickListener(this);
        mView.findViewById(R.id.time_24pm).setOnClickListener(this);
        mView.findViewById(R.id.time_46pm).setOnClickListener(this);
        mView.findViewById(R.id.time_68pm).setOnClickListener(this);

        addView(mView);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.time_68am:
                clearSelectionTimeSlot();
                v.setSelected(true);
                break;
            case R.id.time_1012am:
                clearSelectionTimeSlot();
                v.setSelected(true);
                break;
            case R.id.time_24pm:
                clearSelectionTimeSlot();
                v.setSelected(true);
                break;
            case R.id.time_46pm:
                clearSelectionTimeSlot();
                v.setSelected(true);
                break;
            case R.id.time_68pm:
                clearSelectionTimeSlot();
                v.setSelected(true);
                break;
        }

    }


    void clearSelectionTimeSlot() {
        mView.findViewById(R.id.time_68am).setSelected(false);
        mView.findViewById(R.id.time_1012am).setSelected(false);
        mView.findViewById(R.id.time_24pm).setSelected(false);
        mView.findViewById(R.id.time_46pm).setSelected(false);
        mView.findViewById(R.id.time_68pm).setSelected(false);
    }

    public void setActionListener(OrderActionListener listener) {
        this.listener = listener;
    }

    public void setCalender(Calendar calender) {
        this.calender = calender;
        updateView();
    }


    void updateView() {
        if (calender == null)
            return;










    }
}
