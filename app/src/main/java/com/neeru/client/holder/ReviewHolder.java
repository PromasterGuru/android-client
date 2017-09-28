package com.neeru.client.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.neeru.client.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by brajendra on 04/08/17.
 */

public class ReviewHolder extends RecyclerView.ViewHolder {
    public final RatingBar ratingBar;
    public final TextView tvName,tvMessage,timestamp;
    public final CircleImageView ivImage;

    public ReviewHolder(View convertView) {
        super(convertView);


        tvName = (TextView) convertView.findViewById(R.id.name);
        ivImage = (CircleImageView) convertView.findViewById(R.id.drawer_header_profilePic);
        ratingBar = (RatingBar) convertView.findViewById(R.id.review_rating);
        tvMessage = (TextView) convertView.findViewById(R.id.textMessage);
        timestamp=(TextView)convertView.findViewById(R.id.timestamp);
    }
}
