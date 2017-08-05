package com.neeru.client.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.neeru.client.R;

/**
 * Created by brajendra on 25/07/17.
 */

public class CircularView extends FrameLayout implements View.OnClickListener {
    private TextView mTextView;


    public CircularView(@NonNull Context context) {
        super(context);
        init(null, -1);
    }

    public CircularView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, -1);
    }

    public CircularView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {


        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circular_button_ripple_selector));

        View mView = LayoutInflater.from(getContext()).inflate(R.layout.circular_button_layout, null);


        mTextView = (TextView) mView.findViewById(R.id.textView);


        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Circular_View, defStyle, 0);


        String str = a.getString(R.styleable.Circular_View_text_name);

        mTextView.setText(str);


        addView(mView);


    }


    @Override
    public void onClick(View v) {

        String str = mTextView.getText().toString();

        switch (str) {
            case "1":
                v.setSelected(true);
                break;


        }

    }
}
