package com.neeru.client.adapter.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;

import com.neeru.client.R;
import com.neeru.client.fragment.OverViewFragment;
import com.neeru.client.fragment.ProductFragment;
import com.neeru.client.fragment.ReviewFragment;
import com.neeru.client.models.Location;
import com.neeru.client.models.Product;

/**
 * Created by brajendra on 22/07/17.
 */

public class ProductDetailPagerAdapter extends FragmentPagerAdapter {

    private final Context context;
    private final Product product;
    private final int locationID;



    public ProductDetailPagerAdapter(FragmentManager fm, Context context, Product product, int locationID) {
        super(fm);
        this.context = context;
        this.product = product;
        this.locationID = locationID;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return OverViewFragment.newInstance(product,locationID);
            case 1:
                return ReviewFragment.newInstance(product.id);
        }

        return null;

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.text_overview);
            case 1:
                return context.getString(R.string.text_reviews);
        }
        return null;
    }
}
