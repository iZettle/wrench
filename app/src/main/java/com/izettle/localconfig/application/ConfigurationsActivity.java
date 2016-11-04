package com.izettle.localconfig.application;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.izettle.localconfig.application.databinding.ActivityConfigurationsBinding;
import com.izettle.localconfig.application.library.Application;


public class ConfigurationsActivity extends AppCompatActivity {

    private static final String EXTRA_APPLICATION = "EXTRA_APPLICATION";

    public static Intent newIntent(Context context, Application application) {
        Intent intent = new Intent(context, ConfigurationsActivity.class);
        intent.putExtra(EXTRA_APPLICATION, application);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityConfigurationsBinding activityConfigurationsBinding = DataBindingUtil.setContentView(this, com.izettle.localconfig.application.R.layout.activity_configurations);

        Application application = getIntent().getExtras().getParcelable(EXTRA_APPLICATION);

        setSupportActionBar(activityConfigurationsBinding.toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(activityConfigurationsBinding.container.getId(), ConfigurationsFragment.newInstance(application))
                    .commit();
        }

    }
}
