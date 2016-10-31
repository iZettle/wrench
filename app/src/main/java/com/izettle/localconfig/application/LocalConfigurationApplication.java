package com.izettle.localconfig.application;

import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;

public class LocalConfigurationApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
