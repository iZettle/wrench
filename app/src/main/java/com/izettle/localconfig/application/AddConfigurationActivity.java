package com.izettle.localconfig.application;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.izettle.localconfig.application.databinding.ActivityAddConfigurationBinding;


public class AddConfigurationActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, AddConfigurationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddConfigurationBinding activityAddConfigurationBinding = DataBindingUtil.setContentView(this, com.izettle.localconfig.application.R.layout.activity_add_configuration);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(activityAddConfigurationBinding.container.getId(), AddConfigurationFragment.newInstance())
                    .commit();
        }
    }
}
