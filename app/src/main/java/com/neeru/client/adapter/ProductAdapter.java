package com.neeru.client.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.neeru.client.R;
import com.neeru.client.callbacks.OnViewItemClick;
import com.neeru.client.holder.ProductHolder;
import com.neeru.client.models.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * Created by brajendra on 16/07/17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {


    private final Activity activity;
    private final List<Product> items;
    private final OnViewItemClick listener;
    DecimalFormat twoDForm = new DecimalFormat("#.##");

    public ProductAdapter(Activity activity, List<Product> items, OnViewItemClick listener) {
        this.activity = activity;
        this.items = items;
        this.listener = listener;


    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product, parent, false), listener);
    }


    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {

        Product product = items.get(position);

        holder.mClickableView.setTag(position);

        holder.mTvTitle.setText(product.name);
        holder.mPrice.setText("" + product.price);


        holder.mRating.setRating(Float.valueOf(twoDForm.format(product.reviews.rating)));

        Picasso.with(activity.getApplicationContext()).load(product.avatar).error(R.drawable.water_can).into(holder.ivCover);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
