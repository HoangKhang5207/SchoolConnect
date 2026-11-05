# Software Requirements Specification (SRS) — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect (Zalo Mini App — Preschool Parent & Teacher Portal) |
| **Document ID** | `DOC-01-SRS` |
| **Phase** | Phase 01 — Product Discovery & Requirement Analysis |
| **Project Owner** | Nguyen Vo Hoang Khang (`Nguyễn Võ Hoàng Khang`) |
| **Target Demo Deadline** | **2026-11-05 15:00 (GMT+7)** |
| **Last Updated** | 2026-11-04 |

---

## 2. Business Requirements (`BR-001` to `BR-005`)

| Req ID | Business Requirement Statement | Priority | Status | Source Reference |
| :--- | :--- | :--- | :--- | :--- |
| **`BR-001`** | **Centralized Preschool Parent Portal on Zalo:** Provide parents with a single Zalo Mini App to track their preschool children's attendance, daily care reports, meal menus, and school announcements without installing a standalone native mobile application. | `Must` | `Confirmed` | `SRC-001` (`P478`–`P509`), `OQ-01` |
| **`BR-002`** | **Closed-Loop Digital Leave Request Workflow:** Replace informal chat-based absence requests with an auditable, database-backed workflow where parents submit/cancel leave requests and teachers review (`Approve`/`Reject`) them directly inside the Mini App. | `Must` | `Confirmed` | `SRC-001` (`P577`–`P603`), `OQ-02` |
| **`BR-003`** | **Multi-Child Guardian Isolation:** Support parents who have multiple preschool children enrolled in different classes while strictly enforcing guardian-student data access boundaries. | `Must` | `Confirmed` | `SRC-001` (`P563`–`P573`), `OQ-06` |
| **`BR-004`** | **Zero-Blocker Live Demo & Zalo Readiness:** Ensure the application can be demonstrated live at **15:00 on 2026-11-05** without depending on enterprise Zalo App review by supporting both instant `DEMO_MODE` (with Role Switcher) and configurable `ZALO_AUTH` mode. | `Must` | `Confirmed` | `SRC-001` (`P604`–`P624`), `OQ-03`, `OQ-04` |
| **`BR-005`** | **Brand Safety, Synthetic Data & Visual Originality:** Guarantee 100% synthetic student/parent demo data and an original UI/UX design that does not imitate or copy any of the 5 reference screenshots. | `Must` | `Confirmed` | `SRC-001` (`P574`), `SRC-007` (`CF-05`) |

---

## 3. Functional Requirements Specification (`FR-001` to `FR-014`)

Each functional requirement is specified with all 13 mandatory engineering attributes.

### `FR-001` — Dual-Mode Authentication (`DEMO_MODE` & `ZALO_AUTH`)
- **Requirement ID:** `FR-001`
- **Description:** Authenticate users either via instant pre-seeded Demo Mode (`DEMO_MODE`) or via Zalo Mini App SDK Access Token verification (`ZALO_AUTH`), issuing an internal session/JWT context.
- **Actor:** Parent (`ROLE_PARENT`), Teacher (`ROLE_TEACHER`), Zalo Platform (`STK-05`).
- **Trigger:** Mini App initialization or user tapping "Đăng nhập Zalo" / "Chế độ Demo".
- **Input:**
  - `DEMO_MODE`: `{ "role": "PARENT" | "TEACHER", "parentId"?: number }`
  - `ZALO_AUTH`: `{ "zaloAccessToken": string }`
- **Processing Rules:**
  - In `DEMO_MODE`, Backend resolves the seeded Parent (`Trần Thu Hà`) or Teacher (`Cô Lê Thanh Thảo`) profile and issues a signed JWT (or structured session token) containing `userId`, `role`, and `fullName`.
  - In `ZALO_AUTH`, Backend calls `https://graph.zalo.me/v2.0/me` with `access_token` and HMAC-SHA256 `appsecret_proof`, resolves `zalo_user_id` against `parents`, and issues the internal JWT (`RULE-013`).
- **Expected Output:** `HTTP 200 OK` with `{ accessToken, role, userProfile, linkedChildren }`.
- **Validation Rules:** `role` must be `PARENT` or `TEACHER`; `zaloAccessToken` must be non-blank in `ZALO_AUTH`.
- **Error Handling:** If Zalo SDK fails or App ID is unbound, automatically fall back to `DEMO_MODE` with an informative banner; if token verification fails, return `HTTP 401 Unauthorized`.
- **Dependencies:** `DATA-001`, `DATA-003`, `INT-001`.
- **Priority:** `Must`
- **Status:** `Confirmed`
- **Source Reference:** `SRC-001` (`P604`–`P624`), `OQ-04`.
- **Acceptance Criteria:** `AC-001`, `AC-002`.

