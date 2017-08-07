package com.neeru.client.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.neeru.client.R;
import com.neeru.client.holder.OrderHolder;
import com.neeru.client.holder.ProductHolder;
import com.neeru.client.models.Order;

import java.util.List;

/**
 * Created by brajendra on 07/08/17.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderHolder> {


    private final Activity activity;
    private final List<Order> list;


    public OrderAdapter(Activity activity, List<Order> list) {

        this.activity = activity;
        this.list = list;
    }


    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order, parent, false));
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 50;
    }
}
