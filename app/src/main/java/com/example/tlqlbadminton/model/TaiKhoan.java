package com.example.tlqlbadminton.model;

// Model lưu thông tin tài khoản đăng nhập.
public class TaiKhoan {
    private String tenDangNhap;
    private String matKhau;
    private String tenHienThi;

    // Constructor rỗng dùng khi tạo object rồi set từng thuộc tính.
    public TaiKhoan() {
    }

    // Constructor đầy đủ dùng khi đăng ký hoặc tạo tài khoản mẫu.
    public TaiKhoan(String tenDangNhap, String matKhau, String tenHienThi) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.tenHienThi = tenHienThi;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }

    public void setTenHienThi(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }
}
