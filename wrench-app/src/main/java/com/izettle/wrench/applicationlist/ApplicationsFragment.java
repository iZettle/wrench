package com.izettle.wrench.applicationlist;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;

import com.izettle.wrench.databinding.FragmentApplicationsBinding;
import com.izettle.wrench.di.Injectable;

import java.util.List;

import javax.inject.Inject;


public class ApplicationsFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FragmentApplicationsBinding fragmentApplicationsBinding;

    public ApplicationsFragment() {
    }

    public static ApplicationsFragment newInstance() {
        ApplicationsFragment fragment = new ApplicationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ApplicationViewModel model = ViewModelProviders.of(this, viewModelFactory).get(ApplicationViewModel.class);

        model.getApplications().observe(this, this::applicationsUpdated);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentApplicationsBinding = FragmentApplicationsBinding.inflate(inflater, container, false);
        fragmentApplicationsBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        return fragmentApplicationsBinding.getRoot();
    }

    private void applicationsUpdated(List<com.izettle.wrench.database.WrenchApplication> applications) {
        if (applications.size() == 0) {
            ViewAnimator animator = fragmentApplicationsBinding.animator;
            animator.setDisplayedChild(animator.indexOfChild(fragmentApplicationsBinding.noApplicationsEmptyView));
        } else {
            ViewAnimator animator = fragmentApplicationsBinding.animator;
            animator.setDisplayedChild(animator.indexOfChild(fragmentApplicationsBinding.list));
        }

        ApplicationRecyclerViewAdapter adapter = (ApplicationRecyclerViewAdapter) fragmentApplicationsBinding.list.getAdapter();
        if (adapter == null) {
            adapter = new ApplicationRecyclerViewAdapter(applications);
            fragmentApplicationsBinding.list.setAdapter(adapter);

        } else {
            adapter.setItems(applications);
        }
    }

}
