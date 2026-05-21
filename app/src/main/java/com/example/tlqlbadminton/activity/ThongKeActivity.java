package com.example.tlqlbadminton.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.adapter.SimpleTextAdapter;
import com.example.tlqlbadminton.model.HoaDon;
import com.example.tlqlbadminton.model.PhieuDatSan;
import com.example.tlqlbadminton.model.San;
import com.example.tlqlbadminton.sqlite.HoaDonDAO;
import com.example.tlqlbadminton.sqlite.PhieuDatSanDAO;
import com.example.tlqlbadminton.sqlite.SanDAO;

import java.util.ArrayList;
import java.util.List;

public class ThongKeActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvTongDoanhThu;
    private TextView tvSoLuotDat;
    private TextView tabThongKe;
    private TextView tabLichSu;
    private TextView chipNgay;
    private TextView chipTuan;
    private TextView chipThang;
    private SimpleTextAdapter adapter;
    private HoaDonDAO hoaDonDAO;
    private PhieuDatSanDAO phieuDatSanDAO;
    private SanDAO sanDAO;
    private boolean isThongKeTab = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        hoaDonDAO = new HoaDonDAO(this);
        phieuDatSanDAO = new PhieuDatSanDAO(this);
        sanDAO = new SanDAO(this);
        bindViews();
        setupToolbar();
        setupTabs();
        setupFilterChips();
        loadData();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTongDoanhThu = findViewById(R.id.tvTongDoanhThu);
        tvSoLuotDat = findViewById(R.id.tvSoLuotDat);
        tabThongKe = findViewById(R.id.tabThongKe);
        tabLichSu = findViewById(R.id.tabLichSu);
        chipNgay = findViewById(R.id.chipNgay);
        chipTuan = findViewById(R.id.chipTuan);
        chipThang = findViewById(R.id.chipThang);
        RecyclerView rvHoaDon = findViewById(R.id.rvHoaDon);
        rvHoaDon.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleTextAdapter();
        rvHoaDon.setAdapter(adapter);
    }

    private void setupToolbar() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupTabs() {
        tabThongKe.setOnClickListener(v -> {
            isThongKeTab = true;
            tabThongKe.setTextColor(getColor(R.color.primary));
            tabThongKe.setTypeface(null, android.graphics.Typeface.BOLD);
            tabLichSu.setTextColor(getColor(R.color.text_secondary));
            tabLichSu.setTypeface(null, android.graphics.Typeface.NORMAL);
            loadData();
        });

        tabLichSu.setOnClickListener(v -> {
            isThongKeTab = false;
            tabLichSu.setTextColor(getColor(R.color.primary));
            tabLichSu.setTypeface(null, android.graphics.Typeface.BOLD);
            tabThongKe.setTextColor(getColor(R.color.text_secondary));
            tabThongKe.setTypeface(null, android.graphics.Typeface.NORMAL);
            loadData();
        });
    }

    private void setupFilterChips() {
        chipNgay.setOnClickListener(v -> setFilter(chipNgay, chipTuan, chipThang));
        chipTuan.setOnClickListener(v -> setFilter(chipTuan, chipNgay, chipThang));
        chipThang.setOnClickListener(v -> setFilter(chipThang, chipNgay, chipTuan));
    }

    private void setFilter(TextView active, TextView... inactives) {
        active.setBackgroundResource(R.drawable.bg_chip_filter_active);
        active.setTextColor(getColor(R.color.tertiary));
        for (TextView tv : inactives) {
            tv.setBackgroundResource(R.drawable.bg_chip_filter_inactive);
            tv.setTextColor(getColor(R.color.text_secondary));
        }
        loadData();
    }

    private void loadData() {
        tvTongDoanhThu.setText(formatCurrency((long) hoaDonDAO.getTongDoanhThu()));
        tvSoLuotDat.setText(String.valueOf(hoaDonDAO.countHoaDon()));

        List<SimpleTextAdapter.Row> rows = new ArrayList<>();
        if (isThongKeTab) {
            for (HoaDon hoaDon : hoaDonDAO.getAllHoaDon()) {
                rows.add(new SimpleTextAdapter.Row(
                        hoaDon.getMaHD() + " - " + formatCurrency((long) hoaDon.getTongThanhToan()) + " VNĐ",
                        "Ngày lập: " + hoaDon.getNgayLap() + " | Phiếu #" + hoaDon.getMaPhieu()));
            }
        } else {
            for (PhieuDatSan phieu : phieuDatSanDAO.getPendingBookings()) {
                San san = sanDAO.getSanById(phieu.getMaSan());
                String tenSan = san != null ? san.getTenSan() : "Sân #" + phieu.getMaSan();
                rows.add(new SimpleTextAdapter.Row(
                        tenSan + " - " + phieu.getTenKhach(),
                        phieu.getNgayDat() + " | " + phieu.getKhungGioChoi()));
            }
        }
        adapter.submitList(rows);
    }

    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }
}
