package com.izettle.localconfig.application;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.izettle.localconfig.application.databinding.FragmentConfigurationsBinding;
import com.izettle.localconfig.application.library.ConfigurationFull;
import com.izettle.localconfig.application.library.ConfigurationFullCursorParser;
import com.izettle.localconfiguration.ConfigProviderHelper;

import java.util.ArrayList;


public class ConfigurationsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CONFIGURATIONS_LOADER = 1;
    FragmentConfigurationsBinding fragmentConfigurationsBinding;

    public ConfigurationsFragment() {
    }

    public static ConfigurationsFragment newInstance() {
        ConfigurationsFragment fragment = new ConfigurationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentConfigurationsBinding = FragmentConfigurationsBinding.inflate(inflater, container, false);

        fragmentConfigurationsBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));


        fragmentConfigurationsBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddConfigurationActivity.newIntent(getContext()));
            }
        });

        return fragmentConfigurationsBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(CONFIGURATIONS_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case CONFIGURATIONS_LOADER: {
                return new CursorLoader(getContext(), ConfigProviderHelper.configurationUri(), ConfigurationFullCursorParser.PROJECTION, null, null, null);
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

                ArrayList<ConfigurationFull> newConfigurations = new ArrayList<>();

                if (cursor != null && cursor.moveToFirst()) {
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
