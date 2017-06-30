package com.izettle.wrench.dialogs.booleanvalue;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.izettle.wrench.R;
import com.izettle.wrench.databinding.FragmentBooleanValueBinding;
import com.izettle.wrench.lifecycle.LifecycleDialogFragment;

public class BooleanValueFragment extends LifecycleDialogFragment {

    private static final String ARGUMENT_CONFIGURATION_ID = "ARGUMENT_CONFIGURATION_ID";
    private static final String ARGUMENT_SCOPE_ID = "ARGUMENT_SCOPE_ID";
    private FragmentBooleanValueBinding binding;
    private FragmentBooleanValueViewModel viewModel;

    public static BooleanValueFragment newInstance(long configurationId, long scopeId) {
        BooleanValueFragment fragment = new BooleanValueFragment();
        Bundle args = new Bundle();
        args.putLong(ARGUMENT_CONFIGURATION_ID, configurationId);
        args.putLong(ARGUMENT_SCOPE_ID, scopeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = FragmentBooleanValueBinding.inflate(LayoutInflater.from(getContext()), null);

        viewModel = new FragmentBooleanValueViewModel(getActivity().getApplication());
        viewModel.init(getArguments().getLong(ARGUMENT_CONFIGURATION_ID), getArguments().getLong(ARGUMENT_SCOPE_ID));

        viewModel.getConfiguration().observe(this, wrenchConfiguration -> {
            if (wrenchConfiguration != null) {
                getDialog().setTitle(wrenchConfiguration.key());
            }
        });

        viewModel.getSelectedConfigurationValueLiveData().observe(this, wrenchConfigurationValue -> {
            viewModel.setSelectedConfigurationValue(wrenchConfigurationValue);
            if (wrenchConfigurationValue != null) {
                binding.value.setChecked(Boolean.valueOf(wrenchConfigurationValue.getValue()));
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.select_scope)
                .setView(binding.getRoot())
                .setPositiveButton(android.R.string.ok,
                        (dialog, whichButton) -> {
                            AsyncTask.execute(() -> viewModel.updateConfigurationValue(String.valueOf(binding.value.isChecked())));
                            dismiss();
                        }
                )
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
}
