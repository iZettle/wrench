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

        ApplicationAdapter adapter = new ApplicationAdapter();
        model.getApplications().observe(this, adapter::setList);
        fragmentApplicationsBinding.list.setAdapter(adapter);

        model.isListEmpty().observe(this, isEmpty -> {
            ViewAnimator animator = fragmentApplicationsBinding.animator;
            if (isEmpty == null || isEmpty) {
                animator.setDisplayedChild(animator.indexOfChild(fragmentApplicationsBinding.noApplicationsEmptyView));
            } else {
                animator.setDisplayedChild(animator.indexOfChild(fragmentApplicationsBinding.list));
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentApplicationsBinding = FragmentApplicationsBinding.inflate(inflater, container, false);
        fragmentApplicationsBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        return fragmentApplicationsBinding.getRoot();
    }
}
