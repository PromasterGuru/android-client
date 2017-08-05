package com.neeru.client.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.neeru.client.FinalOrderActivity;
import com.neeru.client.R;
import com.neeru.client.callbacks.OrderActionListener;

/**
 * Created by brajendra on 05/08/17.
 */

public class CansHorizontalView extends HorizontalScrollView implements View.OnClickListener {
    private View mView;
    private OrderActionListener listener;


    public CansHorizontalView(Context context) {
        super(context);
        init(context);
    }

    public CansHorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CansHorizontalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    void init(Context context) {


        mView = LayoutInflater.from(getContext()).inflate(R.layout.horizontal_can_list, null);

        mView.findViewById(R.id.can_1).setOnClickListener(this);
        mView.findViewById(R.id.can_2).setOnClickListener(this);
        mView.findViewById(R.id.can_3).setOnClickListener(this);
        mView.findViewById(R.id.can_4).setOnClickListener(this);
        mView.findViewById(R.id.can_5).setOnClickListener(this);
        mView.findViewById(R.id.can_6).setOnClickListener(this);
        mView.findViewById(R.id.can_7).setOnClickListener(this);
        mView.findViewById(R.id.can_8).setOnClickListener(this);
        mView.findViewById(R.id.can_9).setOnClickListener(this);
        mView.findViewById(R.id.can_10).setOnClickListener(this);

        addView(mView);

    }


    void clearSelectionCan(View v, int quality) {
        mView.findViewById(R.id.can_1).setSelected(false);
        mView.findViewById(R.id.can_2).setSelected(false);
        mView.findViewById(R.id.can_3).setSelected(false);
        mView.findViewById(R.id.can_4).setSelected(false);
        mView.findViewById(R.id.can_5).setSelected(false);
        mView.findViewById(R.id.can_6).setSelected(false);
        mView.findViewById(R.id.can_7).setSelected(false);
        mView.findViewById(R.id.can_8).setSelected(false);
        mView.findViewById(R.id.can_9).setSelected(false);
        mView.findViewById(R.id.can_10).setSelected(false);


        v.setSelected(true);

        if (listener != null) {
            listener.onCanSelected(quality);
        }


    }

    @Override
    public void onClick(View v) {

        int can = 0;
        switch (v.getId()) {
            case R.id.can_1:
                clearSelectionCan(v, 1);

                break;
            case R.id.can_2:
                clearSelectionCan(v, 2);

                break;
            case R.id.can_3:
                clearSelectionCan(v, 3);
                break;
            case R.id.can_4:
                clearSelectionCan(v, 4);
                break;
            case R.id.can_5:
                clearSelectionCan(v, 5);
                break;
            case R.id.can_6:
                clearSelectionCan(v, 6);
                break;
            case R.id.can_7:
                clearSelectionCan(v, 7);
                break;
            case R.id.can_8:
                clearSelectionCan(v, 8);
                break;
            case R.id.can_9:
                clearSelectionCan(v, 9);
                break;
            case R.id.can_10:
                clearSelectionCan(v, 10);
                break;
        }
    }

    public void setActionListener(OrderActionListener listener) {
        this.listener = listener;
    }
}
