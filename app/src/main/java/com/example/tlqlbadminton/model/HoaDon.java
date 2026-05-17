package com.example.tlqlbadminton.model;

public class HoaDon {
    private String maHD;
    private int maPhieu;
    private double tongTienSan;
    private double tongThanhToan;
    private String ngayLap;
    private long gioLapTimestamp;

    public HoaDon() {
    }

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
