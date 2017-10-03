package com.neeru.client.fragment;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neeru.client.FinalOrderActivity;
import com.neeru.client.R;
import com.neeru.client.models.Product;

import static com.neeru.client.LocationActivity.INTENT_EXTRA_LOCATION;
import static com.neeru.client.fragment.ProductFragment.INTENT_EXTRA_PRODUCT;


public class OverViewFragment extends Fragment implements View.OnClickListener {



    private Product product;
    private int locationID;
    private View mRatingView;
    private ImageView mRating;
    private TextView mTextRating;



    public OverViewFragment() {
        // Required empty public constructor
    }


    public static OverViewFragment newInstance(Product product, int locationID) {
        OverViewFragment fragment = new OverViewFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(INTENT_EXTRA_PRODUCT, product);
        bundle.putInt(INTENT_EXTRA_LOCATION, locationID);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.product = getArguments().getParcelable(INTENT_EXTRA_PRODUCT);
            this.locationID = getArguments().getInt(INTENT_EXTRA_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View  rootView = inflater.inflate(R.layout.fragment_overview, container, false);



        mRating = (ImageView) rootView.findViewById(R.id.ivRating);
        mTextRating = (TextView) rootView.findViewById(R.id.textrating);
        mRatingView = rootView.findViewById(R.id.rating_view);
        mRatingView.setOnClickListener(this);

        rootView.findViewById(R.id.button).setOnClickListener(this);




        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rating_view:
                RatingFragment mDialog = RatingFragment.newInstance(product.id);
                mDialog.show(getChildFragmentManager(), "Review Dialog");
                break;
            case R.id.button:
                Intent intent = new Intent(getActivity(), FinalOrderActivity.class);
                intent.putExtra(INTENT_EXTRA_LOCATION, locationID);
                intent.putExtra(INTENT_EXTRA_PRODUCT, product);

                getActivity().startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                break;
        }


    }

    public void isReviewed() {
        mRatingView.setClickable(false);
        mRatingView.setEnabled(false);
        mRating.setImageResource(R.drawable.ic_star_border_grey);
        mTextRating.setTextColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
    }
}
