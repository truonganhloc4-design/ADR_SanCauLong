package com.example.tlqlbadminton.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.model.HoaDon;
import com.example.tlqlbadminton.model.PhieuDatSan;
import com.example.tlqlbadminton.model.San;
import com.example.tlqlbadminton.sqlite.HoaDonDAO;
import com.example.tlqlbadminton.sqlite.PhieuDatSanDAO;
import com.example.tlqlbadminton.sqlite.SanDAO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ThanhToanActivity extends AppCompatActivity {

    public static final String EXTRA_COURT_ID = "extra_court_id";
    public static final String EXTRA_BOOKING_CODE = "extra_booking_code";
    public static final String EXTRA_COURT_INDEX = "extra_court_index";

    private ImageButton btnBack;
    private TextView tvCourtTitle;
    private TextView tvMaCa;
    private TextView tvThoiGianChoi;
    private TextView tvGiaSan;
    private TextView tvThanhTienSan;
    private TextView tvTienCoc;
    private TextView tvTongCong;
    private Button btnXacNhanThanhToan;

    private SanDAO sanDAO;
    private PhieuDatSanDAO phieuDatSanDAO;
    private HoaDonDAO hoaDonDAO;
    private San san;
    private PhieuDatSan phieu;
    private int maSan = 1;
    private long tongTienSan;
    private long tongThanhToan;

    // Màn hình thanh toán cho một sân đang chơi.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        sanDAO = new SanDAO(this);
        phieuDatSanDAO = new PhieuDatSanDAO(this);
        hoaDonDAO = new HoaDonDAO(this);
        String courtId = getIntent().getStringExtra(EXTRA_COURT_ID);
        if (courtId != null) maSan = Integer.parseInt(courtId);

        bindViews();
        setupToolbar();
        setupActions();
        loadBookingInfo();
    }

    // Ánh xạ view trong activity_thanh_toan.xml.
    private void bindViews() {
        btnBack = findViewById(R.id.btnBack);
        tvCourtTitle = findViewById(R.id.tvCourtTitle);
        tvMaCa = findViewById(R.id.tvMaCa);
        tvThoiGianChoi = findViewById(R.id.tvThoiGianChoi);
        tvGiaSan = findViewById(R.id.tvGiaSan);
        tvThanhTienSan = findViewById(R.id.tvThanhTienSan);
        tvTienCoc = findViewById(R.id.tvTienCoc);
        tvTongCong = findViewById(R.id.tvTongCong);
        btnXacNhanThanhToan = findViewById(R.id.btnXacNhanThanhToan);
    }

    // Gắn nút quay lại.
    private void setupToolbar() {
        btnBack.setOnClickListener(v -> finish());
    }

    // Load sân và phiếu đang chơi để tính tiền.
    private void loadBookingInfo() {
        san = sanDAO.getSanById(maSan);
        phieu = phieuDatSanDAO.getActivePhieuBySan(maSan);
        if (san == null || phieu == null) {
            Toast.makeText(this, "Khong tim thay ca dang choi", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Tính tiền theo số phút chơi từ giờ bắt đầu đến hiện tại.
        long now = System.currentTimeMillis();
        long playedMinutes = Math.max(1, (now - phieu.getGioBatDau()) / 60000);
        tongTienSan = Math.round((playedMinutes / 60.0) * san.getGiaMoiGio());
        tongThanhToan = tongTienSan;

        tvCourtTitle.setText(san.getTenSan());
        tvMaCa.setText(phieu.getMaCa());
        tvThoiGianChoi.setText(formatDuration(playedMinutes));
        tvGiaSan.setText(formatCurrency((long) san.getGiaMoiGio()) + " VND/gio");
        tvThanhTienSan.setText(formatCurrency(tongTienSan) + " VND");
        tvTienCoc.setText(formatCurrency(Math.round(phieu.getTienCoc())) + " VND");
        tvTongCong.setText(formatCurrency(tongThanhToan) + " VND");
    }

    // Gắn sự kiện xác nhận thanh toán.
    private void setupActions() {
        btnXacNhanThanhToan.setOnClickListener(v -> confirmPayment());
    }

    // Tạo hóa đơn, đóng phiếu và trả sân về trống.
    private void confirmPayment() {
        if (phieu == null) return;
        long now = System.currentTimeMillis();
        String ngayLap = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(now));
        String maHD = "HD-" + new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault()).format(new Date(now));

        HoaDon hoaDon = new HoaDon(maHD, phieu.getMaPhieu(), tongTienSan, tongThanhToan, ngayLap, now);
        boolean ok = hoaDonDAO.thanhToanSan(phieu.getMaPhieu(), hoaDon, now);
        Toast.makeText(this, ok ? "Thanh toan thanh cong" : "Khong the thanh toan",
                Toast.LENGTH_SHORT).show();
        if (ok) finish();
    }

    // Đổi số phút thành dạng 1h 30p.
    private String formatDuration(long minutes) {
        long hours = minutes / 60;
        long remainMinutes = minutes % 60;
        return hours + "h " + remainMinutes + "p";
    }

    // Format số tiền kiểu 70000 -> 70.000.
    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }
}
