package com.example.tlqlbadminton.model;

public class San {
    private int maSan;
    private String tenSan;
    private double giaMoiGio;
    private String loaiSan;
    private int trangThai; // 0: Trống, 1: Đang chơi
    private int tinhTrangHoatDong; // 1: Sẵn sàng, 0: Bảo trì

    public San() {
    }

    public San(int maSan, String tenSan, double giaMoiGio, String loaiSan, int trangThai, int tinhTrangHoatDong) {
        this.maSan = maSan;
        this.tenSan = tenSan;
        this.giaMoiGio = giaMoiGio;
        this.loaiSan = loaiSan;
        this.trangThai = trangThai;
        this.tinhTrangHoatDong = tinhTrangHoatDong;
    }

    public int getMaSan() {
        return maSan;
    }

    public void setMaSan(int maSan) {
        this.maSan = maSan;
    }

    public String getTenSan() {
        return tenSan;
    }

    public void setTenSan(String tenSan) {
        this.tenSan = tenSan;
    }

    public double getGiaMoiGio() {
        return giaMoiGio;
    }

    public void setGiaMoiGio(double giaMoiGio) {
        this.giaMoiGio = giaMoiGio;
    }

    public String getLoaiSan() {
        return loaiSan;
    }

    public void setLoaiSan(String loaiSan) {
        this.loaiSan = loaiSan;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public int getTinhTrangHoatDong() {
        return tinhTrangHoatDong;
    }

    public void setTinhTrangHoatDong(int tinhTrangHoatDong) {
        this.tinhTrangHoatDong = tinhTrangHoatDong;
    }
}
