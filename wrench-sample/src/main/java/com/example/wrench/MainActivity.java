package com.example.wrench;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.example.wrench.databinding.ActivityMainBinding;
import com.izettle.wrench.preferences.WrenchPreferences;
import com.izettle.wrench.service.WrenchService;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(activityMainBinding.toolbar);

        WrenchSampleViewModel wrenchSampleViewModel = ViewModelProviders.of(this).get(WrenchSampleViewModel.class);

        WrenchPreferences wrenchPreferences = new WrenchPreferences(this);

        wrenchSampleViewModel.getStringBolt().observe(this, bolt -> {
            if (bolt != null) {
                activityMainBinding.stringConfiguration.setText(bolt.value);
            }
        });

        wrenchSampleViewModel.getUrlBolt().observe(this, bolt -> {
            if (bolt != null) {
                activityMainBinding.urlConfiguration.setText(bolt.value);
            }
        });

        wrenchSampleViewModel.getBooleanBolt().observe(this, bolt -> {
            if (bolt != null) {
                activityMainBinding.booleanConfiguration.setText(bolt.value);

            }
        });

        wrenchSampleViewModel.getIntBolt().observe(this, bolt -> {
            if (bolt != null) {
                activityMainBinding.intConfiguration.setText(bolt.value);
            }
        });

        wrenchPreferences.getEnum(getString(R.string.enum_configuration), MyEnum.class, MyEnum.FIRST);
        wrenchSampleViewModel.getBolt(getString(R.string.enum_configuration)).observe(this, bolt -> {
            if (bolt != null) {
                activityMainBinding.enumConfiguration.setText(bolt.value);
            }
        });

        wrenchPreferences.getString(getString(R.string.service_configuration), null);
        wrenchSampleViewModel.getBolt(getString(R.string.service_configuration)).observe(this, bolt -> {
            if (bolt != null) {
                activityMainBinding.serviceConfiguration.setText(bolt.value);
            }
        });

        activityMainBinding.serviceButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), WrenchService.class);
            intent.putExtra(getString(R.string.service_configuration), new Date().toString());
            startService(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @SuppressWarnings("unused")
    enum MyEnum {
        FIRST, SECOND, THIRD
    }
}
