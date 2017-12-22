package com.izettle.wrench.configurationlist;

import android.support.v7.widget.RecyclerView;

import com.izettle.wrench.database.WrenchConfigurationWithValues;
import com.izettle.wrench.databinding.ConfigurationListItemBinding;

public class ConfigurationViewHolder extends RecyclerView.ViewHolder {
    public final ConfigurationListItemBinding binding;
    public WrenchConfigurationWithValues configuration;

    ConfigurationViewHolder(ConfigurationListItemBinding binding, WrenchConfigurationWithValues configuration) {
        super(binding.getRoot());
        this.binding = binding;
        this.configuration = configuration;
    }
}