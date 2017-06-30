package com.izettle.wrench.lifecycle;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.support.v4.app.DialogFragment;

public class LifecycleDialogFragment extends DialogFragment implements LifecycleRegistryOwner {
    private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }
}