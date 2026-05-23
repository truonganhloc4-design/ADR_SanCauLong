package com.example.tlqlbadminton.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tlqlbadminton.model.TaiKhoan;

public class TaiKhoanDAO {
    private final SQLiteDatabase db;

    // Mở database để thao tác với bảng TaiKhoan.
    public TaiKhoanDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Kiểm tra username/password có tồn tại trong DB hay không.
    public boolean checkLogin(String username, String password) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_TAI_KHOAN +
                        " WHERE TenDangNhap=? AND MatKhau=?",
                new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    // Kiểm tra tên đăng nhập đã được đăng ký chưa.
    public boolean isUsernameExists(String username) {
        Cursor cursor = db.rawQuery("SELECT TenDangNhap FROM " + DBHelper.TABLE_TAI_KHOAN +
                        " WHERE TenDangNhap=?",
                new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Thêm tài khoản mới vào bảng TaiKhoan.
    public long insertTaiKhoan(TaiKhoan taiKhoan) {
        ContentValues values = new ContentValues();
        values.put("TenDangNhap", taiKhoan.getTenDangNhap());
        values.put("MatKhau", taiKhoan.getMatKhau());
        values.put("TenHienThi", taiKhoan.getTenHienThi());
        return db.insert(DBHelper.TABLE_TAI_KHOAN, null, values);
    }
}
