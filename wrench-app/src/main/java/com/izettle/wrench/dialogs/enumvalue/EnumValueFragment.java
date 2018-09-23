package com.izettle.wrench.dialogs.enumvalue;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.izettle.wrench.R;
import com.izettle.wrench.database.WrenchPredefinedConfigurationValue;
import com.izettle.wrench.databinding.FragmentEnumValueBinding;
import com.izettle.wrench.di.Injectable;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EnumValueFragment extends DialogFragment implements PredefinedValueRecyclerViewAdapter.Listener, Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FragmentEnumValueBinding binding;
    private FragmentEnumValueViewModel viewModel;
    private PredefinedValueRecyclerViewAdapter adapter;

    public static EnumValueFragment newInstance(EnumValueFragmentArgs args) {
        EnumValueFragment fragment = new EnumValueFragment();
        fragment.setArguments(args.toBundle());
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        assert getArguments() != null;

        binding = FragmentEnumValueBinding.inflate(LayoutInflater.from(getContext()), null);

        EnumValueFragmentArgs args = EnumValueFragmentArgs.fromBundle(getArguments());

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FragmentEnumValueViewModel.class);

        viewModel.init(args.getConfigurationId(), args.getScopeId());

        viewModel.getConfiguration().observe(this, wrenchConfiguration -> {
            if (wrenchConfiguration != null) {
                getDialog().setTitle(wrenchConfiguration.getKey());
            }
        });

        viewModel.getSelectedConfigurationValueLiveData().observe(this, wrenchConfigurationValue -> {
            if (wrenchConfigurationValue != null) {
                viewModel.setSelectedConfigurationValue(wrenchConfigurationValue);
            }
        });

        adapter = new PredefinedValueRecyclerViewAdapter(this);
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));


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
