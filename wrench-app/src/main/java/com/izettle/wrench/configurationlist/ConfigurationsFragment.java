package com.izettle.wrench.configurationlist;

import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;

import com.izettle.wrench.R;
import com.izettle.wrench.core.Bolt;
import com.izettle.wrench.database.WrenchApplication;
import com.izettle.wrench.database.WrenchConfigurationWithValues;
import com.izettle.wrench.databinding.FragmentConfigurationsBinding;
import com.izettle.wrench.di.Injectable;
import com.izettle.wrench.dialogs.booleanvalue.BooleanValueFragment;
import com.izettle.wrench.dialogs.booleanvalue.BooleanValueFragmentArgs;
import com.izettle.wrench.dialogs.enumvalue.EnumValueFragment;
import com.izettle.wrench.dialogs.enumvalue.EnumValueFragmentArgs;
import com.izettle.wrench.dialogs.integervalue.IntegerValueFragment;
import com.izettle.wrench.dialogs.integervalue.IntegerValueFragmentArgs;
import com.izettle.wrench.dialogs.scope.ScopeFragment;
import com.izettle.wrench.dialogs.stringvalue.StringValueFragment;
import com.izettle.wrench.dialogs.stringvalue.StringValueFragmentArgs;

import java.util.List;

import javax.inject.Inject;

public class ConfigurationsFragment extends Fragment implements SearchView.OnQueryTextListener, ConfigurationRecyclerViewAdapter.Listener, Injectable {
    private static final String STATE_FILTER = "STATE_FILTER";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FragmentConfigurationsBinding fragmentConfigurationsBinding;
    private CharSequence currentFilter;
    private SearchView searchView;
    private ConfigurationViewModel model;


    public ConfigurationsFragment() {
    }

    public static ConfigurationsFragment newInstance() {
        ConfigurationsFragment fragment = new ConfigurationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void updateConfigurations(List<WrenchConfigurationWithValues> wrenchConfigurations) {
        ConfigurationRecyclerViewAdapter adapter = (ConfigurationRecyclerViewAdapter) fragmentConfigurationsBinding.list.getAdapter();
        if (adapter == null) {
            adapter = new ConfigurationRecyclerViewAdapter(this, model);

            fragmentConfigurationsBinding.list.setAdapter(adapter);
        }
        adapter.setItems(wrenchConfigurations);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(STATE_FILTER, searchView.getQuery());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            currentFilter = savedInstanceState.getCharSequence(STATE_FILTER);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentConfigurationsBinding = FragmentConfigurationsBinding.inflate(inflater, container, false);

        return fragmentConfigurationsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;

        model = ViewModelProviders.of(this, viewModelFactory).get(ConfigurationViewModel.class);
        ConfigurationsFragmentArgs args = ConfigurationsFragmentArgs.fromBundle(getArguments());
        model.setApplicationId(args.getApplicationId());

        fragmentConfigurationsBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));

        model.getWrenchApplication().observe(this, wrenchApplication -> {
            if (wrenchApplication != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(wrenchApplication.applicationLabel());
            }
        });

        model.getDefaultScopeLiveData().observe(this, scope -> {
            if (scope != null && fragmentConfigurationsBinding.list.getAdapter() != null) {
                fragmentConfigurationsBinding.list.getAdapter().notifyDataSetChanged();
            }
        });

        model.getSelectedScopeLiveData().observe(this, scope -> {
            if (scope != null && fragmentConfigurationsBinding.list.getAdapter() != null) {
                fragmentConfigurationsBinding.list.getAdapter().notifyDataSetChanged();
            }
            fragmentConfigurationsBinding.scopeButton.setText(scope.getName());
        });

        fragmentConfigurationsBinding.scopeButton.setOnClickListener(v -> ScopeFragment.newInstance(args.getApplicationId()).show(getChildFragmentManager(), null));

        model.getConfigurations().observe(this, wrenchConfigurationWithValues -> {
            if (wrenchConfigurationWithValues != null) {
                updateConfigurations(wrenchConfigurationWithValues);
            }
        });