---

### `FR-002` — Interactive Demo Role Switcher (`Parent Mode` <-> `Teacher Mode`)
- **Requirement ID:** `FR-002`
- **Description:** Provide a persistent, accessible Role Switcher control in the Mini App allowing the evaluator to toggle instantaneously between **Parent Portal (`ROLE_PARENT`)** and **Teacher Review Mode (`ROLE_TEACHER`)**.
- **Actor:** Evaluator (`STK-01`), Parent (`ROLE_PARENT`), Teacher (`ROLE_TEACHER`).
- **Trigger:** User taps the Role Switcher pill/button in the top bar or profile sheet.
- **Input:** Target role selection (`PARENT` or `TEACHER`).
- **Processing Rules:** Updates active auth token/context via `/api/v1/auth/demo-session`, clears role-specific view caches, and redirects to the corresponding home screen (`01. Home Dashboard` for Parent; `Teacher Leave Review Queue` for Teacher).
- **Expected Output:** Immediate transition (`< 500ms`) to the target role interface with visual badge indicating active role.
- **Validation Rules:** Only enabled when `DEMO_MODE` is active.
- **Error Handling:** If session switch fails due to network loss, display retry toast and preserve current screen state.
- **Dependencies:** `FR-001`.
- **Priority:** `Must`
- **Status:** `Confirmed`
- **Source Reference:** `OQ-02`.
- **Acceptance Criteria:** `AC-003`.

---

### `FR-003` — Student Home Dashboard (`01. Home Dashboard`)
- **Requirement ID:** `FR-003`
- **Description:** Display a comprehensive daily overview for the currently selected child, including student profile, class & teacher info, height/weight/BMI badge, today's attendance status, today's daily report highlights, quick navigation shortcuts, and latest school announcements.
- **Actor:** Parent (`ROLE_PARENT`).
- **Trigger:** Navigating to the `Home` tab or switching the active child.
- **Input:** `studentId` (path parameter), authenticated Parent context.
- **Processing Rules:** Backend verifies `parent_students` ownership (`RULE-001`), aggregates student profile (`students`), today's attendance (`attendance_records`), today's daily report (`daily_reports`), count of pending leave requests (`leave_requests`), and top 2 recent/pinned announcements (`announcements`).
- **Expected Output:** `HTTP 200 OK` with aggregated `DashboardResponseDto`.
- **Validation Rules:** `studentId` must be a positive integer linked to the authenticated `parent_id`.
- **Error Handling:** `HTTP 403 Forbidden` if unlinked (`RULE-001`); `HTTP 404 Not Found` if student does not exist; skeleton loader during fetch; error state with retry button on network failure.
- **Dependencies:** `FR-001`, `DATA-001` through `DATA-008`.
- **Priority:** `Must`
- **Status:** `Confirmed`
- **Source Reference:** `SRC-001` (`P491`–`P492`, `T509`, `T576`), `OQ-08`.
- **Acceptance Criteria:** `AC-004`, `AC-006`.

---

### `FR-004` — Multi-Child Profile Switcher
- **Requirement ID:** `FR-004`
- **Description:** Allow a parent linked to multiple preschool children to switch the active child context across the entire Parent Portal.
- **Actor:** Parent (`ROLE_PARENT`).
- **Trigger:** Parent taps a child pill/avatar on the Home Dashboard or header (`Nguyễn Minh An` – Lớp Chồi 1 vs. `Nguyễn Minh Khôi` – Lớp Mầm 2).
- **Input:** Selected `studentId`.
- **Processing Rules:** Updates global frontend store (`activeStudentId`) and re-fetches active screen data for the selected child.
- **Expected Output:** All child-specific screens (`Dashboard`, `Daily Report`, `Attendance`, `Absence Request`) immediately display data for the newly selected child.
- **Validation Rules:** Selected `studentId` must belong to the list returned by `GET /api/v1/parent/children`.
- **Error Handling:** Defaults to the primary child (`is_primary = true`) if stored `studentId` is invalid.
- **Dependencies:** `FR-003`, `DATA-002`, `DATA-003`.
- **Priority:** `Must`
- **Status:** `Confirmed`
- **Source Reference:** `SRC-001` (`P563`–`P573`, `T576`), `OQ-06`.
- **Acceptance Criteria:** `AC-005`.

---

