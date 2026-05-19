package com.example.tlqlbadminton.adapter;

import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.ViewHolder> {

    public static class Row {
        public final String title;
        public final String subtitle;

        public Row(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }
    }

    private final List<Row> rows = new ArrayList<>();

    public void submitList(List<Row> newRows) {
        rows.clear();
        rows.addAll(newRows);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout container = new LinearLayout(parent.getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        int padding = dp(parent, 14);
        container.setPadding(padding, padding, padding, padding);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dp(parent, 10));
        container.setLayoutParams(params);

        TextView title = new TextView(parent.getContext());
        title.setTypeface(null, Typeface.BOLD);
        title.setTextSize(15);

        TextView subtitle = new TextView(parent.getContext());
        subtitle.setTextSize(13);
        subtitle.setPadding(0, dp(parent, 4), 0, 0);

        container.addView(title);
        container.addView(subtitle);
        return new ViewHolder(container, title, subtitle);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Row row = rows.get(position);
        holder.title.setText(row.title);
        holder.subtitle.setText(row.subtitle);
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    private int dp(ViewGroup parent, int value) {
        float density = parent.getResources().getDisplayMetrics().density;
        return Math.round(value * density);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView subtitle;

        ViewHolder(@NonNull LinearLayout itemView, TextView title, TextView subtitle) {
            super(itemView);
            this.title = title;
            this.subtitle = subtitle;
        }
    }
}
