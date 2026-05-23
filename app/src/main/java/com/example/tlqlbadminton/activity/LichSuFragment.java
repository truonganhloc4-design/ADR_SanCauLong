package com.example.tlqlbadminton.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.adapter.SimpleTextAdapter;
import com.example.tlqlbadminton.model.PhieuDatSan;
import com.example.tlqlbadminton.model.San;
import com.example.tlqlbadminton.sqlite.PhieuDatSanDAO;
import com.example.tlqlbadminton.sqlite.SanDAO;

import java.util.ArrayList;
import java.util.List;

public class LichSuFragment extends Fragment {

    private TextView tvEmpty;
    private SimpleTextAdapter adapter;
    private PhieuDatSanDAO phieuDatSanDAO;
    private SanDAO sanDAO;

    // Tạo giao diện tab lịch sử/lịch đặt trước trong MainActivity.
    @Nullable
    // Mỗi khi quay lại tab này thì load lại lịch đặt trước.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lich_su, container, false);

        phieuDatSanDAO = new PhieuDatSanDAO(requireContext());
        sanDAO = new SanDAO(requireContext());
        bindViews(root);
        setupBookingList(root);
        refreshList();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    // Ánh xạ TextView thông báo rỗng.
    private void bindViews(View root) {
        tvEmpty = root.findViewById(R.id.tvEmptyLichSu);
    }

    // Gắn RecyclerView với SimpleTextAdapter để hiển thị lịch đặt.
    private void setupBookingList(View root) {
        RecyclerView rvLichDat = root.findViewById(R.id.rvLichDat);
        rvLichDat.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SimpleTextAdapter();
        rvLichDat.setAdapter(adapter);
    }

    // Lấy phiếu đặt trước, cập nhật empty state và danh sách.
    private void refreshList() {
        if (!isViewReady()) return;

        List<PhieuDatSan> bookings = phieuDatSanDAO.getPendingBookings();
        updateEmptyState(bookings);
        adapter.submitList(buildBookingRows(bookings));
    }

    // Kiểm tra view/DAO đã sẵn sàng chưa để tránh lỗi null.
    private boolean isViewReady() {
        return phieuDatSanDAO != null && sanDAO != null && tvEmpty != null && adapter != null;
    }

    // Hiện thông báo rỗng nếu chưa có lịch đặt.
    private void updateEmptyState(List<PhieuDatSan> bookings) {
        tvEmpty.setVisibility(bookings.isEmpty() ? View.VISIBLE : View.GONE);
    }

    // Tạo danh sách Row từ các phiếu đặt trước.
    private List<SimpleTextAdapter.Row> buildBookingRows(List<PhieuDatSan> bookings) {
        List<SimpleTextAdapter.Row> rows = new ArrayList<>();

        for (PhieuDatSan phieu : bookings) {
            rows.add(createBookingRow(phieu));
        }

        return rows;
    }

    // Chuyển một phiếu đặt thành 2 dòng hiển thị.
    private SimpleTextAdapter.Row createBookingRow(PhieuDatSan phieu) {
        String tenSan = getTenSan(phieu.getMaSan());
        String title = tenSan + " - " + phieu.getTenKhach();
        String subtitle = phieu.getNgayDat()
                + " | "
                + phieu.getKhungGioChoi()
                + " | Cọc: "
                + formatCurrency((long) phieu.getTienCoc())
                + " VNĐ";

        return new SimpleTextAdapter.Row(title, subtitle);
    }

    // Lấy tên sân; nếu không tìm thấy thì dùng mã sân dự phòng.
    private String getTenSan(int maSan) {
        San san = sanDAO.getSanById(maSan);
        return san != null ? san.getTenSan() : "Sân #" + maSan;
    }

    // Format số tiền kiểu 70000 -> 70.000.
    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }
}
