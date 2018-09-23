package com.izettle.wrench.dialogs.booleanvalue;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.izettle.wrench.R;
import com.izettle.wrench.databinding.FragmentBooleanValueBinding;
import com.izettle.wrench.di.Injectable;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class BooleanValueFragment extends DialogFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FragmentBooleanValueBinding binding;
    private FragmentBooleanValueViewModel viewModel;

    public static BooleanValueFragment newInstance(BooleanValueFragmentArgs args) {
        BooleanValueFragment fragment = new BooleanValueFragment();
        fragment.setArguments(args.toBundle());
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        assert getArguments() != null;

        binding = FragmentBooleanValueBinding.inflate(LayoutInflater.from(getContext()), null);

        BooleanValueFragmentArgs args = BooleanValueFragmentArgs.fromBundle(getArguments());

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FragmentBooleanValueViewModel.class);

        viewModel.init(args.getConfigurationId(), args.getScopeId());

        viewModel.getConfiguration().observe(this, wrenchConfiguration -> {
            if (wrenchConfiguration != null) {
                getDialog().setTitle(wrenchConfiguration.getKey());
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
