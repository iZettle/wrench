package com.izettle.wrench;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;

import com.izettle.wrench.databinding.FragmentApplicationsBinding;
import com.izettle.wrench.lifecycle.AppCompatLifecycleFragment;

import java.util.List;


public class ApplicationsFragment extends AppCompatLifecycleFragment {

    FragmentApplicationsBinding fragmentApplicationsBinding;
    private ApplicationViewModel model;

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
        model = ViewModelProviders.of(getActivity()).get(ApplicationViewModel.class);
        model.getApplications().observe(this, this::applicationsUpdated);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
