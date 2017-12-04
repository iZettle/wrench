package com.izettle.wrench.applicationlist;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.izettle.wrench.R;
import com.izettle.wrench.databinding.ActivityApplicationsBinding;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


public class ApplicationsActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityApplicationsBinding activityApplicationsBinding = DataBindingUtil.setContentView(this, R.layout.activity_applications);

        setSupportActionBar(activityApplicationsBinding.toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(activityApplicationsBinding.container.getId(), ApplicationsFragment.newInstance())
                    .commit();
        }
    }
}
