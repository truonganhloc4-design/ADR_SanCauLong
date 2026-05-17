________________________________________
TÀI LIỆU KIẾN TRÚC DỰ ÁN: HỆ THỐNG QUẢN LÝ SÂN CẦU LÔNG 
Package chính: com.example.tlbadminton (hoặc com.example.baitaplarge tùy theo project trên máy)
•	Nền tảng: Android Native
•	Ngôn ngữ: Java + XML Layout
•	Cơ sở dữ liệu: SQLite Offline (Chạy nội bộ, không cần Internet)
________________________________________
1. NGUYÊN TẮC TỔ CHỨC
•	Tách biệt vai trò: Chia rõ phần thiết kế giao diện (XML Layout) và tư duy xử lý nghiệp vụ (Java Class).
•	Đồng bộ cấu trúc: Quy hoạch thư mục (Package) theo đúng mô hình phân lớp gọn nhẹ của giảng viên hướng dẫn (activity, adapter, model, sqlite).
•	Tối ưu hóa offline: Vận hành hoàn toàn thông qua lớp kế thừa SQLiteOpenHelper, bật kiểm tra khóa ngoại (PRAGMA foreign_keys = ON) để đảm bảo toàn vẹn dữ liệu.
•	Phạm vi MVP tinh gọn: Tập trung tuyệt đối vào luồng quản lý ca chơi, đặt lịch trước và doanh thu. Loại bỏ hoàn toàn mảng bán hàng, dịch vụ đồ uống và tồn kho để tối ưu thời gian triển khai và bám sát đồ án mới.
________________________________________
2. CẤU TRÚC THƯ MỤC THỰC TẾ (ANDROID STUDIO)
Tầng xử lý Java (app/src/main/java/com/example/tlbadminton/)
Plaintext
com.example.tlbadminton
│
├── sqlite/                              # TẦNG DỮ LIỆU (DATABASE)
│   └── DatabaseHelper.java              # Khởi tạo 4 bảng CSDL, mồi data mẫu hệ thống
│
├── model/                               # TẦNG ĐỐI TƯỢNG (ENTITY POJO)
│   ├── TaiKhoan.java                    # Thực thể tài khoản đăng nhập
│   ├── San.java                         # Thực thể Sân cầu lông
│   ├── PhieuDatSan.java                 # Thực thể ca chơi (Đặt trước & Hiện hành)
│   └── HoaDon.java                      # Thực thể hóa đơn chốt ca doanh thu
│
├── adapter/                             # TẦNG TRUNG GIAN (UI BINDING)
│   └── SanAdapter.java                  # Cấu hình nạp dữ liệu lên lưới sơ đồ sân chính
│
└── activity/                            # TẦNG ĐIỀU KHIỂN GIAO DIỆN (VIEW)
    ├── LoginActivity.java               # Màn hình xác thực tài khoản (admin/123)
    ├── MainActivity.java                # Màn hình chính (Chứa 3 Tab Fragment: Sơ đồ, Thống kê, Lịch đặt)
    ├── NhanSanActivity.java             # Form kích hoạt đá ngay (Có ô chọn khách đặt trước)
    ├── DatLichTruocActivity.java        # Form lưu lịch đặt cho tương lai (Chọn ngày/giờ)
    ├── CheckoutActivity.java            # Màn hình biên lai tính tiền giờ, xuất hóa đơn trả sân
    ├── ThemSanActivity.java             # Form thêm sân mới vào hệ thống (Chạy lệnh INSERT)
    └── SuaSanActivity.java              # Form cấu hình giá, đổi trạng thái bảo trì (Chạy lệnh UPDATE)
