package com.izettle.wrench;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class WrenchApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }
}
