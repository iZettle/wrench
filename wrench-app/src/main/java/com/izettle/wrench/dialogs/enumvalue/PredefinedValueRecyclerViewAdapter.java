package com.izettle.wrench.dialogs.enumvalue;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.izettle.wrench.database.WrenchPredefinedConfigurationValue;
import com.izettle.wrench.databinding.SimpleListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PredefinedValueRecyclerViewAdapter extends RecyclerView.Adapter<PredefinedValueRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<WrenchPredefinedConfigurationValue> items = new ArrayList<>();
    private Listener listener;

    public PredefinedValueRecyclerViewAdapter(Listener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SimpleListItemBinding binding = SimpleListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WrenchPredefinedConfigurationValue predefinedConfigurationValue = items.get(position);

        holder.binding.value.setText(predefinedConfigurationValue.getValue());
        holder.binding.getRoot().setOnClickListener(view -> {
            listener.onClick(view);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<WrenchPredefinedConfigurationValue> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public WrenchPredefinedConfigurationValue getItem(int position) {
        return items.get(position);
    }

    interface Listener {
        void onClick(View view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final SimpleListItemBinding binding;

        public ViewHolder(SimpleListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