### `FR-005` — Student Attendance Tracking (`03. Attendance`)
- **Requirement ID:** `FR-005`
- **Description:** Display the selected child's monthly/recent attendance summary metrics and chronological attendance history log.
- **Actor:** Parent (`ROLE_PARENT`).
- **Trigger:** Parent opens `03. Attendance` screen or filters by month.
- **Input:** `studentId` (path), optional `month` (`YYYY-MM`).
- **Processing Rules:** Verifies guardian ownership (`RULE-001`), queries `attendance_records` for the student ordered by `attendance_date DESC`, and calculates summary totals (`totalPresent`, `totalExcusedAbsent`, `totalUnexcusedAbsent`, `totalLate`, `attendanceRatePercent`).
- **Expected Output:** `HTTP 200 OK` with summary counts and list of daily attendance records (`attendanceDate`, `status`, `checkInTime`, `checkOutTime`, `pickupPerson`, `note`).
- **Validation Rules:** `RULE-001`, `RULE-011`.
- **Error Handling:** Empty state rendered if no records exist for the selected month.
- **Dependencies:** `DATA-004`.
- **Priority:** `Must`
- **Status:** `Confirmed`
- **Source Reference:** `SRC-001` (`P496`–`P497`, `T509`, `T576`).
- **Acceptance Criteria:** `AC-007`.

---

### `FR-006` — Daily Activity & Care Report (`02. Daily Report`)
- **Requirement ID:** `FR-006`
- **Description:** Allow parents to view their child's daily preschool care journal covering meals, nap/sleep duration and quality, learning/play activities, mood status, and homeroom teacher comments.
- **Actor:** Parent (`ROLE_PARENT`).
- **Trigger:** Parent opens `02. Daily Report` screen or selects a specific date.
- **Input:** `studentId` (path), optional `date` (`YYYY-MM-DD`).
- **Processing Rules:** Verifies guardian ownership (`RULE-001`) and retrieves `daily_reports` for `studentId` ordered by `report_date DESC`.
- **Expected Output:** `HTTP 200 OK` with daily report details (`reportDate`, `mealStatus`, `mealComment`, `sleepStatus`, `sleepDurationMins`, `activitySummary`, `moodStatus`, `teacherNote`, `recordedBy`, `updatedAt`).
- **Validation Rules:** `RULE-001`.
- **Error Handling:** Displays friendly Empty State if no report is recorded for the chosen date (`AC-009`).
- **Dependencies:** `DATA-005`.
- **Priority:** `Must`
- **Status:** `Confirmed`
- **Source Reference:** `SRC-001` (`P493`–`P494`, `T509`, `T576`).
- **Acceptance Criteria:** `AC-008`, `AC-009`.

---

### `FR-007` — Daily & Weekly Nutritional Meal Menu (`04. Meal Menu`)
- **Requirement ID:** `FR-007`
- **Description:** Display the preschool's daily/weekly meal menu broken down by session (`BREAKFAST` – Bữa sáng, `LUNCH` – Bữa trưa, `AFTERNOON_SNACK` – Bữa xế) with dish lists, estimated calories, and nutrition/allergy notes.
- **Actor:** Parent (`ROLE_PARENT`).
- **Trigger:** Parent opens `04. Meal Menu` screen or selects a weekday tab.
- **Input:** Optional `date` (`YYYY-MM-DD`).
- **Processing Rules:** Queries `meal_menus` for the selected date/week ordered by meal session sequence (`BREAKFAST` -> `LUNCH` -> `AFTERNOON_SNACK`) (`RULE-012`).
- **Expected Output:** `HTTP 200 OK` with structured list of meal sessions, dishes, `caloriesKcal`, and `nutritionNote`.
- **Validation Rules:** Valid ISO date format when `date` filter is provided.
- **Error Handling:** Empty state when viewing a non-school day without menu records (`AC-009`).
- **Dependencies:** `DATA-006`.
- **Priority:** `Must`
- **Status:** `Confirmed`
- **Source Reference:** `SRC-001` (`P499`–`P500`, `T509`, `T576`).
- **Acceptance Criteria:** `AC-009`, `AC-010`.

---

