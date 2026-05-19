package com.example.tlqlbadminton.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.model.AppState;

public class ThanhToanActivity extends AppCompatActivity {

    public static final String EXTRA_COURT_ID     = "extra_court_id";
    public static final String EXTRA_BOOKING_CODE = "extra_booking_code";
    public static final String EXTRA_COURT_INDEX  = "extra_court_index";

    private ImageButton btnBack;
    private TextView tvCourtTitle;
    private TextView tvMaCa;
    private TextView tvThoiGianChoi;
    private TextView tvGiaSan;
    private TextView tvTongCong;
    private Button btnThemDichVu;

    // Payment options
    private LinearLayout optionTienMat;
    private LinearLayout optionChuyenKhoan;
    private LinearLayout optionQR;

    // Actions
    private Button btnInHoaDon;
    private Button btnXacNhanThanhToan;

    private String courtId;
    private String bookingCode;
    private int    courtIndex = 0;
    private long   giaCourtBase = 105000L;
    private long   tongCong;
    private String selectedPayment = "tienmat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        courtId     = getIntent().getStringExtra(EXTRA_COURT_ID);
        bookingCode = getIntent().getStringExtra(EXTRA_BOOKING_CODE);
        courtIndex  = getIntent().getIntExtra(EXTRA_COURT_INDEX, 0);
        if (courtId == null) courtId = "1";
        if (bookingCode == null) bookingCode = "#BK-00001";

        bindViews();
        setupToolbar();
        setupPaymentOptions();
        setupActions();
        loadBookingInfo();
    }

    private void bindViews() {
        btnBack               = findViewById(R.id.btnBack);
        tvCourtTitle          = findViewById(R.id.tvCourtTitle);
        tvMaCa                = findViewById(R.id.tvMaCa);
        tvThoiGianChoi        = findViewById(R.id.tvThoiGianChoi);
        tvGiaSan              = findViewById(R.id.tvGiaSan);
        tvTongCong            = findViewById(R.id.tvTongCong);
        btnThemDichVu         = findViewById(R.id.btnThemDichVu);
        optionTienMat         = findViewById(R.id.optionTienMat);
        optionChuyenKhoan     = findViewById(R.id.optionChuyenKhoan);
        optionQR              = findViewById(R.id.optionQR);
        btnInHoaDon           = findViewById(R.id.btnInHoaDon);
        btnXacNhanThanhToan   = findViewById(R.id.btnXacNhanThanhToan);
    }

    private void setupToolbar() {
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void loadBookingInfo() {
        tvCourtTitle.setText("Sân 0" + courtId + " - Cỏ nhân tạo");
        tvMaCa.setText(bookingCode);
        tvThoiGianChoi.setText("14:00 - 15:30 (1.5h)");
        tvGiaSan.setText(formatCurrency(giaCourtBase) + " VNĐ");
        tongCong = giaCourtBase;
        updateTotal();
    }

    private void setupPaymentOptions() {
        optionTienMat.setOnClickListener(v -> selectPayment("tienmat", optionTienMat, optionChuyenKhoan, optionQR));
        optionChuyenKhoan.setOnClickListener(v -> selectPayment("chuyen_khoan", optionChuyenKhoan, optionTienMat, optionQR));
        optionQR.setOnClickListener(v -> selectPayment("qr", optionQR, optionTienMat, optionChuyenKhoan));
    }

    private void selectPayment(String method, LinearLayout selected, LinearLayout... others) {
        selectedPayment = method;
        selected.setBackgroundResource(R.drawable.bg_payment_option_selected);
        for (LinearLayout other : others) {
            other.setBackgroundResource(R.drawable.bg_payment_option);
        }
    }

    private void setupActions() {
        btnThemDichVu.setOnClickListener(v -> {
            // TODO: Open dịch vụ dialog
            Toast.makeText(this, "Tính năng thêm dịch vụ đang phát triển", Toast.LENGTH_SHORT).show();
        });

        btnInHoaDon.setOnClickListener(v -> {
            // TODO: Print/export invoice
            Toast.makeText(this, "Đang in hóa đơn...", Toast.LENGTH_SHORT).show();
        });

        btnXacNhanThanhToan.setOnClickListener(v -> confirmPayment());
    }

    private void confirmPayment() {
        // Ghi AppState → sân về xanh (Trống), sinh hóa đơn vào Thống kê
        java.text.SimpleDateFormat sdf =
            new java.text.SimpleDateFormat("HH:mm dd/MM", java.util.Locale.getDefault());
        String now = sdf.format(new java.util.Date());

        AppState.InvoiceEntry entry = new AppState.InvoiceEntry(
                bookingCode,
                "Sân 0" + courtId,
                now,
                tongCong,
                now);
        AppState.getInstance().thanhToan(courtIndex, entry);

        Toast.makeText(this,
                "Thanh toán thành công! " + formatCurrency(tongCong) + " VNĐ",
                Toast.LENGTH_LONG).show();
        finish();
    }

    private void updateTotal() {
        tvTongCong.setText(formatCurrency(tongCong) + " VNĐ");
    }

    private String formatCurrency(long amount) {
        // Format with dot separator: 105000 → 105.000
        return String.format("%,d", amount).replace(",", ".");
    }
}
