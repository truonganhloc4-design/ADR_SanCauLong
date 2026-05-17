package com.example.tlqlbadminton.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
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

public class DatLichTruocActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etHoTen;
    private EditText etSoDienThoai;
    private EditText etGhiChu;

    private LinearLayout btnChonSan;
    private TextView tvSanDuocChon;
    private LinearLayout btnChonNgay;
    private TextView tvNgayDuocChon;
    private LinearLayout btnChonGioBatDau;
    private TextView tvGioBatDau;
    private LinearLayout btnChonGioKetThuc;
    private TextView tvGioKetThuc;

    private Button btnXacNhanDatLich;

    private String selectedCourtId = null;
    private int year, month, day;
    private int startHour = 7, startMinute = 0;
    private int endHour = 9, endMinute = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_lich_truoc);

        Calendar today = Calendar.getInstance();
        year  = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH);
        day   = today.get(Calendar.DAY_OF_MONTH);

        bindViews();
        setupToolbar();
        setupPickers();
        setupSubmit();
    }

    private void bindViews() {
        btnBack           = findViewById(R.id.btnBack);
        etHoTen           = findViewById(R.id.etHoTen);
        etSoDienThoai     = findViewById(R.id.etSoDienThoai);
        etGhiChu          = findViewById(R.id.etGhiChu);
        btnChonSan        = findViewById(R.id.btnChonSan);
        tvSanDuocChon     = findViewById(R.id.tvSanDuocChon);
        btnChonNgay       = findViewById(R.id.btnChonNgay);
        tvNgayDuocChon    = findViewById(R.id.tvNgayDuocChon);
        btnChonGioBatDau  = findViewById(R.id.btnChonGioBatDau);
        tvGioBatDau       = findViewById(R.id.tvGioBatDau);
        btnChonGioKetThuc = findViewById(R.id.btnChonGioKetThuc);
        tvGioKetThuc      = findViewById(R.id.tvGioKetThuc);
        btnXacNhanDatLich = findViewById(R.id.btnXacNhanDatLich);
    }

    private void setupToolbar() {
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void setupPickers() {
        // Chọn sân (simple dialog — replace with actual court list)
        btnChonSan.setOnClickListener(v -> {
            String[] courts = {"Sân 01", "Sân 02", "Sân 03", "Sân 04"};
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Chọn sân")
                    .setItems(courts, (dialog, which) -> {
                        selectedCourtId = String.valueOf(which + 1);
                        tvSanDuocChon.setText(courts[which]);
                    })
                    .show();
        });

        // Chọn ngày
        btnChonNgay.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(this,
                    (view, y, m, d) -> {
                        year  = y;
                        month = m;
                        day   = d;
                        tvNgayDuocChon.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y));
                    }, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.show();
        });

        // Giờ bắt đầu
        btnChonGioBatDau.setOnClickListener(v -> {
            new TimePickerDialog(this, (view, h, min) -> {
                startHour   = h;
                startMinute = min;
                tvGioBatDau.setText(String.format(Locale.getDefault(), "%02d:%02d", h, min));
            }, startHour, startMinute, true).show();
        });

        // Giờ kết thúc
        btnChonGioKetThuc.setOnClickListener(v -> {
            new TimePickerDialog(this, (view, h, min) -> {
                endHour   = h;
                endMinute = min;
                tvGioKetThuc.setText(String.format(Locale.getDefault(), "%02d:%02d", h, min));
            }, endHour, endMinute, true).show();
        });
    }

    private void setupSubmit() {
        btnXacNhanDatLich.setOnClickListener(v -> submitDatLich());
    }

    private void submitDatLich() {
        String hoTen = etHoTen.getText().toString().trim();
        String sdt   = etSoDienThoai.getText().toString().trim();

        if (TextUtils.isEmpty(hoTen)) {
            Toast.makeText(this, "Vui lòng nhập họ tên khách hàng", Toast.LENGTH_SHORT).show();
            etHoTen.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(sdt)) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            etSoDienThoai.requestFocus();
            return;
        }
        if (selectedCourtId == null) {
            Toast.makeText(this, "Vui lòng chọn sân", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvNgayDuocChon.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày đặt", Toast.LENGTH_SHORT).show();
            return;
        }
        if (endHour < startHour || (endHour == startHour && endMinute <= startMinute)) {
            Toast.makeText(this, "Giờ kết thúc phải sau giờ bắt đầu", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Save booking to DB via DatabaseHelper
        Toast.makeText(this,
                "Đã đặt lịch: Sân " + selectedCourtId + " cho " + hoTen,
                Toast.LENGTH_LONG).show();
        finish();
    }
}
