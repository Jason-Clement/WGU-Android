package me.jasonclement.c196.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import me.jasonclement.c196.R;
import me.jasonclement.c196.entities.Entity;

import static me.jasonclement.c196.ui.MainActivity.EXTRA_ID;

public class ListAdapter<T extends Entity> {

    private AppListAdapter appListAdapter;
    public AppListAdapter getAppListAdapter() {
        return appListAdapter;
    }

    private Intent clickIntent;

    public ListAdapter(Context context, Intent clickIntent) {
        appListAdapter = new AppListAdapter(context);
        this.clickIntent = clickIntent;
    }

    public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppListHolder> {

        class AppListHolder extends RecyclerView.ViewHolder {
            private final TextView titleView;
            private final TextView subtitleView;

            private AppListHolder(View itemView) {
                super(itemView);
                this.titleView = itemView.findViewById(R.id.titleView);
                this.subtitleView = itemView.findViewById(R.id.subtitleView);
            }
        }

        private final LayoutInflater inflater;
        private List<T> items = Collections.emptyList();

        private AppListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public AppListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
            return new AppListHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull AppListHolder holder, int position) {
            T current = items.get(position);
            holder.itemView.setTag(current.getId());
            holder.titleView.setText(current.getTitle());
            holder.subtitleView.setVisibility(View.VISIBLE);
            if (current.getSubtitle().isEmpty())
                holder.subtitleView.setVisibility(View.GONE);
            else
                holder.subtitleView.setText(current.getSubtitle());
            holder.itemView.setOnClickListener(v -> {
                Context context = v.getContext();
                clickIntent.putExtra(EXTRA_ID, (int)v.getTag());
                context.startActivity(clickIntent);
            });
        }

        public void setItems(List<T> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