Tầng tài nguyên XML (app/src/main/res/layout/)
Plaintext
layout/
│
├── activity_login.xml                   # Giao diện màn hình đăng nhập bo góc
├── activity_main.xml                    # Giao diện chính chứa thanh BottomNavigationView
│
├── [Màn hình chức năng đơn]
│   ├── activity_nhan_san.xml            # Form nhận sân nhanh + Dropdown khách đặt trước
│   ├── activity_dat_lich_truoc.xml      # Form nhập lịch đặt tương lai kèm Date/Time Picker
│   ├── activity_checkout.xml            # Biên lai tính tiền giờ sạch sẽ (không dính nước suối)
│   ├── activity_them_san.xml            # Giao diện nhập thông tin thêm sân mới
│   └── activity_sua_san.xml             # Giao diện cấu hình sân cũ (Có 2 nút Sẵn sàng / Bảo trì)
│
└── [Thành phần cấu thành danh sách]
    ├── item_san_card.xml                # Thiết kế 1 ô vuông sân (Có cụm nút song song Đặt lịch/Nhận sân)
    ├── item_hoa_don_row.xml             # Dòng hiển thị lịch sử hóa đơn ở trang Thống kê
    └── item_lich_dat_row.xml            # Dòng hiển thị danh sách chờ ở trang Lịch đặt
________________________________________
3. MỤC LỤC CHỨC NĂNG THEO MODULE
•	Module 1: Xác thực hệ thống (Đăng nhập)
o	Kiểm tra tính hợp lệ dữ liệu đầu vào (Validate trống).
o	Truy vấn đối sánh thông tin tài khoản với bảng TaiKhoan trong SQLite.
•	Module 2: Quản lý sơ đồ sân (Dashboard chính)
o	Đổ danh sách sân lên giao diện trực quan dưới dạng ô lưới lưới (GridLayout tích hợp RecyclerView).
o	Kiểm tra trạng thái thời gian thực: Hiển thị cụm nút song song "Đặt lịch / Nhận sân" nếu sân trống (Màu xanh); Hiển thị duy nhất nút "Chi tiết" nếu sân đang đá (Màu đỏ).
•	Module 3: Quản lý Nhận sân / Đặt lịch trước
o	Nhận sân ngay (NhanSanActivity): Cho phép nhập thông tin trực tiếp cho khách vãng lai hoặc chọn nhanh từ danh sách khách đã đặt trước (Autofill dữ liệu phiếu cũ), chuyển trạng thái sân sang Đang chơi và lưu mốc GioBatDau.
o	Đặt lịch trước (DatLichTruocActivity): Cho phép đặt lịch cho ngày mai/ngày mốt, ghi nhận ngày đặt, khung giờ hẹn chơi và tiền cọc. Trạng thái sơ đồ sân hiện hành không bị ảnh hưởng.
•	Module 4: Tính tiền & Trả sân (Checkout)
o	Tự động tính số phút sử dụng bằng công thức trừ thời gian Timestamp hệ thống (System.currentTimeMillis()).
o	Tính toán tổng tiền dựa trên đơn giá thuê của từng sân và trừ đi số tiền khách đã đặt cọc trước.
o	Chốt ca: Lưu thông tin tài chính vào bảng HoaDon, đổi trạng thái phiếu đặt và trả diện mạo sân về Trống (Màu xanh).
•	Module 5: Thống kê & Quản trị hệ thống
o	Tính tổng doanh thu thực tế bằng câu lệnh SUM(TongThanhToan) trong SQLite.
o	Liệt kê lịch sử hóa đơn theo thứ tự thời gian ca chơi mới nhất lên đầu.
o	Thêm sân (ThemSanActivity): Nhập tên sân và đơn giá mới để mở rộng quy mô câu lạc bộ.
o	Sửa cấu hình sân (SuaSanActivity): Cập nhật lại giá thuê khi có biến động hoặc tạm khóa sân chuyển sang trạng thái "Đang bảo trì" để sửa chữa.
________________________________________
4. THIẾT KẾ CƠ SỞ DỮ LIỆU SQLITE OFFLINE (4 BẢNG CỐT LÕI)
SQL
-- 1. Bảng Tài Khoản: Quản lý đăng nhập hệ thống
CREATE TABLE TaiKhoan (
    TenDangNhap TEXT PRIMARY KEY,
    MatKhau TEXT NOT NULL,
    TenHienThi TEXT
);

-- 2. Bảng Sân: Quản lý danh mục sân cầu lông và giá thuê
CREATE TABLE San (
    MaSan INTEGER PRIMARY KEY AUTOINCREMENT,
    TenSan TEXT NOT NULL,
    GiaMoiGio REAL NOT NULL,
    LoaiSan TEXT DEFAULT 'Cỏ nhân tạo',
    TrangThai INTEGER DEFAULT 0,            -- 0: Trống (Xanh), 1: Đang chơi (Đỏ)
    TinhTrangHoatDong INTEGER DEFAULT 1    -- 1: Sẵn sàng hoạt động, 0: Đang bảo trì
);

