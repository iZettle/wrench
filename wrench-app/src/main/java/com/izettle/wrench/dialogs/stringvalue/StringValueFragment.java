package com.izettle.wrench.dialogs.stringvalue;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;

import com.izettle.wrench.R;
import com.izettle.wrench.databinding.FragmentStringValueBinding;
import com.izettle.wrench.lifecycle.LifecycleDialogFragment;

public class StringValueFragment extends LifecycleDialogFragment {

    private static final String ARGUMENT_CONFIGURATION_ID = "ARGUMENT_CONFIGURATION_ID";
    private static final String ARGUMENT_SCOPE_ID = "ARGUMENT_SCOPE_ID";
    private FragmentStringValueBinding binding;
    private FragmentStringValueViewModel viewModel;

    public static StringValueFragment newInstance(long configurationId, long scopeId) {
        StringValueFragment fragment = new StringValueFragment();
        Bundle args = new Bundle();
        args.putLong(ARGUMENT_CONFIGURATION_ID, configurationId);
        args.putLong(ARGUMENT_SCOPE_ID, scopeId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        binding = FragmentStringValueBinding.inflate(LayoutInflater.from(getContext()), null);

        viewModel = new FragmentStringValueViewModel(getActivity().getApplication());
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
                .setPositiveButton(R.string.ok,
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
