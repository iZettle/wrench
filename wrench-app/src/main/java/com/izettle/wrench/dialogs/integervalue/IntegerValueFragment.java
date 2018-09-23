package com.izettle.wrench.dialogs.integervalue;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;

import com.izettle.wrench.R;
import com.izettle.wrench.databinding.FragmentIntegerValueBinding;
import com.izettle.wrench.di.Injectable;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class IntegerValueFragment extends DialogFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FragmentIntegerValueBinding binding;
    private FragmentIntegerValueViewModel viewModel;

    public static IntegerValueFragment newInstance(IntegerValueFragmentArgs args) {
        IntegerValueFragment fragment = new IntegerValueFragment();
        fragment.setArguments(args.toBundle());
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        assert getArguments() != null;

        binding = FragmentIntegerValueBinding.inflate(LayoutInflater.from(getContext()), null);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FragmentIntegerValueViewModel.class);

        IntegerValueFragmentArgs args = IntegerValueFragmentArgs.fromBundle(getArguments());
        viewModel.init(args.getConfigurationId(), args.getScopeId());

        viewModel.getConfiguration().observe(this, wrenchConfiguration -> {
            if (wrenchConfiguration != null) {
                getDialog().setTitle(wrenchConfiguration.getKey());
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
