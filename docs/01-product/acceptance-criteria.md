# Testable Acceptance Criteria (`Given / When / Then`) — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect |
| **Document ID** | `DOC-01-ACCEPTANCE-CRITERIA` |
| **Phase** | Phase 01 — Product Discovery & Requirement Analysis |
| **Last Updated** | 2026-11-04 |

---

## 2. Acceptance Criteria Catalog

All criteria below follow the strict **`Given / When / Then`** format, specifying observable, measurable system behaviors without vague qualifiers.

### 2.1. Authentication & Role Switching (`FR-001`, `FR-002`, `INT-001`)

#### `AC-001`: Instant Demo Mode Initialization (`FR-001`)
- **Given** the application is configured with `DEMO_MODE` enabled and the user opens the SchoolConnect Mini App without an active token,
- **When** the initial boot sequence completes,
- **Then** the Frontend automatically obtains a valid `ROLE_PARENT` session for the seeded parent (`Trần Thu Hà`) from `/api/v1/auth/demo-session`, renders the Home Dashboard within 2,000ms on a local/broadband connection, and displays the active role badge `"Chế độ: Phụ huynh (Demo)"`.

#### `AC-002`: Zalo Access Token Verification (`FR-001`, `INT-001`)
- **Given** `ZALO_AUTH` mode is invoked with a Zalo Access Token sent to `POST /api/v1/auth/zalo-login`,
- **When** the Backend verifies the token with `https://graph.zalo.me/v2.0/me` (including HMAC-SHA256 `appsecret_proof`) and matches a linked `zalo_user_id` in `parents`,
- **Then** the Backend returns `HTTP 200 OK` with a signed internal JWT (`accessToken`), token expiration timestamp, and the parent's profile; if the token is invalid or expired, the Backend returns `HTTP 401 Unauthorized` with a structured JSON error payload.

#### `AC-003`: Instant Role Switching Between Parent and Teacher (`FR-002`, `OQ-02`)
- **Given** the user is currently viewing the Mini App in `ROLE_PARENT` mode,
- **When** the user taps the Role Switcher button (`"Chuyển sang: Giáo viên duyệt đơn"`),
- **Then** the active session switches to `ROLE_TEACHER` (`Cô Lê Thanh Thảo`), the UI header updates to `"Chế độ: Giáo viên / Nhà trường (Demo)"`, and the screen navigates to the **Teacher Leave Review Dashboard** displaying all student leave requests from PostgreSQL.

---

### 2.2. Home Dashboard & Multi-Child Switching (`FR-003`, `FR-004`, `RULE-001`)

#### `AC-004`: Home Dashboard Data Completeness (`FR-003`)
- **Given** the authenticated parent (`Trần Thu Hà`) has selected Child 1 (`Nguyễn Minh An`, `studentId = 1`),
- **When** the Home Dashboard loads (`GET /api/v1/children/1/dashboard`),
- **Then** the screen displays:
  1. Child full name (`Nguyễn Minh An`), student code (`HS-2026-001`), class (`Lớp Chồi 1`), homeroom teacher (`Cô Lê Thanh Thảo`), and physical metrics badge (height `104 cm`, weight `16.8 kg`, BMI `15.5`).
  2. Today's attendance status badge (`PRESENT` / *Có mặt*) and check-in time (`07:35`).
  3. Today's quick summary of meals, nap duration, and mood.
  4. The latest 2 school/class announcements.

#### `AC-005`: Multi-Child Switcher & Data Isolation (`FR-004`, `RULE-001`)
- **Given** the parent (`Trần Thu Hà`) is linked to two children (`Nguyễn Minh An` – `id=1` and `Nguyễn Minh Khôi` – `id=2`),
- **When** the parent taps the Child Switcher chip for `Nguyễn Minh Khôi`,
- **Then** the Frontend invokes `GET /api/v1/children/2/dashboard` and updates all displayed cards (class `Lớp Mầm 2`, attendance, daily report, and leave request counts) to reflect `Nguyễn Minh Khôi`'s data exclusively.

