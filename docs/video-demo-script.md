# Kịch Bản Quay Video Demo — SchoolConnect (3 Đến 5 Phút)

**Dự án:** SchoolConnect — Zalo Mini App Sổ Liên Lạc Mầm Non  
**Người trình bày:** Nguyễn Võ Hoàng Khang  
**Đối tượng theo dõi:** Ban Giám Đốc Công ty Cổ phần Công nghệ TitKul  
**Thời lượng tối ưu:** **3 phút 30 giây — 4 phút**  
**Chuẩn bị trước khi quay:**
1. Chạy lệnh: `.\scripts\start_demo.ps1` (Đảm bảo Backend 8080 và PostgreSQL 5432 xanh mượt).
2. Mở trình duyệt tại `http://localhost:3000` (Bật F12 Mobile Viewport, chọn khung điện thoại iPhone 12/14 Pro).
3. Đặt lại dữ liệu sạch: `& "D:\DB_Design\PostgreSQL\pgsql\bin\psql.exe" -U postgres -d schoolconnect -c "DELETE FROM leave_requests WHERE id > 2;"`

---

## BẢNG PHÂN CẢNH THAO TÁC VÀ LỜI DẪN CHI TIẾT

```
+----------------------------------------------------------------------------------------------------+
| CẢNH 1: MỞ ĐẦU & GIỚI THIỆU TỔNG QUAN (00:00 - 00:30)                                              |
+----------------------------------------------------------------------------------------------------+
| THAO TÁC MÀN HÌNH (VISUAL ACTION):                                                                 |
| - Hiển thị màn hình giao diện khởi động SchoolConnect trên trình duyệt (khung di động).            |
| - Header hiển thị logo Trường Mầm Non Sao Mai với tông màu xanh ngọc (Emerald) và kem ấm (Cream). |
|                                                                                                    |
| LỜI DẪN DEMO (VOICEOVER SCRIPT):                                                                   |
| "Kính chào Ban Giám Đốc và anh chị! Hôm nay em xin phép trình diễn sản phẩm SchoolConnect — Giải   |
| pháp Sổ liên lạc mầm non thế hệ mới được xây dựng trực tiếp trên nền tảng Zalo Mini App.          |
|                                                                                                    |
| Thay vì phải tải các ứng dụng nặng nề từ App Store hay để tin nhắn trao đổi bị trôi trên các nhóm  |
| chat thông thường, phụ huynh chỉ cần mở Zalo quét mã là có thể đồng hành cùng con mọi lúc mọi nơi. |
| Hệ thống được vận hành bởi Backend Spring Boot 3.3.5, CSDL PostgreSQL 17 và kiến trúc phân lớp sạch|
| đạt chuẩn Enterprise."                                                                             |
+----------------------------------------------------------------------------------------------------+
```

```
+----------------------------------------------------------------------------------------------------+
| CẢNH 2: TRANG CHỦ, CHỈ SỐ BMI & QUẢN LÝ ĐA HỌC SINH (00:30 - 01:10)                                 |
+----------------------------------------------------------------------------------------------------+
| THAO TÁC MÀN HÌNH (VISUAL ACTION):                                                                 |
| 1. Chỉ vào thông tin phụ huynh mẫu: "Chị Trần Thu Hà".                                             |
| 2. Thẻ học sinh mặc định: Bé Nguyễn Minh An - Lớp Chồi 1.                                          |
| 3. Cuộn nhẹ chỉ vào Thẻ thể trạng: Cao 100cm, Nặng 15.5kg -> Chỉ số BMI 15.5 -> Huy hiệu "Bình     |
|    thường (Phát triển tốt)".                                                                       |
| 4. Bấm vào thẻ "Bé Nguyễn Minh Khôi" trên thanh chọn con -> Toàn bộ dữ liệu đổi sang Lớp Mầm 2.    |
| 5. Bấm chọn lại "Bé Nguyễn Minh An".                                                               |
|                                                                                                    |
| LỜI DẪN DEMO (VOICEOVER SCRIPT):                                                                   |
| "Ngay khi truy cập, hệ thống tự động nhận diện phụ huynh Trần Thu Hà.                              |
| Tại Trang chủ, phụ huynh có thể theo dõi toàn diện thể trạng của bé Nguyễn Minh An. Hệ thống tự    |
| động tính toán chỉ số khối cơ thể BMI đạt 15.5 và gắn nhãn 'Phát triển tốt' theo tiêu chuẩn WHO.   |
|                                                                                                    |
| Điểm đặc biệt là đối với các gia đình có nhiều con cùng theo học, phụ huynh chỉ cần chạm nhẹ vào   |
| tên bé Nguyễn Minh Khôi, toàn bộ dữ liệu lớp học, giáo viên phụ trách và tình trạng điểm danh sẽ  |
| lập tức chuyển đổi mượt mà theo từng bé mà không cần đăng nhập lại."                               |
+----------------------------------------------------------------------------------------------------+
```

