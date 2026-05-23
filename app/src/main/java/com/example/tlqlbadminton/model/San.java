package com.example.tlqlbadminton.model;

// Model lưu thông tin một sân trong bảng San.
public class San {
    private int maSan;
    private String tenSan;
    private double giaMoiGio;
    private String loaiSan;
    private int trangThai; // 0: Trống, 1: Đang chơi
    private int tinhTrangHoatDong; // 1: Sẵn sàng, 0: Bảo trì

    // Constructor rỗng dùng khi tạo object rồi set từng thuộc tính.
    public San() {
    }

    // Constructor đầy đủ dùng khi đã có sẵn toàn bộ thông tin sân.
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
