package com.neeru.client.fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.neeru.client.R;
import com.neeru.client.callbacks.OnReviewLoad;
import com.neeru.client.models.Review;
import com.neeru.client.models.User;
import com.neeru.client.network.ApiStatus;
import com.neeru.client.network.NetworkHelper;
import com.neeru.client.network.PostListener;
import com.neeru.client.prefs.AuthPreference;
import com.neeru.client.util.DialogHelper;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Request;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends DialogFragment implements View.OnClickListener, PostListener {

    public static String ARG_PRODUCT_ID = "product_id";


    private View mRoot;
    private View mCard;
    private RatingBar mRating;
    private EditText mEditText;
    private TextView mSubmit;
    private DialogHelper dialogHelper;
    private NetworkHelper networkHelper;
    private int productId;
    private CircleImageView mProfile;
    private OnReviewLoad listener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.productId = getArguments().getInt(ARG_PRODUCT_ID);
        }
    }

    public static RatingFragment newInstance(int productId) {
        RatingFragment fragment = new RatingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PRODUCT_ID, productId);
        fragment.setArguments(bundle);
        return fragment;
    }


    public RatingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialogHelper = new DialogHelper();

        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mRoot = inflater.inflate(R.layout.fragment_rating, container, false);


        mCard = mRoot.findViewById(R.id.id_card);

        mRating = (RatingBar) mRoot.findViewById(R.id.product_rating);
        mEditText = (EditText) mRoot.findViewById(R.id.editText);
        mProfile = (CircleImageView) mRoot.findViewById(R.id.mProfile);

        mCard.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
        mCard.setMinimumHeight((int) (displayRectangle.height() * 0.5f));

        mSubmit = (TextView) mRoot.findViewById(R.id.mSubmit);

        mRoot.findViewById(R.id.mCancel).setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        networkHelper = new NetworkHelper(this);

        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                enableDisableSubmitButton();
            }
        });


        User mUser = new AuthPreference(getActivity()).getUser();

        if (mUser.avatar != null) {
            Picasso.with(getActivity()).load(mUser.avatar).placeholder(R.drawable.ic_avatar_default).error(R.drawable.ic_avatar_default).into(mProfile);
        }


        enableDisableSubmitButton();
        return mRoot;
        // Inflate the layout for this fragment

    }

    void enableDisableSubmitButton() {
        float rating = mRating.getRating();


        if (rating > 0) {
            mSubmit.setClickable(true);
            mSubmit.setEnabled(true);
            mSubmit.setTextColor(Color.parseColor("#3f79d7"));
        } else {
            mSubmit.setTextColor(Color.parseColor("#eaebed"));
            mSubmit.setClickable(false);
            mSubmit.setEnabled(false);
        }
    }


    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mCancel:
                getDialog().dismiss();
                break;
            case R.id.mSubmit:

                Review review = new Review();
                review.rating = (int) mRating.getRating();
                review.productId = productId;
                review.feedback = mEditText.getText().toString();

                dialogHelper.showProgressDialog(getActivity(), "");

                networkHelper.createReview(new AuthPreference(getActivity()).getAccessTocken(), review);
                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (OnReviewLoad) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onViewSelected");
        }
    }

    @Override
    public void onResult(Integer position, Request request, Response response, boolean isError, Throwable throwable, ApiStatus status) {

        dialogHelper.hideProgressDialog();


        if (isError) {
            dialogHelper.showSnackBar("Error in review submission!", mRoot);
        } else if (!response.isSuccessful()) {
            dialogHelper.showSnackBar("Error in review submission!", mRoot);
        } else {
            dismiss();
            listener.onReviewSubmission();

        }
    }
}
