package com.neeru.client;

import android.app.Application;

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
}
