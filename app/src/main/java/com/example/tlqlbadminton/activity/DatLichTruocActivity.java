package com.example.tlqlbadminton.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
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
import com.example.tlqlbadminton.model.PhieuDatSan;
import com.example.tlqlbadminton.model.San;
import com.example.tlqlbadminton.sqlite.DBHelper;
import com.example.tlqlbadminton.sqlite.PhieuDatSanDAO;
import com.example.tlqlbadminton.sqlite.SanDAO;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DatLichTruocActivity extends AppCompatActivity {

    public static final String EXTRA_COURT_ID = "extra_court_id";
    public static final String EXTRA_COURT_INDEX = "extra_court_index";

    private ImageButton btnBack;
    private EditText etHoTen;
    private EditText etSoDienThoai;
    private EditText etTienCoc;
    private LinearLayout btnChonNgay;
    private TextView tvNgayDuocChon;
    private LinearLayout btnChonKhungGio;
    private TextView tvKhungGioDuocChon;
    private final TextView[] btnCourts = new TextView[4];
    private Button btnXacNhanDatLich;

    private SanDAO sanDAO;
    private PhieuDatSanDAO phieuDatSanDAO;
    private List<San> courts;
    private int selectedCourtIndex = -1;
    private int selectedMaSan = -1;
    private int year;
    private int month;
    private int day;
    private String selectedKhungGio;

    private static final String[] KHUNG_GIO_LIST = {
            "06:00 - 08:00", "08:00 - 10:00", "10:00 - 12:00", "12:00 - 14:00",
            "14:00 - 16:00", "16:00 - 18:00", "18:00 - 20:00", "20:00 - 22:00"
    };

    // Màn hình đặt lịch trước cho một sân và khung giờ.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_lich_truoc);

        sanDAO = new SanDAO(this);
        phieuDatSanDAO = new PhieuDatSanDAO(this);
        Calendar today = Calendar.getInstance();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH);
        day = today.get(Calendar.DAY_OF_MONTH);

        bindViews();
        setupToolbar();
        setupCourts();
        setupPickers();
        setupSubmit();

        String preCourtId = getIntent().getStringExtra(EXTRA_COURT_ID);
        if (preCourtId != null) selectCourtByMaSan(Integer.parseInt(preCourtId));
    }

    // Ánh xạ view trong activity_dat_lich_truoc.xml.
    private void bindViews() {
        btnBack = findViewById(R.id.btnBack);
        etHoTen = findViewById(R.id.etHoTen);
        etSoDienThoai = findViewById(R.id.etSoDienThoai);
        etTienCoc = findViewById(R.id.etTienCoc);
        btnCourts[0] = findViewById(R.id.btnSan1);
        btnCourts[1] = findViewById(R.id.btnSan2);
        btnCourts[2] = findViewById(R.id.btnSan3);
        btnCourts[3] = findViewById(R.id.btnSan4);
        btnChonNgay = findViewById(R.id.btnChonNgay);
        tvNgayDuocChon = findViewById(R.id.tvNgayDuocChon);
        btnChonKhungGio = findViewById(R.id.btnChonKhungGio);
        tvKhungGioDuocChon = findViewById(R.id.tvKhungGioDuocChon);
        btnXacNhanDatLich = findViewById(R.id.btnXacNhanDatLich);
    }

    // Gắn nút quay lại.
    private void setupToolbar() {
        btnBack.setOnClickListener(v -> finish());
    }

    // Load danh sách sân và gắn vào các nút chọn sân.
    private void setupCourts() {
        courts = sanDAO.getAllSan();
        for (int i = 0; i < btnCourts.length; i++) {
            final int index = i;
            if (i < courts.size()) {
                btnCourts[i].setText(courts.get(i).getTenSan());
                btnCourts[i].setOnClickListener(v -> selectCourt(index));
            } else {
                btnCourts[i].setText("--");
                btnCourts[i].setEnabled(false);
            }
        }
    }

    // Chọn sẵn sân nếu Activity được mở từ một sân cụ thể.
    private void selectCourtByMaSan(int maSan) {
        for (int i = 0; i < courts.size() && i < btnCourts.length; i++) {
            if (courts.get(i).getMaSan() == maSan) {
                selectCourt(i);
                return;
            }
        }
    }

    // Lưu sân đang chọn và đổi giao diện nút sân.
    private void selectCourt(int index) {
        selectedCourtIndex = index;
        selectedMaSan = courts.get(index).getMaSan();
        for (int i = 0; i < btnCourts.length; i++) {
            if (i == index) {
                btnCourts[i].setBackgroundResource(R.drawable.bg_chip_filter_active);
                btnCourts[i].setTextColor(Color.parseColor("#00105c"));
            } else {
                btnCourts[i].setBackgroundResource(R.drawable.bg_input_default);
                btnCourts[i].setTextColor(getResources().getColor(R.color.text_secondary));
            }
        }
    }

    // Gắn DatePicker cho ngày đặt và dialog chọn khung giờ.
    private void setupPickers() {
        btnChonNgay.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(this, (view, y, m, d) -> {
                year = y;
                month = m;
                day = d;
                tvNgayDuocChon.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y));
            }, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.show();
        });

        btnChonKhungGio.setOnClickListener(v ->
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Chọn khung giờ chơi")
                        .setItems(KHUNG_GIO_LIST, (dialog, which) -> {
                            selectedKhungGio = KHUNG_GIO_LIST[which];
                            tvKhungGioDuocChon.setText(selectedKhungGio);
                        })
                        .show());
    }

    // Gắn sự kiện xác nhận đặt lịch.
    private void setupSubmit() {
        btnXacNhanDatLich.setOnClickListener(v -> submitDatLich());
    }

    // Kiểm tra form rồi lưu phiếu đặt trước vào DB.
    private void submitDatLich() {
        String hoTen = etHoTen.getText().toString().trim();
        String sdt = etSoDienThoai.getText().toString().trim();
        double tienCoc = parseMoney(etTienCoc.getText().toString().trim());

        if (TextUtils.isEmpty(hoTen)) {
            Toast.makeText(this, "Vui lòng nhập họ tên khách hàng", Toast.LENGTH_SHORT).show();
            etHoTen.requestFocus();
            return;
        }
        if (selectedCourtIndex < 0) {
            Toast.makeText(this, "Vui lòng chọn sân", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tvNgayDuocChon.getText().toString())) {
            Toast.makeText(this, "Vui lòng chọn ngày đặt", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedKhungGio == null) {
            Toast.makeText(this, "Vui lòng chọn khung giờ chơi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Phiếu đặt trước chỉ lưu lịch, chưa đổi sân sang đang chơi.
        PhieuDatSan phieu = new PhieuDatSan();
        phieu.setMaSan(selectedMaSan);
        phieu.setTenKhach(hoTen);
        phieu.setSoDienThoai(sdt);
        phieu.setTienCoc(tienCoc);
        phieu.setNgayDat(tvNgayDuocChon.getText().toString());
        phieu.setKhungGioChoi(selectedKhungGio);
        phieu.setTrangThaiPhieu(DBHelper.PHIEU_DAT_TRUOC);

        long result = phieuDatSanDAO.insertPhieuDatTruoc(phieu);
        Toast.makeText(this, result != -1 ? "Đã đặt lịch thành công" : "Không thể đặt lịch",
                Toast.LENGTH_SHORT).show();
        if (result != -1) finish();
    }

    // Chuyển chuỗi tiền cọc thành số.
    private double parseMoney(String value) {
        if (TextUtils.isEmpty(value)) return 0;
        try {
            return Double.parseDouble(value.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
