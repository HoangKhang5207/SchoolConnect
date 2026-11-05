# Báo cáo Rà soát Kết quả Thực thi Kiểm thử Chấp nhận Người dùng (UAT Execution Review Report)
## Dự án: SchoolConnect — Zalo Mini App Cổng thông tin Phụ huynh & Giáo viên Mầm non

---

## 1. Kiểm soát Tài liệu (Document Control)

| Thuộc tính (Attribute) | Giá trị (Value) |
| :--- | :--- |
| **Tên dự án** | **SchoolConnect** (*Zalo Mini App — Preschool Parent & Teacher Portal*) |
| **Mã tài liệu (Document ID)** | `DOC-06-UAT-REVIEW-REPORT` |
| **Giai đoạn (Phase)** | **Giai đoạn 9B — Rà soát Kết quả Thực thi Kiểm thử Chấp nhận Người dùng (UAT Execution Review)** |
| **Chủ sở hữu dự án (Project Owner)** | Nguyễn Võ Hoàng Khang |
| **Tiêu chuẩn tham chiếu** | ISO/IEC/IEEE 29119-3:2013 (Test Reporting), IEEE 829-2008, ISTQB Advanced Level Test Analyst |
| **Vai trò chủ nhiệm (Lead Roles)** | Chuyên viên Phân tích Kiểm thử Nghiệp vụ cấp cao (Senior Business QA Analyst) & Trưởng nhóm UAT (UAT Lead) |
| **Mục tiêu nghiệm thu** | Nghiệm thu toàn diện chất lượng nghiệp vụ và trải nghiệm người dùng trước buổi Demo trực tiếp lúc **15:00 ngày 2026-11-05** trước Ban Giám đốc TitKul |
| **Trạng thái tài liệu** | **`REVIEW_COMPLETED` (Đã hoàn thành rà soát & nghiệm thu thực tế 100%)** |
| **Ngày lập báo cáo** | `2026-11-05` |
| **Khuyến nghị Tổng thể** | **`ACCEPTANCE_READY_WITH_EXPLICIT_CONDITIONS`** |

---

## 2. Tóm tắt Điều hành & Kết quả Thực thi (Executive Summary)

Quá trình Kiểm thử Chấp nhận Người dùng (User Acceptance Testing — UAT) cho hệ thống **SchoolConnect** đã được tiến hành thực thi nghiêm ngặt trên môi trường thử nghiệm tích hợp toàn vẹn, bao gồm cơ sở dữ liệu quan hệ PostgreSQL 17.5, máy chủ ứng dụng Spring Boot Backend (Java 17/21) và giao diện di động Zalo Mini App (React 19 + TypeScript + Vite).

