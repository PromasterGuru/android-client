package com.neeru.client.fragment;


import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.transition.Transition;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.neeru.client.R;
import com.neeru.client.anim.BounceInterpolator;


public class SuccessDialogFragment extends DialogFragment {


    private View mRoot;
    private View mCard;
    private View mOK;


    public SuccessDialogFragment() {
        // Required empty public constructor
    }

    public static SuccessDialogFragment newInstance() {

        SuccessDialogFragment fragment = new SuccessDialogFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        mRoot = inflater.inflate(R.layout.fragment_dialog, container, false);


        mCard = mRoot.findViewById(R.id.id_card);
        mOK = mRoot.findViewById(R.id.layout_ok);


        mCard.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
        mCard.setMinimumHeight((int) (displayRectangle.height() * 0.5f));


        return mRoot;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;


        final Animation myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);


        mOK.startAnimation(myAnim);

    }


}
