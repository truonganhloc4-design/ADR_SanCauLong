# Redesign UI Layouts to Match Stitch Mockups

The user has provided Stitch mockups for the application. The current UI deviates significantly from these mockups (e.g., presence of gradient banners, missing FAB, incorrect chip colors, etc.). This plan outlines the steps to align the current Android XML layouts with the provided Stitch designs.

## User Review Required

Please review the proposed changes below. Once approved, I will implement them step by step. 

> [!IMPORTANT]
> The FAB (Floating Action Button) for adding a new court is present in the provided Stitch mockups. I will restore it and remove the `+` icon from the top toolbar that I added previously.

## Proposed Changes

### 1. Đăng nhập (Login) Layout
- **Remove** the large gradient banner (`bg_header_gradient.xml`).
- **Update** the background to a light gray color (`@color/background`).
- **Center** the login card.
- **Update Icon**: Use a circular blue icon for the app logo instead of the green one.
- **Update Typography**: "SportCenter Pro" in large black text, "Chào mừng trở lại" as subtitle.
- **Inputs**: Use rounded inputs with a light gray border.
- **Button**: Change "Đăng nhập" button to include a right arrow icon.
- **Info Box**: Wrap the default credentials note in a light gray rounded box with an info icon.

### 2. Sơ đồ sân (Dashboard) Layout & Main Shell
- **Toolbar**: 
  - Change to a white toolbar.
  - Text "Sơ đồ sân" in bold blue (`@color/tertiary` or `primary`).
  - Right icons: Stats (bar chart) and User avatar.
- **FAB**: 
  - Add back the blue Floating Action Button (`+`) for adding a new court in the bottom right corner of the screen.
- **Bottom Navigation**: 
  - Active tab should have a light blue pill background behind the icon/text.
- **Filter Chips**: 
  - "Tất cả" (Active) -> Blue background, white text.
  - "Sân trống", "Đang chơi" (Inactive) -> White background, gray border, gray text.
- **Court Cards**:
  - Add a colored left border (green for "Trống", red for "Đang chơi").
  - **Trống**: 
    - Text "Sẵn sàng" with clock icon. 
    - "Đặt lịch" button (Outlined blue).
    - "Nhận sân" button (Solid blue).
  - **Đang chơi**: 
    - Text "Còn 45p" (Red text) with clock icon. 
    - "Nguyễn Văn A" with user icon. 
    - "Chi tiết" button (Outlined gray).

### 3. Đặt lịch trước (Pre-booking) Layout
- **Toolbar**: White background, back arrow (`<-`), "Đặt lịch trước" in blue text.
- **Background**: Light gray.
- **Cards**: Wrap sections in white cards with rounded corners.
  - Card 1: "Thông tin khách hàng"
  - Card 2: "Chi tiết đặt sân"
- **Button**: Move the "XÁC NHẬN ĐẶT LỊCH" button to the very bottom, full width, solid blue.

### 4. Thanh toán (Checkout) Layout
- **Toolbar**: White background, close icon (`X`), "Thanh toán" in blue text.
- **Cards**: White card for details.
  - Header: "Sân 01 - Cỏ nhân tạo", badge "Đang chờ".
  - Total section: Light blue background for "Cần thanh toán".
- **Button**: Solid blue button with a checkmark icon "Xác nhận & Chốt ca".

### 5. Thống kê doanh thu (Stats) Layout
- **Toolbar**: White background, hamburger menu, "SportCenter Pro".
- **Header Title**: "Thống kê doanh thu" + subtitle.
- **Revenue Card**: Large solid blue card with a wallet watermark.
- **Chips**: Filter chips below the revenue card.
- **Search Bar**: Full-width rounded search bar.
- **Invoice List**: Items should have a blue receipt icon on a light blue circle.

## Verification Plan

### Manual Verification
- Compile and run the app.
- Visually compare each screen (`DangNhapActivity`, `MainActivity`, `NhanSanActivity`, `DatLichTruocActivity`, `ThanhToanActivity`) with the provided Stitch mockups.
- Ensure the jumping text issue in the login screen remains fixed.
- Test the new FAB button to ensure it navigates to `ThemSanActivity`.
