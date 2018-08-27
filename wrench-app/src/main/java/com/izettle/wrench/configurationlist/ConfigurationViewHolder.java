package com.izettle.wrench.configurationlist;

import com.izettle.wrench.database.WrenchConfigurationWithValues;
import com.izettle.wrench.databinding.ConfigurationListItemBinding;

import androidx.recyclerview.widget.RecyclerView;

public class ConfigurationViewHolder extends RecyclerView.ViewHolder {
    public final ConfigurationListItemBinding binding;
    public WrenchConfigurationWithValues configuration;

    ConfigurationViewHolder(ConfigurationListItemBinding binding, WrenchConfigurationWithValues configuration) {
        super(binding.getRoot());
        this.binding = binding;
        this.configuration = configuration;
    }
}