### `FR-008` — School & Class Announcements (`05. Announcements`)
- **Requirement ID:** `FR-008`
- **Description:** Display official notifications and announcements from the preschool administration and homeroom teachers, supporting category badges, pinned priority, and detail inspection.
- **Actor:** Parent (`ROLE_PARENT`).
- **Trigger:** Parent opens `05. Announcements` screen or taps an announcement card.
- **Input:** Optional `category` (`SCHOOL`, `CLASS`, `HEALTH`, `EVENT`) or `className`.
- **Processing Rules:** Queries `announcements` matching either school-wide (`target_class IS NULL`) or the child's class, sorted by `is_pinned DESC, published_at DESC`.
- **Expected Output:** `HTTP 200 OK` with list of announcements (`id`, `title`, `summary`, `content`, `category`, `targetClass`, `isPinned`, `authorName`, `publishedAt`).
- **Validation Rules:** Category filter must match allowed enum values if provided.
- **Error Handling:** Standard empty/error state handling.
- **Dependencies:** `DATA-007`.
- **Priority:** `Must`
- **Status:** `Confirmed`
- **Source Reference:** `SRC-001` (`P502`–`P503`, `T509`, `T576`).
- **Acceptance Criteria:** `AC-011`.

---

### `FR-009` — Submit Student Leave Request & View History (`06. Absence Request`)
- **Requirement ID:** `FR-009`
- **Description:** Allow a parent to submit a new absence/leave request for their child and view the complete history and live approval status of submitted leave requests.
- **Actor:** Parent (`ROLE_PARENT`).
- **Trigger:** Parent fills and submits the Leave Request form on `06. Absence Request` (`POST /api/v1/children/{id}/leave-requests`) or views the history tab (`GET /api/v1/leave-requests`).
- **Input:**
  - `studentId` (path)
  - Body: `{ "startDate": "YYYY-MM-DD", "endDate": "YYYY-MM-DD", "sessionType": "FULL_DAY" | "MORNING" | "AFTERNOON", "reason": string }`
- **Processing Rules:**
  1. Verify guardian-student ownership (`RULE-001`).
  2. Validate date range (`today <= startDate <= endDate`, max 14 days, `RULE-002`) and reason length (`5..500` chars, `RULE-009`).
  3. Inside an `@Transactional` block, check for overlapping active (`PENDING` or `APPROVED`) leave requests for the same student and session (`RULE-003`).
  4. Generate unique `request_code` (`LR-YYYYMMDD-XXXX`, `RULE-010`).
  5. Insert into PostgreSQL `leave_requests` with `status = 'PENDING'`.
- **Expected Output:** `HTTP 201 Created` returning the persisted `LeaveRequestDto` (`id`, `requestCode`, `studentId`, `studentName`, `className`, `startDate`, `endDate`, `sessionType`, `reason`, `status`, `createdAt`).
- **Validation Rules:** `RULE-001`, `RULE-002`, `RULE-003`, `RULE-004`, `RULE-009`, `RULE-010`.
- **Error Handling:** `HTTP 400` on validation errors; `HTTP 403` on ownership violation; `HTTP 409 Conflict` on overlapping request.
- **Dependencies:** `DATA-008`, `DATA-003`.
- **Priority:** `Must`
- **Status:** `Confirmed`
- **Source Reference:** `SRC-001` (`P505`–`P506`, `P577`–`P603`, `T576`), `OQ-07`.
- **Acceptance Criteria:** `AC-012`, `AC-013`.

---

### `FR-010` — Parent Cancels Pending Leave Request
- **Requirement ID:** `FR-010`
- **Description:** Allow a parent to cancel their own child's leave request while its status is still `PENDING`.
- **Actor:** Parent (`ROLE_PARENT`).
- **Trigger:** Parent taps "Hủy đơn" on a `PENDING` leave request card (`PATCH /api/v1/leave-requests/{id}/cancel`).
- **Input:** `id` (leave request ID), authenticated Parent context.
- **Processing Rules:** Verify that the leave request belongs to a student linked to the authenticated parent (`RULE-006`) and that `status == 'PENDING'` (`RULE-005`). Update `status = 'CANCELLED'` and `updated_at = NOW()`.
- **Expected Output:** `HTTP 200 OK` with updated `LeaveRequestDto` (`status = 'CANCELLED'`).
- **Validation Rules:** `RULE-005`, `RULE-006`.
- **Error Handling:** `HTTP 403` if not owner; `HTTP 409 Conflict` (`INVALID_STATUS_TRANSITION`) if request is already `APPROVED`, `REJECTED`, or `CANCELLED`.
- **Dependencies:** `FR-009`, `DATA-008`.
- **Priority:** `Should` (Included in MVP)
- **Status:** `Confirmed`
- **Source Reference:** `SRC-001` (`T129` FR-08 adapted to SchoolConnect), `OQ-07`.
- **Acceptance Criteria:** `AC-014`, `AC-017`.

---

