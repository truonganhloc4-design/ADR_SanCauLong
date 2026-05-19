package com.example.tlqlbadminton.model;

import java.util.ArrayList;
import java.util.List;

/**
 * AppState — Singleton lưu trạng thái toàn cục trong bộ nhớ (in-process).
 * Cho phép các Fragment và Activity chia sẻ dữ liệu mà không cần EventBus.
 */
public class AppState {

    // Trạng thái sân: 0=Trống, 1=Đang chơi, 2=Đã đặt trước
    public static final int STATUS_AVAILABLE = 0;
    public static final int STATUS_OCCUPIED   = 1;
    public static final int STATUS_BOOKED     = 2;

    private static AppState instance;

    public static AppState getInstance() {
        if (instance == null) instance = new AppState();
        return instance;
    }

    // Trạng thái 4 sân (index 0 = Sân 1, …)
    private final int[] courtStatus = {STATUS_AVAILABLE, STATUS_OCCUPIED, STATUS_BOOKED, STATUS_AVAILABLE};
    private final String[] courtInfo = {"Cỏ nhân tạo", "14:00 • 45 phút", "16:00 • Nguyễn A", "Gỗ"};
    private final int[] courtPhieuId = {-1, 1, 2, -1}; // MaPhieu đang mở (-1 = không có)

    // Danh sách hóa đơn đã thanh toán (hiển thị ở Tab Lịch sử)
    public static class InvoiceEntry {
        public String maHD;
        public String tenSan;
        public String thoiGian;
        public long tongTien;
        public String ngay;

        public InvoiceEntry(String maHD, String tenSan, String thoiGian, long tongTien, String ngay) {
            this.maHD = maHD;
            this.tenSan = tenSan;
            this.thoiGian = thoiGian;
            this.tongTien = tongTien;
            this.ngay = ngay;
        }
    }

    // Danh sách lịch đặt (chưa nhận sân)
    public static class BookingEntry {
        public int maPhieu;
        public String tenSan;
        public String tenKhach;
        public String sdt;
        public String ngayDat;
        public String khungGio;
        public double tienCoc;

        public BookingEntry(int maPhieu, String tenSan, String tenKhach, String sdt,
                            String ngayDat, String khungGio, double tienCoc) {
            this.maPhieu = maPhieu;
            this.tenSan = tenSan;
            this.tenKhach = tenKhach;
            this.sdt = sdt;
            this.ngayDat = ngayDat;
            this.khungGio = khungGio;
            this.tienCoc = tienCoc;
        }
    }

    private final List<InvoiceEntry> invoiceList = new ArrayList<>();
    private final List<BookingEntry> bookingList = new ArrayList<>();
    private long tongDoanhThu = 0;
    private int soLuotDat = 0;

    // Observer interface để Fragment lắng nghe thay đổi
    public interface OnStateChangedListener {
        void onCourtStatusChanged(int courtIndex, int newStatus, String info);
        void onInvoiceAdded(InvoiceEntry entry, long tongDoanhThu);
        void onBookingAdded(BookingEntry entry);
    }

    private final List<OnStateChangedListener> listeners = new ArrayList<>();

    public void addListener(OnStateChangedListener l) {
        if (!listeners.contains(l)) listeners.add(l);
    }

    public void removeListener(OnStateChangedListener l) {
        listeners.remove(l);
    }

    // ── Getters ──────────────────────────────────────────
    public int getCourtStatus(int index)  { return courtStatus[index]; }
    public String getCourtInfo(int index) { return courtInfo[index]; }
    public int getCourtPhieuId(int index) { return courtPhieuId[index]; }
    public List<InvoiceEntry> getInvoiceList() { return invoiceList; }
    public List<BookingEntry> getBookingList()  { return bookingList; }
    public long getTongDoanhThu() { return tongDoanhThu; }
    public int getSoLuotDat()     { return soLuotDat; }

    public int countAvailable() {
        int c = 0; for (int s : courtStatus) if (s == STATUS_AVAILABLE) c++; return c;
    }
    public int countOccupied() {
        int c = 0; for (int s : courtStatus) if (s == STATUS_OCCUPIED) c++; return c;
    }
    public int countBooked() {
        int c = 0; for (int s : courtStatus) if (s == STATUS_BOOKED) c++; return c;
    }

    // ── Luồng 1: Nhận sân ───────────────────────────────
    public void nhanSan(int courtIndex, int maPhieu, String gioBatDau) {
        courtStatus[courtIndex] = STATUS_OCCUPIED;
        courtInfo[courtIndex] = gioBatDau + " • Đang chơi";
        courtPhieuId[courtIndex] = maPhieu;
        soLuotDat++;
        for (OnStateChangedListener l : listeners)
            l.onCourtStatusChanged(courtIndex, STATUS_OCCUPIED, courtInfo[courtIndex]);
    }

    // ── Luồng 2: Đặt lịch trước ─────────────────────────
    public void datLichTruoc(int courtIndex, BookingEntry entry) {
        courtStatus[courtIndex] = STATUS_BOOKED;
        courtInfo[courtIndex] = entry.khungGio + " • " + entry.tenKhach;
        courtPhieuId[courtIndex] = entry.maPhieu;
        bookingList.add(0, entry);
        for (OnStateChangedListener l : listeners) {
            l.onCourtStatusChanged(courtIndex, STATUS_BOOKED, courtInfo[courtIndex]);
            l.onBookingAdded(entry);
        }
    }

    // ── Luồng 3: Thanh toán ──────────────────────────────
    public void thanhToan(int courtIndex, InvoiceEntry entry) {
        courtStatus[courtIndex] = STATUS_AVAILABLE;
        courtInfo[courtIndex] = "Cỏ nhân tạo";
        courtPhieuId[courtIndex] = -1;
        invoiceList.add(0, entry);
        tongDoanhThu += entry.tongTien;
        for (OnStateChangedListener l : listeners) {
            l.onCourtStatusChanged(courtIndex, STATUS_AVAILABLE, courtInfo[courtIndex]);
            l.onInvoiceAdded(entry, tongDoanhThu);
        }
    }
}
