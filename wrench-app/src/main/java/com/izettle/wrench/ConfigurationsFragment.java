package com.izettle.wrench;

import android.app.ActivityManager;
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

import com.izettle.wrench.database.WrenchConfiguration;
import com.izettle.wrench.databinding.FragmentConfigurationsBinding;
import com.izettle.wrench.dialogs.booleanvalue.BooleanValueFragment;
import com.izettle.wrench.dialogs.enumvalue.EnumValueFragment;
import com.izettle.wrench.dialogs.integervalue.IntegerValueFragment;
import com.izettle.wrench.dialogs.stringvalue.StringValueFragment;

import java.util.List;

public class ConfigurationsFragment extends Fragment implements SearchView.OnQueryTextListener, ConfigurationRecyclerViewAdapter.Listener {
    private static final String STATE_FILTER = "STATE_FILTER";
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

    private void updateConfigurations(List<WrenchConfiguration> wrenchConfigurations) {
        ViewAnimator animator = fragmentConfigurationsBinding.animator;
        if (wrenchConfigurations.size() == 0 && animator.getDisplayedChild() != animator.indexOfChild(fragmentConfigurationsBinding.noConfigurationsEmptyView)) {
            animator.setDisplayedChild(animator.indexOfChild(fragmentConfigurationsBinding.noConfigurationsEmptyView));
        } else if (animator.getDisplayedChild() != animator.indexOfChild(fragmentConfigurationsBinding.list)) {
            animator.setDisplayedChild(animator.indexOfChild(fragmentConfigurationsBinding.list));
        }

        ConfigurationRecyclerViewAdapter adapter = (ConfigurationRecyclerViewAdapter) fragmentConfigurationsBinding.list.getAdapter();
        if (adapter == null) {
            adapter = new ConfigurationRecyclerViewAdapter(this, this, model);

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

        model = ViewModelProviders.of(getActivity()).get(ConfigurationViewModel.class);

        fragmentConfigurationsBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));

        model.getConfigurations().observe(ConfigurationsFragment.this, wrenchConfigurations -> {
            if (wrenchConfigurations != null) {
                updateConfigurations(wrenchConfigurations);
            }
        });

        model.getWrenchApplication().observe(this, wrenchApplication -> {
            if (wrenchApplication != null) {
                getActivity().setTitle(wrenchApplication.applicationLabel());
                model.wrenchApplication = wrenchApplication;
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

                ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
                activityManager.killBackgroundProcesses(model.wrenchApplication.packageName());

                Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(model.wrenchApplication.packageName());
                if (intent != null) {
                    getContext().startActivity(Intent.makeRestartActivityTask(intent.getComponent()));
                } else if (getView() != null) {
                    Snackbar.make(getView(), R.string.application_not_installed, Snackbar.LENGTH_LONG).show();
                }

                return true;
            }
            case R.id.action_application_settings: {
                startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", model.wrenchApplication.packageName(), null)));
                return true;
            }
            case R.id.action_delete_application: {
                AsyncTask.execute(() -> model.deleteApplication(model.wrenchApplication));
                getActivity().finish();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void configurationClicked(WrenchConfiguration configuration) {
        if (model.getSelectedScopeLiveData().getValue() == null) {
            Snackbar.make(fragmentConfigurationsBinding.getRoot(), "No selected scope found", Snackbar.LENGTH_LONG).show();
            return;
        }

        long selectedScopeId = model.getSelectedScopeLiveData().getValue().id();

        if (TextUtils.equals(String.class.getName(), configuration.type())) {
            StringValueFragment.newInstance(configuration.id(), selectedScopeId).show(getChildFragmentManager(), null);

        } else if (TextUtils.equals(Integer.class.getName(), configuration.type())) {
            IntegerValueFragment.newInstance(configuration.id(), selectedScopeId).show(getChildFragmentManager(), null);

        } else if (TextUtils.equals(Boolean.class.getName(), configuration.type())) {
            BooleanValueFragment.newInstance(configuration.id(), selectedScopeId).show(getChildFragmentManager(), null);

        } else if (TextUtils.equals(Enum.class.getName(), configuration.type())) {
            EnumValueFragment.newInstance(configuration.id(), selectedScopeId).show(getChildFragmentManager(), null);

        } else {
            throw new IllegalArgumentException("crazy type: " + configuration.type());
        }
    }
}
