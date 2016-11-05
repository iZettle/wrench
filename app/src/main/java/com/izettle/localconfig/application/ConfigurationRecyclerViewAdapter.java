package com.izettle.localconfig.application;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.izettle.localconfig.application.databinding.ConfigurationListItemBinding;
import com.izettle.localconfig.application.library.ConfigurationFull;
import com.izettle.localconfig.application.library.ConfigurationFullContentValueProducer;
import com.izettle.localconfiguration.ConfigProviderHelper;

import java.util.ArrayList;
import java.util.List;


public class ConfigurationRecyclerViewAdapter extends RecyclerView.Adapter<ConfigurationRecyclerViewAdapter.ConfigurationViewHolder> {

    private static final String TAG = "localconfig";

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
    public ConfigurationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConfigurationListItemBinding fragmentConfigurationBinding = ConfigurationListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ConfigurationViewHolder(fragmentConfigurationBinding);
    }

    @Override
    public void onBindViewHolder(final ConfigurationViewHolder holder, int position) {
        ConfigurationFull configuration = items.get(position);

        holder.configurationListItemBinding.id.setText(String.valueOf(configuration._id));
        holder.configurationListItemBinding.content.setText(configuration.key + " = " + configuration.value + " :: " + configuration.applicationId);

        holder.configurationListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ConfigurationFull conf = items.get(holder.getAdapterPosition());
                final ConfigurationFullContentValueProducer configurationFullContentValueProducer = new ConfigurationFullContentValueProducer();

                if (conf.type.equals(String.class.getName())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle(conf.key);
                    final EditText input = new EditText(v.getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setText(conf.value);
                    input.selectAll();
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            conf.value = input.getText().toString();
                            v.getContext().getContentResolver().update(ConfigProviderHelper.configurationUri().buildUpon().appendPath(String.valueOf(conf._id)).build(), configurationFullContentValueProducer.toContentValues(conf), null, null);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    dialog.show();

                } else if (conf.type.equals(Integer.class.getName())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle(conf.key);
                    final EditText input = new EditText(v.getContext());
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setText(conf.value);
                    input.selectAll();
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            conf.value = input.getText().toString();
                            v.getContext().getContentResolver().update(ConfigProviderHelper.configurationUri().buildUpon().appendPath(String.valueOf(conf._id)).build(), configurationFullContentValueProducer.toContentValues(conf), null, null);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    dialog.show();
                } else if (conf.type.equals(Boolean.class.getName())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle(conf.key);
                    final CheckBox input = new CheckBox(v.getContext());
                    input.setChecked(Boolean.valueOf(conf.value));
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            conf.value = String.valueOf(input.isChecked());
                            v.getContext().getContentResolver().update(ConfigProviderHelper.configurationUri().buildUpon().appendPath(String.valueOf(conf._id)).build(), configurationFullContentValueProducer.toContentValues(conf), null, null);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
                Log.d(TAG, conf._id + " was clicked");
            }
        });
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

    class ConfigurationViewHolder extends RecyclerView.ViewHolder {
        private final ConfigurationListItemBinding configurationListItemBinding;

        private ConfigurationViewHolder(ConfigurationListItemBinding configurationListItemBinding) {
            super(configurationListItemBinding.getRoot());
            this.configurationListItemBinding = configurationListItemBinding;
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
    }
}
