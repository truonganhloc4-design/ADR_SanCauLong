package com.example.tlqlbadminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.sqlite.TaiKhoanDAO;

public class DangNhapActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvLoginError;
    private TextView tvGoRegister;
    private TaiKhoanDAO taiKhoanDAO;

    // Default credentials (thay bằng DB sau)
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "123";

    // Màn hình đăng nhập: khởi tạo DAO, ánh xạ view và gắn sự kiện.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        taiKhoanDAO = new TaiKhoanDAO(this);
        bindViews();
        setupInputs();
        setupListeners();
    }

    // Ánh xạ các control trong activity_dang_nhap.xml sang biến Java.
    private void bindViews() {
        etUsername   = findViewById(R.id.etUsername);
        etPassword   = findViewById(R.id.etPassword);
        btnLogin     = findViewById(R.id.btnLogin);
        tvLoginError = findViewById(R.id.tvLoginError);
        tvGoRegister = findViewById(R.id.tvGoRegister);
    }

    // Chuẩn bị ô nhập để bàn phím hiện đúng khi người dùng chạm vào.
    private void setupInputs() {
        prepareInput(etUsername);
        prepareInput(etPassword);
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

    // Gắn sự kiện cho nút đăng nhập và link sang đăng ký.
    private void setupListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
        tvGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, DangKyActivity.class)));
    }

    // Kiểm tra dữ liệu nhập, sau đó gọi DAO để xác thực tài khoản.
    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate
        if (TextUtils.isEmpty(username)) {
            showError(getString(R.string.error_empty_username));
            etUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showError(getString(R.string.error_empty_password));
            etPassword.requestFocus();
            return;
        }

        // Check credentials
        if (taiKhoanDAO.checkLogin(username, password)) {
            hideError();
            navigateToMain();
        } else {
            showError(getString(R.string.error_wrong_credentials));
        }
    }

    // Hiện lỗi đăng nhập lên màn hình.
    private void showError(String message) {
        tvLoginError.setText(message);
        tvLoginError.setVisibility(View.VISIBLE);
    }

    // Ẩn lỗi khi đăng nhập đúng.
    private void hideError() {
        tvLoginError.setVisibility(View.INVISIBLE);
    }

    // Chuyển sang MainActivity và xóa màn hình login khỏi back stack.
    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
