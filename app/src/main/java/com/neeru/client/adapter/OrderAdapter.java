package com.neeru.client.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.neeru.client.R;
import com.neeru.client.holder.OrderHolder;
import com.neeru.client.holder.ProductHolder;
import com.neeru.client.models.Order;
import com.neeru.client.util.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

        Order order = list.get(position);
        holder.mOrderId.setText("Order No " + order.id);
        holder.mOrderTotal.setText("" + order.totalPrice);
        holder.mOrderDate.setText(getDate(order.createdAt));

        holder.mOrderStatus.setText(order.status);

        holder.mOrderProductName.setText(order.items.get(0).product.name);

        Picasso.with(activity.getApplicationContext()).load(order.items.get(0).product.avatar).error(R.drawable.water_can).into(holder.mImage);




    }

    public String getDate(String date) {
        String finalDate = null;
        Constants.formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date mDate = Constants.formatter.parse(date);
            finalDate = Constants.formatterOrderDate.format(mDate);
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        return finalDate;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
