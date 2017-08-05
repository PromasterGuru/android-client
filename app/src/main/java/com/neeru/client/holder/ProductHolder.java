package com.neeru.client.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.neeru.client.R;
import com.neeru.client.callbacks.OnViewItemClick;

/**
 * Created by brajendra on 16/07/17.
 */

public class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public final TextView mTvTitle;
    private final OnViewItemClick listener;
    public final CardView mClickableView;
    public final ImageView ivCover;
    public final View mContainer;
    public final TextView mPrice;
    public final RatingBar mRating;

    public ProductHolder(View itemView, OnViewItemClick listener) {
        super(itemView);
        mTvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        ivCover = (ImageView) itemView.findViewById(R.id.ivCover);
        mPrice = (TextView) itemView.findViewById(R.id.textView_price);
        mClickableView = (CardView) itemView.findViewById(R.id.card_view);
        mContainer = itemView.findViewById(R.id.container);
        mRating = (RatingBar) itemView.findViewById(R.id.product_rating);

        this.listener = listener;
        mClickableView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.card_view:
                if (listener != null) {
                    Integer pos = (Integer) mClickableView.getTag();
                    listener.onTransitionView(pos, this);
                }
                break;
        }
    }
}
