# RESTful API Contracts Specification — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect |
| **Document ID** | `DOC-03-API-CONTRACTS` |
| **Phase** | Phase 03 — System Architecture & Technical Design |
| **Base Path** | `/api/v1` |
| **Content-Type** | `application/json; charset=utf-8` |
| **Last Updated** | 2026-11-04 |

---

## 2. Standard Response & Error Envelope

All endpoints return a consistent JSON structure (`ApiResponse<T>`):

### 2.1. Success Envelope (`200 OK`, `201 Created`)
```json
{
  "success": true,
  "message": "OK",
  "data": { ... },
  "errorCode": null,
  "timestamp": "2026-11-04T16:45:00+07:00"
}
```

### 2.2. Standardized Error Envelope (`400`, `401`, `403`, `404`, `409`, `500`)
```json
{
  "success": false,
  "message": "Học sinh đã có đơn xin nghỉ trùng lặp trong khoảng thời gian này",
  "data": null,
  "errorCode": "LEAVE_REQUEST_OVERLAP",
  "fieldErrors": {
    "startDate": "Ngày bắt đầu không được nhỏ hơn ngày hiện tại"
  },
  "timestamp": "2026-11-04T16:45:00+07:00"
}
```

### 2.3. Standard Error Codes Matrix
| HTTP Status | `errorCode` | Trigger Scenario |
| :--- | :--- | :--- |
| `400 Bad Request` | `VALIDATION_ERROR` | Invalid request body fields, `startDate > endDate`, `startDate < today`, or missing `teacherNote` on rejection (`RULE-002`, `RULE-008`, `RULE-009`). |
| `401 Unauthorized` | `UNAUTHORIZED` | Missing, expired, or invalid JWT / Zalo Access Token. |
| `403 Forbidden` | `ACCESS_DENIED_NOT_GUARDIAN` | Parent requests or mutates a `studentId` not linked to their account (`RULE-001`), or `ROLE_PARENT` calls `/api/v1/teacher/**` (`RULE-007`). |
| `404 Not Found` | `RESOURCE_NOT_FOUND` | Requested `studentId`, `announcementId`, or `leaveRequestId` does not exist. |
| `409 Conflict` | `LEAVE_REQUEST_OVERLAP` | Student already has a `PENDING` or `APPROVED` leave request overlapping the date & session (`RULE-003`). |
| `409 Conflict` | `INVALID_STATUS_TRANSITION` | Attempting to cancel or review a leave request that is no longer `PENDING` (`RULE-005`). |

---

## 3. Endpoint Catalog Summary (`12 REST Endpoints`)

| # | Method | Endpoint | Auth / Role | Summary |
| :--- | :--- | :--- | :--- | :--- |
| 1 | `GET` | `/api/v1/health` | Public | System & PostgreSQL liveliness check (`FR-012`). |
| 2 | `POST` | `/api/v1/auth/demo-session` | Public (`DEMO_MODE`) | Issue JWT session for Demo Parent (`Trần Thu Hà`) or Demo Teacher (`Cô Lê Thanh Thảo`) (`FR-001`, `FR-002`). |
| 3 | `POST` | `/api/v1/auth/zalo-login` | Public (`ZALO_AUTH`) | Verify Zalo Access Token via `graph.zalo.me` (`appsecret_proof`) and issue internal JWT (`FR-001`). |
| 4 | `GET` | `/api/v1/parent/children` | `ROLE_PARENT` | Get list of preschool children linked to the authenticated parent (`FR-004`). |
| 5 | `GET` | `/api/v1/children/{studentId}/dashboard` | `ROLE_PARENT` | Aggregated Home Dashboard for selected child (`FR-003`). |
| 6 | `GET` | `/api/v1/children/{studentId}/attendance` | `ROLE_PARENT` | Monthly attendance summary metrics & daily check-in/out records (`FR-005`). |
| 7 | `GET` | `/api/v1/children/{studentId}/daily-reports` | `ROLE_PARENT` | Daily care journal (Meals, Sleep, Activities, Mood, Teacher Note) (`FR-006`). |
| 8 | `GET` | `/api/v1/meal-menus` | `ROLE_PARENT` / `TEACHER` | Daily/weekly meal menus (`BREAKFAST`, `LUNCH`, `AFTERNOON_SNACK`) (`FR-007`). |
| 9 | `GET` | `/api/v1/announcements` | `ROLE_PARENT` / `TEACHER` | Pinned & categorized school/class announcements (`FR-008`). |
| 10 | `POST` | `/api/v1/children/{studentId}/leave-requests` | `ROLE_PARENT` | Submit a new student leave request (`FR-009`). |
| 11 | `GET` | `/api/v1/leave-requests` | `ROLE_PARENT` | List leave request history for parent's linked child(ren) (`FR-009`). |
| 12 | `PATCH` | `/api/v1/leave-requests/{id}/cancel` | `ROLE_PARENT` | Parent cancels a `PENDING` leave request (`FR-010`). |
| 13 | `GET` | `/api/v1/teacher/leave-requests` | `ROLE_TEACHER` | Teacher views leave request review queue filtered by status (`FR-011`). |
| 14 | `PATCH` | `/api/v1/teacher/leave-requests/{id}/review` | `ROLE_TEACHER` | Teacher approves (`APPROVED`) or rejects (`REJECTED`) a `PENDING` leave request (`FR-011`). |

