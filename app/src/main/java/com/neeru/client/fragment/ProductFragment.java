package com.neeru.client.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.neeru.client.ProductDetailActivity;
import com.neeru.client.R;
import com.neeru.client.adapter.ProductAdapter;
import com.neeru.client.callbacks.OnViewItemClick;
import com.neeru.client.holder.ProductHolder;
import com.neeru.client.models.Product;
import com.neeru.client.network.GsonRequest;
import com.neeru.client.network.NetworkHandler;
import com.neeru.client.util.Constants;
import com.neeru.client.views.EmptyRecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.neeru.client.LocationActivity.INTENT_EXTRA_LOCATION;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment implements OnViewItemClick, Response.Listener<Product[]>, Response.ErrorListener {

    public static String TAG = "ProductFragment";
    public static String LOCATION_ID = "location_id";
    public static String INTENT_EXTRA_PRODUCT = "intent_product";
    private View mRoot;
    private EmptyRecyclerView mRecyclerView;
    private List<Product> items;
    private ProductAdapter mAdapter;
    private int locationID;


    public ProductFragment() {
        // Required empty public constructor
    }


    public static ProductFragment newInstance(int locationID) {
        ProductFragment fragment = new ProductFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(LOCATION_ID, locationID);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationID = getArguments().getInt(LOCATION_ID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mRoot = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        mRecyclerView = (EmptyRecyclerView) mRoot.findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        init();
        return mRoot;
    }


    void init() {


        String url = url = Constants.URL + "inventory/v1/product?locationId=" + locationID;


        Type tc = new TypeToken<ArrayList<Product>>() {
        }.getType();

        GsonRequest<Product[]> request = new GsonRequest(url, Product[].class, null, this, this);
        NetworkHandler.getInstance(getActivity()).addToRequestQueue(request);
    }

    @Override
    public void onItemClick(int position) {


    }

    @Override
    public void onTransitionView(int position, RecyclerView.ViewHolder viewHolder) {

        ProductHolder holder = (ProductHolder) viewHolder;

        Product product = items.get(position);


        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra(INTENT_EXTRA_PRODUCT, product);
        intent.putExtra(INTENT_EXTRA_LOCATION,locationID);

        Pair<View, String> p1 = Pair.create((View) holder.ivCover, getActivity().getString(R.string.transition_cover));
        Pair<View, String> p2 = Pair.create((View) holder.mContainer, getActivity().getString(R.string.transition_container));


        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), p1, p2);
        getActivity().startActivity(intent, options.toBundle());
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.v("Error--------", new String(error.networkResponse.data));
    }

    @Override
    public void onResponse(Product[] response) {

        items = Arrays.asList(response);

        mAdapter = new ProductAdapter(getActivity(), items, this);
        mRecyclerView.setAdapter(mAdapter);

    }
}
