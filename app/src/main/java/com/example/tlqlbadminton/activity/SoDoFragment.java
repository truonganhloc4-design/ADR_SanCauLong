package com.example.tlqlbadminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private RecyclerView rvSoDoSan;
    private CourtAdapter courtAdapter;

    private SanDAO sanDAO;
    private PhieuDatSanDAO phieuDatSanDAO;
    private final List<San> displayedCourts = new ArrayList<>();
    private int activeStatusFilter = -1;

    // Tạo giao diện sơ đồ sân và load danh sách sân lần đầu.
    @Nullable
    // Khi quay lại tab này thì refresh trạng thái sân.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_so_do, container, false);
        sanDAO = new SanDAO(requireContext());
        phieuDatSanDAO = new PhieuDatSanDAO(requireContext());
        bindViews(root);
        setupCourtList();
        setupFilterChips();
        refreshAll();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAll();
    }

    // Ánh xạ các view trong fragment_so_do.xml.
    private void bindViews(View root) {
        tvCountAll = root.findViewById(R.id.chipTatCa);
        tvCountAvailable = root.findViewById(R.id.chipSanTrong);
        tvCountOccupied = root.findViewById(R.id.chipDangChoi);
        rvSoDoSan = root.findViewById(R.id.rvSoDoSan);
    }

    // Gắn RecyclerView dạng lưới 2 cột cho danh sách sân.
    private void setupCourtList() {
        courtAdapter = new CourtAdapter();
        rvSoDoSan.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvSoDoSan.setAdapter(courtAdapter);
    }

    // Gắn sự kiện lọc tất cả/trống/đang chơi.
    private void setupFilterChips() {
        tvCountAll.setOnClickListener(v -> setStatusFilter(-1));
        tvCountAvailable.setOnClickListener(v -> setStatusFilter(DBHelper.SAN_TRONG));
        tvCountOccupied.setOnClickListener(v -> setStatusFilter(DBHelper.SAN_DANG_CHOI));
        updateFilterChipStyle();
    }

    // Lưu trạng thái lọc và load lại danh sách sân.
    private void setStatusFilter(int statusFilter) {
        activeStatusFilter = statusFilter;
        refreshAll();
    }

    // Load sân từ DB, lọc theo trạng thái và cập nhật số lượng trên chip.
    public void refreshAll() {
        if (sanDAO == null || tvCountAll == null || courtAdapter == null) return;
        List<San> allCourts = sanDAO.getAllSan();
        displayedCourts.clear();
        for (San san : allCourts) {
            if (activeStatusFilter == -1 || san.getTrangThai() == activeStatusFilter) {
                displayedCourts.add(san);
            }
        }

        tvCountAll.setText("Tất cả (" + sanDAO.countAllSan() + ")");
        tvCountAvailable.setText("Sân trống (" +
                sanDAO.countSanByTrangThai(DBHelper.SAN_TRONG) + ")");
        tvCountOccupied.setText("Đang chơi (" +
                sanDAO.countSanByTrangThai(DBHelper.SAN_DANG_CHOI) + ")");

        updateFilterChipStyle();
        courtAdapter.notifyDataSetChanged();
    }

    // Mở màn hình nhận sân cho sân được chọn.
    private void openNhanSan(int position) {
        San san = getDisplayedSan(position);
        if (san == null) return;
        Intent intent = new Intent(getActivity(), NhanSanActivity.class);
        intent.putExtra(NhanSanActivity.EXTRA_COURT_ID, String.valueOf(san.getMaSan()));
        intent.putExtra(NhanSanActivity.EXTRA_COURT_INDEX, position);
        startActivity(intent);
    }

    // Mở màn hình đặt lịch trước cho sân được chọn.
    private void openDatLich(int position) {
        San san = getDisplayedSan(position);
        if (san == null) return;
        Intent intent = new Intent(getActivity(), DatLichTruocActivity.class);
        intent.putExtra(DatLichTruocActivity.EXTRA_COURT_ID, String.valueOf(san.getMaSan()));
        intent.putExtra(DatLichTruocActivity.EXTRA_COURT_INDEX, position);
        startActivity(intent);
    }

    // Mở màn hình thanh toán cho sân đang chơi.
    private void openThanhToan(int position) {
        San san = getDisplayedSan(position);
        if (san == null) return;
        PhieuDatSan phieu = phieuDatSanDAO.getActivePhieuBySan(san.getMaSan());
        Intent intent = new Intent(getActivity(), ThanhToanActivity.class);
        intent.putExtra(ThanhToanActivity.EXTRA_COURT_ID, String.valueOf(san.getMaSan()));
        intent.putExtra(ThanhToanActivity.EXTRA_COURT_INDEX, position);
        intent.putExtra(ThanhToanActivity.EXTRA_BOOKING_CODE,
                phieu != null ? phieu.getMaCa() : "#BK-" + san.getMaSan());
        startActivity(intent);
    }

    // Lấy sân đang hiển thị theo vị trí trong RecyclerView.
    private San getDisplayedSan(int index) {
        return index >= 0 && index < displayedCourts.size() ? displayedCourts.get(index) : null;
    }

    // Cập nhật giao diện chip lọc đang được chọn.
    private void updateFilterChipStyle() {
        if (tvCountAll == null) return;
        styleFilterChip(tvCountAll, activeStatusFilter == -1);
        styleFilterChip(tvCountAvailable, activeStatusFilter == DBHelper.SAN_TRONG);
        styleFilterChip(tvCountOccupied, activeStatusFilter == DBHelper.SAN_DANG_CHOI);
    }

    // Style một chip lọc theo trạng thái active/inactive.
    private void styleFilterChip(TextView chip, boolean active) {
        chip.setBackgroundResource(active ? R.drawable.bg_btn_primary : R.drawable.bg_btn_outlined);
        chip.setTextColor(ContextCompat.getColor(requireContext(),
                active ? R.color.white : R.color.text_secondary));
        chip.setTypeface(null, active ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
    }

    // Format số tiền kiểu 70000 -> 70.000.
    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }

    private class CourtAdapter extends RecyclerView.Adapter<CourtAdapter.CourtViewHolder> {
        // Tạo item view cho một sân trong lưới.
        @NonNull
        // Gán dữ liệu sân vào item view theo position.
        @Override
        public CourtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_item_so_do_san, parent, false);
            return new CourtViewHolder(view);
        }

        // Trả về số sân đang hiển thị.
        @Override
        public void onBindViewHolder(@NonNull CourtViewHolder holder, int position) {
            holder.bind(displayedCourts.get(position), position);
        }

        @Override
        public int getItemCount() {
            return displayedCourts.size();
        }

        class CourtViewHolder extends RecyclerView.ViewHolder {
            private final View accent;
            private final TextView tvCourtName;
            private final TextView tvCourtStatus;
            private final ImageView imgInfoIcon;
            private final TextView tvCourtInfo;
            private final LinearLayout layoutCustomer;
            private final TextView tvCustomerName;
            private final LinearLayout layoutAvailableActions;
            private final TextView btnDatLich;
            private final TextView btnNhanSan;
            private final TextView btnChiTiet;

            CourtViewHolder(@NonNull View itemView) {
                super(itemView);
                accent = itemView.findViewById(R.id.viewAccent);
                tvCourtName = itemView.findViewById(R.id.tvCourtName);
                tvCourtStatus = itemView.findViewById(R.id.tvCourtStatus);
                imgInfoIcon = itemView.findViewById(R.id.imgInfoIcon);
                tvCourtInfo = itemView.findViewById(R.id.tvCourtInfo);
                layoutCustomer = itemView.findViewById(R.id.layoutCustomer);
                tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
                layoutAvailableActions = itemView.findViewById(R.id.layoutAvailableActions);
                btnDatLich = itemView.findViewById(R.id.btnDatLich);
                btnNhanSan = itemView.findViewById(R.id.btnNhanSan);
                btnChiTiet = itemView.findViewById(R.id.btnChiTiet);
            }

            // Hiển thị sân theo trạng thái: trống hoặc đang chơi.
            void bind(San san, int position) {
                tvCourtName.setText(san.getTenSan());
                btnDatLich.setOnClickListener(v -> openDatLich(position));
                btnNhanSan.setOnClickListener(v -> openNhanSan(position));
                btnChiTiet.setOnClickListener(v -> openThanhToan(position));

                // Sân đang chơi: hiện khách, khung giờ và nút Chi tiết.
                if (san.getTrangThai() == DBHelper.SAN_DANG_CHOI) {
                    PhieuDatSan phieu = phieuDatSanDAO.getActivePhieuBySan(san.getMaSan());
                    accent.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.status_occupied));
                    tvCourtName.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                    tvCourtStatus.setText("Đang chơi");
                    tvCourtStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_occupied));
                    tvCourtStatus.setBackgroundResource(R.drawable.bg_chip_occupied);
                    imgInfoIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.status_occupied));
                    tvCourtInfo.setText(phieu == null ? san.getLoaiSan() : phieu.getKhungGioChoi());
                    tvCourtInfo.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_occupied));
                    tvCustomerName.setText(phieu == null ? "" : phieu.getTenKhach());
                    layoutCustomer.setVisibility(phieu == null ? View.GONE : View.VISIBLE);
                    layoutAvailableActions.setVisibility(View.GONE);
                    btnChiTiet.setVisibility(View.VISIBLE);
                } else {
                    // Sân trống: hiện giá và nút Đặt lịch/Nhận sân.
                    accent.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.status_available));
                    tvCourtName.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary));
                    tvCourtStatus.setText("Trống");
                    tvCourtStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_available));
                    tvCourtStatus.setBackgroundResource(R.drawable.bg_chip_available);
                    imgInfoIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.text_secondary));
                    tvCourtInfo.setText(formatCurrency((long) san.getGiaMoiGio()) + " VND/gio");
                    tvCourtInfo.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
                    layoutCustomer.setVisibility(View.GONE);
                    layoutAvailableActions.setVisibility(View.VISIBLE);
                    btnChiTiet.setVisibility(View.GONE);
                }
            }
        }
    }
}
