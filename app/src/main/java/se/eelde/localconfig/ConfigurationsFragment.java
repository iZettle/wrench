package se.eelde.localconfig;

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

import java.util.ArrayList;

import se.eelde.localconfig.databinding.FragmentConfigurationsBinding;
import se.eelde.localconfiguration.library.ConfigProviderHelper;
import se.eelde.localconfiguration.library.Configuration;

public class ConfigurationsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CONFIGURATIONS_LOADER = 1;
    private ConfigurationRecyclerViewAdapter configurationRecyclerViewAdapter;

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
        FragmentConfigurationsBinding fragmentConfigurationsBinding = FragmentConfigurationsBinding.inflate(inflater, container, false);

        fragmentConfigurationsBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));

        configurationRecyclerViewAdapter = new ConfigurationRecyclerViewAdapter();
        fragmentConfigurationsBinding.list.setAdapter(configurationRecyclerViewAdapter);

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
                return new CursorLoader(getContext(), ConfigProviderHelper.configurationUri(), se.eelde.localconfiguration.library.Configuration.PROJECTION, null, null, null);
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

                ArrayList<se.eelde.localconfiguration.library.Configuration> newConfigurations = new ArrayList<>();
                while (cursor.moveToNext()) {
                    newConfigurations.add(Configuration.configurationFromCursor(cursor));
                }
                configurationRecyclerViewAdapter.setItems(newConfigurations);
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
