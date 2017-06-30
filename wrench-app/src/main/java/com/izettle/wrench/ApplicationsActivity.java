package com.izettle.wrench;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.izettle.wrench.databinding.ActivityApplicationsBinding;
import com.izettle.wrench.lifecycle.AppCompatLifecycleActivity;


public class ApplicationsActivity extends AppCompatLifecycleActivity {

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
