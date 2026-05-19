package com.example.tlqlbadminton.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.model.AppState;
import java.util.Calendar;
import java.util.Locale;

public class NhanSanActivity extends AppCompatActivity {

    public static final String EXTRA_COURT_ID    = "extra_court_id";
    public static final String EXTRA_COURT_INDEX = "extra_court_index";

    private ImageButton  btnBack;
    private TextView     tvToolbarTitle;

    // Dropdown chọn khách đặt trước
    private LinearLayout btnChonKhachDatTruoc;
    private TextView     tvKhachDatTruocDuocChon;
    private String       selectedKhachId = null;

    // Thông tin khách
    private EditText etTenKhach;
    private EditText etSoDienThoai;
    private EditText etTienCoc;

    // Thời gian bắt đầu (chỉ 1 ô)
    private ImageView btnPickGioBatDau;
    private TextView  tvGioBatDau;

    // Submit
    private Button btnHuy;
    private Button btnXacNhanNhanSan; // Nút Bắt đầu chơi

    private String courtId;
    private int    courtIndex = 0;
    private int    startHour, startMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_san);

        courtId    = getIntent().getStringExtra(EXTRA_COURT_ID);
        courtIndex = getIntent().getIntExtra(EXTRA_COURT_INDEX, 0);
        if (courtId == null) courtId = "1";

        bindViews();
        setupToolbar();
        setupDropdownKhach();
        setupTimePicker();
        setupSubmit();
        setCurrentTime();
    }

    private void bindViews() {
        btnBack                  = findViewById(R.id.btnBack);
        tvToolbarTitle           = findViewById(R.id.tvToolbarTitle);
        btnChonKhachDatTruoc     = findViewById(R.id.btnChonKhachDatTruoc);
        tvKhachDatTruocDuocChon  = findViewById(R.id.tvKhachDatTruocDuocChon);
        etTenKhach               = findViewById(R.id.etTenKhach);
        etSoDienThoai            = findViewById(R.id.etSoDienThoai);
        etTienCoc                = findViewById(R.id.etTienCoc);
        btnPickGioBatDau         = findViewById(R.id.btnPickGioBatDau);
        tvGioBatDau              = findViewById(R.id.tvGioBatDau);
        btnHuy                   = findViewById(R.id.btnHuy);
        btnXacNhanNhanSan        = findViewById(R.id.btnXacNhanNhanSan);

        tvToolbarTitle.setText("Nhận sân - Sân 0" + courtId);
    }

    private void setupToolbar() {
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    /** Dropdown chọn khách đã đặt trước — tự động điền thông tin */
    private void setupDropdownKhach() {
        // TODO: Thay bằng danh sách thực từ DatabaseHelper (PhieuDatSan TrangThaiPhieu=0)
        String[] danhSachKhach = {
                "— Không chọn (Khách vãng lai) —",
                "Nguyễn Văn A • 0912345678",
                "Trần Thị B • 0987654321"
        };
        btnChonKhachDatTruoc.setOnClickListener(v ->
            new android.app.AlertDialog.Builder(this)
                .setTitle("Chọn khách đặt trước")
                .setItems(danhSachKhach, (dialog, which) -> {
                    if (which == 0) {
                        // Khách vãng lai — xóa autofill
                        selectedKhachId = null;
                        tvKhachDatTruocDuocChon.setText("");
                        tvKhachDatTruocDuocChon.setHint("Chọn khách hàng...");
                        etTenKhach.setText("");
                        etSoDienThoai.setText("");
                        etTienCoc.setText("0");
                    } else {
                        selectedKhachId = String.valueOf(which);
                        tvKhachDatTruocDuocChon.setText(danhSachKhach[which]);
                        // TODO: Autofill từ DB theo selectedKhachId
                        if (which == 1) {
                            etTenKhach.setText("Nguyễn Văn A");
                            etSoDienThoai.setText("0912345678");
                            etTienCoc.setText("100000");
                        } else if (which == 2) {
                            etTenKhach.setText("Trần Thị B");
                            etSoDienThoai.setText("0987654321");
                            etTienCoc.setText("50000");
                        }
                    }
                })
                .show()
        );
    }

    private void setCurrentTime() {
        Calendar now = Calendar.getInstance();
        startHour   = now.get(Calendar.HOUR_OF_DAY);
        startMinute = now.get(Calendar.MINUTE);
        tvGioBatDau.setText(
            String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute));
    }

    private void setupTimePicker() {
        btnPickGioBatDau.setOnClickListener(v ->
            new TimePickerDialog(this,
                (view, h, min) -> {
                    startHour   = h;
                    startMinute = min;
                    tvGioBatDau.setText(
                        String.format(Locale.getDefault(), "%02d:%02d", h, min));
                }, startHour, startMinute, true).show()
        );
    }

    private void setupSubmit() {
        btnHuy.setOnClickListener(v -> finish());
        btnXacNhanNhanSan.setOnClickListener(v -> submitNhanSan());
    }

    private void submitNhanSan() {
        String tenKhach = etTenKhach.getText().toString().trim();
        String sdt      = etSoDienThoai.getText().toString().trim();
        String cocStr   = etTienCoc.getText().toString().trim();
        double tienCoc  = cocStr.isEmpty() ? 0 : Double.parseDouble(cocStr);

        if (TextUtils.isEmpty(tenKhach)) {
            Toast.makeText(this, "Vui lòng nhập tên khách hàng", Toast.LENGTH_SHORT).show();
            etTenKhach.requestFocus();
            return;
        }

        String gioBatDau = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);

        // Cập nhật AppState → sân lập tức đổi sang ĐỎ trên SoDoFragment
        AppState.getInstance().nhanSan(courtIndex, Integer.parseInt(courtId), gioBatDau);

        Toast.makeText(this,
            "Đã nhận Sân " + courtId + " cho " + tenKhach + " lúc " + gioBatDau,
            Toast.LENGTH_LONG).show();
        finish();
    }
}
