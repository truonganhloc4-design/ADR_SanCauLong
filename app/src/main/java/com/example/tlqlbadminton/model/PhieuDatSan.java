package com.example.tlqlbadminton.model;

// Model lưu thông tin một phiếu đặt sân hoặc một ca đang chơi.
public class PhieuDatSan {
    private int maPhieu;
    private int maSan;
    private String maCa;
    private String tenKhach;
    private String soDienThoai;
    private double tienCoc;
    private String ngayDat;
    private String khungGioChoi;
    private long gioBatDau;
    private long gioKetThuc;
    private int trangThaiPhieu; // 0: Đặt trước, 1: Đang chơi, 2: Đã thanh toán

    // Constructor rỗng dùng khi tạo phiếu rồi set từng thuộc tính.
    public PhieuDatSan() {
    }

    // Constructor đầy đủ dùng khi đã có sẵn toàn bộ thông tin phiếu.
    public PhieuDatSan(int maPhieu, int maSan, String maCa, String tenKhach, String soDienThoai, double tienCoc, String ngayDat, String khungGioChoi, long gioBatDau, long gioKetThuc, int trangThaiPhieu) {
        this.maPhieu = maPhieu;
        this.maSan = maSan;
        this.maCa = maCa;
        this.tenKhach = tenKhach;
        this.soDienThoai = soDienThoai;
        this.tienCoc = tienCoc;
        this.ngayDat = ngayDat;
        this.khungGioChoi = khungGioChoi;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.trangThaiPhieu = trangThaiPhieu;
    }

    public int getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(int maPhieu) {
        this.maPhieu = maPhieu;
    }

    public int getMaSan() {
        return maSan;
    }

    public void setMaSan(int maSan) {
        this.maSan = maSan;
    }

    public String getMaCa() {
        return maCa;
    }

    public void setMaCa(String maCa) {
        this.maCa = maCa;
    }

    public String getTenKhach() {
        return tenKhach;
    }

    public void setTenKhach(String tenKhach) {
        this.tenKhach = tenKhach;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public double getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(double tienCoc) {
        this.tienCoc = tienCoc;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getKhungGioChoi() {
        return khungGioChoi;
    }

    public void setKhungGioChoi(String khungGioChoi) {
        this.khungGioChoi = khungGioChoi;
    }

    public long getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(long gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public long getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(long gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public int getTrangThaiPhieu() {
        return trangThaiPhieu;
    }

    public void setTrangThaiPhieu(int trangThaiPhieu) {
        this.trangThaiPhieu = trangThaiPhieu;
    }
}
