package com.neeru.client.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.Button;

import com.neeru.client.R;

/**
 * Created by brajendra on 23/07/17.
 */

public class ButtonView extends AppCompatButton {



    public ButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }

    public ButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context, attrs);

    }

    private void style(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ReaderFontTextView);
        int cf = a.getInteger(R.styleable.ReaderFontTextView_fontName, 0);
        String fontName;
        switch (cf) {
            case 1:
                fontName =  "Roboto_Regular";
                break;
            case 2:
                fontName =  "Roboto_Medium";
                break;
            case 3:
                fontName =  "Roboto_Bold";
                break;
            case 4:
                fontName =  "Roboto_Italic";
                break;
            case 5:
                fontName =  "Roboto_MediumItalic";
                break;
            case 6:
                fontName =  "Roboto_BoldItalic";
                break;
            default:
                fontName =  "Roboto_Regular";
                break;
        }



        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/" + fontName + ".ttf");
        setTypeface(tf);
        a.recycle();
    }


    public void setTextFont(String fontName){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/" + fontName + ".ttf");
        setTypeface(tf);
    }

}