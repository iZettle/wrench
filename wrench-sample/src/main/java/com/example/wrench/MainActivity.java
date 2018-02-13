package com.example.wrench;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.example.wrench.databinding.ActivityMainBinding;
import com.example.wrench.livedataprefs.LiveDataPreferencesFragment;
import com.example.wrench.wrenchprefs.WrenchPreferencesFragment;
import com.izettle.wrench.preferences.WrenchPreferences;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    WrenchPreferences wrenchPreferences;

    private ActivityMainBinding activityMainBinding;

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(activityMainBinding.toolbar);

        WrenchSampleViewModel wrenchSampleViewModel = ViewModelProviders.of(this, viewModelFactory).get(WrenchSampleViewModel.class);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(activityMainBinding.container.getId(), LiveDataPreferencesFragment.newInstance())
                .commit();

        activityMainBinding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_live_data: {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(activityMainBinding.container.getId(), LiveDataPreferencesFragment.newInstance())
                            .commit();
                    return true;
                }
                case R.id.nav_wrench_prefs: {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(activityMainBinding.container.getId(), WrenchPreferencesFragment.newInstance())
                            .commit();
                    return true;
                }
                default: {
                    throw new IllegalStateException("Unknown id");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @SuppressWarnings("unused")
    public enum MyEnum {
        FIRST, SECOND, THIRD
    }
}
