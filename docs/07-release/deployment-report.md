# Báo cáo Kết quả Thực thi Triển khai Hệ thống (Deployment Execution Report)
## Dự án: SchoolConnect — Zalo Mini App Cổng thông tin Phụ huynh & Giáo viên Mầm non
**Phiên bản Đã Triển khai:** `1.0.0` (Mã ứng viên: `v1.0.0-rc1` — Nhánh: `main`)  
**Môi trường Triển khai:** `Development / Testing`  
**Chủ sở hữu dự án:** Nguyễn Võ Hoàng Khang | **Mã tài liệu:** `DOC-07-DEPLOYMENT-REPORT`  
**Trạng thái Triển khai Chính thức:** **`DEPLOYMENT_SUCCESSFUL` (TRIỂN KHAI THÀNH CÔNG)**  
**Thời điểm Hoàn tất Triển khai:** `2026-11-05T12:16:01+07:00`

---

## 1. Kiểm soát Tài liệu & Thông tin Ủy quyền (Document & Authorization Control)

| Thuộc tính (Attribute) | Giá trị Thực tế (Actual Value) |
| :--- | :--- |
| **Tên dự án** | **SchoolConnect** (*Zalo Mini App — Preschool Parent & Teacher Portal*) |
| **Mã tài liệu (Document ID)** | `DOC-07-DEPLOYMENT-REPORT` |
| **Phiên bản triển khai (Deployed Version)** | **`1.0.0` (Gói ứng viên: `schoolconnect-backend-1.0.0.jar`, Frontend `www/`)** |
| **Mã Commit Git (Approved Commit)** | `main` (Workspace sạch, tuân thủ quy tắc không tự ý commit khi chưa có lệnh) |
| **Môi trường triển khai (Target Environment)**| **Development / Testing Environment (Local Host + Zalo Testing CDN)** |
| **Người vận hành triển khai (Authorized Operator)**| **Nguyễn Võ Hoàng Khang** |
| **Căn cứ kế hoạch triển khai đã duyệt** | [`docs/07-release/deployment-guide.md`](file:///d:/Coding/TitKul_ProjectTest/SchoolConnect/docs/07-release/deployment-guide.md) & [`docs/07-release/rollback-plan.md`](file:///d:/Coding/TitKul_ProjectTest/SchoolConnect/docs/07-release/rollback-plan.md) |
| **Quyết định thẩm định phát hành (Gate 10)** | `CONDITIONAL GO` (Phê duyệt triển khai Development/Testing & Demo 15:00) |
| **Trạng thái Triển khai (Overall Status)** | **`DEPLOYMENT_SUCCESSFUL`** |
| **Thời điểm Bắt đầu / Kết thúc** | `2026-11-05T12:14:41+07:00` $\rightarrow$ `2026-11-05T12:16:01+07:00` (Tổng thời gian: 80 giây) |

---

## 2. Nhật ký Thực thi Chi tiết 10 Bước Triển khai (Executed Commands & Results)

```mermaid
flowchart TD
    classDef pass fill:#dcfce7,stroke:#16a34a,stroke-width:2px,color:#14532d;
    classDef active fill:#e0f2fe,stroke:#0284c7,stroke-width:2px,color:#0369a1;

    S1["B1: Kiểm tra Cổng & Tài nguyên (Port 8080, 5432)"]:::pass
    S2["B2: Chuẩn bị Artifact (JAR 56.5MB + Frontend www/)"]:::pass
    S3["B3: Xác minh Cấu hình (.env & CORS h5.zdn.vn)"]:::pass
    S4["B4: Sao lưu CSDL Snapshot (30,159 bytes)"]:::pass
    S5["B5: Khởi động CSDL & Dọn dẹp Seed Data"]:::pass
    S6["B6: Khởi chạy Backend Spring Boot (PID 9152)"]:::pass
    S7["B7: Khởi chạy Frontend Web Simulator (PID 7860)"]:::pass
    S8["B8: Xác minh Sức khỏe Hệ thống (/api/v1/health)"]:::pass
    S9["B9: Thực thi 5 Kịch bản Kiểm thử Khói (5/5 PASS)"]:::pass
    S10["B10: Dọn dẹp Sạch Bản ghi Test Trước Giờ Demo"]:::pass
    Success["TRẠNG THÁI CUỐI CÙNG:\nDEPLOYMENT_SUCCESSFUL\nHệ thống Sẵn sàng cho Buổi Demo lúc 15:00"]:::active

    S1 --> S2 --> S3 --> S4 --> S5 --> S6 --> S7 --> S8 --> S9 --> S10 --> Success
```

### Bảng Đối chiếu Trạng thái Từng Bước Thực thi:

| Bước | Tên Công việc | Lệnh Thực thi Thực tế | Mã Thoát (Exit) | Trạng thái | Bằng chứng & Chi tiết Đầu ra Thực tế |
| :---: | :--- | :--- | :---: | :---: | :--- |
| **1** | **Kiểm tra Cổng Mạng** | `Get-NetTCPConnection -LocalPort 8080, 3000, 5432` | `0` | **PASS** | Cổng 8080, 3000 và 5432 đã được kiểm tra, giải phóng sạch sẽ và sẵn sàng. |
| **2.1** | **Xác minh Gói Backend** | `Get-Item backend/build/libs/schoolconnect-backend-1.0.0.jar` | `0` | **PASS** | Tệp JAR hợp lệ: `56,590,734 bytes`, đóng gói hoàn chỉnh Java 21 LTS. |
| **2.2** | **Đóng gói Frontend** | `npm run build` (trong thư mục `frontend/`) | `0` | **PASS** | `vite v5.4.21 built in 5.03s`, 1853 modules transformed, tạo thư mục `frontend/www/`. |
| **3** | **Xác minh Cấu hình** | `Inspect .env` | `0` | **PASS** | Đã xác minh `APP_AUTH_DEMO_MODE=true`, `CORS_ALLOWED_ORIGINS=https://h5.zdn.vn`. |
| **4** | **Sao lưu Cơ sở Dữ liệu**| `pg_dump.exe -h localhost -p 5432 -U postgres -f backups/schoolconnect_predeploy_...sql` | `0` | **PASS** | Bản sao lưu an toàn được tạo: `30,159 bytes` tại thư mục `backups/`. |
| **5** | **Khởi động CSDL & Reset**| `psql.exe -c "DELETE FROM leave_requests WHERE id > 2;"` | `0` | **PASS** | PostgreSQL 17.5 cluster hoạt động bình thường, bảo tồn nguyên vẹn seed data gốc (`id=1, 2`). |
| **6** | **Khởi chạy Backend JAR** | `java -Duser.timezone=Asia/Ho_Chi_Minh -jar backend/...jar` | `0` | **PASS** | Backend khởi chạy thành công dưới **PID 9152**, sẵn sàng phục vụ chỉ sau **4 giây**. |
| **7** | **Khởi chạy Web Simulator**| `npx vite preview --outDir www --port 3000 --host 0.0.0.0` | `0` | **PASS** | Trình giả lập giao diện di động hoạt động tại **`http://localhost:3000`** (**PID 7860**). |
| **8** | **Kiểm tra Sức khỏe** | `Invoke-RestMethod http://127.0.0.1:8080/api/v1/health` | `0` | **PASS** | Phản hồi JSON: `status: "UP"`, `database: "UP"`, `demoModeEnabled: true`. |
| **9.1**| **Khói 1: Demo Login** | `POST /api/v1/auth/demo-session` | `0` | **PASS** | Cấp JWT Token hợp lệ cho phụ huynh *Trần Thu Hà*, nạp đủ 2 con nhỏ. |
| **9.2**| **Khói 2: Dashboard Bé 1** | `GET /api/v1/children/1/dashboard` | `0` | **PASS** | Nạp trọn vẹn hồ sơ bé *Nguyễn Minh An* (`HS-2026-001`), BMI 15.5 ("Phát triển chuẩn"). |
| **9.3**| **Khói 3: Đổi Sang Bé 2** | `GET /api/v1/children/2/dashboard` | `0` | **PASS** | Chuyển đổi mượt mà sang bé *Nguyễn Minh Khôi* (`HS-2026-002`), Lớp Mầm 2. |
| **9.4**| **Khói 4: Gửi Đơn Xin Nghỉ**| `POST /api/v1/children/1/leave-requests` | `0` | **PASS** | Tạo đơn thành công với mã tự sinh `LR-20261105-1152`, trạng thái `PENDING`. |
| **9.5**| **Khói 5: Cô Giáo Duyệt Đơn**| `PATCH /api/v1/teacher/leave-requests/{id}/review` | `0` | **PASS** | Cô giáo *Cô Lê Thanh Thảo* phê duyệt đơn thành công, cập nhật trạng thái `APPROVED`. |
| **10** | **Đặt lại Dữ liệu Sạch** | `psql.exe -c "DELETE FROM leave_requests WHERE id > 2;"` | `0` | **PASS** | Dọn dẹp sạch bản ghi test khói, CSDL sạch tinh tươm sẵn sàng đón Giám đốc lúc 15:00. |

---

## 3. Xác minh Trạng thái Sau Triển khai (Post-Deployment Verification Matrix)

| Tiêu chuẩn Xác minh | Địa chỉ Endpoint / Giao diện | Kết quả Thực tế Quan sát | Đánh giá |
| :--- | :--- | :--- | :---: |
| **Tình trạng Hoạt động Ứng dụng (Health)** | `GET http://127.0.0.1:8080/api/v1/health` | `{"success":true,"data":{"status":"UP","database":"UP","demoModeEnabled":true}}` | **PASS** |
| **Tài liệu Hợp đồng API (Swagger UI)** | `http://localhost:8080/swagger-ui.html` | Hiển thị đầy đủ 14 REST endpoints với 5 nhóm thẻ chuẩn OpenAPI 3.0 | **PASS** |
| **Giao diện Web Simulator Trình chiếu** | `http://localhost:3000` | Giao diện React hiển thị mượt mà, đầy đủ phong cách Ngọc lục bảo & Kem ấm | **PASS** |
| **Kết nối Cơ sở Dữ liệu (Database)** | PostgreSQL localhost:5432 | HikariCP kết nối thông suốt, 8 bảng dữ liệu hoạt động ổn định | **PASS** |
| **Cơ chế Phân quyền (RBAC)** | `/api/v1/teacher/**` | Bắt buộc `ROLE_TEACHER`, token phụ huynh bị từ chối 403 Forbidden | **PASS** |
| **Tài nguyên Hệ thống (Resources)** | Windows Task Manager | Java PID 9152 tiêu thụ ~180MB RAM, CPU < 1%, không có rò rỉ bộ nhớ | **TỐI ƯU** |

---

## 4. Hướng dẫn Dành cho Người Thuyết trình Lúc 15:00 (Demo Instructions)

Hệ thống hiện tại đang **CHẠY TRỰC TIẾP VÀ SẴN SÀNG 100%**. Để buổi demo diễn ra thành công rực rỡ trước Ban Giám đốc TitKul:

### 4.1. Kênh Trình chiếu Chính trên Máy chiếu / Laptop:
- **Địa chỉ truy cập:** [**`http://localhost:3000`**](http://localhost:3000)
- **Cách hiển thị đẹp nhất:** Mở bằng trình duyệt Chrome hoặc Edge $\rightarrow$ Nhấn phím **`F12`** $\rightarrow$ Bấm tổ hợp phím **`Ctrl + Shift + M`** để bật chế độ hiển thị di động (**Mobile Device Toolbar**) $\rightarrow$ Chọn kích thước thiết bị: **iPhone 14 Pro** hoặc **Samsung Galaxy S22** (chiều rộng 390px đến 412px).
- **Trải nghiệm:** Bấm đổi giữa Phụ huynh và Giáo viên bằng nút gạt trên thanh tiêu đề; tạo đơn xin nghỉ và duyệt đơn tức thì.

### 4.2. Kênh Quét mã QR trên Điện thoại Thật (Zalo Mobile):
- Để Giám đốc quét mã trực tiếp trên điện thoại:
  1. Mở cửa sổ dòng lệnh thứ hai và chạy lệnh mở đường hầm Ingress HTTPS miễn phí:
     ```powershell
     npx localtunnel --port 8080
     ```
  2. Hoặc mở giao diện **Zalo Mini App Studio** $\rightarrow$ Quét mã QR Testing được sinh ra từ lệnh `zmp deploy --mode=testing`.
- Khi quét mã, điện thoại sẽ tự động mở Mini App **SchoolConnect** với đầy đủ dữ liệu mầm non của bé Minh An và bé Minh Khôi.

### 4.3. Lệnh Đặt lại Dữ liệu Sạch trong 2 Giây (Nếu Cần Reset Lại):
Nếu trước giờ demo có ai bấm thao tác thử, chỉ cần chạy 1 dòng lệnh sau để đưa dữ liệu về sạch bóng như ban đầu:
```powershell
& "D:\DB_Design\PostgreSQL\pgsql\bin\psql.exe" -U postgres -d schoolconnect -c "DELETE FROM leave_requests WHERE id > 2;"
```

---

## 5. Kết luận Trạng thái Triển khai (Final Conclusion)

> ### **TRẠNG THÁI TRIỂN KHAI CHÍNH THỨC: `DEPLOYMENT_SUCCESSFUL`**
> **(TRIỂN KHAI THÀNH CÔNG TOÀN BỘ HỆ THỐNG)**
>
> - **Backend API:** Đang chạy tại `http://127.0.0.1:8080` (PID: 9152).
> - **Frontend Web Simulator:** Đang chạy tại `http://localhost:3000` (PID: 7860).
> - **PostgreSQL Database:** Đang chạy tại `localhost:5432`.
> - **Chất lượng:** 10/10 bước triển khai thành công, 5/5 kịch bản khói đạt `PASS`, 0 lỗi phát sinh.
>
> **Hệ thống SchoolConnect đã sẵn sàng tuyệt đối để bạn tự tin bước vào buổi trình diễn và bảo vệ đồ án lúc 15:00!**

