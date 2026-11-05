# Final Project Report — SchoolConnect (Báo Cáo Tổng Kết Dự Án)

**Dự án:** SchoolConnect — Preschool Parent & Teacher Portal  
**Chủ sở hữu & Kỹ sư Triển khai:** Nguyễn Võ Hoàng Khang  
**Khách hàng & Đơn vị Tiếp nhận Đánh giá:** Ban Giám Đốc Công ty Cổ phần Công nghệ TitKul  
**Thời gian Bàn giao & Báo cáo:** 14:15 ngày 2026-11-05 (GMT+7)  
**Tình trạng Hoàn thành:** **HOÀN THÀNH TOÀN DIỆN 100% (READY FOR LIVE DEFENSE AT 15:00)**  

---

## 1. Bối cảnh Dự án & Mục tiêu Nghiệp vụ Ban đầu

Tại các trường mầm non tại Việt Nam, sự gắn kết thông tin giữa phụ huynh và giáo viên chủ nhiệm đóng vai trò cốt lõi trong việc chăm sóc và phát triển thể chất của trẻ. Tuy nhiên, các kênh liên lạc truyền thống (sổ giấy, tin nhắn nhóm Zalo hỗn độn, gọi điện thoại riêng lẻ) bộc lộ nhiều nhược điểm:
1. Thông tin về bữa ăn, giấc ngủ trưa và thể trạng của trẻ bị trôi hoặc thiếu chi tiết.
2. Quy trình xin nghỉ học thủ công gây khó khăn cho giáo viên trong việc thống kê suất ăn trưa và điểm danh báo cáo lên Ban Giám Hiệu.
3. Rào cản cài đặt các ứng dụng cồng kềnh từ bên thứ ba khiến tỷ lệ phụ huynh sử dụng thực tế rất thấp.

**Mục tiêu Chiến lược của Dự án SchoolConnect:**
Xây dựng một giải pháp Sổ liên lạc mầm non số hóa thế hệ mới hoạt động trực tiếp dưới dạng **Zalo Mini App** — khai thác triệt để thói quen sử dụng Zalo hằng ngày của người Việt, mang lại trải nghiệm mượt mà không cần cài đặt, minh bạch $100\%$ dinh dưỡng và tự động hóa quy trình xin nghỉ phép hai chiều.

---

## 2. Phạm vi Công việc Đã Bàn giao vs. Kế hoạch Ban đầu

Hệ thống đã bàn giao thành công **$100\%$ tính năng theo cam kết ban đầu (36/36 Nhiệm vụ Kỹ thuật)** được phân bổ trên 4 Vertical Slices:

