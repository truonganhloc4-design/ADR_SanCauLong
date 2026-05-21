package com.example.tlqlbadminton.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.adapter.SimpleTextAdapter;
import com.example.tlqlbadminton.model.HoaDon;
import com.example.tlqlbadminton.sqlite.HoaDonDAO;

import java.util.ArrayList;
import java.util.List;

public class ThongKeFragment extends Fragment {

    private TextView tvTongDoanhThu;
    private TextView tvSoLuotDat;
    private TextView chipNgay;
    private TextView chipTuan;
    private TextView chipThang;
    private TextView tvEmpty;
    private SimpleTextAdapter adapter;
    private HoaDonDAO hoaDonDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_thong_ke, container, false);
        hoaDonDAO = new HoaDonDAO(requireContext());

        tvTongDoanhThu = root.findViewById(R.id.tvTongDoanhThu);
        tvSoLuotDat = root.findViewById(R.id.tvSoLuotDat);
        chipNgay = root.findViewById(R.id.chipNgay);
        chipTuan = root.findViewById(R.id.chipTuan);
        chipThang = root.findViewById(R.id.chipThang);
        tvEmpty = root.findViewById(R.id.tvEmptyThongKe);
        RecyclerView rvHoaDon = root.findViewById(R.id.rvHoaDon);
        rvHoaDon.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SimpleTextAdapter();
        rvHoaDon.setAdapter(adapter);

        setupFilterChips();
        refreshStats();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshStats();
    }

    private void setupFilterChips() {
        chipNgay.setOnClickListener(v -> setFilter(chipNgay, chipTuan, chipThang));
        chipTuan.setOnClickListener(v -> setFilter(chipTuan, chipNgay, chipThang));
        chipThang.setOnClickListener(v -> setFilter(chipThang, chipNgay, chipTuan));
    }

    private void setFilter(TextView active, TextView... inactives) {
        active.setBackgroundResource(R.drawable.bg_chip_filter_active);
        active.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary));
        active.setTypeface(null, android.graphics.Typeface.BOLD);
        for (TextView tv : inactives) {
            tv.setBackgroundResource(R.drawable.bg_chip_filter_inactive);
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
            tv.setTypeface(null, android.graphics.Typeface.NORMAL);
        }
        refreshStats();
    }

    private void refreshStats() {
        if (hoaDonDAO == null || tvTongDoanhThu == null) return;
        List<HoaDon> hoaDonList = hoaDonDAO.getAllHoaDon();
        tvTongDoanhThu.setText(formatCurrency((long) hoaDonDAO.getTongDoanhThu()));
        tvSoLuotDat.setText(String.valueOf(hoaDonDAO.countHoaDon()));
        tvEmpty.setVisibility(hoaDonList.isEmpty() ? View.VISIBLE : View.GONE);

        List<SimpleTextAdapter.Row> rows = new ArrayList<>();
        for (HoaDon hoaDon : hoaDonList) {
            rows.add(new SimpleTextAdapter.Row(
                    hoaDon.getMaHD() + " - " + formatCurrency((long) hoaDon.getTongThanhToan()) + " VNĐ",
                    "Ngày lập: " + hoaDon.getNgayLap() + " | Phiếu #" + hoaDon.getMaPhieu()));
        }
        adapter.submitList(rows);
    }

    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }
}
