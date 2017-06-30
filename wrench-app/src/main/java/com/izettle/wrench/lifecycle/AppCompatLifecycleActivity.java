package com.izettle.wrench.lifecycle;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.support.v7.app.AppCompatActivity;

public class AppCompatLifecycleActivity extends AppCompatActivity implements LifecycleRegistryOwner {
    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    public AppCompatLifecycleActivity() {
    }

    public LifecycleRegistry getLifecycle() {
        return this.mRegistry;
    }
}
