package com.izettle.localconfig.sampleapplication;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import com.izettle.localconfig.sampleapplication.databinding.ActivityMainBinding;
import com.izettle.localconfiguration.LocalConfiguration;

import java.util.Date;
import java.util.Map;


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

        LocalConfiguration localConfiguration = new LocalConfiguration(this);

        activityMainBinding.stringConfigurationTitle.setText(R.string.string_configuration);
        activityMainBinding.stringConfiguration.setText(localConfiguration.getString("String configuration", "welcome!"));

        activityMainBinding.booleanConfigurationTitle.setText(R.string.boolean_configuration);
        activityMainBinding.booleanConfiguration.setText(String.valueOf(localConfiguration.getBoolean("boolean configuration", false)));

        activityMainBinding.intConfigurationTitle.setText(R.string.int_configuration);
        activityMainBinding.intConfiguration.setText(String.valueOf(localConfiguration.getInt("int configuration", 2)));

        activityMainBinding.enumConfigurationTitle.setText(R.string.enum_configuration);

        MyEnum myEnum = localConfiguration.getEnum("enum configuration", MyEnum.class, MyEnum.FIRST);
        activityMainBinding.enumConfiguration.setText(String.valueOf(myEnum));

        StringBuilder all = new StringBuilder("All keys and values\n");
        for (Map.Entry<String, ?> entry : localConfiguration.getAll().entrySet()) {
            if (entry.getValue() != null) {
                all.append(entry.getKey() + ": (" + entry.getValue().getClass().getSimpleName() + ")" + entry.getValue() + "\n");
            } else {
                all.append(entry.getKey() + ": (?) NULL \n");
            }
        }
        activityMainBinding.all.setText(all.toString());

        activityMainBinding.serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ConfigurationService.class);
                intent.putExtra("service configuration", new Date().toString());
                startService(intent);
            }
        });

        activityMainBinding.serviceConfiguration.setText(localConfiguration.getString("service configuration", null));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    enum MyEnum {
        FIRST, SECOND, THIRD
    }
}
