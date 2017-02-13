package com.izettle.localconfig.application;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class LocalConfigurationApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
