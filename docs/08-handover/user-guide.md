# User Guide — SchoolConnect (Hướng Dẫn Sử Dụng)

**Ứng dụng:** SchoolConnect — Sổ Liên Lạc Mầm Non Thông Minh  
**Nền tảng:** Zalo Mini App (Điện thoại di động) & Web Simulator (Trình duyệt máy tính)  
**Phiên bản:** 1.0.0 (Gói Zalo: v3)  
**Tổ chức phát hành:** Trường Mầm Non Sao Mai & Đội ngũ Kỹ thuật SchoolConnect  

---

## 1. Giới thiệu & Phương thức Truy cập

SchoolConnect là ứng dụng Sổ liên lạc điện tử chạy trực tiếp trên nền tảng **Zalo** dành cho Phụ huynh và Giáo viên Trường Mầm Non Sao Mai. Ứng dụng cung cấp giải pháp liên lạc hai chiều theo thời gian thực, minh bạch hóa dinh dưỡng học đường, theo dõi sức khỏe và quy trình xin nghỉ phép khép kín không cần giấy tờ.

### Cách 1: Trải nghiệm trên Điện thoại Di động (Zalo Mobile)
1. Mở ứng dụng **Zalo** trên điện thoại thông minh (iOS hoặc Android).
2. Chọn biểu tượng **Quét mã QR** ở góc trên bên phải màn hình Zalo.
3. Quét mã QR Testing phiên bản mới nhất được cấp bởi hệ thống.
4. Ứng dụng SchoolConnect sẽ tải tức thì trong vòng 1-2 giây trên Zalo WebView mà không cần cài đặt thêm bất kỳ ứng dụng nào khác từ App Store/Google Play.

### Cách 2: Trải nghiệm trên Máy tính (Web Simulator)
1. Mở trình duyệt web Google Chrome / Microsoft Edge trên máy trạm.
2. Truy cập địa chỉ cục bộ: **`http://localhost:3000`**
3. Bật chế độ giả lập điện thoại di động (*F12 $\rightarrow$ Toggle Device Toolbar $\rightarrow$ Chọn iPhone 12/14 Pro*).

---

## 2. Tài khoản & Hồ sơ Thử nghiệm Mẫu (Demo Personas)

Hệ thống được nạp sẵn $100\%$ dữ liệu mẫu hợp lệ, cho phép đánh giá toàn bộ chức năng mà không cần đăng ký tài khoản mới:

| Vai trò | Họ và Tên | Số điện thoại | Hồ sơ Liên kết | Ghi chú Trải nghiệm |
| :--- | :--- | :--- | :--- | :--- |
| **Phụ huynh (Mặc định)** | **Trần Thu Hà** | `0901234567` | - Bé **Nguyễn Minh An** (Lớp Chồi 1, 4 tuổi)<br>- Bé **Nguyễn Minh Khôi** (Lớp Mầm 2, 2 tuổi) | Quản lý cả 2 con trong cùng 1 tài khoản; theo dõi sức khỏe, thực đơn và gửi đơn nghỉ phép. |
| **Giáo viên Chủ nhiệm** | **Cô Lê Thanh Thảo** | `0912345678` | Chủ nhiệm **Lớp Chồi 1** (Niên khóa 2025-2026) | Tiếp nhận, tra cứu và phê duyệt đơn xin nghỉ phép của phụ huynh. |

> [!TIP]
> **Chuyển đổi Vai trò Tức thì (Instant Role Switcher):** Trên thanh tiêu đề (Top Header) luôn có nút chuyển đổi: **`Chuyển sang: Giáo viên duyệt đơn`** và **`Chuyển sang: Phụ huynh`**. Người dùng chỉ cần chạm vào nút này để luân chuyển giữa hai góc nhìn trải nghiệm mà không cần đăng xuất.

---

## 3. Hướng dẫn Dành cho Phụ huynh (Parent Portal)

