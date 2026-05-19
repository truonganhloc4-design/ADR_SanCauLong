package com.example.tlqlbadminton.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
}