-- 3. Bảng Phiếu Đặt Sân: Lưu vết lịch sử đặt trước và các ca đang chơi thực tế
CREATE TABLE PhieuDatSan (
    MaPhieu INTEGER PRIMARY KEY AUTOINCREMENT,
    MaSan INTEGER NOT NULL,
    MaCa TEXT,                              -- Mã chuỗi dạng #BK-XXXXX hiển thị lên hóa đơn
    TenKhach TEXT NOT NULL,
    SoDienThoai TEXT,
    TienCoc REAL DEFAULT 0,
    NgayDat TEXT,                           -- Khung ngày hẹn đặt lịch trước (mm/dd/yyyy)
    KhungGioChoi TEXT,                      -- Khung giờ hẹn đặt lịch trước (VD: 18:00 - 20:00)
    GioBatDau INTEGER,                      -- Lưu timestamp lúc bấm nút nhận sân bắt đầu chơi
    GioKetThuc INTEGER,                     -- Lưu timestamp lúc bấm nút trả sân tính tiền
    TrangThaiPhieu INTEGER DEFAULT 0,       -- 0: Lịch đặt trước, 1: Đang chơi, 2: Đã thanh toán xong
    FOREIGN KEY(MaSan) REFERENCES San(MaSan)
);

-- 4. Bảng Hóa Đơn: Lưu vết dữ liệu tài chính phục vụ báo cáo doanh thu
CREATE TABLE HoaDon (
    MaHD TEXT PRIMARY KEY,                  -- Mã chuỗi định danh hóa đơn (VD: HD-2410-089)
    MaPhieu INTEGER NOT NULL,
    TongTienSan REAL NOT NULL,
    TongThanhToan REAL NOT NULL,
    NgayLap TEXT NOT NULL,                  -- Chuỗi hiển thị ngày chốt hóa đơn (dd/MM/yyyy)
    GioLapTimestamp INTEGER NOT NULL,       -- Dùng để ORDER BY đưa hóa đơn mới nhất lên đầu
    FOREIGN KEY(MaPhieu) REFERENCES PhieuDatSan(MaPhieu)
);
________________________________________
5. THỨ TỰ ƯU TIÊN KHI TRIỂN KHAI CODE
Để quá trình lập trình diễn ra mạch lạc, không bị lỗi thiếu file bắc cầu, quy trình xây dựng dự án được thực hiện nghiêm ngặt theo các bước sau:
Plaintext
TIẾN TRÌNH CODE CHUẨN LUỒNG:
[Bước 1: Database & Cấu hình] ➔ [Bước 2: Giao diện nền & Login] ➔ [Bước 3: Sơ đồ sân Grid] ➔ [Bước 4: Nghiệp vụ & Quản trị]
•	Bước 1: Khởi tạo Database & Cấu hình nền tảng: Viết file DatabaseHelper.java trong thư mục sqlite/ kèm theo các dòng dữ liệu mồi mẫu. Khai báo hệ màu nhận diện thương hiệu trong res/values/colors.xml.
•	Bước 2: Xây dựng màn hình Đăng nhập: Thiết kế các file drawable bo góc, vẽ layout activity_login.xml và viết logic xử lý so khớp dữ liệu tài khoản trong LoginActivity.java.
•	Bước 3: Hiện thực hóa Sơ đồ sân dạng lưới: Vẽ giao diện thẻ sân item_san_card.xml chứa cụm nút song song. Viết class SanAdapter.java ứng dụng logic if-else trạng thái để ẩn/hiện nút. Nạp danh sách lên MainActivity.java.
•	Bước 4: Hoàn thiện các Form Nghiệp vụ & Quản trị đơn lẻ: Triển khai code giao diện và logic xử lý cho các Activity chức năng theo thứ tự: NhanSanActivity.java ➔ DatLichTruocActivity.java ➔ CheckoutActivity.java ➔ ThemSanActivity.java ➔ SuaSanActivity.java.

