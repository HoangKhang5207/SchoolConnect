# Test Cases Specification — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect (*Zalo Mini App — Preschool Parent & Teacher Portal*) |
| **Document ID** | `DOC-04-TEST-CASES` |
| **Phase** | Phase 07B — Test Execution & Defect Analysis |
| **Last Updated** | 2026-11-04 |

---

## 2. Test Cases Matrix (`Positive`, `Negative`, `Boundary`)

### 2.1. Module: Authentication & Role Switcher (`FR-001`, `FR-002`, `AC-001`..`AC-003`)

| Test ID | Req ID | Type | Scenario | Preconditions | Steps | Expected Result | Actual Result | Priority | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `TC-AUTH-001` | `FR-001`<br>`AC-001` | Positive | Issue Parent Demo Session in `DEMO_MODE` | `app.auth.demo-mode=true` | Send `POST /api/v1/auth/demo-session` with body `{"role":"PARENT"}` | Returns `200 OK`, signed JWT, `user.fullName: "Trần Thu Hà"`, and 2 linked children | Verified (`ControllerIntegrationTest.java#order(1)`) | P0 (Critical) | **PASS** |
| `TC-AUTH-002` | `FR-002`<br>`AC-003` | Positive | Switch to Teacher Demo Session | `app.auth.demo-mode=true` | Send `POST /api/v1/auth/demo-session` with body `{"role":"TEACHER"}` | Returns `200 OK`, signed JWT with `ROLE_TEACHER`, `user.fullName: "Cô Lê Thanh Thảo"` | Verified (`ControllerIntegrationTest.java#order(1)`) | P0 (Critical) | **PASS** |
| `TC-AUTH-003` | `FR-001` | Negative | Issue demo session when `demo-mode` disabled | `app.auth.demo-mode=false` | Send `POST /api/v1/auth/demo-session` with body `{"role":"PARENT"}` | Returns `403 Forbidden` with `errorCode: "DEMO_MODE_DISABLED"` | Verified (`AuthService.java#L64`) | P1 (High) | **PASS** |
| `TC-AUTH-004` | `FR-001`<br>`AC-002` | Positive | Zalo Login with valid token in test mode | Parent account exists | Send `POST /api/v1/auth/zalo-login` with `{"accessToken":"mock_zalo_token_parent"}` | Returns `200 OK`, `authMode: "ZALO_AUTH"`, parent user profile | Verified (`ControllerIntegrationTest.java#order(1)`) | P1 (High) | **PASS** |
| `TC-AUTH-005` | `FR-001`<br>`AC-002` | Negative | Zalo Login with invalid/expired token | Invalid token string | Send `POST /api/v1/auth/zalo-login` with `{"accessToken":"invalid_token_123"}` | Expected `401 Unauthorized` per `AC-002` & API contract | Currently returns `400 Bad Request` (`BUG-002 / CR-003`) | P1 (High) | **FAIL** |
| `TC-AUTH-006` | `FR-001` | Boundary | Send empty or blank token | None | Send `POST /api/v1/auth/zalo-login` with `{"accessToken":""}` | Returns `400 Bad Request` (`VALIDATION_ERROR`) | Verified via Jakarta Validation | P2 (Medium) | **PASS** |

---

### 2.2. Module: Parent Student & Home Dashboard (`FR-003`, `FR-004`, `RULE-001`, `AC-004`..`AC-006`)

