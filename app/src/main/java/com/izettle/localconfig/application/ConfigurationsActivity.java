package com.izettle.localconfig.application;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.izettle.localconfig.application.databinding.ActivityConfigurationsBinding;


public class ConfigurationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityConfigurationsBinding activityConfigurationsBinding = DataBindingUtil.setContentView(this, com.izettle.localconfig.application.R.layout.activity_configurations);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(activityConfigurationsBinding.container.getId(), ConfigurationsFragment.newInstance())
                    .commit();
        }

    }


}
