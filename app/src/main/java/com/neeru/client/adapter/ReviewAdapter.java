package com.neeru.client.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.neeru.client.R;
import com.neeru.client.holder.ProductHolder;
import com.neeru.client.holder.ReviewHolder;
import com.neeru.client.models.Review;

import java.util.List;

/**
 * Created by brajendra on 04/08/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {


    private final List<Review> items;
    private final Context context;


    public ReviewAdapter(Context context, List<Review> items) {
        this.items = items;
        this.context = context;
    }


    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false));
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {

        Review review = items.get(position);

        holder.tvName.setText(review.firstName);
        holder.ratingBar.setRating(review.rating);
        holder.tvMessage.setText(review.feedback);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
