package com.example.tlqlbadminton.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tlqlbadminton.model.HoaDon;
import com.example.tlqlbadminton.model.PhieuDatSan;

import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {
    private final SQLiteDatabase db;

    public HoaDonDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean thanhToanSan(int maPhieu, HoaDon hoaDon, long gioKetThuc) {
        PhieuDatSan phieu = getPhieuById(maPhieu);
        if (phieu == null) return false;

        db.beginTransaction();
        boolean ok;
        try {
            ContentValues phieuValues = new ContentValues();
            phieuValues.put("GioKetThuc", gioKetThuc);
            phieuValues.put("TrangThaiPhieu", DBHelper.PHIEU_DA_THANH_TOAN);
            db.update(DBHelper.TABLE_PHIEU_DAT_SAN, phieuValues, "MaPhieu=?",
                    new String[]{String.valueOf(maPhieu)});

            ContentValues hdValues = new ContentValues();
            hdValues.put("MaHD", hoaDon.getMaHD());
            hdValues.put("MaPhieu", hoaDon.getMaPhieu());
            hdValues.put("TongTienSan", hoaDon.getTongTienSan());
            hdValues.put("TongThanhToan", hoaDon.getTongThanhToan());
            hdValues.put("NgayLap", hoaDon.getNgayLap());
            hdValues.put("GioLapTimestamp", hoaDon.getGioLapTimestamp());
            ok = db.insert(DBHelper.TABLE_HOA_DON, null, hdValues) != -1;

            ContentValues sanValues = new ContentValues();
            sanValues.put("TrangThai", DBHelper.SAN_TRONG);
            db.update(DBHelper.TABLE_SAN, sanValues, "MaSan=?", new String[]{String.valueOf(phieu.getMaSan())});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return ok;
    }

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_HOA_DON +
                " ORDER BY GioLapTimestamp DESC", null);
        while (cursor.moveToNext()) {
            list.add(mapHoaDon(cursor));
        }
        cursor.close();
        return list;
    }

    public double getTongDoanhThu() {
        Cursor cursor = db.rawQuery("SELECT IFNULL(SUM(TongThanhToan),0) FROM " + DBHelper.TABLE_HOA_DON, null);
        double total = cursor.moveToNext() ? cursor.getDouble(0) : 0;
        cursor.close();
        return total;
    }

    public int countHoaDon() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DBHelper.TABLE_HOA_DON, null);
        int count = cursor.moveToNext() ? cursor.getInt(0) : 0;
        cursor.close();
        return count;
    }

    private PhieuDatSan getPhieuById(int maPhieu) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_PHIEU_DAT_SAN + " WHERE MaPhieu=?",
                new String[]{String.valueOf(maPhieu)});
        PhieuDatSan phieu = null;
        if (cursor.moveToNext()) {
            phieu = new PhieuDatSan();
            phieu.setMaPhieu(cursor.getInt(0));
            phieu.setMaSan(cursor.getInt(1));
            phieu.setTrangThaiPhieu(cursor.getInt(10));
        }
        cursor.close();
        return phieu;
    }

    private HoaDon mapHoaDon(Cursor cursor) {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD(cursor.getString(0));
        hoaDon.setMaPhieu(cursor.getInt(1));
        hoaDon.setTongTienSan(cursor.getDouble(2));
        hoaDon.setTongThanhToan(cursor.getDouble(3));
        hoaDon.setNgayLap(cursor.getString(4));
        hoaDon.setGioLapTimestamp(cursor.getLong(5));
        return hoaDon;
    }
}
