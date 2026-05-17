package com.example.tlqlbadminton.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tlqlbadminton.R;
import java.util.Calendar;
import java.util.Locale;

public class NhanSanActivity extends AppCompatActivity {

    public static final String EXTRA_COURT_ID = "extra_court_id";

    private ImageButton btnBack;
    private TextView tvCourtName;
    private Button btnWalkIn;
    private Button btnDatTruocTab;

    private EditText etTenKhach;
    private EditText etSoDienThoai;

    private LinearLayout btnPickGioBatDau;
    private TextView tvGioBatDau;
    private LinearLayout btnPickGioKetThuc;
    private TextView tvGioKetThuc;

    private EditText etGiaThue;
    private Button btnXacNhanNhanSan;

    private String courtId;
    private int startHour, startMinute;
    private int endHour, endMinute;
    private boolean hasEndTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_san);

        courtId = getIntent().getStringExtra(EXTRA_COURT_ID);
        if (courtId == null) courtId = "1";

        bindViews();
        setupToolbar();
        setupTimePickers();
        setupSubmit();
        setCurrentTime();
    }

    private void bindViews() {
        btnBack            = findViewById(R.id.btnBack);
        tvCourtName        = findViewById(R.id.tvCourtName);
        btnWalkIn          = findViewById(R.id.btnWalkIn);
        btnDatTruocTab     = findViewById(R.id.btnDatTruocTab);
        etTenKhach         = findViewById(R.id.etTenKhach);
        etSoDienThoai      = findViewById(R.id.etSoDienThoai);
        btnPickGioBatDau   = findViewById(R.id.btnPickGioBatDau);
        tvGioBatDau        = findViewById(R.id.tvGioBatDau);
        btnPickGioKetThuc  = findViewById(R.id.btnPickGioKetThuc);
        tvGioKetThuc       = findViewById(R.id.tvGioKetThuc);
        etGiaThue          = findViewById(R.id.etGiaThue);
        btnXacNhanNhanSan  = findViewById(R.id.btnXacNhanNhanSan);

        tvCourtName.setText(String.format("Sân 0%s • Cỏ nhân tạo", courtId));
    }

    private void setupToolbar() {
        btnBack.setOnClickListener(v -> onBackPressed());

        btnWalkIn.setOnClickListener(v -> {
            // already on walk-in
        });
        btnDatTruocTab.setOnClickListener(v -> {
            startActivity(new Intent(this, DatLichTruocActivity.class));
            finish();
        });
    }

    private void setCurrentTime() {
        Calendar now = Calendar.getInstance();
        startHour   = now.get(Calendar.HOUR_OF_DAY);
        startMinute = now.get(Calendar.MINUTE);
        tvGioBatDau.setText(String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute));
    }

    private void setupTimePickers() {
        btnPickGioBatDau.setOnClickListener(v -> {
            TimePickerDialog dialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        startHour   = hourOfDay;
                        startMinute = minute;
                        tvGioBatDau.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                    }, startHour, startMinute, true);
            dialog.show();
        });

        btnPickGioKetThuc.setOnClickListener(v -> {
            TimePickerDialog dialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        endHour   = hourOfDay;
                        endMinute = minute;
                        hasEndTime = true;
                        tvGioKetThuc.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                    }, startHour + 1, 0, true);
            dialog.show();
        });
    }

    private void setupSubmit() {
        btnXacNhanNhanSan.setOnClickListener(v -> submitNhanSan());
    }

    private void submitNhanSan() {
        String tenKhach  = etTenKhach.getText().toString().trim();
        String sdt       = etSoDienThoai.getText().toString().trim();
        String giaThueStr = etGiaThue.getText().toString().trim();

        if (TextUtils.isEmpty(giaThueStr)) {
            Toast.makeText(this, "Vui lòng nhập giá thuê", Toast.LENGTH_SHORT).show();
            etGiaThue.requestFocus();
            return;
        }

        long giaThue = Long.parseLong(giaThueStr.replaceAll("[^0-9]", ""));
        String gioBatDau = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
        String gioKetThuc = hasEndTime
                ? String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute)
                : null;

        // TODO: Save to DB via DatabaseHelper
        Toast.makeText(this, "Đã nhận sân " + courtId + " lúc " + gioBatDau, Toast.LENGTH_LONG).show();
        finish();
    }
}
