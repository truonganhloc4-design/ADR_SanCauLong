package com.example.tlqlbadminton.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tlqlbadminton.R;

public class CauHinhSanActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageButton btnThemSan;

    // Form fields
    private EditText etTenSan;
    private EditText etLoaiSan;
    private EditText etGiaMacDinh;
    private EditText etMoTa;
    private Button btnHuy;
    private Button btnLuuSan;

    // Court list
    private RecyclerView rvDanhSachSan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cau_hinh_san);

        bindViews();
        setupToolbar();
        setupFormActions();
        loadCourtList();
    }

    private void bindViews() {
        btnBack       = findViewById(R.id.btnBack);
        btnThemSan    = findViewById(R.id.btnThemSan);
        etTenSan      = findViewById(R.id.etTenSan);
        etLoaiSan     = findViewById(R.id.etLoaiSan);
        etGiaMacDinh  = findViewById(R.id.etGiaMacDinh);
        etMoTa        = findViewById(R.id.etMoTa);
        btnHuy        = findViewById(R.id.btnHuy);
        btnLuuSan     = findViewById(R.id.btnLuuSan);
        rvDanhSachSan = findViewById(R.id.rvDanhSachSan);

        rvDanhSachSan.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        btnBack.setOnClickListener(v -> onBackPressed());
        btnThemSan.setOnClickListener(v -> clearForm());
    }

    private void setupFormActions() {
        btnHuy.setOnClickListener(v -> {
            clearForm();
            Toast.makeText(this, "Đã hủy", Toast.LENGTH_SHORT).show();
        });

        btnLuuSan.setOnClickListener(v -> saveCourt());

        // Edit/Delete buttons for static court item 1
        ImageButton btnEditCourt1   = findViewById(R.id.btnEditCourt1);
        ImageButton btnDeleteCourt1 = findViewById(R.id.btnDeleteCourt1);

        btnEditCourt1.setOnClickListener(v -> {
            // Pre-fill form with court 1 data
            etTenSan.setText("Sân 01");
            etLoaiSan.setText("Cỏ nhân tạo");
            etGiaMacDinh.setText("70000");
            etMoTa.setText("");
        });

        btnDeleteCourt1.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa sân")
                    .setMessage(R.string.confirm_delete_court)
                    .setPositiveButton(R.string.dong_y, (dialog, which) -> {
                        // TODO: Delete from DB
                        Toast.makeText(this, "Đã xóa sân 01", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(R.string.huy, null)
                    .show();
        });
    }

    private void saveCourt() {
        String tenSan     = etTenSan.getText().toString().trim();
        String loaiSan    = etLoaiSan.getText().toString().trim();
        String giaStr     = etGiaMacDinh.getText().toString().trim();

        if (TextUtils.isEmpty(tenSan)) {
            Toast.makeText(this, "Vui lòng nhập tên sân", Toast.LENGTH_SHORT).show();
            etTenSan.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(loaiSan)) {
            Toast.makeText(this, "Vui lòng nhập loại sân", Toast.LENGTH_SHORT).show();
            etLoaiSan.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(giaStr)) {
            Toast.makeText(this, "Vui lòng nhập giá mặc định", Toast.LENGTH_SHORT).show();
            etGiaMacDinh.requestFocus();
            return;
        }

        long giaMacDinh = Long.parseLong(giaStr.replaceAll("[^0-9]", ""));

        // TODO: Insert/Update in DB via DatabaseHelper
        Toast.makeText(this, "Đã lưu: " + tenSan, Toast.LENGTH_LONG).show();
        clearForm();
        loadCourtList();
    }

    private void clearForm() {
        etTenSan.setText("");
        etLoaiSan.setText("");
        etGiaMacDinh.setText("");
        etMoTa.setText("");
        etTenSan.requestFocus();
    }

    private void loadCourtList() {
        // TODO: Load courts from DatabaseHelper and bind to RecyclerView adapter
    }
}
