package com.example.tlqlbadminton.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.adapter.SanAdapter;
import com.example.tlqlbadminton.model.San;
import com.example.tlqlbadminton.sqlite.DBHelper;
import com.example.tlqlbadminton.sqlite.SanDAO;

import java.util.ArrayList;
import java.util.List;

public class CauHinhSanActivity extends AppCompatActivity {

    private EditText etTenSan, etLoaiSan, etGiaMacDinh, etMoTa;
    private SanDAO sanDAO;
    private SanAdapter sanAdapter;
    private ScrollView scrollContent;
    private ListView lvDanhSachSan;
    private View sectionDanhSachSan;
    private final List<San> dsSan = new ArrayList<>();
    private San editingSan;

    // Màn hình cấu hình sân: thêm, sửa, xóa và xem danh sách sân.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cau_hinh_san);

        sanDAO = new SanDAO(this);
        AddViews();
        AddEvents();
        loadCourtList();
    }

    // Ánh xạ view và gắn các nút chính.
    private void AddViews() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnThemSan = findViewById(R.id.btnThemSan);
        etTenSan = findViewById(R.id.etTenSan);
        etLoaiSan = findViewById(R.id.etLoaiSan);
        etGiaMacDinh = findViewById(R.id.etGiaMacDinh);
        etMoTa = findViewById(R.id.etMoTa);
        scrollContent = findViewById(R.id.scrollContent);
        lvDanhSachSan = findViewById(R.id.lvDanhSachSan);
        sectionDanhSachSan = findViewById(R.id.tvDanhSachSanTitle);
        Button btnHuy = findViewById(R.id.btnHuy);
        Button btnLuuSan = findViewById(R.id.btnLuuSan);

        btnBack.setOnClickListener(v -> finish());
        btnThemSan.setOnClickListener(v -> clearForm());
        btnHuy.setOnClickListener(v -> clearForm());
        btnLuuSan.setOnClickListener(v -> saveCourt());
    }

    // Chỗ này để mở rộng thêm sự kiện nếu cần.
    private void AddEvents() {
        // Su kien cua tung dong san duoc gan trong SanAdapter.
    }

    // Load danh sách sân từ DB và đưa vào SanAdapter.
    private void loadCourtList() {
        dsSan.clear();
        dsSan.addAll(sanDAO.getAllSanNewestFirst());

        if (sanAdapter == null) {
            sanAdapter = new SanAdapter(this, R.layout.layout_item_san, dsSan,
                    new SanAdapter.OnSanActionListener() {
                        @Override
                        public void onSuaSan(San san) {
                            fillForm(san);
                        }

                        @Override
                        public void onXoaSan(San san) {
                            confirmDelete(san);
                        }
                    });
            lvDanhSachSan.setAdapter(sanAdapter);
        } else {
            sanAdapter.notifyDataSetChanged();
        }

        lvDanhSachSan.post(() -> setListViewHeightBasedOnChildren(lvDanhSachSan));
    }

    // Thêm sân mới hoặc cập nhật sân đang sửa.
    private void saveCourt() {
        String tenSan = etTenSan.getText().toString().trim();
        String loaiSan = etLoaiSan.getText().toString().trim();
        String giaStr = etGiaMacDinh.getText().toString().trim();

        if (TextUtils.isEmpty(tenSan) || TextUtils.isEmpty(loaiSan) || TextUtils.isEmpty(giaStr)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin sân", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu editingSan có dữ liệu nghĩa là đang sửa; ngược lại là thêm mới.
        San san = editingSan != null ? editingSan : new San();
        san.setTenSan(tenSan);
        san.setLoaiSan(loaiSan);
        san.setGiaMoiGio(parseMoney(giaStr));
        if (editingSan == null) {
            san.setTrangThai(DBHelper.SAN_TRONG);
            san.setTinhTrangHoatDong(1);
        }

        boolean ok = editingSan == null ? sanDAO.insertSan(san) : sanDAO.updateSan(san);
        Toast.makeText(this, ok ? "Đã lưu sân" : "Không thể lưu sân", Toast.LENGTH_SHORT).show();
        if (ok) {
            clearForm();
            loadCourtList();
            scrollContent.postDelayed(() -> scrollContent.smoothScrollTo(0, sectionDanhSachSan.getTop()), 120);
        }
    }

    // Đưa dữ liệu sân được chọn lên form để sửa.
    private void fillForm(San san) {
        editingSan = san;
        etTenSan.setText(san.getTenSan());
        etLoaiSan.setText(san.getLoaiSan());
        etGiaMacDinh.setText(String.valueOf((long) san.getGiaMoiGio()));
        etMoTa.setText("");
    }

    // Hỏi xác nhận trước khi xóa sân.
    private void confirmDelete(San san) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa sân")
                .setMessage("Bạn có chắc muốn xóa " + san.getTenSan() + "?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    int result = sanDAO.deleteSan(san.getMaSan());
                    Toast.makeText(this, result > 0 ? "Đã xóa sân" : "Không thể xóa sân đang có dữ liệu",
                            Toast.LENGTH_SHORT).show();
                    loadCourtList();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Xóa form và quay về chế độ thêm sân mới.
    private void clearForm() {
        editingSan = null;
        etTenSan.setText("");
        etLoaiSan.setText("Cỏ nhân tạo");
        etGiaMacDinh.setText("");
        etMoTa.setText("");
        etTenSan.requestFocus();
    }

    // Chuyển chuỗi tiền thành số.
    private double parseMoney(String value) {
        try {
            return Double.parseDouble(value.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Tính lại chiều cao ListView khi đặt trong ScrollView để hiện đủ item.
    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) return;

        int totalHeight = 0;
        int availableWidth = listView.getWidth()
                - listView.getPaddingStart()
                - listView.getPaddingEnd();
        if (availableWidth <= 0) {
            availableWidth = getResources().getDisplayMetrics().widthPixels
                    - listView.getPaddingStart()
                    - listView.getPaddingEnd();
        }
        int widthSpec = View.MeasureSpec.makeMeasureSpec(availableWidth, View.MeasureSpec.EXACTLY);
        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, listView);
            item.measure(widthSpec, View.MeasureSpec.UNSPECIFIED);
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + listView.getDividerHeight() * Math.max(0, adapter.getCount() - 1);
        listView.setLayoutParams(params);
    }

    // Khi quay lại màn hình thì refresh danh sách sân.
    @Override
    protected void onResume() {
        super.onResume();
        if (sanDAO != null) loadCourtList();
    }
}
