package com.izettle.wrench.dialogs.scope;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.izettle.wrench.database.WrenchScope;
import com.izettle.wrench.databinding.SimpleListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ScopeRecyclerViewAdapter extends RecyclerView.Adapter<ScopeRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<WrenchScope> items = new ArrayList<>();
    private final Listener listener;

    public ScopeRecyclerViewAdapter(Listener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SimpleListItemBinding binding = SimpleListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WrenchScope wrenchScope = items.get(position);

        holder.binding.value.setText(wrenchScope.getName());
        holder.binding.getRoot().setOnClickListener(listener::onClick);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<WrenchScope> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public WrenchScope getItem(int position) {
        return items.get(position);
    }

    interface Listener {
        void onClick(View view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final SimpleListItemBinding binding;

        public ViewHolder(SimpleListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
