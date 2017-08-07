package com.neeru.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.neeru.client.adapter.OrderAdapter;
import com.neeru.client.views.EmptyRecyclerView;

public class OrderActivity extends AppCompatActivity {

    private EmptyRecyclerView mRecyclerView;
    private OrderAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recyclerview);

        mRecyclerView = (EmptyRecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(null);

        mAdapter= new OrderAdapter(this,null);
        mRecyclerView.setAdapter(mAdapter);
    }
}
