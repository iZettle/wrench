package com.izettle.localconfig.sampleapplication;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.izettle.localconfig.sampleapplication.databinding.ActivityMainBinding;
import com.izettle.localconfiguration.LocalConfiguration;

import java.util.Map;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(activityMainBinding.toolbar);

        LocalConfiguration localConfiguration = new LocalConfiguration(this);

        // text that defaults to something
        activityMainBinding.log1key.setText("WELCOME_TITLE:");
        activityMainBinding.log1value.setText(localConfiguration.getString("WELCOME_TITLE", "welcome!"));

        // welcome text that can be null
        activityMainBinding.log2key.setText("WELCOME_TEXT:");
        activityMainBinding.log2value.setText(localConfiguration.getString("WELCOME_TEXT", null));

        // check if strict mode is enabled, default to false/DISABLED
        activityMainBinding.log3key.setText("STRICT_MODE:");
        activityMainBinding.log3value.setText(localConfiguration.getBoolean("STRICT_MODE", false) ? "ENABLED" : "DISABLED");

        // get num columns to show, defaults to 2
        activityMainBinding.log4key.setText("NUM_COLUMNS:");
        activityMainBinding.log4value.setText(String.valueOf(localConfiguration.getInt("NUM_COLUMNS", 2)));


        StringBuilder all = new StringBuilder("All keys and values\n");
        for (Map.Entry<String, ?> entry : localConfiguration.getAll().entrySet()) {
            if (entry.getValue() != null) {
                all.append(entry.getKey() + ": (" + entry.getValue().getClass().getSimpleName() + ")" + entry.getValue() + "\n");
            } else {
                all.append(entry.getKey() + ": (?) NULL \n");
            }
        }
        activityMainBinding.all.setText(all.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }
}
