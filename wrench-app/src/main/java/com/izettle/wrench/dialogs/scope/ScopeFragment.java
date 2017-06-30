package com.izettle.wrench.dialogs.scope;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.izettle.wrench.R;
import com.izettle.wrench.database.WrenchScope;
import com.izettle.wrench.databinding.FragmentScopeBinding;
import com.izettle.wrench.lifecycle.LifecycleDialogFragment;

public class ScopeFragment extends LifecycleDialogFragment implements ScopeRecyclerViewAdapter.Listener {

    private static final String ARGUMENT_APPLICATION_ID = "ARGUMENT_APPLICATION_ID";
    private FragmentScopeBinding binding;
    private ScopeFragmentViewModel viewModel;
    private ScopeRecyclerViewAdapter adapter;

    public static ScopeFragment newInstance(long applicationId) {
        ScopeFragment fragment = new ScopeFragment();
        Bundle args = new Bundle();
        args.putLong(ARGUMENT_APPLICATION_ID, applicationId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        binding = FragmentScopeBinding.inflate(LayoutInflater.from(getContext()), null);

        viewModel = new ScopeFragmentViewModel(getActivity().getApplication());
        viewModel.init(getArguments().getLong(ARGUMENT_APPLICATION_ID));

        viewModel.getScopes().observe(this, scopes -> adapter.setItems(scopes));

        viewModel.getSelectedScopeLiveData().observe(this, wrenchScope -> viewModel.setSelectedScope(wrenchScope));

        adapter = new ScopeRecyclerViewAdapter(this);
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.select_scope)
                .setView(binding.getRoot())
                .setPositiveButton("Add",
                        (dialog, whichButton) -> {
                            EditText input = new EditText(getContext());
                            input.setSingleLine();

                            new AlertDialog.Builder(getContext())
                                    .setTitle("Create new scope")
                                    .setView(input)
                                    .setPositiveButton("OK", (dialog2, which) -> {
                                        String scopeName = input.getText().toString();
                                        AsyncTask.execute(() -> viewModel.createScope(scopeName));
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        }
                )
                .setNegativeButton("Delete",
                        (dialog, whichButton) -> {
                            WrenchScope selectedScope = viewModel.getSelectedScope();
                            if (selectedScope == null) {
                                return;
                            }
                            if (!WrenchScope.isDefaultScope(selectedScope)) {
                                AsyncTask.execute(() -> viewModel.removeScope(selectedScope));
                            }
                        }
                )
                .create();
    }

    @Override
    public void onClick(View view) {
        WrenchScope wrenchScope = adapter.getItem(binding.list.getChildAdapterPosition(view));
        AsyncTask.execute(() -> viewModel.selectScope(wrenchScope));
        dismiss();
    }
}
