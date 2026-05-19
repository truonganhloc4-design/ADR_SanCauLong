package com.example.tlqlbadminton.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    private TextView tvTongCong;
    private Button btnThemDichVu;
    private LinearLayout optionTienMat;
    private LinearLayout optionChuyenKhoan;
    private LinearLayout optionQR;
    private Button btnInHoaDon;
    private Button btnXacNhanThanhToan;

    private SanDAO sanDAO;
    private PhieuDatSanDAO phieuDatSanDAO;
    private HoaDonDAO hoaDonDAO;
    private San san;
    private PhieuDatSan phieu;
    private int maSan = 1;
    private long tongTienSan;
    private long tongThanhToan;

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
        setupPaymentOptions();
        setupActions();
        loadBookingInfo();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btnBack);
        tvCourtTitle = findViewById(R.id.tvCourtTitle);
        tvMaCa = findViewById(R.id.tvMaCa);
        tvThoiGianChoi = findViewById(R.id.tvThoiGianChoi);
        tvGiaSan = findViewById(R.id.tvGiaSan);
        tvTongCong = findViewById(R.id.tvTongCong);
        btnThemDichVu = findViewById(R.id.btnThemDichVu);
        optionTienMat = findViewById(R.id.optionTienMat);
        optionChuyenKhoan = findViewById(R.id.optionChuyenKhoan);
        optionQR = findViewById(R.id.optionQR);
        btnInHoaDon = findViewById(R.id.btnInHoaDon);
        btnXacNhanThanhToan = findViewById(R.id.btnXacNhanThanhToan);
    }

    private void setupToolbar() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadBookingInfo() {
        san = sanDAO.getSanById(maSan);
        phieu = phieuDatSanDAO.getActivePhieuBySan(maSan);
        if (san == null || phieu == null) {
            Toast.makeText(this, "Khong tim thay ca dang choi", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        long now = System.currentTimeMillis();
        long playedMinutes = Math.max(1, (now - phieu.getGioBatDau()) / 60000);
        tongTienSan = Math.round((playedMinutes / 60.0) * san.getGiaMoiGio());
        tongThanhToan = Math.max(0, Math.round(tongTienSan - phieu.getTienCoc()));

        tvCourtTitle.setText(san.getTenSan() + " - " + san.getLoaiSan());
        tvMaCa.setText(phieu.getMaCa());
        tvThoiGianChoi.setText(formatDuration(playedMinutes));
        tvGiaSan.setText(formatCurrency((long) san.getGiaMoiGio()) + " VND/gio");
        tvTongCong.setText(formatCurrency(tongThanhToan) + " VND");
    }

    private void setupPaymentOptions() {
        optionTienMat.setOnClickListener(v -> selectPayment(optionTienMat, optionChuyenKhoan, optionQR));
        optionChuyenKhoan.setOnClickListener(v -> selectPayment(optionChuyenKhoan, optionTienMat, optionQR));
        optionQR.setOnClickListener(v -> selectPayment(optionQR, optionTienMat, optionChuyenKhoan));
    }

    private void selectPayment(LinearLayout selected, LinearLayout... others) {
        selected.setBackgroundResource(R.drawable.bg_payment_option_selected);
        for (LinearLayout other : others) other.setBackgroundResource(R.drawable.bg_payment_option);
    }

    private void setupActions() {
        btnThemDichVu.setOnClickListener(v ->
                Toast.makeText(this, "MVP hien chi tinh tien san", Toast.LENGTH_SHORT).show());
        btnInHoaDon.setOnClickListener(v ->
                Toast.makeText(this, "Hoa don se duoc luu sau khi chot ca", Toast.LENGTH_SHORT).show());
        btnXacNhanThanhToan.setOnClickListener(v -> confirmPayment());
    }

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

    private String formatDuration(long minutes) {
        long hours = minutes / 60;
        long remainMinutes = minutes % 60;
        return hours + "h " + remainMinutes + "p";
    }

    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }
}
