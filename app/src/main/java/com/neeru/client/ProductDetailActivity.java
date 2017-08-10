package com.neeru.client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.neeru.client.adapter.pager.ProductDetailPagerAdapter;
import com.neeru.client.callbacks.OnReviewLoad;
import com.neeru.client.models.Location;
import com.neeru.client.models.Product;
import com.neeru.client.network.NetworkHandler;
import com.neeru.client.util.State;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;


import static com.neeru.client.LocationActivity.INTENT_EXTRA_LOCATION;
import static com.neeru.client.fragment.ProductFragment.INTENT_EXTRA_PRODUCT;


public class ProductDetailActivity extends AppCompatActivity implements OnReviewLoad {


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ProductDetailPagerAdapter mAdapter;
    private Product product;
    DecimalFormat twoDForm = new DecimalFormat("#.##");
    private TextView mReviewCount;
    private int locationID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        product = getIntent().getParcelableExtra(INTENT_EXTRA_PRODUCT);
        locationID = getIntent().getIntExtra(INTENT_EXTRA_LOCATION, -1);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);


        mTabLayout.setupWithViewPager(mViewPager);

        mAdapter = new ProductDetailPagerAdapter(getSupportFragmentManager(), this, product, locationID);

        mViewPager.setAdapter(mAdapter);

        initHeader();

    }

    void initHeader() {
        ImageView ivCover = (ImageView) findViewById(R.id.ivCover);
        TextView mTvTitle = (TextView) findViewById(R.id.tvTitle);
        TextView mPrice = (TextView) findViewById(R.id.textView_price);
        mReviewCount = (TextView) findViewById(R.id.text_review_count);


        RatingBar mRating = (RatingBar) findViewById(R.id.product_rating);
        mRating.setRating(Float.valueOf(twoDForm.format(product.reviews.rating)));
        Picasso.with(getApplicationContext()).load(product.avatar).error(R.drawable.water_can).into(ivCover);
        mTvTitle.setText(product.name);
        mPrice.setText("" + product.price);


    }


    @Override
    public void onLoad(int count) {
        mReviewCount.setText("(" + count + ")");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
