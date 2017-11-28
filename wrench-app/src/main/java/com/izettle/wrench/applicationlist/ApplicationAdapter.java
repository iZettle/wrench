package com.izettle.wrench.applicationlist;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.izettle.wrench.database.WrenchApplication;
import com.izettle.wrench.databinding.ApplicationListItemBinding;

public class ApplicationAdapter extends PagedListAdapter<WrenchApplication, ApplicationViewHolder> {
    private static final DiffCallback<WrenchApplication> DIFF_CALLBACK = new DiffCallback<WrenchApplication>() {
        @Override
        public boolean areItemsTheSame(@NonNull WrenchApplication oldApplication, @NonNull WrenchApplication newApplication) {
            return oldApplication.id() == newApplication.id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull WrenchApplication oldApplication, @NonNull WrenchApplication newApplication) {
            return oldApplication.equals(newApplication);
        }
    };

    ApplicationAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override
    public ApplicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ApplicationViewHolder(ApplicationListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ApplicationViewHolder holder, int position) {
        WrenchApplication application = getItem(position);

        if (application != null) {
            holder.bindTo(application);
        } else {
            holder.clear();
        }
    }
}
