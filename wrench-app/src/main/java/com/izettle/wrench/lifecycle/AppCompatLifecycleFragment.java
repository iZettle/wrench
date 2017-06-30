package com.izettle.wrench.lifecycle;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.support.v4.app.Fragment;

public class AppCompatLifecycleFragment extends Fragment implements LifecycleRegistryOwner {
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    public AppCompatLifecycleFragment() {
    }

    public LifecycleRegistry getLifecycle() {
        return this.mLifecycleRegistry;
    }
}