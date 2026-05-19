package com.example.tlqlbadminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.model.PhieuDatSan;
import com.example.tlqlbadminton.model.San;
import com.example.tlqlbadminton.sqlite.DBHelper;
import com.example.tlqlbadminton.sqlite.PhieuDatSanDAO;
import com.example.tlqlbadminton.sqlite.SanDAO;

import java.util.ArrayList;
import java.util.List;

public class SoDoFragment extends Fragment {

    private TextView tvCountAll, tvCountAvailable, tvCountOccupied;
    private TextView[] courtNames;
    private View[] accents;
    private TextView[] statusChips;
    private TextView[] infoLabels;
    private LinearLayout[] availableGroups;
    private TextView[] chiTietBtns;
    private TextView[] datLichBtns;
    private TextView[] nhanSanBtns;

    private SanDAO sanDAO;
    private PhieuDatSanDAO phieuDatSanDAO;
    private final List<San> displayedCourts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_so_do, container, false);
        sanDAO = new SanDAO(requireContext());
        phieuDatSanDAO = new PhieuDatSanDAO(requireContext());
        bindViews(root);
        setupButtons();
        refreshAll();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAll();
    }

    private void bindViews(View root) {
        tvCountAll = root.findViewById(R.id.chipTatCa);
        tvCountAvailable = root.findViewById(R.id.chipSanTrong);
        tvCountOccupied = root.findViewById(R.id.chipDangChoi);

        courtNames = new TextView[]{
                root.findViewById(R.id.tvCourt1Name),
                root.findViewById(R.id.tvCourt2Name),
                root.findViewById(R.id.tvCourt3Name),
                root.findViewById(R.id.tvCourt4Name)
        };
        accents = new View[]{
                root.findViewById(R.id.accentSan1),
                root.findViewById(R.id.accentSan2),
                root.findViewById(R.id.accentSan3),
                root.findViewById(R.id.accentSan4)
        };
        statusChips = new TextView[]{
                root.findViewById(R.id.tvCourt1Status),
                root.findViewById(R.id.tvCourt2Status),
                root.findViewById(R.id.tvCourt3Status),
                root.findViewById(R.id.tvCourt4Status)
        };
        infoLabels = new TextView[]{
                root.findViewById(R.id.tvCourt1Info),
                root.findViewById(R.id.tvCourt2Info),
                root.findViewById(R.id.tvCourt3Info),
                root.findViewById(R.id.tvCourt4Info)
        };
        availableGroups = new LinearLayout[]{
                root.findViewById(R.id.btnGroupSan1Available),
                root.findViewById(R.id.btnGroupSan2Available),
                root.findViewById(R.id.btnGroupSan3Available),
                root.findViewById(R.id.btnGroupSan4Available)
        };
        chiTietBtns = new TextView[]{
                root.findViewById(R.id.btnCourt1ChiTiet),
                root.findViewById(R.id.btnCourt2ChiTiet),
                root.findViewById(R.id.btnCourt3ChiTiet),
                root.findViewById(R.id.btnCourt4ChiTiet)
        };
        datLichBtns = new TextView[]{
                root.findViewById(R.id.btnCourt1DatLich),
                root.findViewById(R.id.btnCourt2DatLich),
                root.findViewById(R.id.btnCourt3DatLich),
                root.findViewById(R.id.btnCourt4DatLich)
        };
        nhanSanBtns = new TextView[]{
                root.findViewById(R.id.btnCourt1NhanSan),
                root.findViewById(R.id.btnCourt2NhanSan),
                root.findViewById(R.id.btnCourt3NhanSan),
                root.findViewById(R.id.btnCourt4NhanSan)
        };
    }

    private void setupButtons() {
        for (int i = 0; i < 4; i++) {
            final int displayIndex = i;
            nhanSanBtns[i].setOnClickListener(v -> openNhanSan(displayIndex));
            datLichBtns[i].setOnClickListener(v -> openDatLich(displayIndex));
            chiTietBtns[i].setOnClickListener(v -> openThanhToan(displayIndex));
        }
    }

    private void openNhanSan(int displayIndex) {
        San san = getDisplayedSan(displayIndex);
        if (san == null) return;
        Intent intent = new Intent(getActivity(), NhanSanActivity.class);
        intent.putExtra(NhanSanActivity.EXTRA_COURT_ID, String.valueOf(san.getMaSan()));
        intent.putExtra(NhanSanActivity.EXTRA_COURT_INDEX, displayIndex);
        startActivity(intent);
    }

    private void openDatLich(int displayIndex) {
        San san = getDisplayedSan(displayIndex);
        if (san == null) return;
        Intent intent = new Intent(getActivity(), DatLichTruocActivity.class);
        intent.putExtra(DatLichTruocActivity.EXTRA_COURT_ID, String.valueOf(san.getMaSan()));
        intent.putExtra(DatLichTruocActivity.EXTRA_COURT_INDEX, displayIndex);
        startActivity(intent);
    }

    private void openThanhToan(int displayIndex) {
        San san = getDisplayedSan(displayIndex);
        if (san == null) return;
        PhieuDatSan phieu = phieuDatSanDAO.getActivePhieuBySan(san.getMaSan());
        Intent intent = new Intent(getActivity(), ThanhToanActivity.class);
        intent.putExtra(ThanhToanActivity.EXTRA_COURT_ID, String.valueOf(san.getMaSan()));
        intent.putExtra(ThanhToanActivity.EXTRA_COURT_INDEX, displayIndex);
        intent.putExtra(ThanhToanActivity.EXTRA_BOOKING_CODE,
                phieu != null ? phieu.getMaCa() : "#BK-" + san.getMaSan());
        startActivity(intent);
    }

    private void refreshAll() {
        if (sanDAO == null || tvCountAll == null) return;
        List<San> allCourts = sanDAO.getAllSan();
        displayedCourts.clear();
        for (int i = 0; i < allCourts.size() && i < 4; i++) {
            displayedCourts.add(allCourts.get(i));
        }

        tvCountAll.setText("Tat ca (" + sanDAO.countAllSan() + ")");
        tvCountAvailable.setText("San trong (" +
                sanDAO.countSanByTrangThai(DBHelper.SAN_TRONG) + ")");
        tvCountOccupied.setText("Dang choi (" +
                sanDAO.countSanByTrangThai(DBHelper.SAN_DANG_CHOI) + ")");

        for (int i = 0; i < 4; i++) {
            San san = getDisplayedSan(i);
            if (san == null) {
                courtNames[i].setText("San --");
                infoLabels[i].setText("Chua co du lieu");
                availableGroups[i].setVisibility(View.GONE);
                chiTietBtns[i].setVisibility(View.GONE);
            } else {
                updateCourtCard(i, san);
            }
        }
    }

    private void updateCourtCard(int idx, San san) {
        courtNames[idx].setText(san.getTenSan());
        if (san.getTrangThai() == DBHelper.SAN_DANG_CHOI) {
            PhieuDatSan phieu = phieuDatSanDAO.getActivePhieuBySan(san.getMaSan());
            String info = phieu == null ? san.getLoaiSan() : phieu.getTenKhach();
            infoLabels[idx].setText(info);
            accents[idx].setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.status_occupied));
            statusChips[idx].setText("Dang choi");
            statusChips[idx].setTextColor(ContextCompat.getColor(requireContext(), R.color.status_occupied));
            statusChips[idx].setBackgroundResource(R.drawable.bg_chip_occupied);
            availableGroups[idx].setVisibility(View.GONE);
            chiTietBtns[idx].setVisibility(View.VISIBLE);
            chiTietBtns[idx].setBackgroundResource(R.drawable.bg_btn_danger);
            chiTietBtns[idx].setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        } else {
            infoLabels[idx].setText(san.getLoaiSan() + " - " + formatCurrency((long) san.getGiaMoiGio()) + " VND/gio");
            accents[idx].setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.status_available));
            statusChips[idx].setText("Trong");
            statusChips[idx].setTextColor(ContextCompat.getColor(requireContext(), R.color.status_available));
            statusChips[idx].setBackgroundResource(R.drawable.bg_chip_available);
            availableGroups[idx].setVisibility(View.VISIBLE);
            chiTietBtns[idx].setVisibility(View.GONE);
        }
    }

    private San getDisplayedSan(int index) {
        return index >= 0 && index < displayedCourts.size() ? displayedCourts.get(index) : null;
    }

    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }
}
