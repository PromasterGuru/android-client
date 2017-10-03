package com.neeru.client;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.neeru.client.adapter.pager.ProductDetailPagerAdapter;
import com.neeru.client.callbacks.OnReviewLoad;
import com.neeru.client.fragment.OverViewFragment;
import com.neeru.client.fragment.ReviewFragment;
import com.neeru.client.models.Product;
import com.neeru.client.util.DialogHelper;
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
    DecimalFormat oneDForm = new DecimalFormat("#.#");
    private TextView mReviewCount;
    private int locationID;
    private View mRoot;
    private RatingBar mRating;
    private TextView mTextRating;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRoot = findViewById(R.id.root_view);

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
        mTextRating = (TextView) findViewById(R.id.textRating);
        mReviewCount = (TextView) findViewById(R.id.text_review_count);



        mRating = (RatingBar) findViewById(R.id.product_rating);
        mRating.setRating(Float.valueOf(twoDForm.format(product.reviews.rating)));
        Picasso.with(getApplicationContext()).load(product.avatar).error(R.drawable.water_can).into(ivCover);
        mTvTitle.setText(product.name);
        mPrice.setText("" + product.price);
        mTextRating.setText(oneDForm.format(product.reviews.rating));


    }


    @Override
    public void onLoad(int count, float avg) {
        product.reviews.rating = avg;
        mReviewCount.setText("(" + count + ")");
        mRating.setRating(Float.valueOf(twoDForm.format(avg)));
        mTextRating.setText(oneDForm.format(avg));
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


    @Override
    public void onReviewSubmission() {

        DialogHelper dialogHelper = new DialogHelper();
        dialogHelper.showSnackBar("Your review is submitted successfully", mRoot);

        ReviewFragment fragment = (ReviewFragment) getFragment(1);

        if (fragment != null) {
            fragment.init();
        }


    }

    @Override
    public void isReviewed() {
        OverViewFragment fragment = (OverViewFragment) getFragment(0);

        if(fragment !=null){
            fragment.isReviewed();
        }



    }

    private Fragment getFragment(int pos) {
        return getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + (pos));
    }


}
