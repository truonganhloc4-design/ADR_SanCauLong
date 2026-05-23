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

    private static final int ITEM_PADDING_DP = 14;
    private static final int ITEM_BOTTOM_MARGIN_DP = 10;
    private static final int SUBTITLE_TOP_PADDING_DP = 4;

    public static class Row {
        public final String title;
        public final String subtitle;

        // Lưu dữ liệu cho một dòng: dòng trên là title, dòng dưới là subtitle.
        public Row(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }
    }

    private final List<Row> rows = new ArrayList<>();

    // Nhận danh sách mới từ Fragment/Activity rồi yêu cầu RecyclerView vẽ lại.
    public void submitList(List<Row> newRows) {
        rows.clear();
        rows.addAll(newRows);
        notifyDataSetChanged();
    }

    // Tạo giao diện cho một item gồm 2 TextView: title và subtitle.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout container = createItemContainer(parent);
        TextView title = createTitleTextView(parent);
        TextView subtitle = createSubtitleTextView(parent);

        container.addView(title);
        container.addView(subtitle);
        return new ViewHolder(container, title, subtitle);
    }

    // Tạo khung dọc cho item và set padding/margin.
    private LinearLayout createItemContainer(@NonNull ViewGroup parent) {
        LinearLayout container = new LinearLayout(parent.getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        int padding = dp(parent, ITEM_PADDING_DP);
        container.setPadding(padding, padding, padding, padding);

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dp(parent, ITEM_BOTTOM_MARGIN_DP));
        container.setLayoutParams(params);

        return container;
    }

    // Tạo TextView dòng tiêu đề, chữ đậm để dễ nhìn.
    private TextView createTitleTextView(@NonNull ViewGroup parent) {
        TextView title = new TextView(parent.getContext());
        title.setTypeface(null, Typeface.BOLD);
        title.setTextSize(15);
        return title;
    }

    // Tạo TextView dòng mô tả nhỏ hơn, nằm dưới title.
    private TextView createSubtitleTextView(@NonNull ViewGroup parent) {
        TextView subtitle = new TextView(parent.getContext());
        subtitle.setTextSize(13);
        subtitle.setPadding(0, dp(parent, SUBTITLE_TOP_PADDING_DP), 0, 0);
        return subtitle;
    }

    // Gán dữ liệu Row vào ViewHolder khi RecyclerView hiển thị item.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Row row = rows.get(position);
        holder.bind(row);
    }

    // Trả về số dòng cần hiển thị.
    @Override
    public int getItemCount() {
        return rows.size();
    }

    // Đổi đơn vị dp sang pixel theo mật độ màn hình.
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

        // Gán title/subtitle lên 2 TextView của item.
        void bind(Row row) {
            title.setText(row.title);
            subtitle.setText(row.subtitle);
        }
    }
}
