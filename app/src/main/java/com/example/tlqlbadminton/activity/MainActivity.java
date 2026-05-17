package com.example.tlqlbadminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tlqlbadminton.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    // Summary stats
    private TextView tvCountAvailable;
    private TextView tvCountOccupied;
    private TextView tvCountBooked;

    // Court cards action buttons
    private Button btnCourt1Action;
    private Button btnCourt2Action;
    private Button btnCourt3Action;
    private Button btnCourt4Action;

    // Bottom navigation
    private LinearLayout navSoDo;
    private LinearLayout navThongKe;
    private LinearLayout navLichSu;
    private LinearLayout navCaiDat;

    // FAB
    private ExtendedFloatingActionButton fabDatSanNhanh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        setupNavigation();
        setupCourtActions();
        loadCourtData();
    }

    private void bindViews() {
        tvCountAvailable = findViewById(R.id.tvCountAvailable);
        tvCountOccupied  = findViewById(R.id.tvCountOccupied);
        tvCountBooked    = findViewById(R.id.tvCountBooked);

        btnCourt1Action  = findViewById(R.id.btnCourt1Action);
        btnCourt2Action  = findViewById(R.id.btnCourt2Action);
        btnCourt3Action  = findViewById(R.id.btnCourt3Action);
        btnCourt4Action  = findViewById(R.id.btnCourt4Action);

        navSoDo    = findViewById(R.id.navSoDo);
        navThongKe = findViewById(R.id.navThongKe);
        navLichSu  = findViewById(R.id.navLichSu);
        navCaiDat  = findViewById(R.id.navCaiDat);

        fabDatSanNhanh = findViewById(R.id.fabDatSanNhanh);
    }

    private void setupNavigation() {
        navThongKe.setOnClickListener(v -> {
            startActivity(new Intent(this, ThongKeActivity.class));
        });

        navLichSu.setOnClickListener(v -> {
            startActivity(new Intent(this, ThongKeActivity.class));
        });

        navCaiDat.setOnClickListener(v -> {
            startActivity(new Intent(this, CauHinhSanActivity.class));
        });

        fabDatSanNhanh.setOnClickListener(v -> {
            startActivity(new Intent(this, DatLichTruocActivity.class));
        });
    }

    private void setupCourtActions() {
        // Sân 1: Available → Nhận sân
        btnCourt1Action.setOnClickListener(v -> openNhanSan("1"));

        // Sân 2: Occupied → Trả sân (goes to Thanh toán)
        btnCourt2Action.setOnClickListener(v -> openThanhToan("2", "#BK-00002"));

        // Sân 3: Booked → Chi tiết
        btnCourt3Action.setOnClickListener(v -> openNhanSan("3"));

        // Sân 4: Available → Nhận sân
        btnCourt4Action.setOnClickListener(v -> openNhanSan("4"));
    }

    private void openNhanSan(String courtId) {
        Intent intent = new Intent(this, NhanSanActivity.class);
        intent.putExtra(NhanSanActivity.EXTRA_COURT_ID, courtId);
        startActivity(intent);
    }

    private void openThanhToan(String courtId, String bookingCode) {
        Intent intent = new Intent(this, ThanhToanActivity.class);
        intent.putExtra(ThanhToanActivity.EXTRA_COURT_ID, courtId);
        intent.putExtra(ThanhToanActivity.EXTRA_BOOKING_CODE, bookingCode);
        startActivity(intent);
    }

    /** Load dữ liệu sân từ DB — kết nối với DatabaseHelper sau */
    private void loadCourtData() {
        // TODO: load from DatabaseHelper
        // Tạm thời set static values
        tvCountAvailable.setText("2");
        tvCountOccupied.setText("1");
        tvCountBooked.setText("1");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh court statuses when returning
        loadCourtData();
    }
}