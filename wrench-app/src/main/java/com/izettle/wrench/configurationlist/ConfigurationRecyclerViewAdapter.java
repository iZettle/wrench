package com.izettle.wrench.configurationlist;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.izettle.wrench.database.WrenchConfigurationValue;
import com.izettle.wrench.database.WrenchConfigurationWithValues;
import com.izettle.wrench.database.WrenchScope;
import com.izettle.wrench.databinding.ConfigurationListItemBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


class ConfigurationRecyclerViewAdapter extends RecyclerView.Adapter<ConfigurationViewHolder> {

    private static final int VIEW_TYPE_GENERAL = 1;
    private final ArrayList<WrenchConfigurationWithValues> items = new ArrayList<>();
    private final ConfigurationViewModel model;
    private final Listener listener;

    ConfigurationRecyclerViewAdapter(Listener listener, ConfigurationViewModel model) {
        this.listener = listener;
        this.model = model;
    }

    void setItems(List<WrenchConfigurationWithValues> items) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ConfigurationListDiffCallbacks(this.items, items));
        diffResult.dispatchUpdatesTo(this);
        this.items.clear();
        this.items.addAll(items);
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_GENERAL;
    }

    @Override
    public ConfigurationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_GENERAL: {
                ConfigurationListItemBinding binding = ConfigurationListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ConfigurationViewHolder(binding, null);
            }
            default: {
                throw new IllegalStateException("Unknown view type");
            }
        }
    }

    @Override
    public void onBindViewHolder(final ConfigurationViewHolder holder, int position) {
        onBindViewHolder(holder, position, new ArrayList<>());
    }

    @Override
    public void onBindViewHolder(final ConfigurationViewHolder viewHolder, int position, List<Object> payloads) {
        viewHolder.configuration = items.get(viewHolder.getAdapterPosition());

        viewHolder.binding.title.setText(viewHolder.configuration.getKey());

        if (viewHolder.configuration.getConfigurationValues() == null) {
            return;
        }

        WrenchScope defaultScope = model.getDefaultScopeLiveData().getValue();
        WrenchScope selectedScope = model.getSelectedScopeLiveData().getValue();

        WrenchConfigurationValue defaultScopedItem = getItemForScope(defaultScope, viewHolder.configuration.getConfigurationValues());
        if (defaultScopedItem == null) {
            return;
        }

        viewHolder.binding.defaultValue.setText(defaultScopedItem.getValue());

        WrenchConfigurationValue selectedScopedItem = getItemForScope(selectedScope, viewHolder.configuration.getConfigurationValues());
        if (selectedScopedItem != null && selectedScopedItem.getScope() != defaultScope.id()) {
            viewHolder.binding.defaultValue.setPaintFlags(viewHolder.binding.defaultValue.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.binding.customValue.setText(selectedScopedItem.getValue());
            viewHolder.binding.customValue.setVisibility(View.VISIBLE);
        } else {
            viewHolder.binding.customValue.setText(null);
            viewHolder.binding.defaultValue.setPaintFlags(viewHolder.binding.defaultValue.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            viewHolder.binding.customValue.setVisibility(View.GONE);
        }
        viewHolder.binding.getRoot().setOnClickListener(view -> listener.configurationClicked(view, items.get(viewHolder.getAdapterPosition())));
    }

    @Nullable
    private WrenchConfigurationValue getItemForScope(@Nullable WrenchScope scope, List<WrenchConfigurationValue> wrenchConfigurationValues) {
        if (scope == null) {
            return null;
        }

        for (WrenchConfigurationValue wrenchConfigurationValue : wrenchConfigurationValues) {
            if (wrenchConfigurationValue.getScope() == scope.id()) {
                return wrenchConfigurationValue;
            }
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface Listener {
        void configurationClicked(View v, WrenchConfigurationWithValues configuration);
    }

    private class ConfigurationListDiffCallbacks extends DiffUtil.Callback {
        private final ArrayList<WrenchConfigurationWithValues> oldItems;
        private final List<WrenchConfigurationWithValues> newItems;

        ConfigurationListDiffCallbacks(ArrayList<WrenchConfigurationWithValues> oldItems, List<WrenchConfigurationWithValues> newItems) {
            this.oldItems = oldItems;
            this.newItems = newItems;
        }

        @Override
        public int getOldListSize() {
            return oldItems.size();
        }

        @Override
        public int getNewListSize() {
            return newItems.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            WrenchConfigurationWithValues oldItem = oldItems.get(oldItemPosition);
            WrenchConfigurationWithValues newItem = newItems.get(newItemPosition);

            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            WrenchConfigurationWithValues oldItem = oldItems.get(oldItemPosition);
            WrenchConfigurationWithValues newItem = newItems.get(newItemPosition);

            if (oldItem.getConfigurationValues().size() != newItem.getConfigurationValues().size()) {
                return false;
            }

            for (int i = 0; i < oldItem.getConfigurationValues().size(); i++) {
                WrenchConfigurationValue oldWrenchConfigurationValue = oldItem.getConfigurationValues().get(i);
                WrenchConfigurationValue newWrenchConfigurationValue = newItem.getConfigurationValues().get(i);

                if (!TextUtils.equals(oldWrenchConfigurationValue.getValue(), newWrenchConfigurationValue.getValue())) {
                    return false;
                }
            }

            return TextUtils.equals(oldItem.getKey(), newItem.getKey()) &&
                    TextUtils.equals(oldItem.getType(), newItem.getType());
        }
    }
}