```
+----------------------------------------------------------------------------------------------------+
| CẢNH 3: SỔ BÉ NGOAN, THỰC ĐƠN & ĐIỂM DANH HỌC ĐƯỜNG (01:10 - 01:50)                                 |
+----------------------------------------------------------------------------------------------------+
| THAO TÁC MÀN HÌNH (VISUAL ACTION):                                                                 |
| 1. Bấm vào tab "Sổ bé ngoan" -> Xem nhật ký bữa ăn (hết suất), giấc ngủ trưa (115 phút), vệ sinh,   |
|    và lời nhắn dặn dò của cô Lê Thanh Thảo.                                                        |
| 2. Bấm sang tab "Thực đơn" -> Hiển thị khẩu phần 3 bữa (Bún bò, Cơm thịt kho, Sữa chua xế) kèm     |
|    định lượng 1,280 kcal.                                                                          |
| 3. Bấm sang tab "Điểm danh" -> Xem lịch chấm công và tỷ lệ chuyên cần 95.5%.                       |
|                                                                                                    |
| LỜI DẪN DEMO (VOICEOVER SCRIPT):                                                                   |
| "Chuyển sang mục Sổ bé ngoan, đây là nơi phụ huynh an tâm nhất khi nắm bắt được nhật ký hằng ngày: |
| từ lượng thức ăn con dùng, giấc ngủ trưa tròn giấc 115 phút, cho đến lời nhận xét riêng của cô giáo.|
|                                                                                                    |
| Nhà trường cũng minh bạch 100% chế độ dinh dưỡng tại tab Thực đơn với đầy đủ 3 bữa sáng, trưa, xế |
| và định lượng calo khoa học; cùng lịch sử Điểm danh chi tiết với tỷ lệ chuyên cần tháng đạt 95.5%."|
+----------------------------------------------------------------------------------------------------+
```

```
+----------------------------------------------------------------------------------------------------+
| CẢNH 4: [TRỌNG TÂM] PHỤ HUYNH TẠO ĐƠN XIN NGHỈ PHÉP (01:50 - 02:35)                                 |
+----------------------------------------------------------------------------------------------------+
| THAO TÁC MÀN HÌNH (VISUAL ACTION):                                                                 |
| 1. Bấm vào tab "Xin nghỉ".                                                                         |
| 2. Chọn ngày bắt đầu và kết thúc (chọn ngày mai).                                                  |
| 3. Chọn Buổi nghỉ: "Cả ngày".                                                                      |
| 4. Nhập lý do: "Bé Minh An bị sốt nhẹ, gia đình xin phép cho bé nghỉ ở nhà theo dõi sức khỏe."     |
| 5. Bấm nút "Gửi đơn xin nghỉ phép".                                                                |
| 6. Màn hình hiện thông báo thành công. Danh sách đơn xuất hiện mã đơn mới (ví dụ #LR-202611-xxx)   |
|    với huy hiệu màu vàng cam: "Chờ duyệt (PENDING)".                                               |
|                                                                                                    |
| LỜI DẪN DEMO (VOICEOVER SCRIPT):                                                                   |
| "Bây giờ, em xin trình diễn quy trình trọng tâm: Luồng Đơn xin nghỉ phép khép kín.                 |
| Thay vì phải gọi điện thoại hay viết đơn giấy, phụ huynh chỉ cần vào tab Xin nghỉ, chọn khoảng     |
| thời gian, chọn buổi nghỉ và nhập lý do.                                                           |
|                                                                                                    |
| Khi bấm 'Gửi đơn', hệ thống lập tức kiểm tra tính hợp lệ ở backend, sinh mã định danh duy nhất     |
| và đưa đơn vào trạng thái 'Chờ duyệt' với thời gian phản hồi dưới 50 mili-giây."                   |
+----------------------------------------------------------------------------------------------------+
```

