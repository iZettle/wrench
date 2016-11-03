package com.izettle.localconfig.application;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.izettle.localconfig.application.databinding.ActivityApplicationsBinding;


public class ApplicationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityApplicationsBinding activityApplicationsBinding = DataBindingUtil.setContentView(this, R.layout.activity_applications);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(activityApplicationsBinding.container.getId(), ApplicationsFragment.newInstance())
                    .commit();
        }
    }
}
