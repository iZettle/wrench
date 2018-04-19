package com.example.wrench;

import android.content.Context;

import com.example.wrench.di.AppInjector;
import com.example.wrench.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class SampleApplication extends DaggerApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        AppInjector.init(this);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
