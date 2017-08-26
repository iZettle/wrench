package com.izettle.wrench.dialogs.enumvalue;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.izettle.wrench.R;
import com.izettle.wrench.database.WrenchPredefinedConfigurationValue;
import com.izettle.wrench.databinding.FragmentEnumValueBinding;
import com.izettle.wrench.di.Injectable;
import com.izettle.wrench.lifecycle.LifecycleDialogFragment;

import javax.inject.Inject;

public class EnumValueFragment extends LifecycleDialogFragment implements PredefinedValueRecyclerViewAdapter.Listener, Injectable {

    private static final String ARGUMENT_CONFIGURATION_ID = "ARGUMENT_CONFIGURATION_ID";
    private static final String ARGUMENT_SCOPE_ID = "ARGUMENT_SCOPE_ID";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FragmentEnumValueBinding binding;
    private FragmentEnumValueViewModel viewModel;
    private PredefinedValueRecyclerViewAdapter adapter;

    public static EnumValueFragment newInstance(long configurationId, long scopeId) {
        EnumValueFragment fragment = new EnumValueFragment();
        Bundle args = new Bundle();
        args.putLong(ARGUMENT_CONFIGURATION_ID, configurationId);
        args.putLong(ARGUMENT_SCOPE_ID, scopeId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        binding = FragmentEnumValueBinding.inflate(LayoutInflater.from(getContext()), null);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FragmentEnumValueViewModel.class);

        viewModel.init(getArguments().getLong(ARGUMENT_CONFIGURATION_ID), getArguments().getLong(ARGUMENT_SCOPE_ID));

        viewModel.getConfiguration().observe(this, wrenchConfiguration -> {
            if (wrenchConfiguration != null) {
                getDialog().setTitle(wrenchConfiguration.key());
            }
        });

        viewModel.getSelectedConfigurationValueLiveData().observe(this, wrenchConfigurationValue -> {
            if (wrenchConfigurationValue != null) {
                viewModel.setSelectedConfigurationValue(wrenchConfigurationValue);
            }
        });

        adapter = new PredefinedValueRecyclerViewAdapter(this);
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        viewModel.getPredefinedValues().observe(this, items -> {
            if (items != null) {
                Log.d("TAG", String.format("items: %d", items.size()));
                adapter.setItems(items);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.select_scope)
                .setView(binding.getRoot())
                .setNegativeButton(R.string.revert,
                        (dialog, whichButton) -> {
                            if (viewModel.getSelectedConfigurationValue() != null) {
                                AsyncTask.execute(() -> viewModel.deleteConfigurationValue());
                            }
                            dismiss();
                        }
                )
                .create();
    }

    @Override
    public void onClick(View view) {
        WrenchPredefinedConfigurationValue predefinedConfigurationValue = adapter.getItem(binding.list.getChildAdapterPosition(view));
        AsyncTask.execute(() -> viewModel.updateConfigurationValue(predefinedConfigurationValue.getValue()));
        dismiss();
    }
}
