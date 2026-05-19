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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.adapter.SanAdapter;
import com.example.tlqlbadminton.model.San;
import com.example.tlqlbadminton.sqlite.DBHelper;
import com.example.tlqlbadminton.sqlite.SanDAO;

import java.util.List;

public class CauHinhSanActivity extends AppCompatActivity {

    private EditText etTenSan, etLoaiSan, etGiaMacDinh, etMoTa;
    private SanDAO sanDAO;
    private SanAdapter sanAdapter;
    private San editingSan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cau_hinh_san);

        sanDAO = new SanDAO(this);
        AddViews();
        AddEvents();
        loadCourtList();
    }

    private void AddViews() {
        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnThemSan = findViewById(R.id.btnThemSan);
        etTenSan = findViewById(R.id.etTenSan);
        etLoaiSan = findViewById(R.id.etLoaiSan);
        etGiaMacDinh = findViewById(R.id.etGiaMacDinh);
        etMoTa = findViewById(R.id.etMoTa);
        Button btnHuy = findViewById(R.id.btnHuy);
        Button btnLuuSan = findViewById(R.id.btnLuuSan);

        btnBack.setOnClickListener(v -> finish());
        btnThemSan.setOnClickListener(v -> clearForm());
        btnHuy.setOnClickListener(v -> clearForm());
        btnLuuSan.setOnClickListener(v -> saveCourt());
    }

    private void AddEvents() {
        // Su kien cua tung dong san duoc gan trong SanAdapter.
    }

    private void loadCourtList() {
        List<San> dsSan = sanDAO.getAllSan();
        ListView lvDanhSachSan = findViewById(R.id.lvDanhSachSan);
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
        setListViewHeightBasedOnChildren(lvDanhSachSan);
    }

    private void saveCourt() {
        String tenSan = etTenSan.getText().toString().trim();
        String loaiSan = etLoaiSan.getText().toString().trim();
        String giaStr = etGiaMacDinh.getText().toString().trim();

        if (TextUtils.isEmpty(tenSan) || TextUtils.isEmpty(loaiSan) || TextUtils.isEmpty(giaStr)) {
            Toast.makeText(this, "Vui long nhap day du thong tin san", Toast.LENGTH_SHORT).show();
            return;
        }

        San san = editingSan != null ? editingSan : new San();
        san.setTenSan(tenSan);
        san.setLoaiSan(loaiSan);
        san.setGiaMoiGio(parseMoney(giaStr));
        if (editingSan == null) {
            san.setTrangThai(DBHelper.SAN_TRONG);
            san.setTinhTrangHoatDong(1);
        }

        boolean ok = editingSan == null ? sanDAO.insertSan(san) : sanDAO.updateSan(san);
        Toast.makeText(this, ok ? "Da luu san" : "Khong the luu san", Toast.LENGTH_SHORT).show();
        if (ok) {
            clearForm();
            loadCourtList();
        }
    }

    private void fillForm(San san) {
        editingSan = san;
        etTenSan.setText(san.getTenSan());
        etLoaiSan.setText(san.getLoaiSan());
        etGiaMacDinh.setText(String.valueOf((long) san.getGiaMoiGio()));
        etMoTa.setText("");
    }

    private void confirmDelete(San san) {
        new AlertDialog.Builder(this)
                .setTitle("Xoa san")
                .setMessage("Ban co chac muon xoa " + san.getTenSan() + "?")
                .setPositiveButton("Dong y", (dialog, which) -> {
                    int result = sanDAO.deleteSan(san.getMaSan());
                    Toast.makeText(this, result > 0 ? "Da xoa san" : "Khong the xoa san dang co du lieu",
                            Toast.LENGTH_SHORT).show();
                    loadCourtList();
                })
                .setNegativeButton("Huy", null)
                .show();
    }

    private void clearForm() {
        editingSan = null;
        etTenSan.setText("");
        etLoaiSan.setText("Co nhan tao");
        etGiaMacDinh.setText("");
        etMoTa.setText("");
        etTenSan.requestFocus();
    }

    private double parseMoney(String value) {
        try {
            return Double.parseDouble(value.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) return;

        int totalHeight = 0;
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, listView);
            item.measure(widthSpec, View.MeasureSpec.UNSPECIFIED);
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + listView.getDividerHeight() * Math.max(0, adapter.getCount() - 1);
        listView.setLayoutParams(params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sanDAO != null) loadCourtList();
    }
}
