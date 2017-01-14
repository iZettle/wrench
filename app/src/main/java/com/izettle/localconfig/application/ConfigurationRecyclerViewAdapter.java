package com.izettle.localconfig.application;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.izettle.localconfig.application.databinding.ConfigurationBooleanListItemBinding;
import com.izettle.localconfig.application.databinding.ConfigurationEnumListItemBinding;
import com.izettle.localconfig.application.databinding.ConfigurationIntegerListItemBinding;
import com.izettle.localconfig.application.databinding.ConfigurationStringListItemBinding;
import com.izettle.localconfig.application.library.ApplicationConfigProviderHelper;
import com.izettle.localconfig.application.library.ConfigurationFull;
import com.izettle.localconfig.application.library.ConfigurationFullContentValueProducer;
import com.izettle.localconfiguration.ConfigProviderHelper;
import com.izettle.localconfiguration.ConfigurationValue;
import com.izettle.localconfiguration.util.ConfigurationValueCursorParser;

import java.util.ArrayList;
import java.util.List;


public class ConfigurationRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_STRING = 1;
    private static final int VIEW_TYPE_INTEGER = 2;
    private static final int VIEW_TYPE_BOOLEAN = 3;
    private static final int VIEW_TYPE_ENUM = 4;

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

        } else if (TextUtils.equals(configurationFull.type, Enum.class.getName())) {
            return VIEW_TYPE_ENUM;

        } else {
            throw new IllegalStateException("Unknown configuration type " + configurationFull.type);
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
            case VIEW_TYPE_ENUM: {
                ConfigurationEnumListItemBinding configurationBooleanListItemBinding = ConfigurationEnumListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ConfigurationEnumViewHolder(configurationBooleanListItemBinding);
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
            viewHolder.binding.value.setOnFocusChangeListener((view, focus) -> {
                if (!focus && holder.getAdapterPosition() != -1) {
                    ConfigurationFull configurationFull = items.get(holder.getAdapterPosition());
                    configurationFull.value = String.valueOf(((TextInputEditText) view).getText());

                    view.getContext().getContentResolver().update(ApplicationConfigProviderHelper.configurationUri(configurationFull._id), configurationFullContentValueProducer.toContentValues(configurationFull), null, null);
                }
            });

        } else if (holder instanceof ConfigurationIntegerViewHolder) {
            ConfigurationIntegerViewHolder viewHolder = (ConfigurationIntegerViewHolder) holder;
            viewHolder.binding.layout.setHint(configuration.key);
            if (!TextUtils.equals(viewHolder.binding.value.getText(), configuration.value)) {
                viewHolder.binding.value.setText(configuration.value);
            }
            viewHolder.binding.value.setOnFocusChangeListener((view, focus) -> {
                if (!focus && holder.getAdapterPosition() != -1) {
                    ConfigurationFull configurationFull = items.get(holder.getAdapterPosition());
                    configurationFull.value = String.valueOf(((TextInputEditText) view).getText());
                    view.getContext().getContentResolver().update(ApplicationConfigProviderHelper.configurationUri(configurationFull._id), configurationFullContentValueProducer.toContentValues(configurationFull), null, null);
                }
            });

        } else if (holder instanceof ConfigurationBooleanViewHolder) {
            ConfigurationBooleanViewHolder viewHolder = (ConfigurationBooleanViewHolder) holder;
            viewHolder.binding.layout.setText(configuration.key);
            viewHolder.binding.layout.setChecked(Boolean.valueOf(configuration.value));

            viewHolder.binding.layout.setOnCheckedChangeListener((compoundButton, checked) -> {
                ConfigurationFull configurationFull = items.get(holder.getAdapterPosition());
                configurationFull.value = String.valueOf(compoundButton.isChecked());
                compoundButton.getContext().getContentResolver().update(ApplicationConfigProviderHelper.configurationUri(configurationFull._id), configurationFullContentValueProducer.toContentValues(configurationFull), null, null);
            });
        } else if (holder instanceof ConfigurationEnumViewHolder) {
            ConfigurationEnumViewHolder viewHolder = (ConfigurationEnumViewHolder) holder;

            Context context = viewHolder.binding.getRoot().getContext();
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = null;

            int selectedPosition = 0;

            ArrayList<ConfigurationValue> configurationValues = new ArrayList<>();
            try {
                cursor = contentResolver.query(ConfigProviderHelper.configurationValueUri(),
                        ConfigurationValueCursorParser.PROJECTION,
                        ConfigurationValueCursorParser.Columns.CONFIGURATION_ID + " = ?", new String[]{String.valueOf(configuration._id)}, null);

                if (cursor != null && cursor.moveToFirst()) {
                    ConfigurationValueCursorParser configurationValueCursorParser = new ConfigurationValueCursorParser();
                    do {
                        ConfigurationValue configurationValue = configurationValueCursorParser.populateFromCursor(new ConfigurationValue(), cursor);
                        if (TextUtils.equals(configurationValue.value, configuration.value)) {
                            selectedPosition = configurationValues.size();
                        }
                        configurationValues.add(configurationValue);
                    }
                    while (cursor.moveToNext());
                }
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }

            final ArrayAdapter<ConfigurationValue> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, configurationValues);
            viewHolder.binding.layout.setAdapter(adapter);
            viewHolder.binding.layout.setSelection(selectedPosition);
            viewHolder.binding.layout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                    ConfigurationFull configurationFull = items.get(holder.getAdapterPosition());
                    configurationFull.value = adapter.getItem(pos).value;
                    view.getContext().getContentResolver().update(ApplicationConfigProviderHelper.configurationUri(configurationFull._id), configurationFullContentValueProducer.toContentValues(configurationFull), null, null);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

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

    private class ConfigurationBooleanViewHolder extends RecyclerView.ViewHolder {
        private final ConfigurationBooleanListItemBinding binding;

        private ConfigurationBooleanViewHolder(ConfigurationBooleanListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class ConfigurationEnumViewHolder extends RecyclerView.ViewHolder {
        private final ConfigurationEnumListItemBinding binding;

        private ConfigurationEnumViewHolder(ConfigurationEnumListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class ConfigurationStringViewHolder extends RecyclerView.ViewHolder {
        private final ConfigurationStringListItemBinding binding;

        private ConfigurationStringViewHolder(ConfigurationStringListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class ConfigurationIntegerViewHolder extends RecyclerView.ViewHolder {
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
