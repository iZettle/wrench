package se.eelde.localconfig;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import se.eelde.localconfig.databinding.ConfigurationListItemBinding;
import se.eelde.localconfiguration.library.Configuration;

public class ConfigurationRecyclerViewAdapter extends RecyclerView.Adapter<ConfigurationRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "localconfig";

    private final ArrayList<Configuration> mValues = new ArrayList<>();

    public ConfigurationRecyclerViewAdapter() {
    }

    public void setItems(ArrayList<Configuration> items) {
        mValues.clear();
        mValues.addAll(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConfigurationListItemBinding fragmentConfigurationBinding = ConfigurationListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(fragmentConfigurationBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Configuration configuration = mValues.get(position);

        holder.configurationListItemBinding.id.setText(String.valueOf(configuration._id));
        holder.configurationListItemBinding.content.setText(configuration.key + " = " + configuration.value + " :: " + configuration.applicationId);

        holder.configurationListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration conf = mValues.get(holder.getAdapterPosition());
                Context context = v.getContext();
                context.startActivity(EditConfigurationActivity.newIntent(context, conf));

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
