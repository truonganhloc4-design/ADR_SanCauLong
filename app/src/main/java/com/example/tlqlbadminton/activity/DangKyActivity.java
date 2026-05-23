package com.example.tlqlbadminton.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.model.TaiKhoan;
import com.example.tlqlbadminton.sqlite.TaiKhoanDAO;

public class DangKyActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etDisplayName;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private TextView tvRegisterError;
    private Button btnRegister;
    private TextView tvGoLogin;
    private TaiKhoanDAO taiKhoanDAO;

    // Màn hình đăng ký: khởi tạo DAO, ánh xạ view và gắn sự kiện.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        taiKhoanDAO = new TaiKhoanDAO(this);
        bindViews();
        setupInputs();
        setupListeners();
    }

    // Ánh xạ các control trong activity_dang_ky.xml.
    private void bindViews() {
        btnBack = findViewById(R.id.btnBack);
        etDisplayName = findViewById(R.id.etDisplayName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tvRegisterError = findViewById(R.id.tvRegisterError);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoLogin = findViewById(R.id.tvGoLogin);
    }

    // Chuẩn bị các ô nhập để dễ focus và bật bàn phím.
    private void setupInputs() {
        prepareInput(etDisplayName);
        prepareInput(etUsername);
        prepareInput(etPassword);
        prepareInput(etConfirmPassword);
    }

    // Cấu hình một EditText có thể focus và tự bật bàn phím.
    private void prepareInput(EditText editText) {
        editText.setEnabled(true);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setCursorVisible(true);
        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                editText.requestFocus();
                editText.postDelayed(() -> {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    }
                }, 80);
            }
            return false;
        });
    }

    // Gắn sự kiện quay lại, sang đăng nhập và đăng ký tài khoản.
    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        tvGoLogin.setOnClickListener(v -> finish());
        btnRegister.setOnClickListener(v -> attemptRegister());
    }

    // Kiểm tra form đăng ký rồi lưu tài khoản mới vào DB.
    private void attemptRegister() {
        String displayName = etDisplayName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(displayName)) {
            showError("Vui long nhap ten hien thi");
            etDisplayName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(username)) {
            showError("Vui long nhap ten dang nhap");
            etUsername.requestFocus();
            return;
        }
        if (username.length() < 4) {
            showError("Ten dang nhap phai co it nhat 4 ky tu");
            etUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showError("Vui long nhap mat khau");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 3) {
            showError("Mat khau phai co it nhat 3 ky tu");
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Mat khau nhap lai khong khop");
            etConfirmPassword.requestFocus();
            return;
        }
        if (taiKhoanDAO.isUsernameExists(username)) {
            showError("Ten dang nhap da ton tai");
            etUsername.requestFocus();
            return;
        }

        TaiKhoan taiKhoan = new TaiKhoan(username, password, displayName);
        long result = taiKhoanDAO.insertTaiKhoan(taiKhoan);
        if (result != -1) {
            Toast.makeText(this, "Dang ky thanh cong", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            showError("Khong the dang ky tai khoan");
        }
    }

    // Hiện lỗi đăng ký lên màn hình.
    private void showError(String message) {
        tvRegisterError.setText(message);
        tvRegisterError.setVisibility(android.view.View.VISIBLE);
    }
}
