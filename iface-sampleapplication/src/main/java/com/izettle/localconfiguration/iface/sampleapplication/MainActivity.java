package com.izettle.localconfiguration.iface.sampleapplication;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.izettle.localconfiguration.iface.sampleapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(activityMainBinding.toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ConfigurationIface configurationIface = new ConfigurationBuilder().create(this, ConfigurationIface.class);

        configurationIface.welcomeTitle();

    }
}
