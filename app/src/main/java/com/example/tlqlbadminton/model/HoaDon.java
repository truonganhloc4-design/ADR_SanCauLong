package com.example.tlqlbadminton.model;

// Model lưu thông tin hóa đơn sau khi thanh toán sân.
public class HoaDon {
    private String maHD;
    private int maPhieu;
    private double tongTienSan;
    private double tongThanhToan;
    private String ngayLap;
    private long gioLapTimestamp;

    // Constructor rỗng dùng khi tạo object rồi set từng thuộc tính.
    public HoaDon() {
    }

    // Constructor đầy đủ dùng khi tạo hóa đơn mới.
    public HoaDon(String maHD, int maPhieu, double tongTienSan, double tongThanhToan, String ngayLap, long gioLapTimestamp) {
        this.maHD = maHD;
        this.maPhieu = maPhieu;
        this.tongTienSan = tongTienSan;
        this.tongThanhToan = tongThanhToan;
        this.ngayLap = ngayLap;
        this.gioLapTimestamp = gioLapTimestamp;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public int getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(int maPhieu) {
        this.maPhieu = maPhieu;
    }

    public double getTongTienSan() {
        return tongTienSan;
    }

    public void setTongTienSan(double tongTienSan) {
        this.tongTienSan = tongTienSan;
    }

    public double getTongThanhToan() {
        return tongThanhToan;
    }

    public void setTongThanhToan(double tongThanhToan) {
        this.tongThanhToan = tongThanhToan;
    }

    public String getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(String ngayLap) {
        this.ngayLap = ngayLap;
    }

    public long getGioLapTimestamp() {
        return gioLapTimestamp;
    }

    public void setGioLapTimestamp(long gioLapTimestamp) {
        this.gioLapTimestamp = gioLapTimestamp;
    }
}
