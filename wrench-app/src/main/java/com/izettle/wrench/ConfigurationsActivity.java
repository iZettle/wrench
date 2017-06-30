package com.izettle.wrench;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.izettle.wrench.database.WrenchScope;
import com.izettle.wrench.databinding.ActivityConfigurationsBinding;
import com.izettle.wrench.dialogs.scope.ScopeFragment;
import com.izettle.wrench.lifecycle.AppCompatLifecycleActivity;


public class ConfigurationsActivity extends AppCompatLifecycleActivity {

    private static final String EXTRA_APPLICATION_ID = "EXTRA_APPLICATION_ID";
    private ActivityConfigurationsBinding binding;

    public static Intent newIntent(Context context, long applicationId) {
        Intent intent = new Intent(context, ConfigurationsActivity.class);
        intent.putExtra(EXTRA_APPLICATION_ID, applicationId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_configurations);

        long applicationId = getIntent().getExtras().getLong(EXTRA_APPLICATION_ID);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ConfigurationViewModel configurationViewModel = ViewModelProviders.of(this).get(ConfigurationViewModel.class);

        configurationViewModel.setApplicationId(applicationId);

        configurationViewModel.getSelectedScopeLiveData().observe(this, wrenchScope -> {
            if (wrenchScope != null) {
                binding.addScope.setText(wrenchScope.getName());
            }
        });

        binding.addScope.setOnClickListener(view -> ScopeFragment.newInstance(applicationId).show(getSupportFragmentManager(), null));

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(binding.container.getId(), ConfigurationsFragment.newInstance())
                    .commit();
        }

        configurationViewModel.getScopes().observe(this, scopes -> {
            if (scopes != null) {
                if (scopes.size() == 0) {
                    AsyncTask.execute(() -> {
                        try {
                            configurationViewModel.createScope(WrenchScope.SCOPE_USER);
                        } catch (SQLiteConstraintException ignored) {
                        }
                    });
                }
            }
        });
    }
}
