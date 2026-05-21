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

    public interface OnSanActionListener {
        void onSuaSan(San san);
        void onXoaSan(San san);
    }

    public SanAdapter(@NonNull Activity context, int resource,
                      @NonNull List<San> objects,
                      OnSanActionListener listener) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View viewItem = convertView;
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

        txtMaSan.setText(String.format("%02d", san.getMaSan()));
        txtTenSan.setText(san.getTenSan());
        txtThongTinSan.setText(san.getLoaiSan() + " | " +
                formatCurrency((long) san.getGiaMoiGio()) + " VNĐ/giờ");

        imgSuaSan.setOnClickListener(v -> listener.onSuaSan(san));
        imgXoaSan.setOnClickListener(v -> listener.onXoaSan(san));
        return viewItem;
    }

    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }
}
