package com.example.wrench;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        WrenchPreferences wrenchPreferences = new WrenchPreferences(this);

        activityMainBinding.stringConfigurationTitle.setText(R.string.string_configuration);
        activityMainBinding.stringConfiguration.setText(wrenchPreferences.getString("String configuration", "welcome!"));

        activityMainBinding.urlConfigurationTitle.setText(R.string.url_configuration);
        activityMainBinding.urlConfiguration.setText(wrenchPreferences.getString("Url configuration (http://www.example.com/path?param=value)", "http://www.example.com/path?param=value"));

        activityMainBinding.booleanConfigurationTitle.setText(R.string.boolean_configuration);
        activityMainBinding.booleanConfiguration.setText(String.valueOf(wrenchPreferences.getBoolean("boolean configuration", false)));

        activityMainBinding.intConfigurationTitle.setText(R.string.int_configuration);
        activityMainBinding.intConfiguration.setText(String.valueOf(wrenchPreferences.getInt("int configuration", 2)));

        activityMainBinding.enumConfigurationTitle.setText(R.string.enum_configuration);

        MyEnum myEnum = wrenchPreferences.getEnum("enum configuration", MyEnum.class, MyEnum.FIRST);
        activityMainBinding.enumConfiguration.setText(String.valueOf(myEnum));

        activityMainBinding.serviceButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), WrenchService.class);
            intent.putExtra("service configuration", new Date().toString());
            startService(intent);
        });

        activityMainBinding.serviceConfiguration.setText(wrenchPreferences.getString("service configuration", null));
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
