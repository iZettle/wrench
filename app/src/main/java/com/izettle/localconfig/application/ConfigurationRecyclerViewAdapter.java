package com.izettle.localconfig.application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.izettle.localconfig.application.databinding.ConfigurationBooleanListItemBinding;
import com.izettle.localconfig.application.databinding.ConfigurationIntegerListItemBinding;
import com.izettle.localconfig.application.databinding.ConfigurationStringListItemBinding;
import com.izettle.localconfig.application.library.ApplicationConfigProviderHelper;
import com.izettle.localconfig.application.library.ConfigurationFull;
import com.izettle.localconfig.application.library.ConfigurationFullContentValueProducer;

import java.util.ArrayList;
import java.util.List;


public class ConfigurationRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_STRING = 1;
    private static final int VIEW_TYPE_INTEGER = 2;
    private static final int VIEW_TYPE_BOOLEAN = 3;

    private final ArrayList<ConfigurationFull> items = new ArrayList<>();

    public ConfigurationRecyclerViewAdapter(ArrayList<ConfigurationFull> newConfigurations) {
        items.addAll(newConfigurations);
    }

    public void setItems(ArrayList<ConfigurationFull> items) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ConfigurationListDiffCallbacks(this.items, items));
        diffResult.dispatchUpdatesTo(this);
        this.items.clear();
        this.items.addAll(items);
    }

    @Override
    public int getItemViewType(int position) {
        ConfigurationFull configurationFull = items.get(position);
        if (TextUtils.equals(configurationFull.type, String.class.getName())) {
            return VIEW_TYPE_STRING;

        } else if (TextUtils.equals(configurationFull.type, Integer.class.getName())) {
            return VIEW_TYPE_INTEGER;

        } else if (TextUtils.equals(configurationFull.type, Boolean.class.getName())) {
            return VIEW_TYPE_BOOLEAN;

        } else {
            throw new IllegalStateException("Unknown configuration type");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_STRING: {
                ConfigurationStringListItemBinding configurationStringListItemBinding = ConfigurationStringListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ConfigurationStringViewHolder(configurationStringListItemBinding);
            }
            case VIEW_TYPE_INTEGER: {
                ConfigurationIntegerListItemBinding configurationIntegerListItemBinding = ConfigurationIntegerListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ConfigurationIntegerViewHolder(configurationIntegerListItemBinding);
            }
            case VIEW_TYPE_BOOLEAN: {
                ConfigurationBooleanListItemBinding configurationBooleanListItemBinding = ConfigurationBooleanListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ConfigurationBooleanViewHolder(configurationBooleanListItemBinding);
            }
            default: {
                throw new IllegalStateException("Unknown view type");
            }
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        onBindViewHolder(holder, position, new ArrayList<>());
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        ConfigurationFull configuration = items.get(position);

        final ConfigurationFullContentValueProducer configurationFullContentValueProducer = new ConfigurationFullContentValueProducer();

        if (holder instanceof ConfigurationStringViewHolder) {
            ConfigurationStringViewHolder viewHolder = (ConfigurationStringViewHolder) holder;
            viewHolder.binding.layout.setHint(configuration.key);
            if (!TextUtils.equals(viewHolder.binding.value.getText(), configuration.value)) {
                viewHolder.binding.value.setText(configuration.value);
            }
            viewHolder.binding.value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        ConfigurationFull configurationFull = items.get(holder.getAdapterPosition());
                        configurationFull.value = String.valueOf(((TextInputEditText) view).getText());

                        view.getContext().getContentResolver().update(ApplicationConfigProviderHelper.configurationUri(configurationFull._id), configurationFullContentValueProducer.toContentValues(configurationFull), null, null);
                    }
                }
            });

        } else if (holder instanceof ConfigurationIntegerViewHolder) {
            ConfigurationIntegerViewHolder viewHolder = (ConfigurationIntegerViewHolder) holder;
            viewHolder.binding.layout.setHint(configuration.key);
            if (!TextUtils.equals(viewHolder.binding.value.getText(), configuration.value)) {
                viewHolder.binding.value.setText(configuration.value);
            }
            viewHolder.binding.value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        ConfigurationFull configurationFull = items.get(holder.getAdapterPosition());
                        configurationFull.value = String.valueOf(((TextInputEditText) view).getText());
                        view.getContext().getContentResolver().update(ApplicationConfigProviderHelper.configurationUri(configurationFull._id), configurationFullContentValueProducer.toContentValues(configurationFull), null, null);
                    }
                }
            });

        } else if (holder instanceof ConfigurationBooleanViewHolder) {
            ConfigurationBooleanViewHolder viewHolder = (ConfigurationBooleanViewHolder) holder;
            viewHolder.binding.layout.setText(configuration.key);
            viewHolder.binding.layout.setChecked(Boolean.valueOf(configuration.value));

            viewHolder.binding.layout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ConfigurationFull configurationFull = items.get(holder.getAdapterPosition());
                    configurationFull.value = String.valueOf(compoundButton.isChecked());
                    compoundButton.getContext().getContentResolver().update(ApplicationConfigProviderHelper.configurationUri(configurationFull._id), configurationFullContentValueProducer.toContentValues(configurationFull), null, null);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ItemTouchHelper getItemTouchHelper(final SwipeDelete swipeDelete) {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                ConfigurationFull item = items.get(viewHolder.getAdapterPosition());
                swipeDelete.swiped(item);
            }
        });
    }

    public interface SwipeDelete {
        void swiped(ConfigurationFull configuration);
    }

    class ConfigurationBooleanViewHolder extends RecyclerView.ViewHolder {
        private final ConfigurationBooleanListItemBinding binding;

        private ConfigurationBooleanViewHolder(ConfigurationBooleanListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class ConfigurationStringViewHolder extends RecyclerView.ViewHolder {
        private final ConfigurationStringListItemBinding binding;

        private ConfigurationStringViewHolder(ConfigurationStringListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class ConfigurationIntegerViewHolder extends RecyclerView.ViewHolder {
        private final ConfigurationIntegerListItemBinding binding;

        private ConfigurationIntegerViewHolder(ConfigurationIntegerListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class ConfigurationListDiffCallbacks extends DiffUtil.Callback {
        private final List<ConfigurationFull> mOldItems;
        private final List<ConfigurationFull> mNewItems;

        public ConfigurationListDiffCallbacks(List<ConfigurationFull> oldItems, List<ConfigurationFull> newItems) {
            mOldItems = oldItems;
            mNewItems = newItems;
        }

        @Override
        public int getOldListSize() {
            return mOldItems.size();
        }

        @Override
        public int getNewListSize() {
            return mNewItems.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            ConfigurationFull oldItem = mOldItems.get(oldItemPosition);
            ConfigurationFull newItem = mNewItems.get(newItemPosition);

            return oldItem._id == newItem._id;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            ConfigurationFull oldItem = mOldItems.get(oldItemPosition);
            ConfigurationFull newItem = mNewItems.get(newItemPosition);

            return TextUtils.equals(oldItem.key, newItem.key) &&
                    TextUtils.equals(oldItem.value, newItem.value) &&
                    TextUtils.equals(oldItem.type, newItem.type);
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            ConfigurationFull oldItem = mOldItems.get(oldItemPosition);
            ConfigurationFull newItem = mNewItems.get(newItemPosition);

            Bundle bundle = new Bundle();

            if (!TextUtils.equals(oldItem.value, newItem.value)) {
                bundle.putString("string", newItem.value);
            }

            return bundle;
        }
    }
}
