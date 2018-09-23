package com.izettle.wrench.applicationlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.izettle.wrench.database.WrenchApplication;
import com.izettle.wrench.databinding.ApplicationListItemBinding;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

public class ApplicationAdapter extends PagedListAdapter<WrenchApplication, ApplicationViewHolder> {
    private static final DiffUtil.ItemCallback<WrenchApplication> DIFF_CALLBACK = new DiffUtil.ItemCallback<WrenchApplication>() {
        @Override
        public boolean areItemsTheSame(@NonNull WrenchApplication oldApplication, @NonNull WrenchApplication newApplication) {
            return oldApplication.getId() == newApplication.getId();
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
