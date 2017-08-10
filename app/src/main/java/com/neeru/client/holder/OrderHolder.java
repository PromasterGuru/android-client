package com.neeru.client.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.neeru.client.R;
import com.neeru.client.views.ButtonView;

/**
 * Created by brajendra on 07/08/17.
 */

public class OrderHolder extends RecyclerView.ViewHolder {


    public final TextView mOrderId;
    public final TextView mOrderDate;
    public final TextView mOrderTotal;
    public final TextView mOrderProductName;
    public final ButtonView mOrderRepeat;
    public final TextView mOrderStatus;
    public final ImageView mImage;


    public OrderHolder(View itemView) {
        super(itemView);

        mImage = (ImageView) itemView.findViewById(R.id.imageView_order);
        mOrderId = (TextView) itemView.findViewById(R.id.text_order_id);
        mOrderDate = (TextView) itemView.findViewById(R.id.text_order_time);
        mOrderTotal = (TextView) itemView.findViewById(R.id.text_order_price);
        mOrderProductName = (TextView) itemView.findViewById(R.id.text_order_product_name);
        mOrderStatus = (TextView) itemView.findViewById(R.id.text_order_status);
        mOrderRepeat = (ButtonView) itemView.findViewById(R.id.text_order_repeat);
    }
}
