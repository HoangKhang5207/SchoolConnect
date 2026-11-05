# Kịch Bản Trình Diễn Trực Tiếp (Live Demo Script — 10 Phút)

- **Dự án:** SchoolConnect — Zalo Mini App Sổ Liên Lạc Mầm Non
- **Người trình bày:** Nguyễn Võ Hoàng Khang
- **Thời điểm Demo mục tiêu:** **15:00 ngày 05/11/2026 (GMT+7)**

---

## Bước 1: Kiểm tra Trạng thái Hệ thống & Kiến trúc Phân lớp (1.5 Phút)
1. Mở `http://localhost:8080/api/v1/health` và `http://localhost:8080/swagger-ui.html`.
2. Chỉ rõ phản hồi JSON xác nhận `status: "UP"`, `database: "UP"`, `architecture: "LAYERED"`, `demoModeEnabled: true`.
3. Giới thiệu 14 RESTful endpoints được phân tách rõ ràng theo `controller` $\rightarrow$ `service` $\rightarrow$ `repository` $\rightarrow$ `entity` / `dto`.

## Bước 2: Trải nghiệm Vai trò Phụ huynh & Chuyển đổi Đa Học sinh (2.5 Phút)
1. Mở giao diện Mini App tại `http://localhost:3000`.
2. Hệ thống tự động khởi tạo phiên **Phụ huynh (`Trần Thu Hà`)** và hiển thị **Trang chủ** của bé **Nguyễn Minh An (`Lớp Chồi 1`)**:
   - Chỉ số thể chất: Cao `104 cm`, Nặng `16.8 kg`, **BMI `15.5` (Phát triển chuẩn)**.
   - Điểm danh hôm nay: `Có mặt đúng giờ (07:35)`.
3. Nhấn chọn thẻ **Nguyễn Minh Khôi (`Lớp Mầm 2`)** trên thanh chọn bé:
   - Toàn bộ thông tin lớp học, giáo viên chủ nhiệm (`Cô Phạm Mai Anh`), chỉ số BMI và nhật ký lập tức cập nhật theo dữ liệu riêng của bé Khôi.
4. Chuyển lại bé **Nguyễn Minh An** và duyệt qua các tab:
   - **Nhật ký**: Xem chi tiết bữa ăn, giấc ngủ trưa `110 phút` và chọn thử **Chủ Nhật (01/11)** để minh họa giao diện **Empty State** chuẩn mực.
   - **Điểm danh**: Xem thống kê chuyên cần và lịch sử giờ vào/ra.
   - **Thực đơn**: Xem thực đơn 3 bữa (`Bữa sáng`, `Bữa trưa`, `Bữa xế`) kèm định lượng `kcal`.
   - **Thông báo**: Xem thông báo ghim quan trọng và mở hộp thoại chi tiết.

## Bước 3: Luồng Khép kín Gửi Đơn Xin Nghỉ Phép & Giáo Viên Duyệt Đơn (4.5 Phút)
1. Tại tab **Xin nghỉ**:
   - Chọn ngày nghỉ (mặc định ngày mai), buổi **Nghỉ cả ngày**, nhập lý do: *"Gia đình xin phép cho bé Minh An nghỉ 1 ngày đi khám sức khỏe định kỳ."*
   - Nhấn **Gửi đơn xin nghỉ** $\rightarrow$ Hệ thống thông báo tạo thành công kèm mã đơn `LR-YYYYMMDD-XXXX` ở trạng thái **Chờ giáo viên duyệt (`PENDING`)**.
   - Tải lại trang (`F5`) để chứng minh dữ liệu đã được lưu bền vững trong PostgreSQL.
2. **Chuyển sang Chế độ Giáo viên:**
   - Nhấn nút **"Chuyển sang: Giáo viên duyệt đơn"** trên thanh đỉnh ứng dụng.
   - Màn hình lập tức chuyển sang **Cổng Giáo Viên (`Cô Lê Thanh Thảo`)** hiển thị danh sách đơn xin nghỉ.
   - Tìm đơn `LR-YYYYMMDD-XXXX` vừa tạo của bé **Nguyễn Minh An**, nhấn **"Duyệt đơn"**, nhập ghi chú phản hồi và nhấn **Xác nhận**.
3. **Kiểm chứng Kết quả Khép kín:**
   - Nhấn **"Chuyển sang: Phụ huynh"** và quay lại tab **Xin nghỉ**.
   - Đơn xin nghỉ đã chuyển sang trạng thái **"Đã duyệt"** màu xanh kèm đầy đủ lời nhắn của `Cô Lê Thanh Thảo` và thời gian phê duyệt.

## Bước 4: Kiểm chứng Bảo mật & Xử lý Xung đột (1.5 Phút)
1. Thử gửi trùng một đơn xin nghỉ vào cùng ngày/buổi đã được duyệt $\rightarrow$ Backend chặn lại với mã lỗi `409 Conflict` (`LEAVE_REQUEST_OVERLAP`).
2. Trên Swagger UI, thử gọi `/api/v1/children/999/dashboard` bằng token của phụ huynh Trần Thu Hà $\rightarrow$ Backend trả về `403 Forbidden` (`ACCESS_DENIED_NOT_GUARDIAN`).