---

## 4. Detailed Endpoint Contracts

### 4.1. `POST /api/v1/auth/demo-session`
- **Auth:** Public (enabled when `app.auth.demo-mode=true`).
- **Idempotency:** Idempotent (returns a fresh stateless JWT for the requested persona).
- **Request Body:**
  ```json
  {
    "role": "PARENT"
  }
  ```
  *(Allowed values for `role`: `"PARENT"` or `"TEACHER"`)*
- **Response (`200 OK`):**
  ```json
  {
    "success": true,
    "message": "Demo session initialized",
    "data": {
      "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
      "authMode": "DEMO_MODE",
      "user": {
        "id": 1,
        "fullName": "Trần Thu Hà",
        "phone": "0901234567",
        "role": "PARENT",
        "avatarUrl": null
      },
      "linkedChildren": [
        {
          "id": 1,
          "studentCode": "HS-2026-001",
          "fullName": "Nguyễn Minh An",
          "className": "Lớp Chồi 1",
          "teacherName": "Cô Lê Thanh Thảo",
          "gender": "FEMALE",
          "dateOfBirth": "2022-04-15"
        },
        {
          "id": 2,
          "studentCode": "HS-2026-002",
          "fullName": "Nguyễn Minh Khôi",
          "className": "Lớp Mầm 2",
          "teacherName": "Cô Phạm Mai Anh",
          "gender": "MALE",
          "dateOfBirth": "2024-02-10"
        }
      ]
    }
  }
  ```

---

### 4.2. `GET /api/v1/children/{studentId}/dashboard`
- **Auth:** `Bearer <JWT>` (`ROLE_PARENT`).
- **Validation:** `RULE-001` (`studentId` must be linked to `parentId`).
- **Response (`200 OK`):**
  ```json
  {
    "success": true,
    "message": "OK",
    "data": {
      "student": {
        "id": 1,
        "studentCode": "HS-2026-001",
        "fullName": "Nguyễn Minh An",
        "dateOfBirth": "2022-04-15",
        "gender": "FEMALE",
        "className": "Lớp Chồi 1",
        "teacherName": "Cô Lê Thanh Thảo",
        "heightCm": 104.0,
        "weightKg": 16.8,
        "bmi": 15.5,
        "bmiStatus": "Bình thường",
        "allergyNotes": "Không dị ứng thực phẩm"
      },
      "todayAttendance": {
        "attendanceDate": "2026-11-04",
        "status": "PRESENT",
        "checkInTime": "07:35:00",
        "checkOutTime": null,
        "pickupPerson": null,
        "note": "Bé đến lớp vui vẻ, nhiệt độ 36.6°C"
      },
      "latestDailyReport": {
        "reportDate": "2026-11-04",
        "mealStatus": "GOOD",
        "mealComment": "Bé ăn hết suất cháo cá hồi và uống hết 180ml sữa hạt.",
        "sleepStatus": "GOOD",
        "sleepDurationMins": 110,
        "activitySummary": "Tham gia giờ vẽ tranh mùa thu và vận động theo nhạc.",
        "moodStatus": "HAPPY",
        "teacherNote": "Con rất ngoan và tích cực phát biểu trong giờ kể chuyện.",
        "recordedBy": "Cô Lê Thanh Thảo"
      },
      "pendingLeaveCount": 1,
      "recentAnnouncements": [ ... ]
    }
  }
  ```

---

### 4.3. `GET /api/v1/children/{studentId}/attendance`
- **Auth:** `Bearer <JWT>` (`ROLE_PARENT`).
- **Query Params:** `month` (optional, format `YYYY-MM`).
- **Response (`200 OK`):**
  ```json
  {
    "success": true,
    "message": "OK",
    "data": {
      "studentId": 1,
      "studentName": "Nguyễn Minh An",
      "summary": {
        "totalDays": 7,
        "presentCount": 5,
        "excusedAbsentCount": 1,
        "unexcusedAbsentCount": 0,
        "lateCount": 1,
        "attendanceRate": 85.7
      },
      "records": [
        {
          "id": 101,
          "attendanceDate": "2026-11-04",
          "status": "PRESENT",
          "checkInTime": "07:35:00",
          "checkOutTime": "16:30:00",
          "pickupPerson": "Mẹ Trần Thu Hà",
          "note": "Bé đến lớp đúng giờ"
        }
      ]
    }
  }
  ```

---

### 4.4. `GET /api/v1/children/{studentId}/daily-reports`
- **Auth:** `Bearer <JWT>` (`ROLE_PARENT`).
- **Query Params:** `date` (optional, format `YYYY-MM-DD`).
- **Response (`200 OK`):** Returns list of `DailyReportDto` items matching the student and optional date filter.

---

