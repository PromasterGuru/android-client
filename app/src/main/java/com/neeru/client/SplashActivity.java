package com.neeru.client;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.neeru.client.fragment.SuccessDialogFragment;
import com.neeru.client.prefs.AuthPreference;

public class SplashActivity extends BaseActivity implements Animation.AnimationListener {

    private TextView tvName;
    private Animation animSlide;

    private final int REQUEST_SMS_PERMISSION = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvName = (TextView) findViewById(R.id.textView_name);

        animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);


        animSlide.setAnimationListener(this);
        tvName.startAnimation(animSlide);



    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (animation == animSlide) {
            startSplash();
        }
    }


    void startSplash() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)) {
                    verifyStoragePermissions();
                } else {
                    launch();
                }


            }
        }, 1000);
    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    void launch() {

        AuthPreference mPrefs = new AuthPreference(getApplicationContext());

        if (mPrefs.getAccessTocken() == null) {
            Intent intent = new Intent(SplashActivity.this, RegisterationActivity.class);
            startActivity(intent);

        } else {
            Intent intent = new Intent(SplashActivity.this, LocationActivity.class);
            startActivity(intent);
        }

        finish();

    }


    public void verifyStoragePermissions() {


        boolean needsRead = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED;

        boolean needsWrite = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED;
        String[] PERMISSIONS_STORAGE = {
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS,
        };

        if (needsRead || needsWrite) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    SplashActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_SMS_PERMISSION
            );

        } else {
            launch();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        boolean needsRead = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED;

        boolean needsWrite = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED;


        switch (requestCode) {
            case REQUEST_SMS_PERMISSION:
                if (!needsRead && !needsWrite) {
                    launch();
                } else {
                    finish();
                }
                break;

        }

    }
}