#### `AC-006`: Unauthorized Student Access Prevention (`RULE-001`, `NFR-005`)
- **Given** an authenticated `ROLE_PARENT` user whose `parent_id` is NOT linked to `studentId = 999` in `parent_students`,
- **When** a client sends `GET /api/v1/children/999/dashboard`, `GET /api/v1/children/999/attendance`, or `POST /api/v1/children/999/leave-requests`,
- **Then** the Backend rejects the request with `HTTP 403 Forbidden` and error code `ACCESS_DENIED_NOT_GUARDIAN`, returning zero student data.

---

### 2.3. Attendance, Daily Report, Meal Menu & Announcements (`FR-005` to `FR-008`)

#### `AC-007`: Attendance History & Summary Accuracy (`FR-005`)
- **Given** the parent navigates to the `03. Attendance` screen for the selected child,
- **When** `GET /api/v1/children/{id}/attendance` completes,
- **Then** the UI displays the numeric counts of `PRESENT`, `ABSENT_EXCUSED`, `ABSENT_UNEXCUSED`, and `LATE` days, followed by a chronological list of attendance records showing date, status badge, check-in time, check-out time, and pickup person.

#### `AC-008`: Daily Activity & Care Report Display (`FR-006`)
- **Given** the parent navigates to the `02. Daily Report` screen for a date that has a seeded report,
- **When** `GET /api/v1/children/{id}/daily-reports` completes,
- **Then** the UI displays 4 distinct sections: Meal Evaluation (`meal_status` & `meal_comment`), Nap/Sleep Summary (`sleep_status` & `sleep_duration_mins` in minutes), Learning/Play Activities (`activity_summary`), and Child Mood & Teacher Note (`mood_status`, `teacher_note`, `recorded_by`).

#### `AC-009`: Empty State Handling on Unrecorded Dates (`FR-006`, `FR-007`, `NFR-008`)
- **Given** the parent selects a date on the Daily Report or Meal Menu screen for which no database record exists,
- **When** the API returns an empty list (`200 OK` with `data: []`),
- **Then** the UI renders a dedicated Empty State illustration and message (*"Chưa có dữ liệu cho ngày được chọn"*) without throwing console errors or rendering blank/broken cards.

#### `AC-010`: Daily Meal Menu Breakdown (`FR-007`, `RULE-012`)
- **Given** the parent opens the `04. Meal Menu` screen,
- **When** `GET /api/v1/meal-menus` returns the menu for the selected date,
- **Then** the UI renders cards grouped in chronological order: `BREAKFAST` (*Bữa sáng*), `LUNCH` (*Bữa trưa*), and `AFTERNOON_SNACK` (*Bữa xế*), listing all dishes, total estimated kilocalories (`calories_kcal`), and nutrition/allergy notes.

#### `AC-011`: School & Class Announcements View (`FR-008`)
- **Given** the parent opens the `05. Announcements` screen,
- **When** `GET /api/v1/announcements` completes,
- **Then** pinned announcements (`is_pinned = true`) appear at the top of the list, each item displays its category badge (`SCHOOL`, `CLASS`, `HEALTH`, `EVENT`), title, publication date, and summary, and tapping an announcement displays its full content.

---

### 2.4. Leave Request Submission, Validation & Cancellation (`FR-009`, `FR-010`)

#### `AC-012`: Successful Leave Request Creation & PostgreSQL Persistence (`FR-009`, `RULE-010`)
- **Given** the parent is on `06. Absence Request` for `studentId = 1` and enters a valid `startDate >= today`, `endDate >= startDate`, `sessionType = "FULL_DAY"`, and `reason` of 25 characters,
- **When** the parent taps **"Gửi đơn xin nghỉ"**,
- **Then**:
  1. The submit button enters a loading/disabled state immediately (`RULE-004`).
  2. The Backend persists a new row in PostgreSQL `leave_requests` with `status = 'PENDING'` and a unique `request_code` matching `^LR-\d{8}-\d{4}$`.
  3. The API returns `HTTP 201 Created`.
  4. After reloading the Mini App (`window.location.reload()`), the newly created leave request remains visible in the history list from PostgreSQL.

#### `AC-013`: Leave Request Input & Overlap Validation (`RULE-002`, `RULE-003`, `RULE-009`)
- **Given** the parent attempts to submit a leave request where:
  - Case A: `endDate` is earlier than `startDate`, OR `reason` has fewer than 5 characters;
  - Case B: An active (`PENDING` or `APPROVED`) leave request already exists for the same student overlapping the requested date and session,