### 4.5. `GET /api/v1/meal-menus`
- **Auth:** `Bearer <JWT>` (`ROLE_PARENT` or `ROLE_TEACHER`).
- **Query Params:** `date` (optional, format `YYYY-MM-DD`).
- **Response (`200 OK`):**
  ```json
  {
    "success": true,
    "message": "OK",
    "data": [
      {
        "id": 1,
        "menuDate": "2026-11-04",
        "ageGroup": "ALL",
        "mealType": "BREAKFAST",
        "dishes": ["Cháo cá hồi bí đỏ", "Sữa hạt óc chó 180ml"],
        "caloriesKcal": 320,
        "nutritionNote": "Giàu Omega-3 và Vitamin A hỗ trợ thị lực"
      },
      {
        "id": 2,
        "menuDate": "2026-11-04",
        "ageGroup": "ALL",
        "mealType": "LUNCH",
        "dishes": ["Cơm mềm", "Thịt gà hầm hạt sen", "Canh rau ngót thịt bằm", "Chuối chín"],
        "caloriesKcal": 480,
        "nutritionNote": "Cân bằng đạm dễ tiêu và chất xơ tự nhiên"
      },
      {
        "id": 3,
        "menuDate": "2026-11-04",
        "ageGroup": "ALL",
        "mealType": "AFTERNOON_SNACK",
        "dishes": ["Sữa chua phô mai tươi", "Bánh flan trứng sữa"],
        "caloriesKcal": 210,
        "nutritionNote": "Bổ sung lợi khuẩn tiêu hóa và Canxi"
      }
    ]
  }
  ```

---

### 4.6. `POST /api/v1/children/{studentId}/leave-requests`
- **Auth:** `Bearer <JWT>` (`ROLE_PARENT`).
- **Idempotency & Concurrency:** Transactional overlap check (`RULE-003`) guarantees that duplicate rapid submissions for the same student/date/session cannot create duplicate rows (`HTTP 409 Conflict`).
- **Request Body:**
  ```json
  {
    "startDate": "2026-11-05",
    "endDate": "2026-11-05",
    "sessionType": "FULL_DAY",
    "reason": "Bé đi khám sức khỏe định kỳ cùng gia đình, xin phép cô cho bé nghỉ 1 ngày."
  }
  ```
- **Validation Rules:**
  - `startDate`: `@NotNull`, `startDate >= today (Asia/Ho_Chi_Minh)`.
  - `endDate`: `@NotNull`, `endDate >= startDate`, `(endDate - startDate + 1) <= 14`.
  - `sessionType`: `@NotBlank`, `FULL_DAY | MORNING | AFTERNOON`.
  - `reason`: `@NotBlank`, `@Size(min = 5, max = 500)`.
- **Response (`201 Created`):**
  ```json
  {
    "success": true,
    "message": "Đã gửi đơn xin nghỉ phép thành công",
    "data": {
      "id": 10,
      "requestCode": "LR-20261104-0003",
      "studentId": 1,
      "studentName": "Nguyễn Minh An",
      "className": "Lớp Chồi 1",
      "parentName": "Trần Thu Hà",
      "parentPhone": "0901234567",
      "startDate": "2026-11-05",
      "endDate": "2026-11-05",
      "sessionType": "FULL_DAY",
      "reason": "Bé đi khám sức khỏe định kỳ cùng gia đình, xin phép cô cho bé nghỉ 1 ngày.",
      "status": "PENDING",
      "teacherNote": null,
      "reviewedBy": null,
      "reviewedAt": null,
      "createdAt": "2026-11-04T16:45:00+07:00"
    }
  }
  ```

---

### 4.7. `PATCH /api/v1/leave-requests/{id}/cancel`
- **Auth:** `Bearer <JWT>` (`ROLE_PARENT`).
- **Rules:** Only the owning parent may cancel (`RULE-006`), and only when `status == 'PENDING'` (`RULE-005`).
- **Response (`200 OK`):** Returns updated `LeaveRequestDto` with `status: "CANCELLED"`.

---

### 4.8. `GET /api/v1/teacher/leave-requests` & `PATCH /api/v1/teacher/leave-requests/{id}/review`
- **Auth:** `Bearer <JWT>` (`ROLE_TEACHER`).
- **Filter Query Params (`GET`):** `status` (optional: `PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`).
- **Review Request Body (`PATCH`):**
  ```json
  {
    "status": "APPROVED",
    "teacherNote": "Cô Thảo đã nhận đơn và báo nhà bếp. Chúc bé đi khám sức khỏe tốt nhé!"
  }
  ```
- **Validation Rules (`PATCH`):**
  - `status`: `@NotNull`, must be `"APPROVED"` or `"REJECTED"`.
  - `teacherNote`: Mandatory (`5..500` chars) if `status == "REJECTED"` (`RULE-008`); optional if `"APPROVED"`.
  - Target request must currently have `status == "PENDING"` (`RULE-005`).
- **Response (`200 OK`):** Returns updated `LeaveRequestDto` with `status: "APPROVED"`, `reviewedBy: "Cô Lê Thanh Thảo"`, `reviewedAt`, and `teacherNote`.

