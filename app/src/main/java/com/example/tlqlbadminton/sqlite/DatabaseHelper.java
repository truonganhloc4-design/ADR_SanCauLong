package com.example.tlqlbadminton.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tlqlbadminton.model.HoaDon;
import com.example.tlqlbadminton.model.PhieuDatSan;
import com.example.tlqlbadminton.model.San;
import com.example.tlqlbadminton.model.TaiKhoan;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QLSanCauLong.db";
    private static final int DATABASE_VERSION = 1;

    // Tên các bảng
    public static final String TABLE_TAI_KHOAN = "TaiKhoan";
    public static final String TABLE_SAN = "San";
    public static final String TABLE_PHIEU_DAT_SAN = "PhieuDatSan";
    public static final String TABLE_HOA_DON = "HoaDon";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Bật kiểm tra khóa ngoại (PRAGMA foreign_keys = ON)
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Tạo bảng Tài Khoản
        String createTableTaiKhoan = "CREATE TABLE " + TABLE_TAI_KHOAN + " (" +
                "TenDangNhap TEXT PRIMARY KEY, " +
                "MatKhau TEXT NOT NULL, " +
                "TenHienThi TEXT)";
        db.execSQL(createTableTaiKhoan);

        // 2. Tạo bảng Sân
        String createTableSan = "CREATE TABLE " + TABLE_SAN + " (" +
                "MaSan INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TenSan TEXT NOT NULL, " +
                "GiaMoiGio REAL NOT NULL, " +
                "LoaiSan TEXT DEFAULT 'Cỏ nhân tạo', " +
                "TrangThai INTEGER DEFAULT 0, " +
                "TinhTrangHoatDong INTEGER DEFAULT 1)";
        db.execSQL(createTableSan);

        // 3. Tạo bảng Phiếu Đặt Sân
        String createTablePhieuDatSan = "CREATE TABLE " + TABLE_PHIEU_DAT_SAN + " (" +
                "MaPhieu INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MaSan INTEGER NOT NULL, " +
                "MaCa TEXT, " +
                "TenKhach TEXT NOT NULL, " +
                "SoDienThoai TEXT, " +
                "TienCoc REAL DEFAULT 0, " +
                "NgayDat TEXT, " +
                "KhungGioChoi TEXT, " +
                "GioBatDau INTEGER, " +
                "GioKetThuc INTEGER, " +
                "TrangThaiPhieu INTEGER DEFAULT 0, " +
                "FOREIGN KEY(MaSan) REFERENCES " + TABLE_SAN + "(MaSan))";
        db.execSQL(createTablePhieuDatSan);

        // 4. Tạo bảng Hóa Đơn
        String createTableHoaDon = "CREATE TABLE " + TABLE_HOA_DON + " (" +
                "MaHD TEXT PRIMARY KEY, " +
                "MaPhieu INTEGER NOT NULL, " +
                "TongTienSan REAL NOT NULL, " +
                "TongThanhToan REAL NOT NULL, " +
                "NgayLap TEXT NOT NULL, " +
                "GioLapTimestamp INTEGER NOT NULL, " +
                "FOREIGN KEY(MaPhieu) REFERENCES " + TABLE_PHIEU_DAT_SAN + "(MaPhieu))";
        db.execSQL(createTableHoaDon);

        // Mồi dữ liệu mẫu (Seeding)
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOA_DON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHIEU_DAT_SAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAI_KHOAN);
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Tài khoản admin mặc định
        ContentValues cvTaiKhoan = new ContentValues();
        cvTaiKhoan.put("TenDangNhap", "admin");
        cvTaiKhoan.put("MatKhau", "123");
        cvTaiKhoan.put("TenHienThi", "Administrator");
        db.insert(TABLE_TAI_KHOAN, null, cvTaiKhoan);

        // 4 sân mẫu
        for (int i = 1; i <= 4; i++) {
            ContentValues cvSan = new ContentValues();
            cvSan.put("TenSan", "Sân 0" + i);
            cvSan.put("GiaMoiGio", 70000.0);
            cvSan.put("LoaiSan", "Cỏ nhân tạo");
            cvSan.put("TrangThai", 0); // 0: Trống
            cvSan.put("TinhTrangHoatDong", 1); // 1: Sẵn sàng
            db.insert(TABLE_SAN, null, cvSan);
        }
    }

    // ==========================================
    // CRUD SÂN (Court)
    // ==========================================
    public List<San> getAllSan() {
        List<San> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SAN, null);
        if (cursor.moveToFirst()) {
            do {
                San san = new San();
                san.setMaSan(cursor.getInt(0));
                san.setTenSan(cursor.getString(1));
                san.setGiaMoiGio(cursor.getDouble(2));
                san.setLoaiSan(cursor.getString(3));
                san.setTrangThai(cursor.getInt(4));
                san.setTinhTrangHoatDong(cursor.getInt(5));
                list.add(san);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean insertSan(San san) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TenSan", san.getTenSan());
        cv.put("GiaMoiGio", san.getGiaMoiGio());
        cv.put("LoaiSan", san.getLoaiSan());
        cv.put("TrangThai", san.getTrangThai());
        cv.put("TinhTrangHoatDong", san.getTinhTrangHoatDong());
        long result = db.insert(TABLE_SAN, null, cv);
        db.close();
        return result != -1;
    }

    public boolean updateSan(San san) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TenSan", san.getTenSan());
        cv.put("GiaMoiGio", san.getGiaMoiGio());
        cv.put("LoaiSan", san.getLoaiSan());
        cv.put("TrangThai", san.getTrangThai());
        cv.put("TinhTrangHoatDong", san.getTinhTrangHoatDong());
        int result = db.update(TABLE_SAN, cv, "MaSan=?", new String[]{String.valueOf(san.getMaSan())});
        db.close();
        return result > 0;
    }

    public boolean deleteSan(int maSan) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_SAN, "MaSan=?", new String[]{String.valueOf(maSan)});
        db.close();
        return result > 0;
    }

    // ==========================================
    // TÀI KHOẢN
    // ==========================================
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TAI_KHOAN + " WHERE TenDangNhap=? AND MatKhau=?", new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }
}