- **When** the request is submitted,
- **Then** Case A is blocked with `HTTP 400 Bad Request` (or client inline validation error) and Case B is rejected by the Backend with `HTTP 409 Conflict` and message `"Học sinh đã có đơn xin nghỉ trùng lặp trong khoảng thời gian này"`, without creating a database row.

#### `AC-014`: Parent Cancellation of Pending Leave Request (`FR-010`, `RULE-005`, `RULE-006`)
- **Given** a leave request owned by the parent currently has `status = 'PENDING'`,
- **When** the parent taps **"Hủy đơn"** and confirms via `PATCH /api/v1/leave-requests/{id}/cancel`,
- **Then** the Backend updates `status` to `'CANCELLED'` and `updated_at` in PostgreSQL, returns `HTTP 200 OK`, and the UI updates the status badge to `"Đã hủy"` and hides the cancel button.

---

### 2.5. Teacher Leave Request Approval & Rejection (`FR-011`, `RULE-005`, `RULE-007`, `RULE-008`)

#### `AC-015`: Teacher Approves a Pending Leave Request (`FR-011`, `OQ-02`)
- **Given** the user is in `ROLE_TEACHER` mode viewing a `PENDING` leave request (`id = X`),
- **When** the teacher taps **"Duyệt đơn"**, optionally enters a `teacherNote`, and submits `PATCH /api/v1/teacher/leave-requests/X/review` with `status = "APPROVED"`,
- **Then** the Backend updates `status = 'APPROVED'`, sets `reviewed_by = 'Cô Lê Thanh Thảo'` and `reviewed_at` to the current timestamp in PostgreSQL, returns `HTTP 200 OK`, and when the user switches back to `ROLE_PARENT` mode, the parent sees the badge `"Đã duyệt"` along with the teacher's note and review timestamp.

#### `AC-016`: Teacher Rejects a Pending Leave Request (Mandatory Note Enforcement) (`FR-011`, `RULE-008`)
- **Given** the user is in `ROLE_TEACHER` mode attempting to reject a `PENDING` leave request (`status = "REJECTED"`),
- **When** the teacher submits with an empty `teacherNote` (or `< 5` characters),
- **Then** the system rejects the action with a validation error requiring a reason; when the teacher provides a valid `teacherNote` (`>= 5` characters), the Backend updates `status = 'REJECTED'` in PostgreSQL and displays the rejection reason to the parent.

#### `AC-017`: Immutable Terminal Leave Statuses (`RULE-005`)
- **Given** a leave request is already in `APPROVED`, `REJECTED`, or `CANCELLED` status,
- **When** either a Parent attempts to cancel it or a Teacher attempts to review it again,
- **Then** the Backend returns `HTTP 409 Conflict` with error code `INVALID_STATUS_TRANSITION` and leaves the database record unchanged.

---

### 2.6. Non-Functional & Platform Compatibility Criteria (`NFR-001` to `NFR-010`)

#### `AC-018`: CORS & Zalo Origin Preflight Compliance (`NFR-006`, `INT-002`)
- **Given** the Spring Boot Backend is running,
- **When** an HTTP `OPTIONS` preflight request is sent to any `/api/v1/**` endpoint with header `Origin: https://h5.zdn.vn` and `Access-Control-Request-Method: POST` or `PATCH`,
- **Then** the Backend responds with `HTTP 200 OK` (or `204 No Content`) including `Access-Control-Allow-Origin: https://h5.zdn.vn` and `Access-Control-Allow-Methods` containing `GET, POST, PUT, PATCH, DELETE, OPTIONS`.

#### `AC-019`: Mobile Viewport Responsiveness & Visual Originality (`NFR-007`, `NFR-008`, `RULE-014`)
- **Given** the Mini App is rendered on mobile viewport widths from `360px` to `430px` (standard iOS/Android Zalo WebView dimensions),
- **When** the user navigates across all 6 Parent screens and the Teacher Review screen,
- **Then** there is zero horizontal overflow (`scrollWidth <= clientWidth`), all touch targets have a minimum height/width of `40px`, and the visual layout uses SchoolConnect's custom design system without copying any of the 5 reference images.

