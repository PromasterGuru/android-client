package com.neeru.client;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;

import com.google.firebase.FirebaseApp;

/**
 * Created by brajendra on 06/08/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());
    }

    public static String getDeviseID(Context context) {
        return Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}
