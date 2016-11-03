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
import android.widget.ViewAnimator;

import com.izettle.localconfig.application.databinding.FragmentApplicationsBinding;
import com.izettle.localconfig.application.library.Application;
import com.izettle.localconfig.application.library.ApplicationConfigProviderHelper;
import com.izettle.localconfig.application.library.ApplicationCursorParser;

import java.util.ArrayList;


public class ApplicationsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int APPLICATIONS_LOADER = 1;
    FragmentApplicationsBinding fragmentApplicationsBinding;

    public ApplicationsFragment() {
    }

    public static ApplicationsFragment newInstance() {
        ApplicationsFragment fragment = new ApplicationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentApplicationsBinding = FragmentApplicationsBinding.inflate(inflater, container, false);

        fragmentApplicationsBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));

        return fragmentApplicationsBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(APPLICATIONS_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case APPLICATIONS_LOADER: {
                return new CursorLoader(getContext(), ApplicationConfigProviderHelper.applicationUri(), ApplicationCursorParser.PROJECTION, null, null, null);
            }
            default: {
                throw new UnsupportedOperationException("Invalid id: " + id);
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case APPLICATIONS_LOADER: {

                if (cursor.getCount() == 0) {
                    ViewAnimator animator = fragmentApplicationsBinding.animator;
                    animator.setDisplayedChild(animator.indexOfChild(fragmentApplicationsBinding.noApplicationsEmptyView));
                } else {
                    ViewAnimator animator = fragmentApplicationsBinding.animator;
                    animator.setDisplayedChild(animator.indexOfChild(fragmentApplicationsBinding.list));
                }

                ArrayList<Application> newApplications = new ArrayList<>();

                if (cursor.moveToFirst()) {
                    ApplicationCursorParser applicationCursorParser = new ApplicationCursorParser();
                    do {
                        newApplications.add(applicationCursorParser.populateFromCursor(new Application(), cursor));
                    }
                    while (cursor.moveToNext());
                }

                ApplicationRecyclerViewAdapter adapter = (ApplicationRecyclerViewAdapter) fragmentApplicationsBinding.list.getAdapter();
                if (adapter == null) {
                    adapter = new ApplicationRecyclerViewAdapter(newApplications);
                    fragmentApplicationsBinding.list.setAdapter(adapter);
                } else {
                    adapter.setItems(newApplications);
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
