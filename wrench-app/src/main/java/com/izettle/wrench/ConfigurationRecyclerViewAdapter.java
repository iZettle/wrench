package com.izettle.wrench;

import android.arch.lifecycle.LifecycleOwner;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.izettle.wrench.database.WrenchConfiguration;
import com.izettle.wrench.database.WrenchConfigurationValue;
import com.izettle.wrench.database.WrenchScope;
import com.izettle.wrench.databinding.ConfigurationListItemBinding;

import java.util.ArrayList;
import java.util.List;


class ConfigurationRecyclerViewAdapter extends RecyclerView.Adapter<ConfigurationViewHolder> {

    private static final int VIEW_TYPE_GENERAL = 1;
    private final ArrayList<WrenchConfiguration> items = new ArrayList<>();
    private final ConfigurationViewModel model;
    private final LifecycleOwner lifecycleOwner;
    private final Listener listener;

    ConfigurationRecyclerViewAdapter(Listener listener, LifecycleOwner lifecycleOwner, ConfigurationViewModel model) {
        this.listener = listener;
        this.lifecycleOwner = lifecycleOwner;
        this.model = model;
    }

    void setItems(List<WrenchConfiguration> items) {
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
        if (viewHolder.configuration != null) {
            model.getConfigurationValues(viewHolder.configuration.id()).removeObservers(lifecycleOwner);
        }

        viewHolder.configuration = items.get(viewHolder.getAdapterPosition());

        viewHolder.binding.title.setText(viewHolder.configuration.key());
        model.getConfigurationValues(viewHolder.configuration.id()).observe(lifecycleOwner, wrenchConfigurationValues -> {
            if (wrenchConfigurationValues == null) {
                return;
            }

            WrenchScope defaultScope = model.getDefaultScopeLiveData().getValue();
            WrenchScope selectedScope = model.getSelectedScopeLiveData().getValue();

            WrenchConfigurationValue defaultScopedItem = getItemForScope(defaultScope, wrenchConfigurationValues);
            if (defaultScopedItem == null) {
                return;
            }

            viewHolder.binding.defaultValue.setText(defaultScopedItem.getValue());

            WrenchConfigurationValue selectedScopedItem = getItemForScope(selectedScope, wrenchConfigurationValues);
            if (selectedScopedItem != null && selectedScopedItem.getScope() != defaultScope.id()) {
                viewHolder.binding.defaultValue.setPaintFlags(viewHolder.binding.defaultValue.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.binding.customValue.setText(selectedScopedItem.getValue());
                viewHolder.binding.customValue.setVisibility(View.VISIBLE);
            } else {
                viewHolder.binding.customValue.setText(null);
                viewHolder.binding.defaultValue.setPaintFlags(viewHolder.binding.defaultValue.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                viewHolder.binding.customValue.setVisibility(View.GONE);
            }
        });
        viewHolder.binding.getRoot().setOnClickListener(view -> listener.configurationClicked(items.get(viewHolder.getAdapterPosition())));
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
        void configurationClicked(WrenchConfiguration configuration);
    }


    private class ConfigurationListDiffCallbacks extends DiffUtil.Callback {
        private final ArrayList<WrenchConfiguration> oldItems;
        private final List<WrenchConfiguration> newItems;

        ConfigurationListDiffCallbacks(ArrayList<WrenchConfiguration> oldItems, List<WrenchConfiguration> newItems) {
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
            WrenchConfiguration oldItem = oldItems.get(oldItemPosition);
            WrenchConfiguration newItem = newItems.get(newItemPosition);

            return oldItem.id() == newItem.id();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            WrenchConfiguration oldItem = oldItems.get(oldItemPosition);
            WrenchConfiguration newItem = newItems.get(newItemPosition);

            return TextUtils.equals(oldItem.key(), newItem.key()) &&
                    TextUtils.equals(oldItem.type(), newItem.type());
        }
    }
}
