package com.neeru.client.callbacks;

import android.support.v7.widget.RecyclerView;

/**
 * Created by brajendra on 15/07/17.
 */

public interface OnViewItemClick {

    public void onItemClick(int position);
    public void onTransitionView(int position, RecyclerView.ViewHolder holder);
}
