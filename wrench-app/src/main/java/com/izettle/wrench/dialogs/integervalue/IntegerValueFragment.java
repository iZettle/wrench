package com.izettle.wrench.dialogs.integervalue;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;

import com.izettle.wrench.R;
import com.izettle.wrench.databinding.FragmentIntegerValueBinding;
import com.izettle.wrench.di.Injectable;

import javax.inject.Inject;

public class IntegerValueFragment extends DialogFragment implements Injectable {

    private static final String ARGUMENT_CONFIGURATION_ID = "ARGUMENT_CONFIGURATION_ID";
    private static final String ARGUMENT_SCOPE_ID = "ARGUMENT_SCOPE_ID";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FragmentIntegerValueBinding binding;
    private FragmentIntegerValueViewModel viewModel;

    public static IntegerValueFragment newInstance(long configurationId, long scopeId) {
        IntegerValueFragment fragment = new IntegerValueFragment();
        Bundle args = new Bundle();
        args.putLong(ARGUMENT_CONFIGURATION_ID, configurationId);
        args.putLong(ARGUMENT_SCOPE_ID, scopeId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = FragmentIntegerValueBinding.inflate(LayoutInflater.from(getContext()), null);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FragmentIntegerValueViewModel.class);

        viewModel.init(getArguments().getLong(ARGUMENT_CONFIGURATION_ID), getArguments().getLong(ARGUMENT_SCOPE_ID));

        viewModel.getConfiguration().observe(this, wrenchConfiguration -> {
            if (wrenchConfiguration != null) {
                getDialog().setTitle(wrenchConfiguration.key());
            }
        });

        viewModel.getSelectedConfigurationValueLiveData().observe(this, wrenchConfigurationValue -> {
            viewModel.setSelectedConfigurationValue(wrenchConfigurationValue);
            if (wrenchConfigurationValue != null) {
                binding.value.setText(wrenchConfigurationValue.getValue());
            }
        });

        binding.value.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                AsyncTask.execute(() -> viewModel.updateConfigurationValue(binding.value.getText().toString()));
                dismiss();
            }
            return false;
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.select_scope)
                .setView(binding.getRoot())
                .setPositiveButton(android.R.string.ok,
                        (dialog, whichButton) -> {
                            AsyncTask.execute(() -> viewModel.updateConfigurationValue(binding.value.getText().toString()));
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
