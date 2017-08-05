package com.neeru.client.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by brajendra on 11/07/17.
 */

public class EmptyRecyclerView extends RecyclerView {
    private View mEmptyView;


    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEmptyView(View view) {
        mEmptyView = view;
    }



    final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override public void onChanged() {
            super.onChanged();
            updateEmptyState();
        }
    };

    @Override public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }


    private void updateEmptyState() {
        if (mEmptyView != null) {
            final Adapter adapter = getAdapter();
            if (adapter != null) {
                final boolean emptyViewVisible = adapter.getItemCount() == 0;
                mEmptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.INVISIBLE);
                setVisibility(emptyViewVisible ? View.INVISIBLE : View.VISIBLE);
            } else {
                // EmptyView, but no adapter: Show empty view
                mEmptyView.setVisibility(View.VISIBLE);
                setVisibility(View.INVISIBLE);
            }
        } else {
            // No EmptyView: Show RecyclerView
            setVisibility(View.VISIBLE);
        }
    }
}