### `FR-011` — Teacher Leave Request Review Queue & Approval/Rejection (`Demo Teacher Mode`)
- **Requirement ID:** `FR-011`
- **Description:** Enable a Homeroom Teacher / School Admin (`ROLE_TEACHER`) to view all student leave requests, filter by status (`ALL`, `PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`), and approve or reject any `PENDING` request with a teacher note directly in the Mini App UI.
- **Actor:** Homeroom Teacher (`ROLE_TEACHER`) / Evaluator in Teacher Mode.
- **Trigger:**
  - Viewing queue: `GET /api/v1/teacher/leave-requests?status=...`
  - Reviewing request: `PATCH /api/v1/teacher/leave-requests/{id}/review`
- **Input:**
  - `id` (leave request ID)
  - Body: `{ "status": "APPROVED" | "REJECTED", "teacherNote"?: string }`
- **Processing Rules:**
  1. Enforce `ROLE_TEACHER` authorization (`RULE-007`).
  2. Verify target leave request is currently in `PENDING` state (`RULE-005`).
  3. Validate `teacherNote`: mandatory (`5..500` chars) if `status == 'REJECTED'`; optional (defaults to standard confirmation message if blank) when `status == 'APPROVED'` (`RULE-008`).
  4. Update `status`, `teacher_note`, `reviewed_by = teacher.fullName`, `reviewed_at = NOW()`, and `updated_at = NOW()` inside a database transaction.
- **Expected Output:** `HTTP 200 OK` with updated `LeaveRequestDto` including `reviewedBy`, `reviewedAt`, and `teacherNote`.
- **Validation Rules:** `RULE-005`, `RULE-007`, `RULE-008`.
- **Error Handling:** `HTTP 400` if rejecting without `teacherNote`; `HTTP 403` if called by `ROLE_PARENT`; `HTTP 409` if request is no longer `PENDING`.
- **Dependencies:** `FR-002`, `FR-009`, `DATA-008`.
- **Priority:** `Must`
- **Status:** `Confirmed`
- **Source Reference:** `OQ-02`.
- **Acceptance Criteria:** `AC-015`, `AC-016`, `AC-017`.

---

### `FR-012` — Backend Health & Diagnostics Endpoint
- **Requirement ID:** `FR-012`
- **Description:** Provide a public health-check endpoint reporting Backend API status, database connectivity, and active authentication mode (`DEMO_MODE` enabled/disabled).
- **Actor:** Evaluator (`STK-01`), Developer (`STK-04`), Deployment Probe.
- **Trigger:** `GET /api/v1/health`
- **Input:** None.
- **Processing Rules:** Checks PostgreSQL connection liveliness and returns system metadata.
- **Expected Output:** `HTTP 200 OK` with `{ status: "UP", database: "UP", demoModeEnabled: true, timestamp: "..." }`.
- **Validation Rules:** None.
- **Error Handling:** Returns `HTTP 503 Service Unavailable` if database is unreachable.
- **Dependencies:** PostgreSQL connection pool.
- **Priority:** `Must`
- **Status:** `Confirmed`
- **Source Reference:** `SRC-001` (`T151`, `P258`).
- **Acceptance Criteria:** Verified via Swagger UI / browser health check.

---

### `FR-013` — Deferred & Rejected Features (Explicit Scope Boundaries)

| Req ID | Feature Name | Priority | Status | Rationale / Source |
| :--- | :--- | :--- | :--- | :--- |
| **`FR-013`** | **Advanced Longitudinal BMI Growth Charts** | `Could` | `Deferred` | Basic Height/Weight/BMI summary badge is included in `FR-003` (`Confirmed`), while multi-month WHO percentile charting is deferred per `SRC-001` (`T509`: *Later*) and `OQ-08`. |
| **`FR-014`** | **Tuition Payment, AI Nutrition Bot, Face ID & Dept. of Education Sync** (`TitKul Mini Hub Service Booking` also rejected) | `Won't` | `Rejected` | Explicitly excluded in `SRC-001` (`T509`: *Out of scope*, `P510`) and `OQ-01` (Part 1 Service Booking rejected). |

---

## 4. Non-Functional Requirements (`NFR-001` to `NFR-012`)

