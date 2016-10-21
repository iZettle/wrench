package se.eelde.localconfig;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import se.eelde.localconfig.databinding.ActivityEditConfigurationBinding;
import se.eelde.localconfiguration.library.Configuration;

public class EditConfigurationActivity extends AppCompatActivity {

    private static final String EXTRA_CONFIGURATION = "EXTRA_CONFIGURATION";

    public static Intent newIntent(Context context, Configuration configuration) {
        return new Intent(context, AddConfigurationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditConfigurationBinding activityEditConfigurationBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_configuration);

        Configuration configuration = getIntent().getExtras().getParcelable(EXTRA_CONFIGURATION);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(activityEditConfigurationBinding.container.getId(), EditConfigurationFragment.newInstance(configuration))
                    .commit();
        }
    }

}
