package com.example.tlqlbadminton.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tlqlbadminton.model.San;

import java.util.ArrayList;
import java.util.List;

public class SanDAO {
    private final SQLiteDatabase db;

    public SanDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public List<San> getAllSan() {
        List<San> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_SAN + " ORDER BY MaSan ASC", null);
        while (cursor.moveToNext()) {
            list.add(mapSan(cursor));
        }
        cursor.close();
        return list;
    }

    public List<San> getAllSanNewestFirst() {
        List<San> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_SAN + " ORDER BY MaSan DESC", null);
        while (cursor.moveToNext()) {
            list.add(mapSan(cursor));
        }
        cursor.close();
        return list;
    }

    public San getSanById(int maSan) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_SAN + " WHERE MaSan=?",
                new String[]{String.valueOf(maSan)});
        San san = cursor.moveToNext() ? mapSan(cursor) : null;
        cursor.close();
        return san;
    }

    public boolean insertSan(San san) {
        return db.insert(DBHelper.TABLE_SAN, null, buildValues(san)) != -1;
    }

    public boolean updateSan(San san) {
        return db.update(DBHelper.TABLE_SAN, buildValues(san), "MaSan=?",
                new String[]{String.valueOf(san.getMaSan())}) > 0;
    }

    public int deleteSan(int maSan) {
        return db.delete(DBHelper.TABLE_SAN, "MaSan=?", new String[]{String.valueOf(maSan)});
    }

    public void updateTrangThaiSan(int maSan, int trangThai) {
        ContentValues values = new ContentValues();
        values.put("TrangThai", trangThai);
        db.update(DBHelper.TABLE_SAN, values, "MaSan=?", new String[]{String.valueOf(maSan)});
    }

    public int countAllSan() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DBHelper.TABLE_SAN, null);
        int count = cursor.moveToNext() ? cursor.getInt(0) : 0;
        cursor.close();
        return count;
    }

    public int countSanByTrangThai(int trangThai) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DBHelper.TABLE_SAN + " WHERE TrangThai=?",
                new String[]{String.valueOf(trangThai)});
        int count = cursor.moveToNext() ? cursor.getInt(0) : 0;
        cursor.close();
        return count;
    }

    private ContentValues buildValues(San san) {
        ContentValues values = new ContentValues();
        values.put("TenSan", san.getTenSan());
        values.put("GiaMoiGio", san.getGiaMoiGio());
        values.put("LoaiSan", san.getLoaiSan());
        values.put("TrangThai", san.getTrangThai());
        values.put("TinhTrangHoatDong", san.getTinhTrangHoatDong());
        return values;
    }

    private San mapSan(Cursor cursor) {
        San san = new San();
        san.setMaSan(cursor.getInt(0));
        san.setTenSan(cursor.getString(1));
        san.setGiaMoiGio(cursor.getDouble(2));
        san.setLoaiSan(cursor.getString(3));
        san.setTrangThai(cursor.getInt(4));
        san.setTinhTrangHoatDong(cursor.getInt(5));
        return san;
    }
}