| NFR ID | Category | Specification & Measurable Target | Status |
| :--- | :--- | :--- | :--- |
| **`NFR-001`** | **Performance & Response Time** | In local/same-region dev deployment, Backend REST API endpoints (`GET` and `POST`/`PATCH`) must achieve a 95th percentile (`p95`) response time of `<= 500ms` under demo load (`1–10` concurrent users). Production SLA at scale is `TBD` (out of scope for MVP test). | `Confirmed` |
| **`NFR-002`** | **Scalability** | Backend must follow a stateless **Layered Architecture** (`controller`, `service`, `repository`, `entity`, `dto`, `security`, `config`, `exception`) with HikariCP database connection pooling so instances can scale horizontally behind a load balancer in future phases. | `Confirmed` |
| **`NFR-003`** | **Availability** | Must maintain 100% availability during the scheduled evaluation window (**2026-11-05 15:00 GMT+7**), backed by a dual access strategy (`DEMO_MODE` + local/Docker fallback + recorded demo video). | `Confirmed` |
| **`NFR-004`** | **Reliability & Transactional Integrity** | All write operations (`POST` and `PATCH` on `leave_requests`) must execute within ACID database transactions (`@Transactional`) and survive Backend or Frontend restarts without data loss. | `Confirmed` |
| **`NFR-005`** | **Security & Authorization** | Zero hardcoded secrets in source code (all secrets loaded via environment variables). Enforce role-based access (`ROLE_PARENT` vs. `ROLE_TEACHER`) and strict guardian-student ownership (`RULE-001`). Sanitize all text inputs against SQL Injection (via JPA parameterized queries) and XSS. | `Confirmed` |
| **`NFR-006`** | **Compatibility & Zalo CORS** | Frontend must render cleanly on mobile viewports (`360px`–`430px`) inside the Zalo WebView container and modern mobile browsers. Backend must support HTTPS and CORS for `https://h5.zdn.vn` and local dev origins, including `OPTIONS` preflight requests (`AC-018`). | `Confirmed` |
| **`NFR-007`** | **Usability & UX States** | Every data-driven screen must implement 4 explicit states: **Loading State** (skeleton/spinner), **Success State**, **Empty State** (friendly message when list is empty), and **Error State** (actionable retry button when API fails). | `Confirmed` |
| **`NFR-008`** | **Accessibility** | Adhere to mobile readability standards: minimum touch target size of `40x40px`, high-contrast status badges (`PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`), and clear Vietnamese labels for all form controls. | `Confirmed` |
| **`NFR-009`** | **Privacy & Synthetic Data** | 100% of seeded student, parent, and health records must be synthetic (`RULE-014`). Sensitive tokens must never be printed in application logs. | `Confirmed` |
| **`NFR-010`** | **Maintainability & Documentation** | Codebase must be organized in a clean Monorepo (`frontend/`, `backend/`, `database/`, `infra/`, `docs/`), documented with **Springdoc OpenAPI (Swagger UI)** and a comprehensive `README.md`. All source code, identifiers, database schemas, and comments must be written in English. | `Confirmed` |
| **`NFR-011`** | **Backup, Recovery & Seed Reproducibility** | Database schema migrations and realistic synthetic seed data must run automatically and idempotently on startup (via Flyway/SQL init scripts and `docker-compose`), allowing full environment recovery in `< 60 seconds`. | `Confirmed` |
| **`NFR-012`** | **Observability** | Backend must output structured application logs (including timestamp, log level, request correlation/path, HTTP status, and error stack traces when exceptions occur) and expose `/api/v1/health`. | `Confirmed` |

---

## 5. Integration, Reporting & Operational Requirements

| Req ID | Category | Specification | Status |
| :--- | :--- | :--- | :--- |
| **`INT-001`** | **Zalo Mini App SDK & Open API Integration** | Integrate `zmp-sdk` on the Frontend (`getAccessToken`, user info fallback) and Zalo Graph API (`https://graph.zalo.me/v2.0/me` with `appsecret_proof`) on the Backend, while gracefully falling back to `DEMO_MODE` when running outside Zalo or without a corporate App ID. | `Confirmed` |
| **`INT-002`** | **OpenAPI / Swagger UI Integration** | Expose interactive API documentation at `/swagger-ui.html` and `/v3/api-docs` so the TitKul Director can inspect and test all Parent and Teacher endpoints directly during the demo. | `Confirmed` |
| **`REP-001`** | **Attendance & Leave Summary Metrics** | Provide real-time summary counters on the Attendance screen (Present days, Excused Absent days, Late days, Attendance percentage) and status filter counts on the Teacher Leave Review screen (`Pending`, `Approved`, `Rejected`). | `Confirmed` |
| **`OPS-001`** | **Containerized One-Command Startup** | Provide `infra/docker-compose.yml` and clear scripts to launch PostgreSQL and the Spring Boot Backend reproducibly, ready for local testing, Cloud PaaS deployment, or HTTPS Tunneling (`ngrok` / Cloudflare Tunnel) for Zalo Testing before **15:00 on 2026-11-05**. | `Confirmed` |

