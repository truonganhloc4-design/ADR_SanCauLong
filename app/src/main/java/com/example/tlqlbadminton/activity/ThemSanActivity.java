package com.example.tlqlbadminton.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tlqlbadminton.R;

public class ThemSanActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etTenSan;
    private EditText etGiaThue;
    private Button btnXacNhanThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_san);

        btnBack = findViewById(R.id.btnBack);
        etTenSan = findViewById(R.id.etTenSan);
        etGiaThue = findViewById(R.id.etGiaThue);
        btnXacNhanThem = findViewById(R.id.btnXacNhanThem);

        btnBack.setOnClickListener(v -> finish());

        btnXacNhanThem.setOnClickListener(v -> {
            String ten = etTenSan.getText().toString().trim();
            String gia = etGiaThue.getText().toString().trim();

            if (TextUtils.isEmpty(ten) || TextUtils.isEmpty(gia)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Ghi vào SQLite hoặc AppState
            Toast.makeText(this, "Đã thêm " + ten + " thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
