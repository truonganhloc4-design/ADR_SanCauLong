package com.example.tlqlbadminton.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tlqlbadminton.R;

public class ThongKeActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvTongDoanhThu;
    private TextView tvSoLuotDat;
    private TextView tabThongKe;
    private TextView tabLichSu;
    private View tabIndicator;

    private TextView chipNgay;
    private TextView chipTuan;
    private TextView chipThang;

    private RecyclerView rvHoaDon;

    private String currentFilter = "ngay"; // ngay, tuan, thang
    private boolean isThongKeTab = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        bindViews();
        setupToolbar();
        setupTabs();
        setupFilterChips();
        loadData();
    }

    private void bindViews() {
        btnBack         = findViewById(R.id.btnBack);
        tvTongDoanhThu  = findViewById(R.id.tvTongDoanhThu);
        tvSoLuotDat     = findViewById(R.id.tvSoLuotDat);
        tabThongKe      = findViewById(R.id.tabThongKe);
        tabLichSu       = findViewById(R.id.tabLichSu);
        tabIndicator    = findViewById(R.id.tabIndicator);
        chipNgay        = findViewById(R.id.chipNgay);
        chipTuan        = findViewById(R.id.chipTuan);
        chipThang       = findViewById(R.id.chipThang);
        rvHoaDon        = findViewById(R.id.rvHoaDon);

        rvHoaDon.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        btnBack.setOnClickListener(v -> onBackPressed());
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
        chipNgay.setOnClickListener(v  -> setFilter("ngay",  chipNgay,  chipTuan,  chipThang));
        chipTuan.setOnClickListener(v  -> setFilter("tuan",  chipTuan,  chipNgay,  chipThang));
        chipThang.setOnClickListener(v -> setFilter("thang", chipThang, chipNgay,  chipTuan));
    }

    private void setFilter(String filter, TextView active, TextView... inactives) {
        currentFilter = filter;
        active.setBackgroundResource(R.drawable.bg_chip_filter_active);
        active.setTextColor(getColor(R.color.tertiary));
        for (TextView tv : inactives) {
            tv.setBackgroundResource(R.drawable.bg_chip_filter_inactive);
            tv.setTextColor(getColor(R.color.white));
        }
        loadData();
    }

    private void loadData() {
        // TODO: Query from DatabaseHelper based on currentFilter
        // Static placeholder data
        tvTongDoanhThu.setText("2.450.000");
        tvSoLuotDat.setText("18");
        // TODO: Load invoices into RecyclerView adapter
    }
}