| Test ID | Req ID | Type | Scenario | Preconditions | Steps | Expected Result | Actual Result | Priority | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `TC-DASH-001` | `FR-003`<br>`AC-004` | Positive | Load Home Dashboard for linked Child 1 (`Minh An`) | Parent token for `Trần Thu Hà` | `GET /api/v1/children/1/dashboard` | Returns `200 OK`, `student.fullName: "Nguyễn Minh An"`, `className: "Lớp Chồi 1"`, `bmi: 15.5`, today's attendance `PRESENT` | Verified (`ControllerIntegrationTest.java#order(2)`) | P0 (Critical) | **PASS** |
| `TC-DASH-002` | `FR-004`<br>`AC-005` | Positive | Switch to Child 2 (`Minh Khôi`) | Parent token for `Trần Thu Hà` | `GET /api/v1/children/2/dashboard` | Returns `200 OK`, `student.fullName: "Nguyễn Minh Khôi"`, `className: "Lớp Mầm 2"` | Verified (`ControllerIntegrationTest.java#order(2)`) | P0 (Critical) | **PASS** |
| `TC-DASH-003` | `RULE-001`<br>`AC-006` | Negative | Prevent access to unlinked Child 3 (`Gia Hân`) | Parent 1 token (`Trần Thu Hà`) | `GET /api/v1/children/3/dashboard` | Returns `403 Forbidden` with `errorCode: "ACCESS_DENIED_NOT_GUARDIAN"` | Verified (`ControllerIntegrationTest.java#order(2)`) | P0 (Critical) | **PASS** |
| `TC-DASH-004` | `RULE-001`<br>`AC-006` | Negative | Prevent access to non-existent Child ID 999 | Parent 1 token | `GET /api/v1/children/999/dashboard` | Returns `403 Forbidden` (`ACCESS_DENIED_NOT_GUARDIAN`) | Verified (`ControllerIntegrationTest.java#order(2)`) | P0 (Critical) | **PASS** |
| `TC-DASH-005` | `FR-003` | Boundary | BMI calculation with exact boundary metrics | Student: `104cm`, `16.8kg` | Unit test calculation `16.8 / (1.04^2)` | Returns `15.5` rounded to 1 decimal place, status `"Phát triển chuẩn"` | Verified in `StudentDashboardServiceTest` | P2 (Medium) | **PASS** |

---

### 2.3. Module: Attendance, Daily Report, Meal Menu & Announcements (`FR-005`..`FR-008`, `AC-007`..`AC-011`)

| Test ID | Req ID | Type | Scenario | Preconditions | Steps | Expected Result | Actual Result | Priority | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `TC-CARE-001` | `FR-005`<br>`AC-007` | Positive | Get child attendance metrics and history | Child 1 linked to Parent 1 | `GET /api/v1/children/1/attendance` | Returns summary (`totalDays >= 6`, `presentCount`, `rate%`) and chronological records | Verified (`ControllerIntegrationTest.java#order(3)`) | P1 (High) | **PASS** |
| `TC-CARE-002` | `FR-006`<br>`AC-008` | Positive | Get daily report for recorded date (`2026-11-04`) | Seeded report exists | `GET /api/v1/children/1/daily-reports?date=2026-11-04` | Returns report with `mealStatus: "GOOD"`, `sleepDurationMins: 110`, `moodStatus: "HAPPY"` | Verified (`ControllerIntegrationTest.java#order(3)`) | P1 (High) | **PASS** |
| `TC-CARE-003` | `FR-006`<br>`AC-009` | Boundary | Get daily report on Sunday (`2026-11-01` unrecorded) | No report for Sunday | `GET /api/v1/children/1/daily-reports?date=2026-11-01` | Returns `200 OK` with `data: []` enabling UI Empty State | Verified (`ControllerIntegrationTest.java#order(3)`) | P2 (Medium) | **PASS** |
| `TC-CARE-004` | `FR-007`<br>`AC-010` | Positive | Get 3-session nutritional meal menus | Public or authenticated | `GET /api/v1/meal-menus?date=2026-11-04` | Returns 3 items ordered: `BREAKFAST`, `LUNCH`, `AFTERNOON_SNACK` with dishes & kcal | Verified (`ControllerIntegrationTest.java#order(3)`) | P1 (High) | **PASS** |
| `TC-CARE-005` | `FR-008`<br>`AC-011` | Positive | List school announcements with pinned notices first | Public or authenticated | `GET /api/v1/announcements` | Pinned announcements (`isPinned: true`) appear at index 0 | Verified (`ControllerIntegrationTest.java#order(3)`) | P2 (Medium) | **PASS** |

---

### 2.4. Module: Closed-Loop Leave Request Workflow (`FR-009`..`FR-011`, `RULE-002`..`RULE-010`, `AC-012`..`AC-017`)

