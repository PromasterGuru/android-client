package com.neeru.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.neeru.client.fragment.ProductFragment;
import com.neeru.client.models.Location;
import com.neeru.client.models.User;
import com.neeru.client.network.RetrofitApiHelper;
import com.neeru.client.prefs.AuthPreference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.neeru.client.LocationActivity.INTENT_EXTRA_LOCATION;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private static final int REQUEST_EDIT = 1;
    private Location location;
    private DrawerLayout drawer;
    private View mProfileView;
    private CircleImageView ivProfile;
    private TextView tvUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);


        location = getIntent().getParcelableExtra(INTENT_EXTRA_LOCATION);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mProfileView = navigationView.getHeaderView(0);
        mProfileView.setOnClickListener(this);
        ivProfile = (CircleImageView) mProfileView.findViewById(R.id.drawer_header_profilePic);
        tvUserName = (TextView) mProfileView.findViewById(R.id.menu_item_self_name);
        ProductFragment fragment = ProductFragment.newInstance(location.id);
        loadFragment(fragment, ProductFragment.TAG);

        updateHeaderView();
    }


    void updateHeaderView() {
        AuthPreference authPref = new AuthPreference(getApplicationContext());


        User user = authPref.getUser();

        String name = "";

        if (user.firstName != null) {
            name += user.firstName;
        }
        if (user.lastName != null) {
            name += " " + user.lastName;
        }


        if (!TextUtils.isEmpty(name)) {
            tvUserName.setText(name);
        } else {
            tvUserName.setText("username");
        }


        if (user.avatar != null) {
            Picasso.with(getApplicationContext()).load(user.avatar).placeholder(R.drawable.ic_avatar_default).error(R.drawable.ic_avatar_default).into(ivProfile);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    void loadFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction ft = manager.beginTransaction();

        ft.replace(R.id.frame_content, fragment, tag);
        ft.commit();


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        switch (id) {
            case R.id.nav_dashboard:

                loadFragment(ProductFragment.newInstance(location.id), ProductFragment.TAG);
                break;
            case R.id.nav_product:

                loadFragment(ProductFragment.newInstance(location.id), ProductFragment.TAG);


                break;
            case R.id.nav_logout:


                Call<Type> call = RetrofitApiHelper.initApiClient().signOut();

                call.enqueue(new Callback<Type>() {
                    @Override
                    public void onResponse(Call<Type> call, Response<Type> response) {

                    }

                    @Override
                    public void onFailure(Call<Type> call, Throwable t) {

                    }
                });

                AuthPreference auth = new AuthPreference(this);
                auth.clear();
                Intent intent = new Intent(this, RegisterationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                this.finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.navigation_id:
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivityForResult(intent, REQUEST_EDIT);
                drawer.closeDrawers();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        updateHeaderView();

    }
}
