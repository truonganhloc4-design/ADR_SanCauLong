package com.example.tlqlbadminton.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.model.San;
import com.example.tlqlbadminton.sqlite.DBHelper;
import com.example.tlqlbadminton.sqlite.SanDAO;

public class ThemSanActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etTenSan;
    private EditText etGiaThue;
    private Button btnXacNhanThem;
    private SanDAO sanDAO;

    // Màn hình thêm sân nhanh: nhập tên sân và giá thuê.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_san);

        sanDAO = new SanDAO(this);
        btnBack = findViewById(R.id.btnBack);
        etTenSan = findViewById(R.id.etTenSan);
        etGiaThue = findViewById(R.id.etGiaThue);
        btnXacNhanThem = findViewById(R.id.btnXacNhanThem);

        btnBack.setOnClickListener(v -> finish());
        btnXacNhanThem.setOnClickListener(v -> saveCourt());
    }

    // Kiểm tra form rồi thêm sân mới vào bảng San.
    private void saveCourt() {
        String ten = etTenSan.getText().toString().trim();
        String gia = etGiaThue.getText().toString().trim();

        if (TextUtils.isEmpty(ten) || TextUtils.isEmpty(gia)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sân mới mặc định là sân trống và đang hoạt động.
        San san = new San();
        san.setTenSan(ten);
        san.setGiaMoiGio(parseMoney(gia));
        san.setLoaiSan("Cỏ nhân tạo");
        san.setTrangThai(DBHelper.SAN_TRONG);
        san.setTinhTrangHoatDong(1);

        boolean ok = sanDAO.insertSan(san);
        Toast.makeText(this, ok ? "Đã thêm sân thành công" : "Không thể thêm sân",
                Toast.LENGTH_SHORT).show();
        if (ok) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    // Chuyển chuỗi tiền người dùng nhập thành số double.
    private double parseMoney(String value) {
        try {
            return Double.parseDouble(value.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
