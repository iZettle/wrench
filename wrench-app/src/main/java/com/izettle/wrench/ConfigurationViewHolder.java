package com.izettle.wrench;

import android.support.v7.widget.RecyclerView;

import com.izettle.wrench.database.WrenchConfiguration;
import com.izettle.wrench.databinding.ConfigurationListItemBinding;

public class ConfigurationViewHolder extends RecyclerView.ViewHolder {
    public final ConfigurationListItemBinding binding;
    public WrenchConfiguration configuration;

    ConfigurationViewHolder(ConfigurationListItemBinding binding, WrenchConfiguration configuration) {
        super(binding.getRoot());
        this.binding = binding;
        this.configuration = configuration;
    }
}