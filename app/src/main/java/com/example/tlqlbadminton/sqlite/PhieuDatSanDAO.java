package com.example.tlqlbadminton.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tlqlbadminton.model.PhieuDatSan;

import java.util.ArrayList;
import java.util.List;

public class PhieuDatSanDAO {
    private final SQLiteDatabase db;

    // Mở database để thao tác với bảng PhieuDatSan.
    public PhieuDatSanDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Lưu phiếu đặt trước, chưa làm sân chuyển sang đang chơi.
    public long insertPhieuDatTruoc(PhieuDatSan phieu) {
        ContentValues values = buildValues(phieu);
        values.put("TrangThaiPhieu", DBHelper.PHIEU_DAT_TRUOC);
        return db.insert(DBHelper.TABLE_PHIEU_DAT_SAN, null, values);
    }

    // Tạo ca chơi cho khách vãng lai và cập nhật sân sang đang chơi.
    public long insertNhanSanMoi(int maSan, String tenKhach, String sdt, double tienCoc, long gioBatDau) {
        db.beginTransaction();
        long maPhieu = -1;
        try {
            // Transaction giúp phiếu và trạng thái sân được lưu cùng nhau.
            ContentValues values = new ContentValues();
            values.put("MaSan", maSan);
            values.put("MaCa", makeBookingCode(gioBatDau));
            values.put("TenKhach", tenKhach);
            values.put("SoDienThoai", sdt);
            values.put("TienCoc", tienCoc);
            values.put("GioBatDau", gioBatDau);
            values.put("TrangThaiPhieu", DBHelper.PHIEU_DANG_CHOI);
            maPhieu = db.insert(DBHelper.TABLE_PHIEU_DAT_SAN, null, values);
            if (maPhieu != -1) {
                updateTrangThaiSan(maSan, DBHelper.SAN_DANG_CHOI);
                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }
        return maPhieu;
    }

    // Chuyển phiếu đặt trước thành phiếu đang chơi.
    public boolean nhanSanTuPhieuDat(int maPhieu, long gioBatDau) {
        PhieuDatSan phieu = getPhieuById(maPhieu);
        if (phieu == null) return false;

        db.beginTransaction();
        boolean ok;
        try {
            ContentValues values = new ContentValues();
            values.put("MaCa", makeBookingCode(gioBatDau));
            values.put("GioBatDau", gioBatDau);
            values.put("TrangThaiPhieu", DBHelper.PHIEU_DANG_CHOI);
            ok = db.update(DBHelper.TABLE_PHIEU_DAT_SAN, values, "MaPhieu=?",
                    new String[]{String.valueOf(maPhieu)}) > 0;
            updateTrangThaiSan(phieu.getMaSan(), DBHelper.SAN_DANG_CHOI);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return ok;
    }

    // Tìm phiếu theo MaPhieu.
    public PhieuDatSan getPhieuById(int maPhieu) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_PHIEU_DAT_SAN + " WHERE MaPhieu=?",
                new String[]{String.valueOf(maPhieu)});
        PhieuDatSan phieu = cursor.moveToNext() ? mapPhieu(cursor) : null;
        cursor.close();
        return phieu;
    }

    // Lấy phiếu đang chơi mới nhất của một sân.
    public PhieuDatSan getActivePhieuBySan(int maSan) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_PHIEU_DAT_SAN +
                        " WHERE MaSan=? AND TrangThaiPhieu=? ORDER BY GioBatDau DESC LIMIT 1",
                new String[]{String.valueOf(maSan), String.valueOf(DBHelper.PHIEU_DANG_CHOI)});
        PhieuDatSan phieu = cursor.moveToNext() ? mapPhieu(cursor) : null;
        cursor.close();
        return phieu;
    }

    // Lấy tất cả phiếu đặt trước đang chờ nhận sân.
    public List<PhieuDatSan> getPendingBookings() {
        return getPendingBookingsBySan(-1);
    }

    // Lấy phiếu đặt trước, có thể lọc theo MaSan nếu maSan > 0.
    public List<PhieuDatSan> getPendingBookingsBySan(int maSan) {
        List<PhieuDatSan> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DBHelper.TABLE_PHIEU_DAT_SAN + " WHERE TrangThaiPhieu=?";
        List<String> args = new ArrayList<>();
        args.add(String.valueOf(DBHelper.PHIEU_DAT_TRUOC));
        if (maSan > 0) {
            sql += " AND MaSan=?";
            args.add(String.valueOf(maSan));
        }
        sql += " ORDER BY MaPhieu DESC";
        Cursor cursor = db.rawQuery(sql, args.toArray(new String[0]));
        while (cursor.moveToNext()) {
            list.add(mapPhieu(cursor));
        }
        cursor.close();
        return list;
    }

    // Cập nhật trạng thái sân từ trong DAO phiếu đặt.
    private void updateTrangThaiSan(int maSan, int trangThai) {
        ContentValues values = new ContentValues();
        values.put("TrangThai", trangThai);
        db.update(DBHelper.TABLE_SAN, values, "MaSan=?", new String[]{String.valueOf(maSan)});
    }

    // Chuyển object PhieuDatSan thành ContentValues để lưu DB.
    private ContentValues buildValues(PhieuDatSan phieu) {
        ContentValues values = new ContentValues();
        values.put("MaSan", phieu.getMaSan());
        values.put("MaCa", phieu.getMaCa());
        values.put("TenKhach", phieu.getTenKhach());
        values.put("SoDienThoai", phieu.getSoDienThoai());
        values.put("TienCoc", phieu.getTienCoc());
        values.put("NgayDat", phieu.getNgayDat());
        values.put("KhungGioChoi", phieu.getKhungGioChoi());
        values.put("GioBatDau", phieu.getGioBatDau());
        values.put("GioKetThuc", phieu.getGioKetThuc());
        values.put("TrangThaiPhieu", phieu.getTrangThaiPhieu());
        return values;
    }

    // Chuyển một dòng Cursor thành object PhieuDatSan.
    private PhieuDatSan mapPhieu(Cursor cursor) {
        PhieuDatSan phieu = new PhieuDatSan();
        phieu.setMaPhieu(cursor.getInt(0));
        phieu.setMaSan(cursor.getInt(1));
        phieu.setMaCa(cursor.getString(2));
        phieu.setTenKhach(cursor.getString(3));
        phieu.setSoDienThoai(cursor.getString(4));
        phieu.setTienCoc(cursor.getDouble(5));
        phieu.setNgayDat(cursor.getString(6));
        phieu.setKhungGioChoi(cursor.getString(7));
        phieu.setGioBatDau(cursor.getLong(8));
        phieu.setGioKetThuc(cursor.getLong(9));
        phieu.setTrangThaiPhieu(cursor.getInt(10));
        return phieu;
    }

    // Tạo mã ca chơi đơn giản dựa trên timestamp.
    private String makeBookingCode(long timestamp) {
        return "#BK-" + String.valueOf(timestamp).substring(5);
    }
}
