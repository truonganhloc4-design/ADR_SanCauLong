package com.example.tlqlbadminton.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tlqlbadminton.model.TaiKhoan;

public class TaiKhoanDAO {
    private final SQLiteDatabase db;

    public TaiKhoanDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean checkLogin(String username, String password) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_TAI_KHOAN +
                        " WHERE TenDangNhap=? AND MatKhau=?",
                new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    public boolean isUsernameExists(String username) {
        Cursor cursor = db.rawQuery("SELECT TenDangNhap FROM " + DBHelper.TABLE_TAI_KHOAN +
                        " WHERE TenDangNhap=?",
                new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public long insertTaiKhoan(TaiKhoan taiKhoan) {
        ContentValues values = new ContentValues();
        values.put("TenDangNhap", taiKhoan.getTenDangNhap());
        values.put("MatKhau", taiKhoan.getMatKhau());
        values.put("TenHienThi", taiKhoan.getTenHienThi());
        return db.insert(DBHelper.TABLE_TAI_KHOAN, null, values);
    }
}
