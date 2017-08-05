package com.neeru.client.fragment;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neeru.client.FinalOrderActivity;
import com.neeru.client.R;
import com.neeru.client.models.Product;

import static com.neeru.client.LocationActivity.INTENT_EXTRA_LOCATION;
import static com.neeru.client.fragment.ProductFragment.INTENT_EXTRA_PRODUCT;


public class OverViewFragment extends Fragment implements View.OnClickListener {


    private View mRoot;
    private Product product;
    private int locationID;


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
        mRoot = inflater.inflate(R.layout.fragment_overview, container, false);


        mRoot.findViewById(R.id.button).setOnClickListener(this);
        return mRoot;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), FinalOrderActivity.class);
        intent.putExtra(INTENT_EXTRA_LOCATION, locationID);
        intent.putExtra(INTENT_EXTRA_PRODUCT, product);

        getActivity().startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());


    }
}
