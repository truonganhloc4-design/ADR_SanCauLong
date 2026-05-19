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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lich_su, container, false);
        phieuDatSanDAO = new PhieuDatSanDAO(requireContext());
        sanDAO = new SanDAO(requireContext());
        tvEmpty = root.findViewById(R.id.tvEmptyLichSu);
        RecyclerView rvLichDat = root.findViewById(R.id.rvLichDat);
        rvLichDat.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SimpleTextAdapter();
        rvLichDat.setAdapter(adapter);
        refreshList();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        if (phieuDatSanDAO == null || tvEmpty == null) return;
        List<PhieuDatSan> bookings = phieuDatSanDAO.getPendingBookings();
        tvEmpty.setVisibility(bookings.isEmpty() ? View.VISIBLE : View.GONE);

        List<SimpleTextAdapter.Row> rows = new ArrayList<>();
        for (PhieuDatSan phieu : bookings) {
            San san = sanDAO.getSanById(phieu.getMaSan());
            String tenSan = san != null ? san.getTenSan() : "San #" + phieu.getMaSan();
            rows.add(new SimpleTextAdapter.Row(
                    tenSan + " - " + phieu.getTenKhach(),
                    phieu.getNgayDat() + " | " + phieu.getKhungGioChoi() +
                            " | Coc: " + formatCurrency((long) phieu.getTienCoc()) + " VND"));
        }
        adapter.submitList(rows);
    }

    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }
}