| Test ID | Req ID | Type | Scenario | Preconditions | Steps | Expected Result | Actual Result | Priority | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `TC-LEAVE-001` | `FR-009`<br>`AC-012` | Positive | Parent creates new leave request successfully | Future date `+5d`, valid reason $\ge 5$ chars | `POST /api/v1/children/1/leave-requests` with valid body | Returns `201 Created`, `status: "PENDING"`, code matches `^LR-\d{8}-\d{4}$` | Passed on initial run, failed on repeat due to DB state residue (`BUG-001`) | P0 (Critical) | **FAIL (Contamination)** |
| `TC-LEAVE-002` | `RULE-002` | Negative | Reject leave request with `startDate` in past | Past date | Submit leave request with `startDate < today` | Returns `400 Bad Request` (`VALIDATION_ERROR`) | Verified (`LeaveRequestServiceTest.java`) | P1 (High) | **PASS** |
| `TC-LEAVE-003` | `RULE-002` | Negative | Reject leave request with `endDate < startDate` | Inverted dates | Submit leave request with `endDate < startDate` | Returns `400 Bad Request` (`VALIDATION_ERROR`) | Verified (`LeaveRequestServiceTest.java`) | P1 (High) | **PASS** |
| `TC-LEAVE-004` | `RULE-002` | Boundary | Reject leave request exceeding 14 calendar days | Date range = 15 days | Submit leave request with 15 days duration | Returns `400 Bad Request` (`VALIDATION_ERROR`) | Verified (`LeaveRequestService.java#L81`) | P2 (Medium) | **PASS** |
| `TC-LEAVE-005` | `RULE-003`<br>`AC-013` | Negative | Reject overlapping leave request on same session | Pending/Approved request exists on target date | Submit duplicate leave request for same student, date, and `FULL_DAY` | Returns `409 Conflict` (`LEAVE_REQUEST_OVERLAP`) | Verified (`LeaveRequestServiceTest.java`) | P0 (Critical) | **PASS** |
| `TC-LEAVE-006` | `RULE-009` | Boundary | Reject leave request reason with $< 5$ chars | Reason = `"Nghỉ"` (4 chars) | Submit leave request with 4-char reason | Returns `400 Bad Request` (`VALIDATION_ERROR`) | Verified (`LeaveRequestService.java#L90`) | P2 (Medium) | **PASS** |
| `TC-LEAVE-007` | `RULE-007` | Negative | Parent attempts to call Teacher review endpoint | Parent JWT | `PATCH /api/v1/teacher/leave-requests/1/review` | Returns `403 Forbidden` (`FORBIDDEN_ROLE` / Spring Security) | Verified (`ControllerIntegrationTest.java#order(4)`) | P0 (Critical) | **PASS** |
| `TC-LEAVE-008` | `FR-011`<br>`AC-015` | Positive | Teacher approves pending leave request | Pending request ID | `PATCH /api/v1/teacher/leave-requests/{id}/review` with `{"status":"APPROVED"}` | Returns `200 OK`, `status: "APPROVED"`, `reviewedBy: "Cô Lê Thanh Thảo"` | Verified (`LeaveRequestServiceTest.java`) | P0 (Critical) | **PASS** |
| `TC-LEAVE-009` | `RULE-008`<br>`AC-016` | Negative | Teacher rejects request without mandatory reason | Rejection reason $< 5$ chars | `PATCH .../review` with `status: "REJECTED"`, empty note | Returns `400 Bad Request` requiring rejection reason | Verified (`LeaveRequestServiceTest.java`) | P1 (High) | **PASS** |
| `TC-LEAVE-010` | `FR-010`<br>`AC-014` | Positive | Parent cancels own pending leave request | Request is in `PENDING` status | `PATCH /api/v1/leave-requests/{id}/cancel` | Returns `200 OK`, `status: "CANCELLED"` | Verified (`LeaveRequestServiceTest.java`) | P1 (High) | **PASS** |
| `TC-LEAVE-011` | `RULE-005`<br>`AC-017` | Negative | Reject cancelling already approved request | Request is `APPROVED` | `PATCH /api/v1/leave-requests/{id}/cancel` | Returns `409 Conflict` (`INVALID_STATUS_TRANSITION`) | Verified (`LeaveRequestServiceTest.java`) | P1 (High) | **PASS** |

---

### 2.5. Module: Non-Functional & Compatibility (`NFR-006`..`NFR-008`, `AC-018`, `AC-019`)

| Test ID | Req ID | Type | Scenario | Preconditions | Steps | Expected Result | Actual Result | Priority | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `TC-NFR-001` | `AC-018`<br>`NFR-006` | Positive | CORS Preflight for Zalo WebView origin `https://h5.zdn.vn` | Server running | Send `OPTIONS /api/v1/health` with `Origin: https://h5.zdn.vn` | Returns `200 OK`, `Access-Control-Allow-Origin: https://h5.zdn.vn` | Verified (`HealthControllerTest.java`) | P0 (Critical) | **PASS** |
| `TC-NFR-002` | `AC-019` | Positive | Frontend build & responsive layout check | Mobile dimensions `360px - 430px` | Execute `npm run build` & inspect layout rules | Clean build, mobile shell container bounded at `430px`, touch targets $\ge 40px$ | Verified (`vite build` completed in 5.89s) | P1 (High) | **PASS** |

