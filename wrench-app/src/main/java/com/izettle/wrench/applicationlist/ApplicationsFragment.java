package com.izettle.wrench.applicationlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;

import com.izettle.wrench.databinding.FragmentApplicationsBinding;
import com.izettle.wrench.di.Injectable;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


public class ApplicationsFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FragmentApplicationsBinding fragmentApplicationsBinding;

    public ApplicationsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentApplicationsBinding = FragmentApplicationsBinding.inflate(inflater, container, false);
        fragmentApplicationsBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        return fragmentApplicationsBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ApplicationViewModel model = ViewModelProviders.of(this, viewModelFactory).get(ApplicationViewModel.class);

        ApplicationAdapter adapter = new ApplicationAdapter();
        model.getApplications().observe(this, adapter::submitList);
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


}
