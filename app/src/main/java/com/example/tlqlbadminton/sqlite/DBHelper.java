package com.example.tlqlbadminton.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "QLSanCauLong.sqlite";
    public static final int DB_VERSION = 1;

    public static final int SAN_TRONG = 0;
    public static final int SAN_DANG_CHOI = 1;
    public static final int PHIEU_DAT_TRUOC = 0;
    public static final int PHIEU_DANG_CHOI = 1;
    public static final int PHIEU_DA_THANH_TOAN = 2;

    public static final String TABLE_TAI_KHOAN = "TaiKhoan";
    public static final String TABLE_SAN = "San";
    public static final String TABLE_PHIEU_DAT_SAN = "PhieuDatSan";
    public static final String TABLE_HOA_DON = "HoaDon";

    // Khởi tạo helper để Android biết tên DB và version hiện tại.
    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Bật khóa ngoại để ràng buộc giữa sân, phiếu đặt và hóa đơn có hiệu lực.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Chạy lần đầu khi DB chưa tồn tại: tạo bảng và thêm dữ liệu mẫu.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlTaiKhoan = "CREATE TABLE IF NOT EXISTS " + TABLE_TAI_KHOAN + "(" +
                "TenDangNhap TEXT PRIMARY KEY, " +
                "MatKhau TEXT NOT NULL, " +
                "TenHienThi TEXT)";
        db.execSQL(sqlTaiKhoan);

        String sqlSan = "CREATE TABLE IF NOT EXISTS " + TABLE_SAN + "(" +
                "MaSan INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TenSan TEXT NOT NULL, " +
                "GiaMoiGio REAL NOT NULL, " +
                "LoaiSan TEXT DEFAULT 'Cỏ nhân tạo', " +
                "TrangThai INTEGER DEFAULT 0, " +
                "TinhTrangHoatDong INTEGER DEFAULT 1)";
        db.execSQL(sqlSan);

        String sqlPhieuDatSan = "CREATE TABLE IF NOT EXISTS " + TABLE_PHIEU_DAT_SAN + "(" +
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
        db.execSQL(sqlPhieuDatSan);

        String sqlHoaDon = "CREATE TABLE IF NOT EXISTS " + TABLE_HOA_DON + "(" +
                "MaHD TEXT PRIMARY KEY, " +
                "MaPhieu INTEGER NOT NULL, " +
                "TongTienSan REAL NOT NULL, " +
                "TongThanhToan REAL NOT NULL, " +
                "NgayLap TEXT NOT NULL, " +
                "GioLapTimestamp INTEGER NOT NULL, " +
                "FOREIGN KEY(MaPhieu) REFERENCES " + TABLE_PHIEU_DAT_SAN + "(MaPhieu))";
        db.execSQL(sqlHoaDon);

        insertDuLieuMau(db);
    }

    // Khi tăng DB_VERSION, xóa bảng cũ và tạo lại bảng mới.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOA_DON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHIEU_DAT_SAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAI_KHOAN);
        onCreate(db);
    }

    // Thêm tài khoản admin và 4 sân mẫu để app có dữ liệu khi cài lần đầu.
    private void insertDuLieuMau(SQLiteDatabase db) {
        ContentValues taiKhoan = new ContentValues();
        taiKhoan.put("TenDangNhap", "admin");
        taiKhoan.put("MatKhau", "123");
        taiKhoan.put("TenHienThi", "Administrator");
        db.insert(TABLE_TAI_KHOAN, null, taiKhoan);

        for (int i = 1; i <= 4; i++) {
            ContentValues san = new ContentValues();
            san.put("TenSan", "Sân 0" + i);
            san.put("GiaMoiGio", 70000.0);
            san.put("LoaiSan", "Cỏ nhân tạo");
            san.put("TrangThai", SAN_TRONG);
            san.put("TinhTrangHoatDong", 1);
            db.insert(TABLE_SAN, null, san);
        }
    }
}
