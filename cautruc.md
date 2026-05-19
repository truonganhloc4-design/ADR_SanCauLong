# Kien truc du an: Quan ly san cau long

Package chinh: `com.example.tlqlbadminton`

Nen tang: Android Native  
Ngon ngu: Java + XML Layout  
Co so du lieu: SQLite offline qua `SQLiteOpenHelper`

## 1. Nguyen tac to chuc

- Tach UI XML va logic Java.
- Chia package theo tang: `activity`, `adapter`, `model`, `sqlite`.
- Du lieu nghiep vu phai di qua SQLite, khong luu bang bien RAM cho cac luong chinh.
- Pham vi MVP: dang nhap, so do san, nhan san, dat lich truoc, thanh toan, thong ke, cau hinh san.

## 2. Cau truc thu muc hien tai

```text
app/src/main/java/com/example/tlqlbadminton/
|
|-- sqlite/
|   |-- DBHelper.java
|   |-- TaiKhoanDAO.java
|   |-- SanDAO.java
|   |-- PhieuDatSanDAO.java
|   `-- HoaDonDAO.java
|
|-- model/
|   |-- TaiKhoan.java
|   |-- San.java
|   |-- PhieuDatSan.java
|   `-- HoaDon.java
|
|-- adapter/
|   |-- SanAdapter.java
|   `-- SimpleTextAdapter.java
|
`-- activity/
    |-- DangNhapActivity.java
    |-- MainActivity.java
    |-- SoDoFragment.java
    |-- ThongKeFragment.java
    |-- LichSuFragment.java
    |-- NhanSanActivity.java
    |-- DatLichTruocActivity.java
    |-- ThanhToanActivity.java
    |-- ThemSanActivity.java
    |-- CauHinhSanActivity.java
    `-- ThongKeActivity.java
```

```text
app/src/main/res/layout/
|
|-- activity_dang_nhap.xml
|-- activity_main.xml
|-- fragment_so_do.xml
|-- fragment_thong_ke.xml
|-- fragment_lich_su.xml
|-- activity_nhan_san.xml
|-- activity_dat_lich_truoc.xml
|-- activity_thanh_toan.xml
|-- activity_them_san.xml
|-- activity_cau_hinh_san.xml
|-- layout_item_san.xml
`-- activity_thong_ke.xml
```

## 3. Module chuc nang

### Dang nhap

- `DangNhapActivity` kiem tra tai khoan trong bang `TaiKhoan` qua `TaiKhoanDAO`.
- Tai khoan mau: `admin / 123`.

### So do san

- `SoDoFragment` doc danh sach san tu bang `San` qua `SanDAO`.
- San trong hien nut `Dat lich` va `Nhan san`.
- San dang choi hien nut `Chi tiet` de sang thanh toan.

### Dat lich truoc

- `DatLichTruocActivity` tao dong `PhieuDatSan` voi `TrangThaiPhieu = 0` qua `PhieuDatSanDAO`.
- Dat lich truoc khong doi `TrangThai` cua bang `San`, dung voi yeu cau so do hien hanh khong bi anh huong.

### Nhan san

- `NhanSanActivity` co the nhan khach vang lai hoac chon phieu dat truoc.
- Khi bat dau choi, phieu co `TrangThaiPhieu = 1` va san co `TrangThai = 1`.

### Thanh toan

- `ThanhToanActivity` lay phieu dang choi theo `MaSan` qua `PhieuDatSanDAO`.
- Tinh thoi gian theo `GioBatDau` den thoi diem thanh toan.
- Luu `HoaDon`, cap nhat phieu sang `TrangThaiPhieu = 2`, tra san ve `TrangThai = 0`.

### Thong ke va lich su

- `ThongKeFragment` va `ThongKeActivity` doc doanh thu tu bang `HoaDon` qua `HoaDonDAO`.
- `LichSuFragment` doc danh sach phieu dat truoc con cho nhan san.

### Cau hinh san

- `ThemSanActivity` them san moi vao bang `San`.
- `CauHinhSanActivity` them/sua/xoa san qua `SanDAO`.
- `SanAdapter` va `layout_item_san.xml` hien thi moi san bang cung mot mau card, co nut sua/xoa tren tung dong.

## 4. Thiet ke CSDL SQLite

```sql
CREATE TABLE TaiKhoan (
    TenDangNhap TEXT PRIMARY KEY,
    MatKhau TEXT NOT NULL,
    TenHienThi TEXT
);

CREATE TABLE San (
    MaSan INTEGER PRIMARY KEY AUTOINCREMENT,
    TenSan TEXT NOT NULL,
    GiaMoiGio REAL NOT NULL,
    LoaiSan TEXT DEFAULT 'Co nhan tao',
    TrangThai INTEGER DEFAULT 0,
    TinhTrangHoatDong INTEGER DEFAULT 1
);

CREATE TABLE PhieuDatSan (
    MaPhieu INTEGER PRIMARY KEY AUTOINCREMENT,
    MaSan INTEGER NOT NULL,
    MaCa TEXT,
    TenKhach TEXT NOT NULL,
    SoDienThoai TEXT,
    TienCoc REAL DEFAULT 0,
    NgayDat TEXT,
    KhungGioChoi TEXT,
    GioBatDau INTEGER,
    GioKetThuc INTEGER,
    TrangThaiPhieu INTEGER DEFAULT 0,
    FOREIGN KEY(MaSan) REFERENCES San(MaSan)
);

CREATE TABLE HoaDon (
    MaHD TEXT PRIMARY KEY,
    MaPhieu INTEGER NOT NULL,
    TongTienSan REAL NOT NULL,
    TongThanhToan REAL NOT NULL,
    NgayLap TEXT NOT NULL,
    GioLapTimestamp INTEGER NOT NULL,
    FOREIGN KEY(MaPhieu) REFERENCES PhieuDatSan(MaPhieu)
);
```

## 5. Quy uoc trang thai

- `San.TrangThai = 0`: trong.
- `San.TrangThai = 1`: dang choi.
- `PhieuDatSan.TrangThaiPhieu = 0`: dat truoc.
- `PhieuDatSan.TrangThaiPhieu = 1`: dang choi.
- `PhieuDatSan.TrangThaiPhieu = 2`: da thanh toan.
