package com.example.tlqlbadminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.model.AppState;

public class SoDoFragment extends Fragment implements AppState.OnStateChangedListener {

    // Stats
    private TextView tvCountAvailable, tvCountOccupied, tvCountBooked;

    // Accent bars
    private View[] accents;
    // Status chips
    private TextView[] statusChips;
    // Info labels
    private TextView[] infoLabels;
    // Available button groups
    private LinearLayout[] availableGroups;
    // ChiTiet buttons
    private TextView[] chiTietBtns;

    // DatLich buttons per court
    private TextView[] datLichBtns;
    // NhanSan buttons per court
    private TextView[] nhanSanBtns;

    private AppState appState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_so_do, container, false);
        appState = AppState.getInstance();
        bindViews(root);
        setupButtons();
        refreshAll();
        return root;
    }

    private void bindViews(View root) {
        tvCountAvailable = root.findViewById(R.id.chipSanTrong);
        tvCountOccupied  = root.findViewById(R.id.chipDangChoi);
        tvCountBooked    = root.findViewById(R.id.chipTatCa); // We will update logic for this or just ignore

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
            final int courtIdx = i;
            final String courtId = String.valueOf(i + 1);

            nhanSanBtns[i].setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), NhanSanActivity.class);
                intent.putExtra(NhanSanActivity.EXTRA_COURT_ID, courtId);
                intent.putExtra(NhanSanActivity.EXTRA_COURT_INDEX, courtIdx);
                startActivity(intent);
            });

            datLichBtns[i].setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), DatLichTruocActivity.class);
                intent.putExtra(DatLichTruocActivity.EXTRA_COURT_ID, courtId);
                intent.putExtra(DatLichTruocActivity.EXTRA_COURT_INDEX, courtIdx);
                startActivity(intent);
            });

            chiTietBtns[i].setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ThanhToanActivity.class);
                intent.putExtra(ThanhToanActivity.EXTRA_COURT_ID, courtId);
                intent.putExtra(ThanhToanActivity.EXTRA_COURT_INDEX, courtIdx);
                intent.putExtra(ThanhToanActivity.EXTRA_BOOKING_CODE,
                        "#BK-0000" + appState.getCourtPhieuId(courtIdx));
                startActivity(intent);
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        appState.addListener(this);
        refreshAll();
    }

    @Override
    public void onPause() {
        super.onPause();
        appState.removeListener(this);
    }

    private void refreshAll() {
        tvCountAvailable.setText("Sân trống (" + appState.countAvailable() + ")");
        tvCountOccupied.setText("Đang chơi (" + appState.countOccupied() + ")");
        tvCountBooked.setText("Tất cả (" + (appState.countAvailable() + appState.countOccupied() + appState.countBooked()) + ")");

        for (int i = 0; i < 4; i++) {
            updateCourtCard(i, appState.getCourtStatus(i), appState.getCourtInfo(i));
        }
    }

    private void updateCourtCard(int idx, int status, String info) {
        infoLabels[idx].setText(info);

        switch (status) {
            case AppState.STATUS_AVAILABLE:
                accents[idx].setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.status_available));
                statusChips[idx].setText("Trống");
                statusChips[idx].setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.status_available));
                statusChips[idx].setBackgroundResource(R.drawable.bg_chip_available);
                availableGroups[idx].setVisibility(View.VISIBLE);
                chiTietBtns[idx].setVisibility(View.GONE);
                break;

            case AppState.STATUS_OCCUPIED:
                accents[idx].setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.status_occupied));
                statusChips[idx].setText("Đang chơi");
                statusChips[idx].setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.status_occupied));
                statusChips[idx].setBackgroundResource(R.drawable.bg_chip_occupied);
                availableGroups[idx].setVisibility(View.GONE);
                chiTietBtns[idx].setVisibility(View.VISIBLE);
                chiTietBtns[idx].setBackgroundResource(R.drawable.bg_btn_danger);
                chiTietBtns[idx].setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white));
                break;

            case AppState.STATUS_BOOKED:
                accents[idx].setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.status_booked));
                statusChips[idx].setText("Đã đặt");
                statusChips[idx].setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.status_booked));
                statusChips[idx].setBackgroundResource(R.drawable.bg_chip_booked);
                availableGroups[idx].setVisibility(View.GONE);
                chiTietBtns[idx].setVisibility(View.VISIBLE);
                chiTietBtns[idx].setBackgroundResource(R.drawable.bg_btn_secondary);
                chiTietBtns[idx].setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.primary));
                break;
        }
    }

    // AppState.OnStateChangedListener
    @Override
    public void onCourtStatusChanged(int courtIndex, int newStatus, String info) {
        if (getView() == null) return;
        updateCourtCard(courtIndex, newStatus, info);
        tvCountAvailable.setText("Sân trống (" + appState.countAvailable() + ")");
        tvCountOccupied.setText("Đang chơi (" + appState.countOccupied() + ")");
        tvCountBooked.setText("Tất cả (" + (appState.countAvailable() + appState.countOccupied() + appState.countBooked()) + ")");
    }

    @Override
    public void onInvoiceAdded(AppState.InvoiceEntry entry, long tongDoanhThu) { /* handled by ThongKeFragment */ }

    @Override
    public void onBookingAdded(AppState.BookingEntry entry) { /* handled by LichSuFragment */ }
}
