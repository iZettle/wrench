package com.izettle.localconfig.application;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.IntentCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;

import com.izettle.localconfig.application.databinding.FragmentConfigurationsBinding;
import com.izettle.localconfig.application.library.Application;
import com.izettle.localconfig.application.library.ApplicationConfigProviderHelper;
import com.izettle.localconfig.application.library.ConfigurationFull;
import com.izettle.localconfig.application.library.ConfigurationFullCursorParser;
import com.izettle.localconfiguration.ConfigProviderHelper;
import com.izettle.localconfiguration.util.ConfigurationValueCursorParser;

import java.util.ArrayList;


public class ConfigurationsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CONFIGURATIONS_LOADER = 1;
    private static final String ARG_APPLICATION = "ARG_APPLICATION";
    private static final String LOADER_EXTRA_APPLICATION = "LOADER_EXTRA_APPLICATION";
    private static final int PERMISSION_KILL_BACKGROUND_PROCESS = 132;
    FragmentConfigurationsBinding fragmentConfigurationsBinding;
    private Application application;

    public ConfigurationsFragment() {
    }

    public static ConfigurationsFragment newInstance(Application application) {
        ConfigurationsFragment fragment = new ConfigurationsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_APPLICATION, application);
        fragment.setArguments(args);
        return fragment;
    }

    private static void restartApplication(Context context, Application application) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses(application.applicationName);

        Intent intent = context.getPackageManager().getLaunchIntentForPackage(application.applicationName);
        context.startActivity(IntentCompat.makeRestartActivityTask(intent.getComponent()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = getArguments().getParcelable(ARG_APPLICATION);
        if (application == null) {
            throw new NullPointerException();
        }
        getActivity().setTitle(application.label);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentConfigurationsBinding = FragmentConfigurationsBinding.inflate(inflater, container, false);

        fragmentConfigurationsBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));

        return fragmentConfigurationsBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        Application application = getArguments().getParcelable(ARG_APPLICATION);
        Bundle bundle = new Bundle();
        bundle.putParcelable(LOADER_EXTRA_APPLICATION, application);
        getLoaderManager().initLoader(CONFIGURATIONS_LOADER, bundle, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_configurations_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_restart_application: {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.KILL_BACKGROUND_PROCESSES) == PackageManager.PERMISSION_GRANTED) {

                    restartApplication(getContext(), application);
                } else {

                    if (shouldShowRequestPermissionRationale(Manifest.permission.KILL_BACKGROUND_PROCESSES)) {
                        Snackbar.make(getView(), "My text", BaseTransientBottomBar.LENGTH_LONG)
                                .setAction("", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions(new String[]{Manifest.permission.KILL_BACKGROUND_PROCESSES}, PERMISSION_KILL_BACKGROUND_PROCESS);
                                    }
                                }).show();

                    } else {
                        requestPermissions(new String[]{Manifest.permission.KILL_BACKGROUND_PROCESSES}, PERMISSION_KILL_BACKGROUND_PROCESS);
                    }
                }
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_KILL_BACKGROUND_PROCESS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    restartApplication(getContext(), application);

                } else {


                }
                return;
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case CONFIGURATIONS_LOADER: {
                Application application = args.getParcelable(LOADER_EXTRA_APPLICATION);
                if (application == null) {
                    throw new NullPointerException();
                }
                return new CursorLoader(getContext(), ConfigProviderHelper.configurationUri(), ConfigurationFullCursorParser.PROJECTION,
                        ConfigurationFullCursorParser.Columns.APPLICATION_ID + " = ?", new String[]{String.valueOf(application._id)}, null);
            }
            default: {
                throw new UnsupportedOperationException("Invalid id: " + id);
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case CONFIGURATIONS_LOADER: {

                ViewAnimator animator = fragmentConfigurationsBinding.animator;
                if (cursor.getCount() == 0 && animator.getDisplayedChild() != animator.indexOfChild(fragmentConfigurationsBinding.noConfigurationsEmptyView)) {
                    animator.setDisplayedChild(animator.indexOfChild(fragmentConfigurationsBinding.noConfigurationsEmptyView));
                } else if (animator.getDisplayedChild() != animator.indexOfChild(fragmentConfigurationsBinding.list)) {
                    animator.setDisplayedChild(animator.indexOfChild(fragmentConfigurationsBinding.list));
                }

                ArrayList<ConfigurationFull> newConfigurations = new ArrayList<>();

                if (cursor.moveToFirst()) {
                    ConfigurationFullCursorParser configurationFullCursorParser = new ConfigurationFullCursorParser();
                    do {
                        newConfigurations.add(configurationFullCursorParser.populateFromCursor(new ConfigurationFull(), cursor));
                    }
                    while (cursor.moveToNext());
                }

                ConfigurationRecyclerViewAdapter adapter = (ConfigurationRecyclerViewAdapter) fragmentConfigurationsBinding.list.getAdapter();
                if (adapter == null) {
                    adapter = new ConfigurationRecyclerViewAdapter(newConfigurations);

                    fragmentConfigurationsBinding.list.setAdapter(adapter);

                    ItemTouchHelper itemTouchHelper = adapter.getItemTouchHelper(new ConfigurationRecyclerViewAdapter.SwipeDelete() {
                        @Override
                        public void swiped(ConfigurationFull configuration) {
                            ConfigurationsFragment.this.getContext().getContentResolver().delete(ApplicationConfigProviderHelper.configurationUri(configuration._id), null, null);
                            ConfigurationsFragment.this.getContext().getContentResolver().delete(ApplicationConfigProviderHelper.configurationValueUri(), ConfigurationValueCursorParser.Columns.CONFIGURATION_ID + " = ?", new String[]{String.valueOf(configuration._id)});
                        }
                    });

                    itemTouchHelper.attachToRecyclerView(fragmentConfigurationsBinding.list);

                } else {
                    adapter.setItems(newConfigurations);
                }

                break;
            }
            default: {
                throw new UnsupportedOperationException("Invalid id: " + loader.getId());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
