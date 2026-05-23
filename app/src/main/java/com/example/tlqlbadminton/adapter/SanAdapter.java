package com.example.tlqlbadminton.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.model.San;

import java.util.List;

public class SanAdapter extends ArrayAdapter<San> {
    private final Activity context;
    private final int resource;
    private final OnSanActionListener listener;

    // Interface để Activity nhận sự kiện sửa/xóa từ từng dòng sân.
    public interface OnSanActionListener {
        void onSuaSan(San san);
        void onXoaSan(San san);
    }

    // Nhận danh sách sân, layout item và listener xử lý sửa/xóa.
    public SanAdapter(@NonNull Activity context, int resource,
                      @NonNull List<San> objects,
                      OnSanActionListener listener) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.listener = listener;
    }

    // Tạo hoặc tái sử dụng view cho từng dòng sân trong ListView.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View viewItem = convertView;
        // convertView khác null nghĩa là ListView đang tái sử dụng dòng cũ để tối ưu.
        if (viewItem == null) {
            viewItem = context.getLayoutInflater().inflate(resource, parent, false);
        }

        San san = getItem(position);
        if (san == null) return viewItem;

        TextView txtMaSan = viewItem.findViewById(R.id.txtMaSan);
        TextView txtTenSan = viewItem.findViewById(R.id.txtTenSan);
        TextView txtThongTinSan = viewItem.findViewById(R.id.txtThongTinSan);
        ImageView imgSuaSan = viewItem.findViewById(R.id.imgSuaSan);
        ImageView imgXoaSan = viewItem.findViewById(R.id.imgXoaSan);

        // Đổ dữ liệu sân lên các TextView trong layout_item_san.xml.
        txtMaSan.setText(String.format("%02d", san.getMaSan()));
        txtTenSan.setText(san.getTenSan());
        txtThongTinSan.setText(san.getLoaiSan() + " | " +
                formatCurrency((long) san.getGiaMoiGio()) + " VNĐ/giờ");

        // Adapter chỉ báo sự kiện; Activity mới là nơi xử lý sửa/xóa thật.
        imgSuaSan.setOnClickListener(v -> listener.onSuaSan(san));
        imgXoaSan.setOnClickListener(v -> listener.onXoaSan(san));
        return viewItem;
    }

    // Format số tiền kiểu 70000 -> 70.000.
    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }
}
