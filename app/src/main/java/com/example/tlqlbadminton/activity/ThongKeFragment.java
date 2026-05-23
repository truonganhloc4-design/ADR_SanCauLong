package com.example.tlqlbadminton.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.adapter.SimpleTextAdapter;
import com.example.tlqlbadminton.model.HoaDon;
import com.example.tlqlbadminton.sqlite.HoaDonDAO;

import java.util.ArrayList;
import java.util.List;

public class ThongKeFragment extends Fragment {

    private TextView tvTongDoanhThu;
    private TextView tvSoLuotDat;
    private TextView chipNgay;
    private TextView chipTuan;
    private TextView chipThang;
    private TextView tvEmpty;
    private SimpleTextAdapter adapter;
    private HoaDonDAO hoaDonDAO;

    // Tạo giao diện tab thống kê trong MainActivity.
    @Nullable
    // Mỗi khi quay lại tab này thì refresh lại số liệu.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_thong_ke, container, false);

        hoaDonDAO = new HoaDonDAO(requireContext());
        bindViews(root);
        setupInvoiceList(root);
        setupFilterChips();
        refreshStats();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshStats();
    }

    // Ánh xạ các view thống kê từ fragment_thong_ke.xml.
    private void bindViews(View root) {
        tvTongDoanhThu = root.findViewById(R.id.tvTongDoanhThu);
        tvSoLuotDat = root.findViewById(R.id.tvSoLuotDat);
        chipNgay = root.findViewById(R.id.chipNgay);
        chipTuan = root.findViewById(R.id.chipTuan);
        chipThang = root.findViewById(R.id.chipThang);
        tvEmpty = root.findViewById(R.id.tvEmptyThongKe);
    }

    // Gắn RecyclerView với SimpleTextAdapter để hiển thị hóa đơn.
    private void setupInvoiceList(View root) {
        RecyclerView rvHoaDon = root.findViewById(R.id.rvHoaDon);
        rvHoaDon.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SimpleTextAdapter();
        rvHoaDon.setAdapter(adapter);
    }

    // Gắn sự kiện cho chip ngày/tuần/tháng.
    private void setupFilterChips() {
        chipNgay.setOnClickListener(v -> setFilter(chipNgay, chipTuan, chipThang));
        chipTuan.setOnClickListener(v -> setFilter(chipTuan, chipNgay, chipThang));
        chipThang.setOnClickListener(v -> setFilter(chipThang, chipNgay, chipTuan));
    }

    // Đổi giao diện chip được chọn; hiện tại chưa lọc dữ liệu thật.
    private void setFilter(TextView active, TextView... inactives) {
        active.setBackgroundResource(R.drawable.bg_chip_filter_active);
        active.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary));
        active.setTypeface(null, android.graphics.Typeface.BOLD);

        for (TextView tv : inactives) {
            tv.setBackgroundResource(R.drawable.bg_chip_filter_inactive);
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
            tv.setTypeface(null, android.graphics.Typeface.NORMAL);
        }

        refreshStats();
    }

    // Load hóa đơn, cập nhật card tổng và danh sách hóa đơn.
    private void refreshStats() {
        if (!isViewReady()) return;

        List<HoaDon> hoaDonList = hoaDonDAO.getAllHoaDon();
        updateSummaryCards();
        updateEmptyState(hoaDonList);
        adapter.submitList(buildInvoiceRows(hoaDonList));
    }

    // Kiểm tra view/DAO đã khởi tạo chưa để tránh lỗi null.
    private boolean isViewReady() {
        return hoaDonDAO != null && tvTongDoanhThu != null && adapter != null;
    }

    // Cập nhật tổng doanh thu và số lượt chơi.
    private void updateSummaryCards() {
        tvTongDoanhThu.setText(formatCurrency((long) hoaDonDAO.getTongDoanhThu()));
        tvSoLuotDat.setText(String.valueOf(hoaDonDAO.countHoaDon()));
    }

    // Hiện thông báo rỗng nếu chưa có hóa đơn.
    private void updateEmptyState(List<HoaDon> hoaDonList) {
        tvEmpty.setVisibility(hoaDonList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    // Tạo danh sách Row cho SimpleTextAdapter từ danh sách hóa đơn.
    private List<SimpleTextAdapter.Row> buildInvoiceRows(List<HoaDon> hoaDonList) {
        List<SimpleTextAdapter.Row> rows = new ArrayList<>();

        for (HoaDon hoaDon : hoaDonList) {
            rows.add(createInvoiceRow(hoaDon));
        }

        return rows;
    }

    // Chuyển một hóa đơn thành 2 dòng hiển thị.
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

    // Format số tiền kiểu 70000 -> 70.000.
    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }
}
