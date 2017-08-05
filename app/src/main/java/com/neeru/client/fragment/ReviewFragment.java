package com.neeru.client.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.neeru.client.R;
import com.neeru.client.adapter.ProductAdapter;
import com.neeru.client.adapter.ReviewAdapter;
import com.neeru.client.callbacks.OnReviewLoad;
import com.neeru.client.models.Product;
import com.neeru.client.models.Review;
import com.neeru.client.network.GsonRequest;
import com.neeru.client.network.NetworkHandler;
import com.neeru.client.util.Constants;
import com.neeru.client.views.EmptyRecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment implements Response.Listener<Review[]>, Response.ErrorListener {
    private static final String ARG_PARAM_PRODUCT_ID = "param_product_id";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int productID;
    private View mRoot;
    private RecyclerView mRecyclerView;
    private List<Review> items;
    private ReviewAdapter mAdapter;
    private OnReviewLoad listener;


    public ReviewFragment() {
        // Required empty public constructor
    }

    public static ReviewFragment newInstance(int productID) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_PRODUCT_ID, productID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (OnReviewLoad) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productID = getArguments().getInt(ARG_PARAM_PRODUCT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mRoot = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        mRecyclerView = (EmptyRecyclerView) mRoot.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        init();

        return mRoot;
    }


    void init() {


        String url = Constants.URL + "inventory/v1/review?productId=" + productID;


        Type tc = new TypeToken<ArrayList<Product>>() {
        }.getType();

        GsonRequest<Review[]> request = new GsonRequest(url, Review[].class, null, this, this);
        NetworkHandler.getInstance(getActivity()).addToRequestQueue(request);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.v("Error--------", new String(error.networkResponse.data));
    }

    @Override
    public void onResponse(Review[] response) {


        items = Arrays.asList(response);

        mAdapter = new ReviewAdapter(getActivity(), items);
        mRecyclerView.setAdapter(mAdapter);

        if (listener != null)
            listener.onLoad(items.size());

    }
}