### 3.1 Màn hình Trang chủ & Đổi bé (Home Dashboard)
- **Thanh chọn bé (Child Switcher):** Nếu phụ huynh có nhiều con đang theo học tại trường (ví dụ: bé *Minh An* và bé *Minh Khôi*), chạm vào thẻ tên của bé để chuyển đổi toàn bộ thông tin hiển thị tương ứng.
- **Thẻ Hồ sơ & Chỉ số Thể trạng (BMI Status):**
  - Hiển thị ngày sinh, giới tính, mã học sinh, tên giáo viên phụ trách.
  - Tự động tính toán chỉ số khối cơ thể (BMI) theo công thức chuẩn:
    $$\text{BMI} = \frac{\text{Cân nặng (kg)}}{(\text{Chiều cao (m)})^2}$$
  - Phân loại trạng thái thể chất chuẩn WHO dành cho lứa tuổi mầm non:
    - Bé Minh An (Chiều cao 100cm, Cân nặng 15.5kg) $\rightarrow$ **BMI 15.5** — Huy hiệu: **Bình thường (Phát triển tốt)**.
- **Tóm tắt Sức khỏe & Điểm danh Hôm nay:** Hiển thị giờ đón/trả trẻ, tình trạng điểm danh có mặt trong ngày.
- **Lối tắt Tiện ích:** Chạm nhanh vào 4 nút hành động: *Điểm danh, Thực đơn, Sổ bé ngoan, Xin nghỉ phép*.

### 3.2 Sổ Bé Ngoan (Daily Report)
- Theo dõi nhật ký chăm sóc trẻ trong ngày của giáo viên chủ nhiệm:
  - **Bữa ăn:** Tỷ lệ ăn hết suất (ví dụ: *Ăn hết suất cơm và canh, uống hết sữa*).
  - **Giấc ngủ trưa:** Thời gian ngủ (ví dụ: *Ngủ tròn giấc 115 phút từ 11:45 đến 13:40*).
  - **Vệ sinh & Vận động:** Tự giác đi vệ sinh, tham gia năng nổ các trò chơi vận động ngoài trời.
  - **Lời nhắn của Cô:** Ghi chú cá nhân hóa gửi riêng cho phụ huynh về tâm trạng và hành vi của bé.
- Phụ huynh có thể lọc xem lại nhật ký theo các ngày trước đó qua bộ chọn ngày.

### 3.3 Theo dõi Điểm danh (Attendance)
- Thống kê tỷ lệ đi học chuyên cần trong tháng (ví dụ: $95.5\%$).
- Lịch điểm danh trực quan:
  - Màu xanh lá: Có mặt đúng giờ.
  - Màu vàng cam: Nghỉ học có phép.
  - Màu đỏ: Nghỉ học không phép.

### 3.4 Thực đơn Dinh dưỡng (Meal Menu)
- Công khai minh bạch khẩu phần ăn 3 bữa trong ngày của trẻ:
  - **Bữa sáng (07:30):** Bún bò Huế dinh dưỡng + Sữa tươi tiệt trùng.
  - **Bữa trưa (11:00):** Cơm gạo dẻo, Thịt kho trứng cút, Canh cải bó xôi nấu tôm, Tráng miệng chuối cau.
  - **Bữa xế chiều (14:30):** Sữa chua men sống hoa quả + Bánh bông lan phô mai.
- Hiển thị tổng mức năng lượng nạp vào trong ngày (ví dụ: $1,280\text{ kcal}$).

### 3.5 Bảng Tin & Thông báo Nhà trường (Announcements)
- Cập nhật các thông báo chính thức từ Ban Giám Hiệu Trường Mầm Non Sao Mai:
  - Thông báo khẩn cấp / Nghỉ lễ.
  - Kế hoạch tiêm chủng y tế định kỳ.
  - Lịch dã ngoại khám phá thiên nhiên.
- Hỗ trợ lọc bài viết theo danh mục: *Tất cả, Khẩn cấp, Hoạt động, Y tế, Lễ hội*.

