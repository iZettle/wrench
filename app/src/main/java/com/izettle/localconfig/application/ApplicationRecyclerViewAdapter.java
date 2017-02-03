package com.izettle.localconfig.application;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.izettle.localconfig.application.databinding.ApplicationListItemBinding;
import com.izettle.localconfig.application.library.Application;

import java.util.ArrayList;


public class ApplicationRecyclerViewAdapter extends RecyclerView.Adapter<ApplicationRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "localconfig";

    private final ArrayList<Application> mValues = new ArrayList<>();

    public ApplicationRecyclerViewAdapter(ArrayList<Application> newApplications) {
        mValues.addAll(newApplications);
    }

    public void setItems(ArrayList<Application> items) {
        mValues.clear();
        mValues.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ApplicationListItemBinding applicationListItemBinding = ApplicationListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(applicationListItemBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Application application = mValues.get(position);

        try {
            PackageManager packageManager = holder.applicationListItemBinding.getRoot().getContext().getPackageManager();
            Drawable icon = packageManager.getApplicationIcon(application.applicationId);
            holder.applicationListItemBinding.applicationIcon.setImageDrawable(icon);
            holder.applicationListItemBinding.status.setText("");

        } catch (PackageManager.NameNotFoundException e) {
            holder.applicationListItemBinding.applicationIcon.setImageResource(R.drawable.ic_report_black_24dp);
            holder.applicationListItemBinding.status.setText(R.string.not_installed);
            e.printStackTrace();
        }
        holder.applicationListItemBinding.applicationName.setText(application.applicationLabel);

        holder.applicationListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Application application1 = mValues.get(holder.getAdapterPosition());

                Context context = v.getContext();
                context.startActivity(ConfigurationsActivity.newIntent(context, application1));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ApplicationListItemBinding applicationListItemBinding;

        private ViewHolder(ApplicationListItemBinding applicationListItemBinding) {
            super(applicationListItemBinding.getRoot());
            this.applicationListItemBinding = applicationListItemBinding;
        }
    }
}
