# SchoolConnect — Zalo Mini App Cổng Thông Tin Phụ Huynh & Giáo Viên Mầm Non

**SchoolConnect** là giải pháp **Sổ Liên Lạc Điện Tử Mầm Non trên nền tảng Zalo Mini App** (tham chiếu hệ sinh thái giáo dục *H2School* của **TitKul**), kết nối trực tiếp Phụ huynh và Giáo viên chủ nhiệm theo thời gian thực.

---

## 1. Kiến Trúc Tổng Thể & Ngăn Xếp Công Nghệ (Technology Stack)

| Tầng (Layer) | Công nghệ sử dụng | Mô tả chi tiết |
| :--- | :--- | :--- |
| **Frontend (Zalo Mini App)** | **React 18 + TypeScript 5 + Vite 5 + `zmp-sdk`** | Thiết kế Mobile-first (`360px - 430px`), hệ thống giao diện độc bản **SchoolConnect Emerald & Warm Cream** (`#0F766E` / `#FAFAF7`). |
| **Backend API** | **Java 21 LTS + Spring Boot 3.3.5 + Spring Security + Spring Data JPA** | Tuân thủ nghiêm ngặt **Kiến trúc Phân lớp (`Layered Architecture`)**: `controller` $\rightarrow$ `service` $\rightarrow$ `repository` $\rightarrow$ `entity` / `dto`. |
| **Cơ sở dữ liệu** | **PostgreSQL 16 / 17** | 8 bảng chuẩn hóa 3NF + 7 chỉ mục hiệu năng + dữ liệu mẫu tự động (`schema.sql` & `data.sql`). |
| **Bảo mật & Xác thực** | **Dual Auth (`DEMO_MODE` + `ZALO_AUTH`) & Stateless JWT (HMAC-SHA256)** | Hỗ trợ chuyển nhanh `Phụ huynh` $\leftrightarrows$ `Giáo viên` khi demo và xác thực `zmp.getAccessToken()` qua `graph.zalo.me/v2.0/me` (`appsecret_proof`). |
| **Đóng gói & CI/CD** | **Docker Compose + GitHub Actions** | Đóng gói `infra/docker-compose.yml` và kiểm thử tự động qua `.github/workflows/ci.yml`. |

---

## 2. Danh Mục Tính Năng Đã Hiện Thực Hóa (Implemented Features)

1. **Chuyển đổi Vai trò & Học sinh tức thì (`Role Switcher` & `Multi-Child Switcher`):**
   - Chuyển nhanh giữa **Chế độ Phụ huynh (`Trần Thu Hà`)** và **Chế độ Giáo viên / Nhà trường (`Cô Lê Thanh Thảo`)** ngay trên thanh tiêu đề.
   - Chuyển đổi tức thì giữa 2 bé: **Nguyễn Minh An (`Lớp Chồi 1`)** và **Nguyễn Minh Khôi (`Lớp Mầm 2`)**.
2. **06 Màn hình Cổng Phụ huynh:**
   - `01. Trang chủ (Home Dashboard)`: Hồ sơ bé, chiều cao/cân nặng/chỉ số **BMI**, trạng thái điểm danh hôm nay, tóm tắt nhật ký sinh hoạt và thông báo mới.
   - `02. Nhật ký Bé ngoan (Daily Report)`: Đánh giá bữa ăn, thời lượng giấc ngủ trưa (phút), hoạt động học tập/vui chơi và lời nhắn của cô giáo.
   - `03. Lịch sử Điểm danh (Attendance)`: Thống kê chuyên cần (`Có mặt`, `Nghỉ có phép`, `Đi muộn`, `Tỷ lệ %`) và nhật ký giờ vào/ra, người đón.
   - `04. Thực đơn Dinh dưỡng (Meal Menu)`: Thực đơn 3 bữa (`Bữa sáng`, `Bữa trưa`, `Bữa xế`) kèm năng lượng (`kcal`) và ghi chú dinh dưỡng.
   - `05. Bản tin & Thông báo (Announcements)`: Lọc theo danh mục (`Nhà trường`, `Lớp học`, `Y tế`, `Sự kiện`), ghim tin quan trọng và xem chi tiết.
   - `06. Xin nghỉ phép (Absence Request)`: Gửi đơn xin nghỉ phép có kiểm tra trùng lặp ngày/buổi (`HTTP 409`), tự động sinh mã đơn `LR-YYYYMMDD-XXXX`, xem lịch sử và hủy đơn đang chờ duyệt (`PENDING`).
3. **Cổng Giáo viên Duyệt đơn (`Teacher Leave Review Queue`):**
   - Xem danh sách đơn xin nghỉ theo trạng thái (`Chờ duyệt`, `Đã duyệt`, `Từ chối`, `Tất cả`).
   - Phê duyệt (`APPROVED`) hoặc Từ chối (`REJECTED` — bắt buộc nhập lý do tối thiểu 5 ký tự theo `RULE-008`), lưu bền vững vào PostgreSQL.

---

## 3. Hướng Dẫn Khởi Chạy Nhanh (Quick Start)

### Cách 1: Chạy trên máy Local (PostgreSQL + Gradle + Vite)
```powershell
# 1. Khởi chạy Backend (Cổng 8080)
cd backend
.\gradlew.bat bootRun

# 2. Khởi chạy Frontend (Cổng 3000)
cd frontend
npm install
npm run dev
```
- **Giao diện Mini App:** `http://localhost:3000`
- **Tài liệu Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **Health Check API:** `http://localhost:8080/api/v1/health`

### Cách 2: Chạy bằng Docker Compose
```powershell
docker compose -f infra/docker-compose.yml up -d --build
```

---

## 4. Kiểm Thử Tự Động (Automated Verification)

```powershell
# Kiểm thử Đơn vị & Tích hợp Backend (JUnit 5 + MockMvc)
cd backend
.\gradlew.bat test build --no-daemon

# Kiểm tra ESLint, TypeScript, Unit Test (Vitest) & Production Build Frontend
cd frontend
npm run lint
npm run typecheck
npm test
npm run build
```