```
+----------------------------------------------------------------------------------------------------+
| CẢNH 5: [TRỌNG TÂM] GIÁO VIÊN PHÊ DUYỆT ĐƠN TỨC THÌ (02:35 - 03:15)                                 |
+----------------------------------------------------------------------------------------------------+
| THAO TÁC MÀN HÌNH (VISUAL ACTION):                                                                 |
| 1. Bấm nút trên Header: "Chuyển sang: Giáo viên duyệt đơn".                                        |
| 2. Giao diện lập tức đổi thành Cổng Giáo viên: Cô Lê Thanh Thảo - Lớp Chồi 1.                      |
| 3. Thấy ngay đơn xin nghỉ của bé Nguyễn Minh An vừa gửi ở đầu danh sách Chờ duyệt.                |
| 4. Bấm nút xanh "Duyệt đơn" -> Hộp thoại xác nhận hiện ra.                                         |
| 5. Nhập lời nhắn: "Cô đã nhận thông tin. Chúc bé Minh An mau khỏe và sớm đi học lại nhé!"          |
| 6. Bấm "Xác nhận" -> Trạng thái đơn đổi ngay sang màu xanh lá: "ĐÃ DUYỆT (APPROVED)".              |
|                                                                                                    |
| LỜI DẪN DEMO (VOICEOVER SCRIPT):                                                                   |
| "Để kiểm chứng quy trình hai chiều mà không cần đổi tài khoản phức tạp, em bấm vào nút chuyển vai  |
| trò trên thanh tiêu đề. Giao diện lập tức chuyển sang Cổng Giáo Viên của Cô Lê Thanh Thảo.        |
|                                                                                                    |
| Tại đây, cô giáo thấy ngay đơn của bé Minh An nằm trong hàng đợi. Cô bấm 'Duyệt đơn', gửi kèm một |
| lời nhắn dặn dò ấm áp và bấm Xác nhận. Trạng thái đơn được cập nhật tức thì sang 'Đã duyệt' trong |
| cơ sở dữ liệu."                                                                                    |
+----------------------------------------------------------------------------------------------------+
```

```
+----------------------------------------------------------------------------------------------------+
| CẢNH 6: KIỂM CHỨNG TÍNH KHÉP KÍN & TỔNG KẾT BÀO CÁO (03:15 - 03:50)                                |
+----------------------------------------------------------------------------------------------------+
| THAO TÁC MÀN HÌNH (VISUAL ACTION):                                                                 |
| 1. Bấm nút: "Chuyển sang: Phụ huynh".                                                              |
| 2. Quay lại tab "Xin nghỉ" -> Đơn của bé Minh An đã hiện trạng thái "Đã duyệt", hiển thị rõ tên    |
|    người duyệt "Cô Lê Thanh Thảo" và lời nhắn của cô.                                              |
| 3. (Thao tác nhanh tùy chọn): Bấm thử nút "Hủy đơn" trên một đơn chờ duyệt khác để chứng minh phụ  |
|    huynh có quyền hủy đơn.                                                                         |
| 4. Quay về Trang chủ.                                                                              |
|                                                                                                    |
| LỜI DẪN DEMO (VOICEOVER SCRIPT):                                                                   |
| "Quay trở lại giao diện Phụ huynh, đơn xin nghỉ đã được đồng bộ trạng thái 'Đã duyệt' màu xanh     |
| kèm trọn vẹn lời nhắn phản hồi của cô giáo. Toàn bộ quy trình diễn ra khép kín, minh bạch và chính |
| xác 100%.                                                                                          |
|                                                                                                    |
| Dự án SchoolConnect đã hoàn thành toàn diện 4 Vertical Slices, vượt qua 41 bài kiểm thử tự động,   |
| vận hành ổn định trên hạ tầng miễn phí 0 đồng và sẵn sàng đưa vào áp dụng thực tế.                |
| Em xin chân thành cảm ơn Ban Giám Đốc đã theo dõi phần trình diễn!"                                |
+----------------------------------------------------------------------------------------------------+
```

---

## MẸO QUAY VIDEO ĐẠT ĐIỂM TỐI ĐA (PRO RECORDING TIPS)

1. **Công cụ quay:** Dùng **OBS Studio** hoặc tính năng quay màn hình có sẵn của Windows (**`Windows + Alt + R`**).
2. **Kích thước khung hình:** Thu nhỏ cửa sổ trình duyệt về tỷ lệ điện thoại di động ($390 \times 844\text{ px}$), căn giữa màn hình quay trên nền tối thanh lịch.
3. **Thao tác chuột:** Di chuyển chuột dứt khoát, mượt mà, dừng lại khoảng 1 giây ở mỗi điểm nhấn quan trọng (chỉ số BMI, nút chuyển vai trò, thông báo duyệt đơn) để người xem kịp quan sát.
4. **Giọng đọc:** Giữ âm lượng to rõ, tốc độ vừa phải, tự tin và hào hứng ở phân đoạn tạo đơn và duyệt đơn.