| Hạng mục Chức năng | Phạm vi Cam kết Ban đầu | Trạng thái Bàn giao Thực tế | Ghi chú Bàn giao |
| :--- | :--- | :---: | :--- |
| **`VS-01` Cơ sở Dữ liệu & Trang chủ** | 8 bảng CSDL, Dual Auth, Dashboard | **HOÀN THÀNH 100%** | Tự động tính BMI theo chuẩn WHO, chuyển đổi bé *Minh An* / *Minh Khôi*, chuyển đổi vai trò tức thì. |
| **`VS-02` Sổ Bé Ngoan & Điểm danh** | Nhật ký ngày, tỷ lệ chuyên cần | **HOÀN THÀNH 100%** | Theo dõi chi tiết lượng ăn, giấc ngủ trưa (phút), vệ sinh, nhận xét cá nhân của giáo viên. |
| **`VS-03` Thực đơn & Bảng tin Trường** | Khẩu phần 3 bữa, phân loại tin tức | **HOÀN THÀNH 100%** | Minh bạch dinh dưỡng, lượng calo, bài viết ghim, danh mục y tế/khẩn cấp. |
| **`VS-04` Quy trình Đơn Nghỉ Phép** | Tạo đơn, hủy đơn, duyệt đơn, từ chối | **HOÀN THÀNH 100%** | Sinh mã đơn tự động (`#LR-xxx`), phụ huynh tự hủy đơn chờ duyệt, giáo viên duyệt/từ chối kèm phản hồi bắt buộc. |
| **Hạ tầng Triển khai $0\text{ VNĐ}$** | Zalo CDN + Local Backend + Tunnel | **HOÀN THÀNH 100%** | Tích hợp Cloudflare Edge Tunneling, script khởi chạy tự phục hồi [`start_demo.ps1`](file:///d:/Coding/TitKul_ProjectTest/SchoolConnect/scripts/start_demo.ps1). |

---

## 3. Kiến trúc Hệ thống & Nền tảng Công nghệ (Technology Stack)

```
[ Frontend: Zalo Mini App ]
  ├── Framework: React 18 + TypeScript (Strict Type-checking)
  ├── Build Tool: Vite 5 + zmp-vite-plugin + zmp-cli 4.0.5
  ├── Design System: Original Emerald & Warm Cream System (#0F766E / #FAFAF7)
  └── Ecosystem: Zalo Mini App SDK (zmp-sdk 2.43+)

[ Backend: Enterprise Layered Core ]
  ├── Language & Runtime: Java 21 LTS (OpenJDK / Eclipse Temurin)
  ├── Framework: Spring Boot 3.3.5 (Tomcat 10.1.31)
  ├── Security: Spring Security 6 + JJWT (HMAC-SHA256 Token Engine)
  ├── Build Automation: Gradle 8.10.2
  └── Documentation: SpringDoc OpenAPI 3.0 / Swagger UI (/swagger-ui.html)

[ Data & Storage Fleet ]
  ├── RDBMS: PostgreSQL 17.5 Community Cluster (Port 5432)
  ├── Connection Pool: HikariCP (Max 10, Min-idle 2, Timeout 10s)
  └── Schema Migration: Idempotent SQL Scripts (schema.sql & data.sql)

[ Ingress & Network Fleet ]
  ├── Edge Ingress Tunnel: Cloudflare Quick Tunnel (cloudflared v2026.10+)
  └── Static CDN Hosting: Zalo Cloud Edge CDN (h5.zdn.vn)
```

---

## 4. Tổng kết Kết quả Kiểm thử & Đảm bảo Chất lượng (QA Summary)

Toàn bộ dự án đã trải qua quy trình kiểm thử và rà soát nghiêm ngặt 5 giai đoạn:

1. **Rà soát Mã nguồn Độc lập (Phase 07A):** Đánh giá 12 chiều cạnh kiến trúc, phát hiện 6 điểm cần tối ưu (`CR-001` đến `CR-006`).
2. **Khắc phục Khiếm khuyết Chất lượng (Phase 07C):** Sửa chữa và xác nhận hoàn tất $100\%$ các lỗi (`BUG-001` đến `BUG-006`), đưa tỷ lệ kiểm thử đơn vị lên $100\%$.
3. **Kiểm thử Tích hợp Đầu cuối E2E (Phase 08):** Thực thi 24 kịch bản tích hợp tự động qua 8 ranh giới hệ thống, đạt tỷ lệ đạt **$24/24\text{ (100.0\%)}$**, độ trễ trung bình $49.8\text{ms}$.
4. **Kiểm thử Chấp nhận Người dùng UAT (Phase 09B):** Phê duyệt nghiệm thu nghiệp vụ với **$23/23\text{ (100.0\%)}$** kịch bản chấp nhận người dùng thực tế.
5. **Kiểm thử Khói Triển khai (Phase 11B):** Xác minh $5/5$ kịch bản trọng yếu trên môi trường live đạt `PASS` trong thời gian $< 50\text{ms}$.

---

## 5. Giới hạn Đã biết & Nợ Kỹ thuật (Known Limitations & Technical Debt)

Dù hệ thống hoàn toàn sẵn sàng phục vụ buổi demo trực tiếp lúc 15:00, để đưa sản phẩm vào vận hành thương mại quy mô lớn cần lưu ý các khoản nợ kỹ thuật sau:

| Mã Nợ | Nội dung Nợ Kỹ thuật | Mức độ Ảnh hưởng | Định hướng Khắc phục |
| :--- | :--- | :---: | :--- |
| **`TD-01`** | **Xác thực Doanh nghiệp Zalo KYB:** Ứng dụng hiện đang ở chế độ Testing/Development nên chỉ tài khoản trong danh sách Tester mới quét được mã QR. | Thấp (Demo) / Cao (Prod) | Nhà trường cần hoàn tất nộp giấy phép kinh doanh để được Zalo duyệt Mini App chính thức lên chợ ứng dụng. |
| **`TD-02`** | **Tên miền Cloudflare Cố định (Named Tunnel):** Quick Tunnel cấp URL ngẫu nhiên (`trycloudflare.com`) thay đổi khi bật lại. | Trung bình | Đăng ký một tên miền chính thức (ví dụ: `api.schoolconnect.edu.vn`) và cấu hình Cloudflare Named Tunnel vĩnh viễn. |
| **`TD-03`** | **Bộ đệm Dữ liệu Phân tán (Redis Caching):** Thực đơn và Thông báo hiện truy vấn trực tiếp CSDL PostgreSQL. | Thấp | Bổ sung Redis Cache cho các dữ liệu ít biến động như thực đơn tuần và bài viết thông báo để giảm tải cho DB. |
| **`TD-04`** | **Thông báo Đẩy Zalo (Zalo Notification Service — ZNS):** Khi giáo viên duyệt đơn nghỉ phép, phụ huynh xem trạng thái trên app thay vì nhận tin nhắn ZNS. | Trung bình | Tích hợp ZNS Template API gửi tin nhắn chủ động tới Zalo của phụ huynh ngay khi đơn được duyệt. |

---

## 6. Đúc kết Kinh nghiệm (Lessons Learned)

### 6.1 Những Điểm Thực hiện Tốt (What Worked Well)
- **Kiến trúc Phân lớp Sạch (Layered Architecture):** Phân tách rõ ràng giữa Controller, Service, Repository giúp việc khoanh vùng và khắc phục sự cố (như lỗi mất kết nối DB) diễn ra cực nhanh mà không làm ảnh hưởng các tầng khác.
- **Cơ chế Chuyển đổi Vai trò Thông minh (Demo Role Switcher):** Giúp Ban Giám Đốc và người đánh giá kiểm tra được trọn vẹn luồng gửi đơn $\rightarrow$ duyệt đơn khép kín chỉ trên một màn hình duy nhất trong chưa đầy 30 giây.
- **Hạ tầng Hybrid Zero-Cost:** Kết hợp Zalo CDN và Cloudflare Tunnel giúp triển khai demo thực tế mà không tốn chi phí thuê Cloud VPS hay đăng ký SSL đắt đỏ.

### 6.2 Thách thức Kỹ thuật Đã Vượt qua (Challenges Overcome)
- **Cơ chế Chống Lừa đảo HTTP 511 của Localtunnel:** Đã được nhận diện và xử lý thông qua tự động hóa header `Bypass-Tunnel-Reminder`, đồng thời nâng cấp sang Cloudflare Quick Tunnel để đạt tính ổn định tối đa.
- **Quản lý Tiến trình trên Hệ điều hành Windows:** Lỗi tắt tiến trình PostgreSQL do ngắt console `Ctrl+C` (`0xC000013A`) đã được xử lý dứt điểm bằng cách tách biệt tiến trình chạy ngầm độc lập (Detached Process) trong [`scripts/start_demo.ps1`](file:///d:/Coding/TitKul_ProjectTest/SchoolConnect/scripts/start_demo.ps1).

---

## 7. Kết luận & Khuyến nghị Bàn giao

Hệ thống **SchoolConnect v1.0.0** đã hoàn thành xuất sắc toàn bộ các chỉ tiêu chất lượng, kiến trúc và vận hành. Dự án đã sẵn sàng $100\%$ cho buổi báo cáo và bảo vệ trực tiếp trước Ban Giám Đốc Công ty Cổ phần Công nghệ TitKul vào lúc **15:00 ngày 2026-11-05**.
