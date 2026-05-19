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
import com.example.tlqlbadminton.model.AppState;

public class ThongKeFragment extends Fragment implements AppState.OnStateChangedListener {

    private TextView tvTongDoanhThu;
    private TextView tvSoLuotDat;
    private TextView chipNgay, chipTuan, chipThang;
    private TextView tvEmpty;
    private RecyclerView rvHoaDon;

    private AppState appState;
    private String currentFilter = "ngay";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_thong_ke, container, false);
        appState = AppState.getInstance();

        tvTongDoanhThu = root.findViewById(R.id.tvTongDoanhThu);
        tvSoLuotDat    = root.findViewById(R.id.tvSoLuotDat);
        chipNgay       = root.findViewById(R.id.chipNgay);
        chipTuan       = root.findViewById(R.id.chipTuan);
        chipThang      = root.findViewById(R.id.chipThang);
        tvEmpty        = root.findViewById(R.id.tvEmptyThongKe);
        rvHoaDon       = root.findViewById(R.id.rvHoaDon);
        rvHoaDon.setLayoutManager(new LinearLayoutManager(getContext()));

        setupFilterChips();
        refreshStats();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        appState.addListener(this);
        refreshStats();
    }

    @Override
    public void onPause() {
        super.onPause();
        appState.removeListener(this);
    }

    private void setupFilterChips() {
        chipNgay.setOnClickListener(v  -> setFilter("ngay",  chipNgay,  chipTuan,  chipThang));
        chipTuan.setOnClickListener(v  -> setFilter("tuan",  chipTuan,  chipNgay,  chipThang));
        chipThang.setOnClickListener(v -> setFilter("thang", chipThang, chipNgay,  chipTuan));
    }

    private void setFilter(String filter, TextView active, TextView... inactives) {
        currentFilter = filter;
        // Active chip: blue text, white bg with border
        active.setBackgroundResource(R.drawable.bg_chip_filter_active);
        active.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary));
        active.setTypeface(null, android.graphics.Typeface.BOLD);
        // Inactive chips: dark text, outline bg — sửa lỗi contrast trắng/trắng
        for (TextView tv : inactives) {
            tv.setBackgroundResource(R.drawable.bg_chip_filter_inactive);
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
            tv.setTypeface(null, android.graphics.Typeface.NORMAL);
        }
        refreshStats();
    }

    private void refreshStats() {
        if (tvTongDoanhThu == null) return;
        long tongTien = appState.getTongDoanhThu();
        int soLuot    = appState.getSoLuotDat();

        tvTongDoanhThu.setText(formatCurrency(tongTien));
        tvSoLuotDat.setText(String.valueOf(soLuot));

        boolean isEmpty = appState.getInvoiceList().isEmpty();
        tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);

        // TODO: Set adapter cho rvHoaDon khi có RecyclerView.Adapter
    }

    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }

    // Observer callbacks
    @Override
    public void onCourtStatusChanged(int courtIndex, int newStatus, String info) { /* không cần xử lý */ }

    @Override
    public void onInvoiceAdded(AppState.InvoiceEntry entry, long tongDoanhThu) {
        if (getView() == null) return;
        refreshStats();
    }

    @Override
    public void onBookingAdded(AppState.BookingEntry entry) { /* không cần xử lý */ }
}