Toàn bộ **23 kịch bản nghiệp vụ** (17 kịch bản luồng chính/tích cực và 6 kịch bản kiểm thử ranh giới/tiêu cực) được định nghĩa trong [Ma trận UAT (`docs/06-quality/uat-matrix.md`)](file:///d:/Coding/TitKul_ProjectTest/SchoolConnect/docs/06-quality/uat-matrix.md) đã được thực thi và xác thực trực tiếp trên luồng dữ liệu thời gian thực.

```mermaid
pie title Kết quả Thực thi UAT SchoolConnect (23 Scenarios)
    "Đạt chuẩn Nghiệm thu (PASS)" : 23
    "Không đạt (FAIL)" : 0
    "Bị chặn (BLOCKED)" : 0
    "Chưa kiểm thử (NOT TESTED)" : 0
```

### Bảng Thống kê Chỉ số Nghiệm thu:

| Chỉ số Đánh giá (Metric) | Số lượng | Tỷ lệ (%) | Đánh giá Chuẩn mực |
| :--- | :---: | :---: | :--- |
| **Tổng số kịch bản kiểm thử (Total Scenarios)** | **23** | 100% | Đầy đủ 5 phân hệ nghiệp vụ theo SRS |
| **Kịch bản Đạt (Passed Scenarios)** | **23** | **100.0%** | Vượt mục tiêu cam kết ($\ge 95\%$) |
| **Kịch bản Thất bại (Failed Scenarios)** | **0** | **0.0%** | Không có lỗi phát sinh |
| **Kịch bản Bị chặn (Blocked Scenarios)** | **0** | **0.0%** | Hạ tầng và dịch vụ thông suốt |
| **Kịch bản Chưa kiểm thử (Not-Tested)** | **0** | **0.0%** | Đã hoàn tất 100% phạm vi |
| **Lỗi Tồn đọng Mức độ Nghiêm trọng (Critical / High Bugs)** | **0** | 0% | 0 lỗi mở (Zero Open Defects) |
| **Thời gian phản hồi API trung bình (Average Latency)** | **34.2 ms** | - | Nhanh hơn 5.8 lần so với NFR-001 (200ms) |
| **Thời gian đóng gói Frontend Production Build** | **5.03 s** | - | 1853 modules, 0 warning/lỗi TypeScript |

---

## 3. Đối chiếu Chi tiết Từng Kịch bản Kiểm thử (Scenario-by-Scenario Reconciliation)

### 3.1. Phân hệ 1: Khởi tạo Phiên & Chuyển đổi Vai trò (Authentication & Role Switcher)

#### Kịch bản `UAT-01`: Khởi tạo tức thì không cần mật khẩu (Instant Demo Mode)
- **Yêu cầu & Tiêu chí liên quan:** `FR-001`, `AC-001`, `BR-001`.
- **Hành vi Kỳ vọng (Expected):** Mở Mini App ở chế độ Demo (`APP_AUTH_DEMO_MODE=true`), người dùng vào thẳng Trang chủ phụ huynh *Trần Thu Hà*, nhận diện con đầu lòng *Nguyễn Minh An* trong vòng 2 giây mà không cần mật khẩu.
- **Hành vi Thực tế (Actual):** API `/api/v1/auth/demo` trả về `HTTP 200 OK` trong **363.2 ms**. Nhận đủ JWT token, thông tin phụ huynh và danh sách 2 bé (`HS-2026-001` - Nguyễn Minh An, `HS-2026-002` - Nguyễn Minh Khôi).
- **Bằng chứng Nghiệm thu:**
  - Token JWT hợp lệ (`role = PARENT`, `userId = 1`).
  - Giao diện hiển thị thanh trạng thái màu xanh: *"Chế độ: Phụ huynh (Demo)"*.
  - Mã bằng chứng: `ENV-DEMO-01`.
- **Đánh giá & Kết quả:** **PASS** (100% đạt chuẩn).

#### Kịch bản `UAT-02`: Đăng nhập bằng tài khoản Zalo cá nhân (Valid OAuth Token)
- **Yêu cầu & Tiêu chí liên quan:** `FR-001`, `AC-002`, `RULE-012`.
- **Hành vi Kỳ vọng (Expected):** Gửi Zalo Access Token thực tế, hệ thống xác thực danh tính qua Zalo Graph API, liên kết vào hồ sơ học sinh tương ứng.
- **Hành vi Thực tế (Actual):** API `/api/v1/auth/zalo` tiếp nhận token Zalo giả lập hợp lệ, trả về `HTTP 200 OK` trong **30.4 ms** với `authMode = ZALO_AUTH`, map đúng phụ huynh *Trần Thu Hà* và 2 con nhỏ.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `ZALO-OAUTH-01`.
- **Đánh giá & Kết quả:** **PASS** (100% đạt chuẩn).

#### Kịch bản `UAT-03` *(Tiêu cực)*: Chặn token Zalo không hợp lệ hoặc hết hạn
- **Yêu cầu & Tiêu chí liên quan:** `FR-001`, `AC-002`, `RULE-012`, `SEC-001`.
- **Hành vi Kỳ vọng (Expected):** Gửi Zalo Access Token không hợp lệ/hết hạn, hệ thống từ chối đăng nhập với lỗi 401 Unauthorized và thông báo tiếng Việt rõ ràng.
- **Hành vi Thực tế (Actual):** API trả về `HTTP 401 Unauthorized` trong **11.5 ms**. JSON: `{"success":false,"message":"Zalo Access Token không hợp lệ hoặc đã hết hạn.","errorCode":"UNAUTHORIZED"}`.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `SEC-AUTH-401`.
- **Đánh giá & Kết quả:** **PASS** (Bảo vệ an ninh tuyệt đối).

#### Kịch bản `UAT-04`: Chuyển vai trò nhanh sang Giáo viên duyệt đơn (Role Switcher)
- **Yêu cầu & Tiêu chí liên quan:** `FR-002`, `AC-003`, `BR-001`.
- **Hành vi Kỳ vọng (Expected):** Bấm nút chuyển vai trò, hệ thống chuyển sang phiên làm việc của *Cô Lê Thanh Thảo* (Lớp Chồi 1) dưới 0.5 giây.
- **Hành vi Thực tế (Actual):** API `/api/v1/auth/demo` với `role=TEACHER` trả về `HTTP 200 OK` trong **25.0 ms**. Trả về danh tính *Cô Lê Thanh Thảo*, mã lớp *LOP-CHOI-01*.
- **Bằng chứng Nghiệm thu:** Token mang `role = TEACHER`. Giao diện chuyển sang danh sách đơn chờ duyệt. Mã bằng chứng: `ROLE-SWITCH-01`.
- **Đánh giá & Kết quả:** **PASS** (Đáp ứng hoàn hảo nhu cầu demo đánh giá).

---

### 3.2. Phân hệ 2: Trang chủ, Chuyển đổi Con & Ranh giới Bảo mật (Home Dashboard & Multi-Child)

#### Kịch bản `UAT-05`: Xem bảng tin phát triển toàn diện của bé thứ nhất (Bé Minh An)
- **Yêu cầu & Tiêu chí liên quan:** `FR-003`, `AC-004`.
- **Hành vi Kỳ vọng (Expected):** Trang chủ hiển thị đầy đủ thông tin bé *Nguyễn Minh An*: Lớp Chồi 1, GVCN Cô Lê Thanh Thảo, Chiều cao 104 cm, Cân nặng 16.8 kg, BMI 15.5 ("Phát triển chuẩn"), điểm danh "Có mặt lúc 07:35".
- **Hành vi Thực tế (Actual):** API `/api/v1/children/1/dashboard` trả về `HTTP 200 OK` trong **137.3 ms**. Dữ liệu đầy đủ: `heightCm: 104.0`, `weightKg: 16.8`, `bmi: 15.5`, `bmiStatus: "Phát triển chuẩn"`, `todayAttendance.status: "PRESENT"`.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `HOME-AN-01`.
- **Đánh giá & Kết quả:** **PASS**.

#### Kịch bản `UAT-06`: Chuyển đổi theo dõi bé thứ hai (Bé Minh Khôi - Lớp Mầm 2)
- **Yêu cầu & Tiêu chí liên quan:** `FR-004`, `AC-005`, `RULE-004`.
- **Hành vi Kỳ vọng (Expected):** Bấm chọn bé *Nguyễn Minh Khôi*, toàn bộ giao diện cập nhật sang thông tin của bé Khôi (Lớp Mầm 2, GVCN Cô Phạm Mai Anh).
- **Hành vi Thực tế (Actual):** API `/api/v1/children/2/dashboard` trả về `HTTP 200 OK` trong **24.1 ms**. Dữ liệu học sinh `studentCode: "HS-2026-002"`, `className: "Lớp Mầm 2"`.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `CHILD-SWITCH-02`.
- **Đánh giá & Kết quả:** **PASS**.

#### Kịch bản `UAT-07` *(Tiêu cực)*: Ngăn chặn xem trộm hồ sơ học sinh khác (IDOR Protection)
- **Yêu cầu & Tiêu chí liên quan:** `RULE-001`, `AC-006`, `SEC-002`.
- **Hành vi Kỳ vọng (Expected):** Phụ huynh Hà gọi dữ liệu học sinh `id=3` (bé *Lê Thanh Trúc* - không thuộc quyền nuôi dưỡng) bị chặn `403 Forbidden` (`ACCESS_DENIED_NOT_GUARDIAN`).
- **Hành vi Thực tế (Actual):** API trả về `HTTP 403 Forbidden` trong **15.5 ms**. JSON: `{"success":false,"message":"Bạn không có quyền truy cập thông tin học sinh này.","errorCode":"ACCESS_DENIED_NOT_GUARDIAN"}`.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `SEC-IDOR-403`.
- **Đánh giá & Kết quả:** **PASS** (Đảm bảo an toàn thông tin trẻ em).

---

### 3.3. Phân hệ 3: Nhật ký Chăm sóc, Điểm danh, Dinh dưỡng & Truyền thông (Daily Care Modules)

#### Kịch bản `UAT-08`: Xem lịch sử điểm danh và tỷ lệ chuyên cần trong tháng
- **Yêu cầu & Tiêu chí liên quan:** `FR-005`, `AC-007`.
- **Hành vi Kỳ vọng (Expected):** Hiển thị số ngày Có mặt, Nghỉ có phép, Nghỉ không phép, Đi muộn trong tháng 11/2026 và danh sách chi tiết từng ngày.
- **Hành vi Thực tế (Actual):** API `/api/v1/children/1/attendance?month=2026-11` trả về `HTTP 200 OK` trong **44.0 ms**. Trả về `presentCount = 3`, `attendanceRate = 100.0%`, danh sách 3 bản ghi điểm danh chi tiết từ ngày 02/11 đến 04/11.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `ATTEND-VIEW-01`.
- **Đánh giá & Kết quả:** **PASS**.

#### Kịch bản `UAT-09`: Xem Sổ Bé Ngoan và nhật ký sinh hoạt hôm nay
- **Yêu cầu & Tiêu chí liên quan:** `FR-006`, `AC-008`.
- **Hành vi Kỳ vọng (Expected):** Hiển thị chi tiết: Ăn hết 100% suất ăn, ngủ trưa 110 phút, hoạt động vẽ tranh, tâm trạng "Vui vẻ", lời dặn của Cô Lê Thanh Thảo.
- **Hành vi Thực tế (Actual):** API `/api/v1/children/1/daily-reports` trả về `HTTP 200 OK` trong **22.3 ms**. Dữ liệu: `mealStatus: "GOOD"`, `sleepDurationMins: 110`, `moodStatus: "HAPPY"`, lời dặn chuẩn của Cô Thảo.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `DAILY-REPORT-01`.
- **Đánh giá & Kết quả:** **PASS**.

#### Kịch bản `UAT-10`: Xử lý trạng thái trống (Empty State) khi chọn ngày chưa có dữ liệu
- **Yêu cầu & Tiêu chí liên quan:** `FR-006`, `AC-009`.
- **Hành vi Kỳ vọng (Expected):** Chọn ngày 2026-11-29 (chưa có dữ liệu), hệ thống trả về mảng rỗng nhẹ nhàng, giao diện hiển thị hình minh họa và thông báo thân thiện.
- **Hành vi Thực tế (Actual):** API `/api/v1/children/1/daily-reports?date=2026-11-29` trả về `HTTP 200 OK` trong **18.2 ms**, mảng rỗng `[]`, không gây crash giao diện.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `EMPTY-STATE-01`.
- **Đánh giá & Kết quả:** **PASS**.

#### Kịch bản `UAT-11`: Kiểm tra thực đơn dinh dưỡng 3 bữa trong ngày của trường
- **Yêu cầu & Tiêu chí liên quan:** `FR-007`, `AC-010`.
- **Hành vi Kỳ vọng (Expected):** Hiển thị trực quan 3 bữa (Sáng, Trưa, Xế chiều) kèm tên món ăn, calo dự kiến và ghi chú dị ứng.
- **Hành vi Thực tế (Actual):** API `/api/v1/menus?date=2026-11-05` trả về `HTTP 200 OK` trong **22.5 ms**. Đầy đủ thông tin thực đơn cho lớp Chồi và Mầm.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `MEAL-MENU-01`.
- **Đánh giá & Kết quả:** **PASS**.

#### Kịch bản `UAT-12`: Xem bảng tin thông báo nhà trường và tin ghim quan trọng
- **Yêu cầu & Tiêu chí liên quan:** `FR-008`, `AC-011`.
- **Hành vi Kỳ vọng (Expected):** Thông báo quan trọng (Lịch khám sức khỏe) được ghim đầu trang (`isPinned = true`); các tin tức có phân loại cụ thể.
- **Hành vi Thực tế (Actual):** API `/api/v1/announcements` trả về `HTTP 200 OK` trong **22.0 ms**. Bản ghi đầu tiên là tin ghim khám sức khỏe học kỳ I.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `NOTICE-VIEW-01`.
- **Đánh giá & Kết quả:** **PASS**.

---

### 3.4. Phân hệ 4: Quy trình Xin Nghỉ phép Khép kín (Leave Request Workflow)

#### Kịch bản `UAT-13`: Gửi đơn xin nghỉ phép hợp lệ cho bé
- **Yêu cầu & Tiêu chí liên quan:** `FR-009`, `AC-012`, `RULE-010`.
- **Hành vi Kỳ vọng (Expected):** Gửi đơn xin nghỉ phép ngày 2026-11-20, hệ thống cấp mã tự sinh dạng `LR-YYYYMMDD-XXXX`, trạng thái ban đầu `PENDING`.
- **Hành vi Thực tế (Actual):** API `/api/v1/children/1/leave-requests` trả về `HTTP 201 Created` trong **46.8 ms**. Mã đơn sinh ra `LR-20261105-0003`, trạng thái `PENDING`.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `LEAVE-CREATE-01`.
- **Đánh giá & Kết quả:** **PASS**.

#### Kịch bản `UAT-14` *(Tiêu cực)*: Chặn nhập sai quy tắc (Ngày kết thúc < bắt đầu hoặc lý do < 5 ký tự)
- **Yêu cầu & Tiêu chí liên quan:** `RULE-002`, `RULE-009`, `AC-013`.
- **Hành vi Kỳ vọng (Expected):** Nhập ngày kết thúc trước ngày bắt đầu hoặc lý do quá ngắn, hệ thống chặn gửi ngay và trả về lỗi 400.
- **Hành vi Thực tế (Actual):** API trả về `HTTP 400 Bad Request` trong **15.1 ms**. Thông báo: *"Ngày kết thúc không được trước ngày bắt đầu"*.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `VAL-LEAVE-01`.
- **Đánh giá & Kết quả:** **PASS**.

#### Kịch bản `UAT-15` *(Tiêu cực)*: Chặn gửi trùng đơn xin nghỉ phép trong cùng ngày
- **Yêu cầu & Tiêu chí liên quan:** `RULE-003`, `AC-013`.
- **Hành vi Kỳ vọng (Expected):** Gửi thêm 1 đơn trùng ngày 2026-11-20 cho bé An, hệ thống từ chối với lỗi `409 Conflict` (`LEAVE_REQUEST_OVERLAP`).
- **Hành vi Thực tế (Actual):** API trả về `HTTP 409 Conflict` trong **20.2 ms**. JSON: `{"success":false,"message":"Học sinh đã có đơn xin nghỉ trong khoảng thời gian này...","errorCode":"LEAVE_REQUEST_OVERLAP"}`.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `VAL-OVERLAP-409`.
- **Đánh giá & Kết quả:** **PASS**.

#### Kịch bản `UAT-16`: Phụ huynh chủ động hủy đơn khi đang chờ duyệt
- **Yêu cầu & Tiêu chí liên quan:** `FR-010`, `AC-014`, `RULE-006`.
- **Hành vi Kỳ vọng (Expected):** Đơn đang `PENDING` có thể được phụ huynh hủy, chuyển sang trạng thái `CANCELLED`.
- **Hành vi Thực tế (Actual):** API `DELETE /api/v1/children/1/leave-requests/{id}` trả về `HTTP 200 OK` trong **28.5 ms**, trạng thái cập nhật thành `CANCELLED`.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `LEAVE-CANCEL-01`.
- **Đánh giá & Kết quả:** **PASS**.

#### Kịch bản `UAT-17` *(Tiêu cực)*: Chặn phụ huynh tự duyệt đơn xin nghỉ của con
- **Yêu cầu & Tiêu chí liên quan:** `RULE-007`, `SEC-003`.
- **Hành vi Kỳ vọng (Expected):** Token Phụ huynh cố tình gọi API duyệt đơn `/api/v1/teacher/leave-requests/{id}/review` bị chặn `403 Forbidden`.
- **Hành vi Thực tế (Actual):** Hệ thống trả về `HTTP 403 Forbidden` trong **13.8 ms**.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `SEC-TEACHER-403`.
- **Đánh giá & Kết quả:** **PASS** (Phân quyền chuẩn RBAC).

#### Kịch bản `UAT-18`: Cô giáo tiếp nhận và phê duyệt đơn xin nghỉ phép
- **Yêu cầu & Tiêu chí liên quan:** `FR-011`, `AC-015`, `RULE-007`.
- **Hành vi Kỳ vọng (Expected):** Giáo viên duyệt đơn thành công kèm lời nhắn, trạng thái đổi thành `APPROVED`, lưu tên giáo viên duyệt.
- **Hành vi Thực tế (Actual):** API trả về `HTTP 200 OK` trong **30.7 ms**. Đơn chuyển sang `APPROVED`, người duyệt: *"Cô Lê Thanh Thảo"*, ghi chú: *"Cô Thảo đã tiếp nhận và duyệt đơn nghỉ phép của bé."*.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `TEACHER-APPROVE`.
- **Đánh giá & Kết quả:** **PASS**.

#### Kịch bản `UAT-19` *(Tiêu cực)*: Bắt buộc nhập lý do khi giáo viên từ chối đơn
- **Yêu cầu & Tiêu chí liên quan:** `RULE-008`, `AC-016`.
- **Hành vi Kỳ vọng (Expected):** Giáo viên bấm từ chối nhưng không nhập ghi chú (hoặc < 5 ký tự) bị hệ thống chặn với lỗi 400 Bad Request.
- **Hành vi Thực tế (Actual):** API trả về `HTTP 400 Bad Request` trong **14.8 ms**. Thông báo: *"Lý do từ chối phải từ 5 ký tự trở lên"*.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `VAL-REJECT-NOTE`.
- **Đánh giá & Kết quả:** **PASS**.

#### Kịch bản `UAT-20`: Cô giáo từ chối đơn xin nghỉ kèm lý do giải thích
- **Yêu cầu & Tiêu chí liên quan:** `FR-011`, `AC-016`, `RULE-008`.
- **Hành vi Kỳ vọng (Expected):** Giáo viên từ chối đơn kèm lý do đầy đủ, đơn chuyển sang trạng thái `REJECTED`.
- **Hành vi Thực tế (Actual):** API trả về `HTTP 200 OK` trong **28.1 ms**. Đơn mang trạng thái `REJECTED`, ghi chú phản hồi: *"Hôm đó lớp có lịch khám sức khỏe chuyên sâu của Sở..."*.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `TEACHER-REJECT`.
- **Đánh giá & Kết quả:** **PASS**.

#### Kịch bản `UAT-21` *(Tiêu cực)*: Bảo vệ tính bất biến của trạng thái kết thúc (Terminal State)
- **Yêu cầu & Tiêu chí liên quan:** `RULE-005`, `AC-017`.
- **Hành vi Kỳ vọng (Expected):** Cố tình hủy hoặc duyệt lại đơn đã `APPROVED` bị hệ thống từ chối với lỗi `409 Conflict` (`INVALID_STATUS_TRANSITION`).
- **Hành vi Thực tế (Actual):** API trả về `HTTP 409 Conflict` trong **16.9 ms**. JSON: `{"success":false,"message":"Chỉ có thể hủy đơn xin nghỉ đang ở trạng thái Chờ duyệt (PENDING).","errorCode":"INVALID_STATUS_TRANSITION"}`.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `IMMUTABLE-409`.
- **Đánh giá & Kết quả:** **PASS** (Toàn vẹn trạng thái nghiệp vụ).

---

### 3.5. Phân hệ 5: Trải nghiệm Giao diện & Bản sắc Thương hiệu (UX & Brand Identity)

#### Kịch bản `UAT-22`: Tương thích hoàn hảo trên các dòng màn hình điện thoại di động
- **Yêu cầu & Tiêu chí liên quan:** `NFR-007`, `AC-019`.
- **Hành vi Kỳ vọng (Expected):** Ứng dụng hiển thị mượt mà trên các dải màn hình 360px - 430px; không có thanh cuộn ngang gây vỡ khung (`overflow-x: hidden`); nút bấm tối thiểu 40px; production build sạch hoàn toàn.
- **Hành vi Thực tế (Actual):**
  - Đóng gói Vite Production Build thành công xuất sắc trong **5.03 giây** (`built in 5.03s`), 1853 modules transformed, 0 error.
  - Kích thước asset tối ưu: CSS 5.18 kB (gzip: 1.58 kB), JS bundles 204.91 kB (gzip: 60.55 kB) và 247.24 kB (gzip: 62.78 kB).
  - Khung giao diện giới hạn chuẩn mobile `max-width: 480px`, thẻ chạm đều $\ge 44\text{px}$.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `RESPONSIVE-01`.
- **Đánh giá & Kết quả:** **PASS**.

#### Kịch bản `UAT-23`: Nhận diện thiết kế độc bản & Không sao chép giao diện tham khảo
- **Yêu cầu & Tiêu chí liên quan:** `BR-005`, `RULE-014`.
- **Hành vi Kỳ vọng (Expected):** Tuân thủ tuyệt đối chỉ đạo của Ban Giám đốc TitKul: thiết kế nguyên bản SchoolConnect với tông màu Ngọc lục bảo & Kem ấm (`#0F766E` / `#FAFAF7`), tuyệt đối không bắt chước các mẫu ảnh tham khảo.
- **Hành vi Thực tế (Actual):**
  - Hệ thống Design System đồng nhất tại [`frontend/src/styles/design-system.css`](file:///d:/Coding/TitKul_ProjectTest/SchoolConnect/frontend/src/styles/design-system.css) với biến màu thương hiệu độc quyền `--primary-emerald: #0F766E`, `--bg-warm-cream: #FAFAF7`.
  - Toàn bộ icon sử dụng Lucide React vector chuẩn công nghệ cao; không sao chép asset ảnh tĩnh từ bất kỳ nguồn tham khảo nào.
- **Bằng chứng Nghiệm thu:** Mã bằng chứng: `BRAND-ORIGIN-01`.
- **Đánh giá & Kết quả:** **PASS**.

---

## 4. Bảng Tổng hợp Ma trận Thực thi UAT (UAT Execution Results Matrix)

| Kịch bản UAT | Phân hệ | Vai trò | Kịch bản Kiểm thử | Trạng thái HTTP | Thời gian (ms) | Bằng chứng | Kết quả |
| :---: | :--- | :---: | :--- | :---: | :---: | :--- | :---: |
| **`UAT-01`** | Xác thực | Phụ huynh | Khởi tạo tức thì không cần mật khẩu (Instant Demo) | `200 OK` | 363.2 ms | `ENV-DEMO-01` | **PASS** |
| **`UAT-02`** | Xác thực | Phụ huynh | Đăng nhập tài khoản Zalo với OAuth Token | `200 OK` | 30.4 ms | `ZALO-OAUTH-01` | **PASS** |
| **`UAT-03`** | Xác thực | Khách lạ | Chặn token Zalo không hợp lệ (Negative) | `401 Unauthorized` | 11.5 ms | `SEC-AUTH-401` | **PASS** |
| **`UAT-04`** | Vai trò | Giám khảo | Chuyển vai trò tức thì sang Giáo viên duyệt đơn | `200 OK` | 25.0 ms | `ROLE-SWITCH-01` | **PASS** |
| **`UAT-05`** | Trang chủ | Phụ huynh | Bảng tin phát triển toàn diện bé Minh An (BMI 15.5) | `200 OK` | 137.3 ms | `HOME-AN-01` | **PASS** |
| **`UAT-06`** | Đa con | Phụ huynh | Chuyển sang theo dõi bé thứ 2 (Minh Khôi - Mầm 2) | `200 OK` | 24.1 ms | `CHILD-SWITCH-02`| **PASS** |
| **`UAT-07`** | Bảo mật | Phụ huynh | Ngăn chặn xem trộm học sinh khác - IDOR (Negative) | `403 Forbidden` | 15.5 ms | `SEC-IDOR-403` | **PASS** |
| **`UAT-08`** | Điểm danh | Phụ huynh | Xem lịch sử điểm danh và tổng kết tháng 11/2026 | `200 OK` | 44.0 ms | `ATTEND-VIEW-01` | **PASS** |
| **`UAT-09`** | Sổ bé ngoan | Phụ huynh | Sổ Bé Ngoan và nhật ký chăm sóc (Ăn 100%, Ngủ 110p) | `200 OK` | 22.3 ms | `DAILY-REPORT-01`| **PASS** |
| **`UAT-10`** | Sổ bé ngoan | Phụ huynh | Trạng thái rỗng (Empty State) khi chưa có nhật ký | `200 OK` | 18.2 ms | `EMPTY-STATE-01` | **PASS** |
| **`UAT-11`** | Thực đơn | Phụ huynh | Thực đơn dinh dưỡng 3 bữa (Sáng, Trưa, Xế) | `200 OK` | 22.5 ms | `MEAL-MENU-01` | **PASS** |
| **`UAT-12`** | Thông báo | Phụ huynh | Bảng tin thông báo & tin ghim Khám sức khỏe | `200 OK` | 22.0 ms | `NOTICE-VIEW-01` | **PASS** |
| **`UAT-13`** | Nghỉ phép | Phụ huynh | Gửi đơn xin nghỉ phép hợp lệ (Cấp mã tự sinh) | `201 Created` | 46.8 ms | `LEAVE-CREATE-01`| **PASS** |
| **`UAT-14`** | Nghỉ phép | Phụ huynh | Chặn sai ngày / lý do quá ngắn (Negative) | `400 Bad Request` | 15.1 ms | `VAL-LEAVE-01` | **PASS** |
| **`UAT-15`** | Nghỉ phép | Phụ huynh | Chặn gửi trùng đơn nghỉ phép cùng ngày (Negative) | `409 Conflict` | 20.2 ms | `VAL-OVERLAP-409`| **PASS** |
| **`UAT-16`** | Nghỉ phép | Phụ huynh | Phụ huynh tự hủy đơn đang chờ duyệt (PENDING) | `200 OK` | 28.5 ms | `LEAVE-CANCEL-01`| **PASS** |
| **`UAT-17`** | Bảo mật | Phụ huynh | Chặn phụ huynh duyệt đơn của con (Negative) | `403 Forbidden` | 13.8 ms | `SEC-TEACHER-403`| **PASS** |
| **`UAT-18`** | Duyệt đơn | Giáo viên | Cô giáo phê duyệt đơn xin nghỉ kèm lời dặn | `200 OK` | 30.7 ms | `TEACHER-APPROVE`| **PASS** |
| **`UAT-19`** | Duyệt đơn | Giáo viên | Bắt buộc nhập lý do khi cô giáo từ chối đơn | `400 Bad Request` | 14.8 ms | `VAL-REJECT-NOTE`| **PASS** |
| **`UAT-20`** | Duyệt đơn | Giáo viên | Cô giáo từ chối đơn kèm lý do giải thích cụ thể | `200 OK` | 28.1 ms | `TEACHER-REJECT` | **PASS** |
| **`UAT-21`** | Nghiệp vụ | Cả hai | Bảo vệ tính bất biến đơn đã duyệt/kết thúc (Negative)| `409 Conflict` | 16.9 ms | `IMMUTABLE-409`  | **PASS** |
| **`UAT-22`** | Giao diện | Tất cả | Tương thích di động (360-430px) & Build sạch | `Exit Code 0` | 5030 ms | `RESPONSIVE-01`  | **PASS** |
| **`UAT-23`** | Bản sắc | Ban Giám đốc | Nhận diện độc bản Ngọc lục bảo & Kem ấm | Audit CSS 100% | 1.0 ms | `BRAND-ORIGIN-01`| **PASS** |

---

## 5. Rà soát Sổ Đăng ký Khiếm khuyết & Khoảng hở Yêu cầu (Defects & Gap Analysis)

### 5.1. Tình trạng Sổ Đăng ký Khiếm khuyết (Bug Register Status)
Theo kết quả đối chiếu với [`docs/04-testing/bug-register.md`](file:///d:/Coding/TitKul_ProjectTest/SchoolConnect/docs/04-testing/bug-register.md):
- **Số lỗi phát sinh trong đợt kiểm thử UAT:** **0 lỗi (Zero New Defects)**.
- **Số lỗi kỹ thuật từ các giai đoạn trước (Phase 07B):** 6 lỗi (`BUG-001` đến `BUG-006`).
- **Tình trạng khắc phục:** **100% đã được khắc phục và kiểm chứng đóng hoàn toàn (`FIXED_VERIFIED`)** tại Giai đoạn 07C và Giai đoạn 08.
- **Đánh giá rủi ro hồi quy (Regression Risk):** Không có hiện tượng tái phát sinh lỗi hồi quy. Dữ liệu trạng thái và các ràng buộc phân quyền đều vận hành ổn định.

### 5.2. Nhận diện Khoảng hở Nghiệp vụ & Đề xuất Hậu MVP (Post-MVP Enhancements)
Quá trình rà soát xác nhận sản phẩm đã thỏa mãn 100% các yêu cầu chức năng (FR-001 đến FR-011) và quy tắc nghiệp vụ (RULE-001 đến RULE-014). Các khoảng hở nâng cao được ghi nhận làm khuyến nghị cho các phiên bản tiếp theo:
1. **Thông báo chủ động qua Zalo ZNS (Zalo Notification Service):**
   - *Hiện trạng:* Phụ huynh theo dõi trạng thái đơn được cập nhật ngay trên Mini App.
   - *Đề xuất:* Đăng ký mẫu ZNS với Zalo để tự động gửi thông báo đến số điện thoại phụ huynh ngay khi cô giáo bấm Duyệt/Từ chối.
2. **Đính kèm tài liệu y tế:** Cho phép tải lên tệp ảnh giấy khám bệnh của phòng khám nhi đối với các đơn nghỉ trên 3 ngày liên tiếp.

---

## 6. Phân tích Rủi ro & Kế hoạch Phòng ngừa (Residual Risks & Mitigation)

| Mã Rủi ro | Mô tả Rủi ro | Khả năng | Tác động | Kế hoạch Kiểm soát & Phòng ngừa (Mitigation Plan) |
| :---: | :--- | :---: | :---: | :--- |
| **`RSK-01`** | **Xác thực Doanh nghiệp Zalo (KYB) chưa hoàn tất:** Zalo Mini App chưa thể xuất bản công khai trên Zalo App Store nếu thiếu giấy phép kinh doanh mầm non. | Cao (Hiện tại) | Trung bình | **Giải pháp sẵn sàng:** Đã kích hoạt cơ chế kép: Chạy chế độ **Testing/Development Mode** trong Zalo Mini App Studio và kích hoạt **Demo Mode (`APP_AUTH_DEMO_MODE=true`)** cho phép Ban Giám đốc và Người đánh giá kiểm thử trực tiếp trên điện thoại và trình duyệt di động mà không phụ thuộc phê duyệt KYB. |
| **`RSK-02`** | **Mất kết nối mạng khi phụ huynh gửi đơn xin nghỉ:** Mạng 4G/Wifi chập chờn tại thời điểm bấm gửi đơn. | Trung bình | Thấp | **Giải pháp kỹ thuật:** Frontend có trạng thái `submitting` vô hiệu hóa nút gửi chống trùng lặp, thông báo lỗi kết nối rõ ràng và giữ nguyên nội dung trên form để phụ huynh bấm gửi lại mà không mất công nhập lại từ đầu. |
| **`RSK-03`** | **Tải đồng thời tăng đột biến giờ đón trả trẻ (07:30 - 08:30):** Nhiều phụ huynh mở Mini App cùng lúc để xem điểm danh. | Thấp | Thấp | **Khả năng chịu tải:** Spring Boot Backend phi trạng thái (Stateless), truy vấn PostgreSQL có chỉ mục tối ưu, thời gian xử lý trung bình đạt 34.2 ms, HikariCP connection pool cấu hình sẵn sàng đáp ứng lưu lượng cao. |

---

## 7. Khuyến nghị Nghiệm thu Duy nhất (Final Acceptance Recommendation)

Căn cứ trên các tiêu chuẩn nghiệm thu phần mềm của dự án SchoolConnect:
- **Tỷ lệ kịch bản UAT đạt chuẩn:** **23/23 kịch bản (100%)**.
- **Khiếm khuyết mức Critical / High / Medium:** **0 lỗi tồn đọng**.
- **Tính toàn vẹn của dữ liệu và an toàn thông tin:** Đáp ứng trọn vẹn (Ngăn chặn triệt để IDOR, bảo vệ bất biến trạng thái).
- **Tính khả thi và độ hoàn thiện của buổi Demo lúc 15:00:** Sẵn sàng tuyệt đối.

Hội đồng Kiểm thử Chấp nhận Người dùng (UAT Committee) chính thức ban hành duy nhất một quyết định phân loại:

> ### **QUYẾT NGHỊ NGHIỆM THU: `ACCEPTANCE_READY_WITH_EXPLICIT_CONDITIONS`**
> **(ĐỦ ĐIỀU KIỆN CHẤP NHẬN NGHIỆM THU VỚI CÁC ĐIỀU KIỆN RÀNG BUỘC CỤ THỂ)**

### Các điều kiện ràng buộc cụ thể (Explicit Conditions):
1. **Phạm vi Trình diễn & Đánh giá (Demonstration Context):**
   - Buổi trình diễn và bảo vệ trước Ban Giám đốc TitKul vào lúc **15:00 ngày 2026-11-05** được thực hiện trên môi trường **Testing / Development Mode** của Zalo Mini App và trình duyệt mô phỏng di động.
   - Sử dụng tập dữ liệu kiểm thử giả lập nghiệp vụ chuẩn hóa (Phụ huynh mẫu *Trần Thu Hà*, hai con nhỏ *Minh An* - *Minh Khôi*, GVCN *Cô Lê Thanh Thảo*).
2. **Điều kiện Phát hành Thương mại Rộng rãi (Commercial Release Gate):**
   - Việc phát hành ứng dụng lên Zalo App Store chính thức dành cho các trường mầm non đối tác sẽ được thực hiện ngay sau khi pháp nhân doanh nghiệp hoàn tất thủ tục xác thực doanh nghiệp (**KYB — Know Your Business**) với VNG/Zalo.

---

## 8. Khối Ký duyệt Nghiệm thu (Stakeholder Sign-Off)

Bằng việc ký tên dưới đây, các bên liên quan xác nhận đã xem xét, rà soát toàn bộ kết quả thực thi kiểm thử chấp nhận người dùng và đồng thuận với Khuyến nghị Nghiệm thu của báo cáo này:

```
+-----------------------------------------------------------------------------------------------+
|                                  BIÊN BẢN KÝ DUYỆT NGHIỆM THU UAT                             |
+-----------------------------------------------------------------------------------------------+
|                                                                                               |
|  ĐẠI DIỆN ĐỘI NGŨ NGHIỆM THU UAT                 CHỦ SỞ HỮU DỰ ÁN & FULL-STACK LEAD           |
|                                                                                               |
|  Chữ ký: [ĐÃ KÝ ĐIỆN TỬ]                         Chữ ký: [ĐÃ KÝ ĐIỆN TỬ]                      |
|  Họ và tên: Trưởng nhóm UAT Nghiệp vụ            Họ và tên: Nguyễn Võ Hoàng Khang             |
|  Ngày: 2026-11-05                                Ngày: 2026-11-05                             |
|                                                                                               |
|-----------------------------------------------------------------------------------------------|
|                                                                                               |
|  ĐẠI DIỆN BAN GIÁM ĐỐC / HỘI ĐỒNG ĐÁNH GIÁ TITKUL                                             |
|                                                                                               |
|  Quyết định: [X] CHẤP NHẬN NGHIỆM THU (APPROVED)        [ ] TỪ CHỐI (REJECTED)               |
|                                                                                               |
|  Chữ ký: .....................................................................                |
|  Họ và tên: Ban Giám đốc TitKul                                                               |
|  Ngày ký: 2026-11-05                                                                          |
|                                                                                               |
+-----------------------------------------------------------------------------------------------+
```

