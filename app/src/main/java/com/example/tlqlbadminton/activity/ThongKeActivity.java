package com.example.tlqlbadminton.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.adapter.SimpleTextAdapter;
import com.example.tlqlbadminton.model.HoaDon;
import com.example.tlqlbadminton.model.PhieuDatSan;
import com.example.tlqlbadminton.model.San;
import com.example.tlqlbadminton.sqlite.HoaDonDAO;
import com.example.tlqlbadminton.sqlite.PhieuDatSanDAO;
import com.example.tlqlbadminton.sqlite.SanDAO;

import java.util.ArrayList;
import java.util.List;

public class ThongKeActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvTongDoanhThu;
    private TextView tvSoLuotDat;
    private TextView tabThongKe;
    private TextView tabLichSu;
    private TextView chipNgay;
    private TextView chipTuan;
    private TextView chipThang;
    private SimpleTextAdapter adapter;
    private HoaDonDAO hoaDonDAO;
    private PhieuDatSanDAO phieuDatSanDAO;
    private SanDAO sanDAO;
    private boolean isShowingInvoices = true;

    // Màn hình thống kê độc lập: có tab hóa đơn và tab lịch đặt trước.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        initDaos();
        bindViews();
        setupToolbar();
        setupTabs();
        setupFilterChips();
        loadData();
    }

    // Khởi tạo các DAO cần đọc hóa đơn, phiếu đặt và sân.
    private void initDaos() {
        hoaDonDAO = new HoaDonDAO(this);
        phieuDatSanDAO = new PhieuDatSanDAO(this);
        sanDAO = new SanDAO(this);
    }

    // Ánh xạ view và gắn adapter cho RecyclerView.
    private void bindViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTongDoanhThu = findViewById(R.id.tvTongDoanhThu);
        tvSoLuotDat = findViewById(R.id.tvSoLuotDat);
        tabThongKe = findViewById(R.id.tabThongKe);
        tabLichSu = findViewById(R.id.tabLichSu);
        chipNgay = findViewById(R.id.chipNgay);
        chipTuan = findViewById(R.id.chipTuan);
        chipThang = findViewById(R.id.chipThang);

        RecyclerView rvHoaDon = findViewById(R.id.rvHoaDon);
        rvHoaDon.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleTextAdapter();
        rvHoaDon.setAdapter(adapter);
    }

    // Gắn nút quay lại.
    private void setupToolbar() {
        btnBack.setOnClickListener(v -> finish());
    }

    // Gắn sự kiện chuyển giữa tab thống kê và lịch sử.
    private void setupTabs() {
        tabThongKe.setOnClickListener(v -> showInvoiceTab());
        tabLichSu.setOnClickListener(v -> showPendingBookingTab());
    }

    // Chuyển sang tab hiển thị hóa đơn.
    private void showInvoiceTab() {
        isShowingInvoices = true;
        updateActiveTabStyle();
        loadData();
    }

    // Chuyển sang tab hiển thị lịch đặt trước đang chờ.
    private void showPendingBookingTab() {
        isShowingInvoices = false;
        updateActiveTabStyle();
        loadData();
    }

    // Cập nhật màu/chữ đậm cho tab đang được chọn.
    private void updateActiveTabStyle() {
        styleTab(tabThongKe, isShowingInvoices);
        styleTab(tabLichSu, !isShowingInvoices);
    }

    // Style một tab theo trạng thái active/inactive.
    private void styleTab(TextView tab, boolean active) {
        tab.setTextColor(getColor(active ? R.color.primary : R.color.text_secondary));
        tab.setTypeface(null, active ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
    }

    // Gắn sự kiện cho các chip ngày/tuần/tháng.
    private void setupFilterChips() {
        chipNgay.setOnClickListener(v -> setFilter(chipNgay, chipTuan, chipThang));
        chipTuan.setOnClickListener(v -> setFilter(chipTuan, chipNgay, chipThang));
        chipThang.setOnClickListener(v -> setFilter(chipThang, chipNgay, chipTuan));
    }

    // Đổi style chip được chọn; hiện tại chưa lọc DB thật theo ngày/tuần/tháng.
    private void setFilter(TextView active, TextView... inactives) {
        active.setBackgroundResource(R.drawable.bg_chip_filter_active);
        active.setTextColor(getColor(R.color.tertiary));
        for (TextView tv : inactives) {
            tv.setBackgroundResource(R.drawable.bg_chip_filter_inactive);
            tv.setTextColor(getColor(R.color.text_secondary));
        }
        loadData();
    }

    // Nạp dữ liệu theo tab hiện tại rồi đưa vào SimpleTextAdapter.
    private void loadData() {
        updateSummaryCards();

        List<SimpleTextAdapter.Row> rows = isShowingInvoices
                ? buildInvoiceRows()
                : buildPendingBookingRows();

        adapter.submitList(rows);
    }

    // Cập nhật tổng doanh thu và số hóa đơn.
    private void updateSummaryCards() {
        tvTongDoanhThu.setText(formatCurrency((long) hoaDonDAO.getTongDoanhThu()));
        tvSoLuotDat.setText(String.valueOf(hoaDonDAO.countHoaDon()));
    }

    // Tạo danh sách Row từ danh sách hóa đơn.
    private List<SimpleTextAdapter.Row> buildInvoiceRows() {
        List<SimpleTextAdapter.Row> rows = new ArrayList<>();

        for (HoaDon hoaDon : hoaDonDAO.getAllHoaDon()) {
            rows.add(createInvoiceRow(hoaDon));
        }

        return rows;
    }

    // Chuyển một hóa đơn thành Row(title, subtitle).
    private SimpleTextAdapter.Row createInvoiceRow(HoaDon hoaDon) {
        String title = hoaDon.getMaHD()
                + " - "
                + formatCurrency((long) hoaDon.getTongThanhToan())
                + " VNĐ";

        String subtitle = "Ngày lập: "
                + hoaDon.getNgayLap()
                + " | Phiếu #"
                + hoaDon.getMaPhieu();

        return new SimpleTextAdapter.Row(title, subtitle);
    }

    // Tạo danh sách Row từ các phiếu đặt trước.
    private List<SimpleTextAdapter.Row> buildPendingBookingRows() {
        List<SimpleTextAdapter.Row> rows = new ArrayList<>();

        for (PhieuDatSan phieu : phieuDatSanDAO.getPendingBookings()) {
            rows.add(createPendingBookingRow(phieu));
        }

        return rows;
    }

    // Chuyển một phiếu đặt trước thành Row(title, subtitle).
    private SimpleTextAdapter.Row createPendingBookingRow(PhieuDatSan phieu) {
        String tenSan = getTenSan(phieu.getMaSan());
        String title = tenSan + " - " + phieu.getTenKhach();
        String subtitle = phieu.getNgayDat() + " | " + phieu.getKhungGioChoi();

        return new SimpleTextAdapter.Row(title, subtitle);
    }

    // Lấy tên sân; nếu sân bị xóa/mất thì hiển thị theo mã.
    private String getTenSan(int maSan) {
        San san = sanDAO.getSanById(maSan);
        return san != null ? san.getTenSan() : "Sân #" + maSan;
    }

    // Format số tiền kiểu 70000 -> 70.000.
    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }
}
