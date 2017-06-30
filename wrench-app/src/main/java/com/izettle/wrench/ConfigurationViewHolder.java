package com.izettle.wrench;

import android.support.v7.widget.RecyclerView;

import com.izettle.wrench.databinding.ConfigurationListItemBinding;

public class ConfigurationViewHolder extends RecyclerView.ViewHolder {
    public final ConfigurationListItemBinding binding;

    ConfigurationViewHolder(ConfigurationListItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}