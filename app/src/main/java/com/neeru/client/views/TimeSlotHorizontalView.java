package com.neeru.client.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.neeru.client.R;
import com.neeru.client.callbacks.OrderActionListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by brajendra on 05/08/17.
 */

public class TimeSlotHorizontalView extends HorizontalScrollView implements View.OnClickListener {
    private View mView;
    private OrderActionListener listener;
    private Calendar calender;
    private TextView tvEmpty;


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


        tvEmpty = (TextView) mView.findViewById(R.id.time);


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

                if(listener !=null){
                    listener.onTimeSlotSelection(6);
                }
                break;
            case R.id.time_1012am:
                clearSelectionTimeSlot();
                v.setSelected(true);

                if(listener !=null){
                    listener.onTimeSlotSelection(10);
                }
                break;
            case R.id.time_24pm:
                clearSelectionTimeSlot();
                v.setSelected(true);

                if(listener !=null){
                    listener.onTimeSlotSelection(14);
                }
                break;
            case R.id.time_46pm:
                clearSelectionTimeSlot();
                v.setSelected(true);

                if(listener !=null){
                    listener.onTimeSlotSelection(16);
                }
                break;
            case R.id.time_68pm:
                clearSelectionTimeSlot();
                v.setSelected(true);
                if(listener !=null){
                    listener.onTimeSlotSelection(18);
                }
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


    public void updateView() {


        if (calender == null)
            return;

        Calendar nowDate = Calendar.getInstance();


        if (calender.after(nowDate)) {
            Log.v("AFTER", "AFTER");
            visible();
            listener.onSlotAvailable(true);
        } else {
            visible();
            int hour = calender.get(Calendar.HOUR_OF_DAY);
            remove(hour);
        }
    }


    void remove(int hour) {
        if (hour >= 10 && hour < 12) {
            mView.findViewById(R.id.time_68am).setVisibility(GONE);
            listener.onSlotAvailable(true);
        } else if (hour >= 12 && hour < 16) {
            mView.findViewById(R.id.time_68am).setVisibility(GONE);
            mView.findViewById(R.id.time_1012am).setVisibility(GONE);
            listener.onSlotAvailable(true);
        } else if (hour >= 16 && hour < 18) {
            mView.findViewById(R.id.time_68am).setVisibility(GONE);
            mView.findViewById(R.id.time_1012am).setVisibility(GONE);
            mView.findViewById(R.id.time_24pm).setVisibility(GONE);
            listener.onSlotAvailable(true);
        } else if (hour >= 18 && hour <= 20) {
            mView.findViewById(R.id.time_68am).setVisibility(GONE);
            mView.findViewById(R.id.time_1012am).setVisibility(GONE);
            mView.findViewById(R.id.time_24pm).setVisibility(GONE);
            mView.findViewById(R.id.time_46pm).setVisibility(GONE);
            listener.onSlotAvailable(true);
        } else if (hour > 20) {
            mView.findViewById(R.id.time_68am).setVisibility(GONE);
            mView.findViewById(R.id.time_1012am).setVisibility(GONE);
            mView.findViewById(R.id.time_24pm).setVisibility(GONE);
            mView.findViewById(R.id.time_46pm).setVisibility(GONE);
            mView.findViewById(R.id.time_68pm).setVisibility(GONE);

            tvEmpty.setVisibility(VISIBLE);
            listener.onSlotAvailable(false);



        }
    }





    void visible() {
        tvEmpty.setVisibility(GONE);
        mView.findViewById(R.id.time_68am).setVisibility(VISIBLE);
        mView.findViewById(R.id.time_1012am).setVisibility(VISIBLE);
        mView.findViewById(R.id.time_24pm).setVisibility(VISIBLE);
        mView.findViewById(R.id.time_46pm).setVisibility(VISIBLE);
        mView.findViewById(R.id.time_68pm).setVisibility(VISIBLE);
    }

}
