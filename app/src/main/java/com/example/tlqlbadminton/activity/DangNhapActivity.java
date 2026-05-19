package com.example.tlqlbadminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
    private TaiKhoanDAO taiKhoanDAO;

    // Default credentials (thay bằng DB sau)
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        taiKhoanDAO = new TaiKhoanDAO(this);
        bindViews();
        setupListeners();
    }

    private void bindViews() {
        etUsername   = findViewById(R.id.etUsername);
        etPassword   = findViewById(R.id.etPassword);
        btnLogin     = findViewById(R.id.btnLogin);
        tvLoginError = findViewById(R.id.tvLoginError);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
    }

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

    private void showError(String message) {
        tvLoginError.setText(message);
        tvLoginError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        tvLoginError.setVisibility(View.INVISIBLE);
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
