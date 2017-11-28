package com.izettle.wrench.applicationlist;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.izettle.wrench.ConfigurationsActivity;
import com.izettle.wrench.R;
import com.izettle.wrench.database.WrenchApplication;
import com.izettle.wrench.databinding.ApplicationListItemBinding;

import java.util.ArrayList;
import java.util.List;


public class ApplicationRecyclerViewAdapter extends RecyclerView.Adapter<ApplicationRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<WrenchApplication> items = new ArrayList<>();

    ApplicationRecyclerViewAdapter(List<WrenchApplication> newApplications) {
        items.addAll(newApplications);
    }

    void setItems(List<WrenchApplication> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ApplicationListItemBinding applicationListItemBinding = ApplicationListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(applicationListItemBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        WrenchApplication application = items.get(position);

        try {
            PackageManager packageManager = holder.binding.getRoot().getContext().getPackageManager();
            Drawable icon = packageManager.getApplicationIcon(application.packageName());
            holder.binding.applicationIcon.setImageDrawable(icon);
            holder.binding.status.setText("");

        } catch (PackageManager.NameNotFoundException e) {
            holder.binding.applicationIcon.setImageResource(R.drawable.ic_report_black_24dp);
            holder.binding.status.setText(R.string.not_installed);
            e.printStackTrace();
        }
        holder.binding.applicationName.setText(application.applicationLabel());

        holder.binding.getRoot().setOnClickListener(v -> v.getContext().startActivity(ConfigurationsActivity.newIntent(v.getContext(), items.get(holder.getAdapterPosition()).id())));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private final ApplicationListItemBinding binding;

        private ViewHolder(ApplicationListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