---

## 6. Gap Analysis & Clarification Register

### 6.1. Missing-Information Register (Resolved vs. Remaining)
- All 5 critical gaps identified in Phase 00 (`OQ-01` through `OQ-05`) have been **resolved and confirmed** by the Project Owner.
- **Note on `OQ-04` (Personal Zalo Developer Account vs. Enterprise App ID):**
  - *Technical Clarification for the Project Owner:* On the Zalo Mini App platform, **a personal Zalo account registered as a Zalo Developer CAN create a Mini App ID for `Development` and `Testing` environments** without needing Enterprise/Business verification! Enterprise verification is only required when requesting restricted permissions (such as getting a user's real phone number via `getPhoneNumber`) or publishing to `Live` for the general public. With our **Dual Auth (`DEMO_MODE` + `ZALO_AUTH`)** design, the app will work 100% standalone immediately in `DEMO_MODE` AND you can also bind a personal Mini App ID to generate a real Zalo Testing QR Code in minutes.

### 6.2. Ambiguity & Conflict Register
- **`CNF-01` (Part 1 Service Booking vs. Part 2 SchoolConnect):** **Resolved (`Rejected` Part 1 / `Confirmed` Part 2)**.
- **`CNF-02` (Parent Role vs. Teacher Role):** **Resolved (`Confirmed` Parent 6 screens + Demo Teacher Approval Mode)**.
- **`CNF-03` (Timeline):** **Resolved (`Confirmed` deadline: 2026-11-05 15:00 GMT+7)**.

### 6.3. Stakeholder Clarification Questions Classified by Severity

| Question ID | Severity | Question | Proposed Default (Will Be Used If Approved) |
| :--- | :--- | :--- | :--- |
| *None* | **`BLOCKING`** | **0 Blocking Questions Remain.** All architectural, domain, role, and timeline blockers have been resolved by your answers to `OQ-01`–`OQ-05`. | N/A |
| **`SCQ-01`** | **`NON-BLOCKING`** | **Demo School & Persona Naming:** Are you happy with our synthetic school name **"Trường Mầm Non Sao Mai"** and the seeded personas (**Parent:** *Chị Trần Thu Hà*, **Children:** *Bé Nguyễn Minh An (Lớp Chồi 1)* & *Bé Nguyễn Minh Khôi (Lớp Mầm 2)*, **Teacher:** *Cô Lê Thanh Thảo*)? | Use these realistic Vietnamese synthetic personas in the seed data. |

---

## 7. Requirement Traceability Matrix (RTM)

`Source -> Business Requirement -> Functional Requirement -> User Story -> Acceptance Criteria -> Test Case`

| Source Ref | Business Req | Functional Req & Rules | User Story Summary | Acceptance Criteria | Target Verification / Test Case |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `SRC-001` (`P604`–`P624`), `OQ-04` | `BR-004` | `FR-001`, `INT-001`, `RULE-013` | As a Parent or Evaluator, I want to log in seamlessly via `DEMO_MODE` or `ZALO_AUTH` so I can access the portal without blockers. | `AC-001`, `AC-002` | `TC-AUTH-01`: Boot app in `DEMO_MODE` -> verify JWT & Parent session.<br>`TC-AUTH-02`: Unit/Integration test `ZALO_AUTH` token verification & fallback. |
| `OQ-02` | `BR-002`, `BR-004` | `FR-002`, `RULE-013` | As an Evaluator, I want a one-tap Role Switcher between Parent and Teacher modes so I can test the full workflow in one Mini App. | `AC-003` | `TC-ROLE-01`: Tap Role Switcher -> verify context changes from `ROLE_PARENT` to `ROLE_TEACHER` and Teacher Queue loads. |
| `SRC-001` (`P491`–`P492`), `OQ-06`, `OQ-08` | `BR-001`, `BR-003` | `FR-003`, `FR-004`, `RULE-001` | As a Parent with 2 children, I want to view each child's dashboard and switch between my children securely. | `AC-004`, `AC-005`, `AC-006` | `TC-DASH-01`: Verify Dashboard fields for `studentId=1`.<br>`TC-DASH-02`: Switch to `studentId=2` and verify data isolation.<br>`TC-SEC-01`: Request unlinked `studentId=999` -> assert `HTTP 403`. |
| `SRC-001` (`P496`–`P497`, `T576`) | `BR-001` | `FR-005`, `RULE-011`, `REP-001` | As a Parent, I want to view my child's attendance calendar and monthly summary stats. | `AC-007` | `TC-ATT-01`: Call `GET /api/v1/children/1/attendance` -> verify summary counts and chronological check-in/out list. |
| `SRC-001` (`P493`–`P494`, `T576`) | `BR-001` | `FR-006`, `DATA-005` | As a Parent, I want to read my child's daily report on meals, sleep, activities, and mood. | `AC-008`, `AC-009` | `TC-REP-01`: View Daily Report for seeded date.<br>`TC-REP-02`: Select empty date -> verify Empty State UI. |
| `SRC-001` (`P499`–`P500`, `T576`) | `BR-001` | `FR-007`, `RULE-012`, `DATA-006` | As a Parent, I want to see the daily/weekly meal menu (Breakfast, Lunch, Snack) and nutrition notes. | `AC-009`, `AC-010` | `TC-MEAL-01`: Call `GET /api/v1/meal-menus` -> verify 3 meal sessions and kcal display. |
| `SRC-001` (`P502`–`P503`, `T576`) | `BR-001` | `FR-008`, `DATA-007` | As a Parent, I want to view pinned and categorized school/class announcements. | `AC-011` | `TC-ANN-01`: Call `GET /api/v1/announcements` -> verify pinned ordering and detail modal. |
| `SRC-001` (`P505`–`P603`), `OQ-07` | `BR-002` | `FR-009`, `FR-010`, `RULE-002` to `RULE-006`, `RULE-009`, `RULE-010` | As a Parent, I want to submit a leave request for my child, see it saved in PostgreSQL, and cancel it if still pending. | `AC-012`, `AC-013`, `AC-014`, `AC-017` | `TC-LEAVE-01`: Submit valid leave request -> assert `201 Created`, reload app, verify persistence.<br>`TC-LEAVE-02`: Submit overlapping dates -> assert `409 Conflict`.<br>`TC-LEAVE-03`: Cancel `PENDING` request -> assert `CANCELLED`. |
| `OQ-02`, `OQ-07` | `BR-002` | `FR-011`, `RULE-005`, `RULE-007`, `RULE-008` | As a Homeroom Teacher, I want to view pending leave requests and approve or reject them with a note so parents receive confirmation. | `AC-015`, `AC-016`, `AC-017` | `TC-TEACH-01`: Approve `PENDING` request in Teacher Mode -> switch to Parent Mode -> assert `APPROVED` badge & teacher note.<br>`TC-TEACH-02`: Reject without note -> assert `400 Bad Request`. |

---

## 8. Proposed MVP Scope Summary (For 2026-11-05 15:00 Demo)

1. **Parent Portal (6 Core Screens — `Confirmed`):**
   - `01. Home Dashboard` (with Multi-Child Switcher, Health/BMI badge, Today's Attendance & Daily Report snapshot, Recent Announcements).
   - `02. Daily Report` (Meals, Sleep, Activities, Mood, Teacher note).
   - `03. Attendance` (Summary metrics + Daily check-in/out history).
   - `04. Meal Menu` (Breakfast, Lunch, Afternoon Snack + kcal & nutrition notes).
   - `05. Announcements` (Pinned notices, category filtering, detail view).
   - `06. Absence Request` (Submit new leave request with validation, cancel pending request, view status & teacher response).
2. **Teacher / School Demo Mode (1 Dedicated Review Screen + Role Switcher — `Confirmed` per `OQ-02`):**
   - Instant Role Switcher Pill (`Phụ huynh` <-> `Giáo viên duyệt đơn`).
   - Teacher Leave Request Review Queue (`All`, `Pending`, `Approved`, `Rejected`) with one-tap **Approve (`Duyệt đơn`)** / **Reject (`Từ chối`)** + Teacher Note modal.
3. **Full-Stack Engineering & DevOps Package (`Confirmed` per `OQ-04`, `OQ-05`):**
   - **Frontend:** React + TypeScript + ZaUI + Zalo Mini App SDK + Custom Original Design System.
   - **Backend:** Java 21 + Spring Boot 3.x Layered Architecture (`controller`, `service`, `repository`, `entity`, `dto`, `security`, `config`, `exception`) + Spring Data JPA + Jakarta Validation + Spring Security (JWT + Dual Auth `DEMO_MODE` / `ZALO_AUTH` + Zalo CORS) + Springdoc OpenAPI (Swagger UI) + Unit/Integration Tests.
   - **Database & Infra:** PostgreSQL (8 normalized tables + automatic synthetic seed data) packaged with `docker-compose.yml`.

