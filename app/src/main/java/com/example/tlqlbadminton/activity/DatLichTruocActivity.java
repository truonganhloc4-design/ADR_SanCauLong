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
import com.example.tlqlbadminton.model.AppState;
import java.util.Calendar;
import java.util.Locale;

public class DatLichTruocActivity extends AppCompatActivity {

    public static final String EXTRA_COURT_ID    = "extra_court_id";
    public static final String EXTRA_COURT_INDEX = "extra_court_index";

    private ImageButton btnBack;
    private EditText    etHoTen;
    private EditText    etSoDienThoai;
    private EditText    etTienCoc;

    private LinearLayout btnChonNgay;
    private TextView     tvNgayDuocChon;
    private LinearLayout btnChonKhungGio;
    private TextView     tvKhungGioDuocChon;

    private TextView[] btnCourts = new TextView[4];

    private Button btnXacNhanDatLich;

    private String selectedCourtId = null;
    private int    selectedCourtIndex = 0;
    private int    year, month, day;

    // Danh sách khung giờ mẫu (có thể mở rộng theo ca thực tế)
    private static final String[] KHUNG_GIO_LIST = {
            "06:00 - 08:00",
            "08:00 - 10:00",
            "10:00 - 12:00",
            "12:00 - 14:00",
            "14:00 - 16:00",
            "16:00 - 18:00",
            "18:00 - 20:00",
            "20:00 - 22:00"
    };
    private String selectedKhungGio = null;

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

        // Nếu được mở từ card sân cụ thể
        String preCourtId = getIntent().getStringExtra(EXTRA_COURT_ID);
        if (preCourtId != null) {
            selectedCourtId    = preCourtId;
            selectedCourtIndex = getIntent().getIntExtra(EXTRA_COURT_INDEX, 0);
            selectCourt(selectedCourtIndex);
        }
    }

    private void bindViews() {
        btnBack              = findViewById(R.id.btnBack);
        etHoTen              = findViewById(R.id.etHoTen);
        etSoDienThoai        = findViewById(R.id.etSoDienThoai);
        etTienCoc            = findViewById(R.id.etTienCoc);
        
        btnCourts[0]         = findViewById(R.id.btnSan1);
        btnCourts[1]         = findViewById(R.id.btnSan2);
        btnCourts[2]         = findViewById(R.id.btnSan3);
        btnCourts[3]         = findViewById(R.id.btnSan4);
        
        btnChonNgay          = findViewById(R.id.btnChonNgay);
        tvNgayDuocChon       = findViewById(R.id.tvNgayDuocChon);
        btnChonKhungGio      = findViewById(R.id.btnChonKhungGio);
        tvKhungGioDuocChon   = findViewById(R.id.tvKhungGioDuocChon);
        btnXacNhanDatLich    = findViewById(R.id.btnXacNhanDatLich);
        
        // Thiết lập sự kiện chọn sân
        for (int i = 0; i < btnCourts.length; i++) {
            int index = i;
            if (btnCourts[i] != null) {
                btnCourts[i].setOnClickListener(v -> selectCourt(index));
            }
        }
    }

    private void selectCourt(int index) {
        if (index < 0 || index >= btnCourts.length) return;
        selectedCourtIndex = index;
        selectedCourtId = String.valueOf(index + 1);
        
        for (int i = 0; i < btnCourts.length; i++) {
            if (btnCourts[i] != null) {
                if (i == index) {
                    btnCourts[i].setBackgroundResource(R.drawable.bg_chip_filter_active);
                    btnCourts[i].setTextColor(Color.parseColor("#00105c"));
                } else {
                    btnCourts[i].setBackgroundResource(R.drawable.bg_input_default);
                    btnCourts[i].setTextColor(getResources().getColor(R.color.text_secondary));
                }
            }
        }
    }

    private void setupToolbar() {
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void setupPickers() {
        // Chọn ngày
        btnChonNgay.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(this,
                    (view, y, m, d) -> {
                        year  = y;
                        month = m;
                        day   = d;
                        tvNgayDuocChon.setText(
                            String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y));
                    }, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.show();
        });

        // Chọn khung giờ chơi (dropdown danh sách ca)
        btnChonKhungGio.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Chọn khung giờ chơi")
                    .setItems(KHUNG_GIO_LIST, (dialog, which) -> {
                        selectedKhungGio = KHUNG_GIO_LIST[which];
                        tvKhungGioDuocChon.setText(selectedKhungGio);
                    })
                    .show();
        });
    }

    private void setupSubmit() {
        btnXacNhanDatLich.setOnClickListener(v -> submitDatLich());
    }

    private void submitDatLich() {
        String hoTen = etHoTen.getText().toString().trim();
        String sdt   = etSoDienThoai.getText().toString().trim();
        String tienCocStr = etTienCoc.getText().toString().trim();
        double tienCoc = tienCocStr.isEmpty() ? 0 : Double.parseDouble(tienCocStr);

        if (TextUtils.isEmpty(hoTen)) {
            Toast.makeText(this, "Vui lòng nhập họ tên khách hàng", Toast.LENGTH_SHORT).show();
            etHoTen.requestFocus();
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
        if (selectedKhungGio == null) {
            Toast.makeText(this, "Vui lòng chọn khung giờ chơi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ghi vào AppState → sân ngoài sơ đồ đổi sang badge Đã đặt
        AppState.BookingEntry entry = new AppState.BookingEntry(
                Integer.parseInt(selectedCourtId),
                "Sân 0" + selectedCourtId,
                hoTen, sdt,
                tvNgayDuocChon.getText().toString(),
                selectedKhungGio, tienCoc);
        AppState.getInstance().datLichTruoc(selectedCourtIndex, entry);
        Toast.makeText(this,
                "Đã đặt lịch: Sân " + selectedCourtId + " — " + selectedKhungGio + " cho " + hoTen,
                Toast.LENGTH_LONG).show();
        finish();
    }
}
