package com.example.tlqlbadminton.model;

public class TaiKhoan {
    private String tenDangNhap;
    private String matKhau;
    private String tenHienThi;

    public TaiKhoan() {
    }

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