        model.isListEmpty().observe(this, isEmpty -> {
            ViewAnimator animator = fragmentConfigurationsBinding.animator;
            if (isEmpty == null || isEmpty) {
                animator.setDisplayedChild(animator.indexOfChild(fragmentConfigurationsBinding.noConfigurationsEmptyView));
            } else {
                animator.setDisplayedChild(animator.indexOfChild(fragmentConfigurationsBinding.listWrapper));
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_configurations_list, menu);

        MenuItem item = menu.findItem(R.id.action_filter_configurations);
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true; // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true; // Return true to expand action view
            }
        });

        if (!TextUtils.isEmpty(currentFilter)) {
            item.expandActionView();
            searchView.setQuery(currentFilter, true);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        model.setQuery(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_restart_application: {
                model.getWrenchApplication().observe(this, new Observer<WrenchApplication>() {
                    @Override
                    public void onChanged(@Nullable WrenchApplication wrenchApplication) {
                        model.getWrenchApplication().removeObserver(this);

                        if (wrenchApplication == null) {
                            return;
                        }

                        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
                        activityManager.killBackgroundProcesses(wrenchApplication.packageName());

                        Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(wrenchApplication.packageName());
                        if (intent != null) {
                            ConfigurationsFragment.this.getContext().startActivity(Intent.makeRestartActivityTask(intent.getComponent()));
                        } else if (ConfigurationsFragment.this.getView() != null) {
                            Snackbar.make(ConfigurationsFragment.this.getView(), R.string.application_not_installed, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

                return true;
            }
            case R.id.action_application_settings: {
                model.getWrenchApplication().observe(this, new Observer<WrenchApplication>() {
                    @Override
                    public void onChanged(@Nullable WrenchApplication wrenchApplication) {
                        model.getWrenchApplication().removeObserver(this);
                        if (wrenchApplication == null) {
                            return;
                        }

                        startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", wrenchApplication.packageName(), null)));
                    }
                });
                return true;
            }
            case R.id.action_delete_application: {
                AsyncTask.execute(() -> model.deleteApplication(model.getWrenchApplication().getValue()));
                getActivity().finish();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void configurationClicked(View v, WrenchConfigurationWithValues configuration) {
        if (model.getSelectedScopeLiveData().getValue() == null) {
            Snackbar.make(fragmentConfigurationsBinding.getRoot(), "No selected scope found", Snackbar.LENGTH_LONG).show();
            return;
        }

        int selectedScopeId = (int) model.getSelectedScopeLiveData().getValue().id();

        if (TextUtils.equals(String.class.getName(), configuration.getType()) ||
                TextUtils.equals(Bolt.TYPE.STRING, configuration.getType())) {

            StringValueFragment.newInstance(new StringValueFragmentArgs.Builder((int) configuration.getId(), selectedScopeId).build()).show(getChildFragmentManager(), null);

        } else if (TextUtils.equals(Integer.class.getName(), configuration.getType()) ||
                TextUtils.equals(Bolt.TYPE.INTEGER, configuration.getType())) {

            IntegerValueFragment.newInstance(new IntegerValueFragmentArgs.Builder((int) configuration.getId(), selectedScopeId).build()).show(getChildFragmentManager(), null);

        } else if (TextUtils.equals(Boolean.class.getName(), configuration.getType()) ||
                TextUtils.equals(Bolt.TYPE.BOOLEAN, configuration.getType())) {

            BooleanValueFragment.newInstance(new BooleanValueFragmentArgs.Builder((int) configuration.getId(), selectedScopeId).build()).show(getChildFragmentManager(), null);

        } else if (TextUtils.equals(Enum.class.getName(), configuration.getType()) ||
                TextUtils.equals(Bolt.TYPE.ENUM, configuration.getType())) {

            EnumValueFragment.newInstance(new EnumValueFragmentArgs.Builder((int) configuration.getId(), selectedScopeId).build()).show(getChildFragmentManager(), null);

        } else {

            Snackbar.make(fragmentConfigurationsBinding.getRoot(), "Not sure what to do with type: " + configuration.getType(), Snackbar.LENGTH_LONG).show();
        }
    }
}
