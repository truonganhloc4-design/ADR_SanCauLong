package com.example.tlqlbadminton.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.model.PhieuDatSan;
import com.example.tlqlbadminton.model.San;
import com.example.tlqlbadminton.sqlite.DBHelper;
import com.example.tlqlbadminton.sqlite.PhieuDatSanDAO;
import com.example.tlqlbadminton.sqlite.SanDAO;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NhanSanActivity extends AppCompatActivity {

    public static final String EXTRA_COURT_ID = "extra_court_id";
    public static final String EXTRA_COURT_INDEX = "extra_court_index";

    private ImageButton btnBack;
    private TextView tvToolbarTitle;
    private TextView tvCourtName;
    private TextView tvCourtStatus;
    private TextView tvCourtPrice;
    private LinearLayout layoutKhachDatTruoc;
    private LinearLayout btnChonKhachDatTruoc;
    private TextView tvKhachDatTruocDuocChon;
    private EditText etTenKhach;
    private EditText etSoDienThoai;
    private EditText etTienCoc;
    private ImageView btnPickGioBatDau;
    private TextView tvGioBatDau;
    private Button btnHuy;
    private Button btnXacNhanNhanSan;

    private SanDAO sanDAO;
    private PhieuDatSanDAO phieuDatSanDAO;
    private List<PhieuDatSan> pendingBookings;
    private PhieuDatSan selectedBooking;
    private San currentSan;
    private int maSan = 1;
    private int startHour;
    private int startMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_san);

        sanDAO = new SanDAO(this);
        phieuDatSanDAO = new PhieuDatSanDAO(this);
        String courtId = getIntent().getStringExtra(EXTRA_COURT_ID);
        if (courtId != null) maSan = Integer.parseInt(courtId);

        bindViews();
        setCurrentTime();
        loadCourtInfo();
        setupToolbar();
        setupDropdownKhach();
        setupTimePicker();
        setupSubmit();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btnBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvCourtName = findViewById(R.id.tvCourtName);
        tvCourtStatus = findViewById(R.id.tvCourtStatus);
        tvCourtPrice = findViewById(R.id.tvCourtPrice);
        layoutKhachDatTruoc = findViewById(R.id.layoutKhachDatTruoc);
        btnChonKhachDatTruoc = findViewById(R.id.btnChonKhachDatTruoc);
        tvKhachDatTruocDuocChon = findViewById(R.id.tvKhachDatTruocDuocChon);
        etTenKhach = findViewById(R.id.etTenKhach);
        etSoDienThoai = findViewById(R.id.etSoDienThoai);
        etTienCoc = findViewById(R.id.etTienCoc);
        btnPickGioBatDau = findViewById(R.id.btnPickGioBatDau);
        tvGioBatDau = findViewById(R.id.tvGioBatDau);
        btnHuy = findViewById(R.id.btnHuy);
        btnXacNhanNhanSan = findViewById(R.id.btnXacNhanNhanSan);
    }

    private void loadCourtInfo() {
        currentSan = sanDAO.getSanById(maSan);
        String tenSan = currentSan != null ? currentSan.getTenSan() : "San " + maSan;
        tvToolbarTitle.setText("Nhan san - " + tenSan);
        tvCourtName.setText(tenSan);
        tvCourtStatus.setText(currentSan != null && currentSan.getTrangThai() == DBHelper.SAN_DANG_CHOI
                ? "Dang choi" : "Trong");
        tvCourtPrice.setText(currentSan != null
                ? formatCurrency((long) currentSan.getGiaMoiGio()) + " VND/gio"
                : "-- VND/gio");
    }

    private void setupToolbar() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupDropdownKhach() {
        pendingBookings = phieuDatSanDAO.getPendingBookingsBySan(maSan);
        if (pendingBookings.isEmpty()) {
            layoutKhachDatTruoc.setVisibility(View.GONE);
            return;
        }

        layoutKhachDatTruoc.setVisibility(View.VISIBLE);
        btnChonKhachDatTruoc.setOnClickListener(v -> {
            String[] items = new String[pendingBookings.size() + 1];
            items[0] = "Khach vang lai";
            for (int i = 0; i < pendingBookings.size(); i++) {
                PhieuDatSan phieu = pendingBookings.get(i);
                items[i + 1] = phieu.getTenKhach() + " - " + phieu.getNgayDat()
                        + " - " + phieu.getKhungGioChoi();
            }
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Chon khach dat truoc")
                    .setItems(items, (dialog, which) -> selectBooking(which, items))
                    .show();
        });
    }

    private void selectBooking(int which, String[] items) {
        if (which == 0) {
            selectedBooking = null;
            tvKhachDatTruocDuocChon.setText("Khach vang lai");
            etTenKhach.setText("");
            etSoDienThoai.setText("");
            etTienCoc.setText("0");
            etTenKhach.setEnabled(true);
            etSoDienThoai.setEnabled(true);
            return;
        }

        selectedBooking = pendingBookings.get(which - 1);
        tvKhachDatTruocDuocChon.setText(items[which]);
        etTenKhach.setText(selectedBooking.getTenKhach());
        etSoDienThoai.setText(selectedBooking.getSoDienThoai());
        etTienCoc.setText(String.valueOf((long) selectedBooking.getTienCoc()));
        etTenKhach.setEnabled(false);
        etSoDienThoai.setEnabled(false);
    }

    private void setCurrentTime() {
        Calendar now = Calendar.getInstance();
        startHour = now.get(Calendar.HOUR_OF_DAY);
        startMinute = now.get(Calendar.MINUTE);
        updateStartTimeLabel();
    }

    private void setupTimePicker() {
        btnPickGioBatDau.setOnClickListener(v ->
                new TimePickerDialog(this, (view, h, min) -> {
                    startHour = h;
                    startMinute = min;
                    updateStartTimeLabel();
                }, startHour, startMinute, true).show());
    }

    private void updateStartTimeLabel() {
        tvGioBatDau.setText(String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute));
    }

    private void setupSubmit() {
        btnHuy.setOnClickListener(v -> finish());
        btnXacNhanNhanSan.setOnClickListener(v -> submitNhanSan());
    }

    private void submitNhanSan() {
        if (phieuDatSanDAO.getActivePhieuBySan(maSan) != null) {
            Toast.makeText(this, "San nay dang co ca choi", Toast.LENGTH_SHORT).show();
            return;
        }

        long gioBatDau = buildTodayTimestamp();
        if (gioBatDau > System.currentTimeMillis()) {
            Toast.makeText(this, "Gio bat dau khong duoc lon hon hien tai", Toast.LENGTH_SHORT).show();
            return;
        }

        String tenKhach = etTenKhach.getText().toString().trim();
        String sdt = etSoDienThoai.getText().toString().trim();
        double tienCoc = parseMoney(etTienCoc.getText().toString().trim());

        if (TextUtils.isEmpty(tenKhach)) {
            Toast.makeText(this, "Vui long nhap ten khach hang", Toast.LENGTH_SHORT).show();
            etTenKhach.requestFocus();
            return;
        }

        boolean ok;
        if (selectedBooking != null) {
            ok = phieuDatSanDAO.nhanSanTuPhieuDat(selectedBooking.getMaPhieu(), gioBatDau);
        } else {
            ok = phieuDatSanDAO.insertNhanSanMoi(maSan, tenKhach, sdt, tienCoc, gioBatDau) != -1;
        }

        Toast.makeText(this, ok ? "Da nhan san thanh cong" : "Khong the nhan san",
                Toast.LENGTH_SHORT).show();
        if (ok) finish();
    }

    private long buildTodayTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, startHour);
        calendar.set(Calendar.MINUTE, startMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private double parseMoney(String value) {
        if (TextUtils.isEmpty(value)) return 0;
        try {
            return Double.parseDouble(value.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }
}