### 3.6 Quy trình Tạo & Hủy Đơn Xin Nghỉ Phép (Absence Request)
1. **Gửi Đơn Xin Nghỉ Phép:**
   - Chọn thẻ **Xin nghỉ phép** trên thanh điều hướng dưới cùng.
   - Nhập **Từ ngày** và **Đến ngày** (Mặc định kiểm tra ngày bắt đầu phải từ hôm nay trở đi).
   - Chọn **Buổi nghỉ:** *Cả ngày*, *Buổi sáng*, hoặc *Buổi chiều*.
   - Nhập **Lý do nghỉ học:** Tối thiểu 5 ký tự (ví dụ: *Bé bị sốt phát ban cần đi khám bác sĩ*).
   - Bấm **Gửi đơn xin nghỉ phép**.
   - Hệ thống sinh mã đơn duy nhất (ví dụ: `#LR-202611-001`) và đưa vào trạng thái **Chờ duyệt (PENDING)**.
2. **Hủy Đơn Xin Nghỉ Phép:**
   - Trong danh sách lịch sử đơn, các đơn ở trạng thái *Chờ duyệt* có nút màu đỏ **Hủy đơn**.
   - Chạm vào nút **Hủy đơn** $\rightarrow$ Đơn chuyển ngay sang trạng thái **Đã hủy (CANCELLED)**.

---

## 4. Hướng dẫn Dành cho Giáo viên (Teacher Portal)

Khi chuyển sang chế độ **Giáo viên**, giao diện tự động chuyển thành **Hàng Đợi Duyệt Đơn Nghỉ Phép (Leave Review Queue)** của Lớp Chồi 1:

1. **Xem Danh Sách Đơn:**
   - Lọc theo bộ lọc trạng thái: *Tất cả*, *Chờ duyệt*, *Đã duyệt*, *Đã từ chối*.
   - Mỗi thẻ đơn hiển thị rõ: Mã đơn, Tên học sinh, Lớp học, Tên phụ huynh, Số điện thoại liên hệ, Thời gian nghỉ và Lý do nghỉ.
2. **Phê Duyệt Đơn (Approve):**
   - Chạm vào nút xanh **Duyệt đơn** trên thẻ học sinh.
   - Hộp thoại xác nhận hiện ra. Giáo viên có thể nhập thêm lời nhắn dặn dò phụ huynh (tùy chọn).
   - Bấm **Xác nhận** $\rightarrow$ Đơn chuyển sang trạng thái **APPROVED (Đã duyệt)** và ghi nhận thông tin giáo viên duyệt.
3. **Từ Chối Đơn (Reject):**
   - Chạm vào nút đỏ **Từ chối**.
   - Giáo viên **bắt buộc nhập lý do từ chối** tối thiểu 5 ký tự (ví dụ: *Trùng lịch thi đánh giá định kỳ của trường*).
   - Bấm **Xác nhận** $\rightarrow$ Đơn chuyển sang trạng thái **REJECTED (Đã từ chối)** kèm lý do phản hồi cho phụ huynh.

---

## 5. Câu Hỏi Thường Gặp (FAQ) & Khắc Phục Sự Cố

- **Câu hỏi 1: Làm sao để chuyển đổi xem bé thứ hai?**  
  *Trả lời:* Tại Trang chủ, phụ huynh chạm vào thẻ tên của bé thứ hai (*Bé Nguyễn Minh Khôi*) ở thanh chọn con. Toàn bộ thông tin thể trạng, BMI, lịch học và đơn nghỉ sẽ tự động nạp theo bé được chọn.
- **Câu hỏi 2: Tôi có thể sửa đơn nghỉ phép sau khi đã gửi không?**  
  *Trả lời:* Để đảm bảo tính minh bạch dữ liệu, phụ huynh bấm nút **Hủy đơn** đối với đơn cũ còn đang chờ duyệt, sau đó tạo một đơn mới với thông tin chính xác.
- **Câu hỏi 3: Nếu gặp lỗi "Không thể tải dữ liệu", tôi cần làm gì?**  
  *Trả lời:* Kiểm tra kết nối mạng 4G/Wifi trên điện thoại và chạm vào nút **Thử lại** trên màn hình.

---

## 6. Liên hệ Hỗ trợ Kỹ thuật

- **Trường Mầm Non Sao Mai — Phòng Công Nghệ & Học Vụ:**
  - Hotline kỹ thuật: `+84 779 009 487`
  - Email hỗ trợ: `hoangkhang16112003@gmail.com`
  - Giờ làm việc: 07:00 – 17:30 (Thứ 2 đến Thứ 6)

