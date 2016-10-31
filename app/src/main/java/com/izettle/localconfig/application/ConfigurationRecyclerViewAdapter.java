package com.izettle.localconfig.application;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
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


public class ConfigurationRecyclerViewAdapter extends RecyclerView.Adapter<ConfigurationRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "localconfig";

    private final ArrayList<ConfigurationFull> mValues = new ArrayList<>();

    public ConfigurationRecyclerViewAdapter(ArrayList<ConfigurationFull> newConfigurations) {
        mValues.addAll(newConfigurations);
    }

    public void setItems(ArrayList<ConfigurationFull> items) {
        mValues.clear();
        mValues.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConfigurationListItemBinding fragmentConfigurationBinding = ConfigurationListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(fragmentConfigurationBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ConfigurationFull configuration = mValues.get(position);

        holder.configurationListItemBinding.id.setText(String.valueOf(configuration._id));
        holder.configurationListItemBinding.content.setText(configuration.key + " = " + configuration.value + " :: " + configuration.applicationId);

        holder.configurationListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ConfigurationFull conf = mValues.get(holder.getAdapterPosition());
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
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ConfigurationListItemBinding configurationListItemBinding;

        private ViewHolder(ConfigurationListItemBinding configurationListItemBinding) {
            super(configurationListItemBinding.getRoot());
            this.configurationListItemBinding = configurationListItemBinding;
        }
    }
}
