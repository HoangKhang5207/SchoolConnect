# Product Backlog — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect |
| **Document ID** | `DOC-02-BACKLOG` |
| **Phase** | Phase 04 — Implementation Planning & Engineering Breakdown (Updated) |
| **Backend Architecture** | **Layered Architecture** (`controller` -> `service` -> `repository` -> `entity` / `dto`) |
| **Last Updated** | 2026-11-04 |

---

## 2. Backlog Hierarchy Overview (`Epic -> Feature -> User Story -> Task`)

| Epic ID | Epic Name | Features | User Stories | Priority |
| :--- | :--- | :--- | :--- | :--- |
| **`EPIC-001`** | **Platform Foundation, Layered Architecture & Dual Authentication** | `FEAT-001`, `FEAT-002` | `US-001`, `US-002`, `US-003` | `Must Have` |
| **`EPIC-002`** | **Parent Home Dashboard & Multi-Child Profile Management** | `FEAT-003`, `FEAT-004` | `US-004`, `US-005` | `Must Have` |
| **`EPIC-003`** | **Preschool Daily Care Tracking (Attendance & Daily Report)** | `FEAT-005`, `FEAT-006` | `US-006`, `US-007` | `Must Have` |
| **`EPIC-004`** | **School Nutrition & Communication (Meal Menu & Announcements)** | `FEAT-007`, `FEAT-008` | `US-008`, `US-009` | `Must Have` |
| **`EPIC-005`** | **Closed-Loop Leave Request Workflow (Parent Submit/Cancel & Teacher Review)** | `FEAT-009`, `FEAT-010` | `US-010`, `US-011`, `US-012` | `Must Have` / `Should Have` |

---

## 3. Detailed Epics, Features, User Stories & Tasks

### `EPIC-001` — Platform Foundation, Layered Architecture & Dual Authentication

#### `FEAT-001`: Monorepo, PostgreSQL Schema & Layered Spring Boot Foundation
##### `US-001`: Database Schema, Seed Data & Health Check API
- **Story Statement:**
  - **As a** Technical Evaluator / System Administrator,
  - **I want** the Backend and PostgreSQL database to initialize automatically with all 8 preschool domain tables, realistic synthetic seed data, Swagger UI, and a health-check endpoint,
  - **So that** the system can be started reproducibly and verified via API and database inspection.
- **Description:** Set up the Spring Boot 3.x (Java 21) project using **Layered Architecture** (`controller`, `service`, `repository`, `entity`, `dto`, `config`, `security`, `exception`), configure PostgreSQL schema & synthetic seed scripts, expose `/api/v1/health` and `/swagger-ui.html`, and configure Zalo CORS (`https://h5.zdn.vn`).
- **Acceptance Criteria:** `AC-018` + `GET /api/v1/health` returns `HTTP 200 OK` with `database: "UP"`.
- **Priority:** `Must Have`
- **Estimate:** `M` (`1.5 – 2.0 hours`)
- **Dependencies:** Local Java 21, PostgreSQL 17 / Docker.
- **Technical Considerations:**
  - Strict **Layered Architecture** package layout (`com.schoolconnect.controller`, `com.schoolconnect.service`, `com.schoolconnect.repository`, `com.schoolconnect.entity`, `com.schoolconnect.dto`, `com.schoolconnect.security`, `com.schoolconnect.config`, `com.schoolconnect.exception`).
  - Standardized `ApiResponse<T>` wrapper and `@RestControllerAdvice` global exception handler.
- **Definition of Done (DoD):** All 8 JPA entities mapped; schema + seed SQL executed cleanly; Swagger UI reachable; CORS `OPTIONS` preflight test passing.
- **Decomposed Tasks:**
  - `TASK-001`: Initialize Spring Boot 3.x Gradle project (`build.gradle`, Gradle Wrapper) with Layered Architecture packages.
  - `TASK-002`: Create SQL migration/init script (`schema.sql` & `seed.sql`) for all 8 tables (`parents`, `students`, `parent_students`, `attendance_records`, `daily_reports`, `meal_menus`, `announcements`, `leave_requests`).
  - `TASK-003`: Implement JPA Entities and Spring Data JPA Repositories in `entity/` and `repository/` layers.
  - `TASK-004`: Implement `HealthController`, `GlobalExceptionHandler`, `CorsConfig` (`https://h5.zdn.vn`), and OpenAPI config.

---

#### `FEAT-002`: Dual-Mode Authentication (`DEMO_MODE` & `ZALO_AUTH`) & Role Switcher
##### `US-002`: Dual Authentication Service (`DEMO_MODE` & `ZALO_AUTH`)
- **Story Statement:**
  - **As a** Parent or Evaluator opening the Mini App,
  - **I want** to authenticate seamlessly via instant `DEMO_MODE` (or via Zalo SDK Access Token in `ZALO_AUTH` mode),
  - **So that** I can immediately access the application without being blocked by enterprise Zalo account approvals.
- **Description:** Implement `POST /api/v1/auth/demo-session` and `POST /api/v1/auth/zalo-login` in the Backend `AuthController` -> `AuthService`, issuing signed JWT tokens, and integrate the Frontend auth bootstrap with automatic `DEMO_MODE` fallback.
- **Acceptance Criteria:** `AC-001`, `AC-002`.
- **Priority:** `Must Have`
- **Estimate:** `M` (`1.5 – 2.0 hours`)
- **Dependencies:** `US-001`.
- **Technical Considerations:**
  - Use HMAC-SHA256 `appsecret_proof` calculation when calling `https://graph.zalo.me/v2.0/me` in `ZALO_AUTH` mode (`RULE-013`).
  - Load JWT secret and Zalo App Secret strictly from environment variables (`NFR-005`).
- **Definition of Done (DoD):** JWT authentication filter active; unit tests for `AuthService` passing; Frontend boots into authenticated session in `< 2s`.
- **Decomposed Tasks:**
  - `TASK-005`: Implement `JwtTokenProvider`, `JwtAuthenticationFilter`, and `SecurityConfig` in `security/` layer.
  - `TASK-006`: Implement `AuthService` and `AuthController` supporting `/api/v1/auth/demo-session` and `/api/v1/auth/zalo-login`.
  - `TASK-007`: Initialize React + TypeScript + ZaUI Frontend project (`frontend/`) with original SchoolConnect CSS Design System (`TE-05`).
  - `TASK-008`: Implement Frontend API client (`axios`/`fetch` interceptor) and Auth Store with `DEMO_MODE` & `ZALO_AUTH` support.

##### `US-003`: Interactive Role Switcher (`Parent Mode` <-> `Teacher Mode`)
- **Story Statement:**
  - **As a** Technical Evaluator (`EVALUATOR`),
  - **I want** a one-tap Role Switcher in the Mini App header to toggle between **Parent Mode (`ROLE_PARENT`)** and **Teacher Mode (`ROLE_TEACHER`)**,
  - **So that** I can test both submitting a leave request as a Parent and approving/rejecting it as a Teacher during the live demo.
- **Description:** Build the persistent Role Switcher UI bar/pill in the Frontend that requests a new demo token for the target role (`PARENT` or `TEACHER`) and switches the active navigation layout.
- **Acceptance Criteria:** `AC-003`.
- **Priority:** `Must Have`
- **Estimate:** `S` (`0.5 – 1.0 hour`)
- **Dependencies:** `US-002`.
- **Technical Considerations:** Clear state cache on role switch so fresh PostgreSQL data is always fetched.
- **Definition of Done (DoD):** Tapping Role Switcher transitions between Parent Dashboard and Teacher Leave Review Queue in `< 500ms`.
- **Decomposed Tasks:**
  - `TASK-009`: Create `RoleSwitcherBar` component in `frontend/src/components/` showing active persona and one-tap switch button.
  - `TASK-010`: Wire role switching state transitions and bottom navigation adaptation for `ROLE_PARENT` vs `ROLE_TEACHER`.

---

### `EPIC-002` — Parent Home Dashboard & Multi-Child Profile Management

#### `FEAT-003`: Multi-Child Selector & Guardian Isolation
##### `US-004`: Switch Between Linked Preschool Children
- **Story Statement:**
  - **As a** Parent with multiple children in preschool,
  - **I want** to view my list of linked children (`Nguyễn Minh An` – Lớp Chồi 1 and `Nguyễn Minh Khôi` – Lớp Mầm 2) and switch between them with one tap,
  - **So that** I can monitor each child's specific attendance, daily report, and leave requests securely.
- **Description:** Implement `GET /api/v1/parent/children` and enforce `RULE-001` (Guardian-Student ownership check) in `StudentService` across all `/api/v1/children/{id}/...` endpoints.
- **Acceptance Criteria:** `AC-005`, `AC-006`.
- **Priority:** `Must Have`
- **Estimate:** `S` (`1.0 hour`)
- **Dependencies:** `US-001`, `US-002`.
- **Technical Considerations:** Centralized helper method `guardianAccessService.verifyParentOwnsStudent(parentId, studentId)` in the `service/` layer throwing `AccessDeniedException` (`HTTP 403`) on mismatch.
- **Definition of Done (DoD):** Endpoint returns linked children; switching child in UI updates all child-bound screens; unauthorized `studentId` returns `403 Forbidden`.
- **Decomposed Tasks:**
  - `TASK-011`: Implement `GuardianAccessService` and `ParentController` (`GET /api/v1/parent/children`).
  - `TASK-012`: Build `ChildSwitcher` component in Frontend and bind to global selected `studentId` state.

#### `FEAT-004`: Student Home Dashboard (`01. Home Dashboard`)
##### `US-005`: View Aggregated Student Home Dashboard & BMI Summary
- **Story Statement:**
  - **As a** Parent,
  - **I want** to see my selected child's profile, class, homeroom teacher, height/weight/BMI badge, today's attendance status, today's daily report highlights, and latest announcements on the Home Dashboard,
  - **So that** I can grasp my child's status at school within seconds of opening the Mini App.
- **Description:** Implement `GET /api/v1/children/{id}/dashboard` in `StudentController` -> `StudentService` and build the `01. Home Dashboard` page in the Frontend.
- **Acceptance Criteria:** `AC-004`, `AC-019`.
- **Priority:** `Must Have` (BMI badge: `Could Have` included)
- **Estimate:** `M` (`1.5 hours`)
- **Dependencies:** `US-004`.
- **Technical Considerations:** Compute BMI (`weight_kg / ((height_cm / 100) ^ 2)`) rounded to 1 decimal place in `StudentService` / DTO mapper.
- **Definition of Done (DoD):** Dashboard renders all profile, attendance, daily report, and announcement cards with Loading/Error states and zero horizontal overflow on `360px` viewport.
- **Decomposed Tasks:**
  - `TASK-013`: Implement `DashboardResponseDto`, `StudentService.getStudentDashboard()`, and `StudentController`.
  - `TASK-014`: Build `HomeDashboardPage` UI with student card, BMI badge, quick feature navigation, today's status snapshot, and recent announcements.

---

### `EPIC-003` — Preschool Daily Care Tracking (Attendance & Daily Report)

#### `FEAT-005`: Student Attendance History & Summary (`03. Attendance`)
##### `US-006`: View Student Attendance History & Monthly Metrics
- **Story Statement:**
  - **As a** Parent,
  - **I want** to view my child's attendance summary (Present, Excused Absent, Unexcused Absent, Late, Attendance %) and daily check-in/check-out records,
  - **So that** I can verify when my child arrived at and left school each day.
- **Description:** Implement `GET /api/v1/children/{id}/attendance` in `AttendanceController` -> `AttendanceService` -> `AttendanceRepository` and build the `03. Attendance` screen.
- **Acceptance Criteria:** `AC-007`.
- **Priority:** `Must Have`
- **Estimate:** `M` (`1.0 – 1.5 hours`)
- **Dependencies:** `US-004`.
- **Technical Considerations:** Enforce `RULE-001` ownership check and `RULE-011` status taxonomy.
- **Definition of Done (DoD):** API returns summary counters and chronological list; UI renders badges for `PRESENT`, `ABSENT_EXCUSED`, `LATE`, check-in/out times, and pickup person.
- **Decomposed Tasks:**
  - `TASK-015`: Implement `AttendanceService` and `AttendanceController` (`GET /api/v1/children/{id}/attendance`).
  - `TASK-016`: Build `AttendancePage` UI with summary metric cards and daily check-in/out timeline list.

#### `FEAT-006`: Student Daily Activity & Care Report (`02. Daily Report`)
##### `US-007`: View Student Daily Care Journal (Meals, Sleep, Activities, Mood)
- **Story Statement:**
  - **As a** Parent,
  - **I want** to read my child's daily care report covering meal appetite, nap duration/quality, learning & play activities, mood, and teacher's note by date,
  - **So that** I know how my child ate, slept, and participated in class today.
- **Description:** Implement `GET /api/v1/children/{id}/daily-reports` in `DailyReportController` -> `DailyReportService` -> `DailyReportRepository` and build the `02. Daily Report` screen with date selector.
- **Acceptance Criteria:** `AC-008`, `AC-009`.
- **Priority:** `Must Have`
- **Estimate:** `M` (`1.0 – 1.5 hours`)
- **Dependencies:** `US-004`.
- **Technical Considerations:** Handle date filtering (`?date=YYYY-MM-DD`) and return full history list so parents can browse recent days or select a specific date; render clean Empty State when selecting a weekend/unrecorded date.
- **Definition of Done (DoD):** Daily Report screen displays all 4 care dimensions + teacher note, and passes Empty State check (`AC-009`).
- **Decomposed Tasks:**
  - `TASK-017`: Implement `DailyReportService` and `DailyReportController` (`GET /api/v1/children/{id}/daily-reports`).
  - `TASK-018`: Build `DailyReportPage` UI with date selector pills, 4 structured care cards, and Empty State illustration.

---

### `EPIC-004` — School Nutrition & Communication (Meal Menu & Announcements)

#### `FEAT-007`: Daily & Weekly Preschool Meal Menu (`04. Meal Menu`)
##### `US-008`: View Preschool Nutritional Meal Menu by Date
- **Story Statement:**
  - **As a** Parent,
  - **I want** to view the preschool's daily and weekly meal menu broken down into Breakfast (*Bữa sáng*), Lunch (*Bữa trưa*), and Afternoon Snack (*Bữa xế*) with dish names, calories, and nutrition notes,
  - **So that** I can track my child's nutrition at school and plan appropriate dinners at home.
- **Description:** Implement `GET /api/v1/meal-menus` in `MealMenuController` -> `MealMenuService` -> `MealMenuRepository` and build the `04. Meal Menu` screen.
- **Acceptance Criteria:** `AC-009`, `AC-010`.
- **Priority:** `Must Have`
- **Estimate:** `S` (`1.0 hour`)
- **Dependencies:** `US-001`.
- **Technical Considerations:** Store dish list cleanly (JSON array or structured text parsed into DTO list) and order sessions `BREAKFAST` -> `LUNCH` -> `AFTERNOON_SNACK` (`RULE-012`).
- **Definition of Done (DoD):** Meal Menu screen allows switching weekdays, displays 3 meal cards with kcal and nutrition notes, and handles empty weekend dates gracefully.
- **Decomposed Tasks:**
  - `TASK-019`: Implement `MealMenuService` and `MealMenuController` (`GET /api/v1/meal-menus`).
  - `TASK-020`: Build `MealMenuPage` UI with weekday selector bar and session breakdown cards.

#### `FEAT-008`: School & Class Announcements (`05. Announcements`)
##### `US-009`: Browse Pinned & Categorized School Announcements
- **Story Statement:**
  - **As a** Parent,
  - **I want** to browse official announcements from the school and my child's class, see pinned notices at the top, filter by category, and read full announcement details,
  - **So that** I never miss important school events, health notices, or schedules.
- **Description:** Implement `GET /api/v1/announcements` and `GET /api/v1/announcements/{id}` in `AnnouncementController` -> `AnnouncementService` -> `AnnouncementRepository` and build the `05. Announcements` screen.
- **Acceptance Criteria:** `AC-011`.
- **Priority:** `Must Have`
- **Estimate:** `S` (`1.0 hour`)
- **Dependencies:** `US-001`.
- **Technical Considerations:** Sort by `is_pinned DESC, published_at DESC`. Support optional category filter (`SCHOOL`, `CLASS`, `HEALTH`, `EVENT`).
- **Definition of Done (DoD):** Announcements screen renders pinned badges, category filters, and expandable/modal full content view.
- **Decomposed Tasks:**
  - `TASK-021`: Implement `AnnouncementService` and `AnnouncementController` (`GET /api/v1/announcements`).
  - `TASK-022`: Build `AnnouncementsPage` UI with category filter pills, pinned highlight cards, and detail view modal.

---

### `EPIC-005` — Closed-Loop Leave Request Workflow (Parent Submit/Cancel & Teacher Review)

#### `FEAT-009`: Parent Leave Request Creation, History & Cancellation (`06. Absence Request`)
##### `US-010`: Submit New Leave Request & View Leave History
- **Story Statement:**
  - **As a** Parent,
  - **I want** to submit an absence request for my child (specifying start date, end date, session type, and reason) and see it immediately saved in my Leave Request History with a unique code (`LR-YYYYMMDD-XXXX`),
  - **So that** my child's homeroom teacher is formally notified and I can track the approval status.
- **Description:** Implement `POST /api/v1/children/{id}/leave-requests` and `GET /api/v1/leave-requests` in `LeaveRequestController` -> `LeaveRequestService` -> `LeaveRequestRepository`, enforcing `RULE-001` through `RULE-010`, and build the `06. Absence Request` screen.
- **Acceptance Criteria:** `AC-012`, `AC-013`.
- **Priority:** `Must Have`
- **Estimate:** `L` (`2.0 hours`)
- **Dependencies:** `US-004`.
- **Technical Considerations:**
  - Execute creation inside `@Transactional`.
  - Validate date range (`today <= startDate <= endDate`, `<= 14` days) and overlapping active requests (`PENDING` or `APPROVED`) on matching/intersecting sessions (`RULE-003`).
  - Disable submit button during API flight (`RULE-004`).
- **Definition of Done (DoD):** Submitting valid form returns `201 Created`, persists in PostgreSQL, survives page reload, and blocks invalid/overlapping requests with `400`/`409`.
- **Decomposed Tasks:**
  - `TASK-023`: Implement `LeaveRequestService.createLeaveRequest()` and `getLeaveRequestsForParent()` with full validation and overlap detection.
  - `TASK-024`: Implement `LeaveRequestController` (`POST /api/v1/children/{id}/leave-requests`, `GET /api/v1/leave-requests`).
  - `TASK-025`: Build `AbsenceRequestPage` UI with validated creation form, session selector (`Cả ngày`, `Buổi sáng`, `Buổi chiều`), and status history cards.

##### `US-011`: Cancel Pending Leave Request (Parent)
- **Story Statement:**
  - **As a** Parent,
  - **I want** to cancel a leave request that I previously submitted while it is still in `PENDING` status,
  - **So that** I can withdraw the absence notice if my child recovers and attends school as normal.
- **Description:** Implement `PATCH /api/v1/leave-requests/{id}/cancel` in `LeaveRequestController` -> `LeaveRequestService` and add the "Hủy đơn" action button on `PENDING` cards in the Parent UI.
- **Acceptance Criteria:** `AC-014`, `AC-017`.
- **Priority:** `Should Have` (Included in MVP)
- **Estimate:** `S` (`0.5 – 1.0 hour`)
- **Dependencies:** `US-010`.
- **Technical Considerations:** Enforce `RULE-005` and `RULE-006` (only `PENDING` requests owned by the authenticated parent can transition to `CANCELLED`).
- **Definition of Done (DoD):** Cancelling a `PENDING` request updates status to `CANCELLED` in PostgreSQL; attempting to cancel an `APPROVED`/`REJECTED` request returns `409 Conflict`.
- **Decomposed Tasks:**
  - `TASK-026`: Implement `LeaveRequestService.cancelLeaveRequest()` and `PATCH /api/v1/leave-requests/{id}/cancel`.
  - `TASK-027`: Add cancel confirmation action to `PENDING` leave request cards in `AbsenceRequestPage`.

---

#### `FEAT-010`: Teacher Leave Request Review Queue & Approval/Rejection (`Demo Teacher Mode`)
##### `US-012`: Teacher Reviews (Approves / Rejects) Student Leave Requests
- **Story Statement:**
  - **As a** Homeroom Teacher (`ROLE_TEACHER` in Demo Teacher Mode),
  - **I want** to view all student leave requests filtered by status and approve (`APPROVED`) or reject (`REJECTED`) any `PENDING` request with a teacher note,
  - **So that** parents receive official confirmation and the school's attendance records stay accurate.
- **Description:** Implement `GET /api/v1/teacher/leave-requests` and `PATCH /api/v1/teacher/leave-requests/{id}/review` in `TeacherLeaveRequestController` -> `LeaveRequestService`, enforcing `RULE-005`, `RULE-007`, and `RULE-008`, and build the **Teacher Leave Review Dashboard** screen in the Mini App.
- **Acceptance Criteria:** `AC-015`, `AC-016`, `AC-017`.
- **Priority:** `Must Have` (`OQ-02`)
- **Estimate:** `M` (`1.5 – 2.0 hours`)
- **Dependencies:** `US-003`, `US-010`.
- **Technical Considerations:**
  - Require `ROLE_TEACHER` authority in Spring Security (`@PreAuthorize("hasRole('TEACHER')")` or URL matcher).
  - Enforce mandatory `teacherNote` (`5..500` chars) when `status == 'REJECTED'`, and default polite confirmation note when `status == 'APPROVED'` and note is blank (`RULE-008`).
- **Definition of Done (DoD):** Teacher can filter requests, approve/reject with note in UI, and Parent Mode immediately reflects the updated status, reviewer name (`Cô Lê Thanh Thảo`), timestamp, and teacher note.
- **Decomposed Tasks:**
  - `TASK-028`: Implement `LeaveRequestService.getAllLeaveRequestsForTeacher()` and `reviewLeaveRequest()` with state machine and `RULE-008` validation.
  - `TASK-029`: Implement `TeacherLeaveRequestController` (`GET /api/v1/teacher/leave-requests`, `PATCH /api/v1/teacher/leave-requests/{id}/review`).
  - `TASK-030`: Build `TeacherReviewPage` UI with status filter tabs (`Chờ duyệt`, `Đã duyệt`, `Từ chối`, `Tất cả`), student/parent info cards, and Approve/Reject review modal.

---

### Cross-Cutting Quality, Packaging & Documentation Tasks (`TASK-031` to `TASK-036`)
- **`TASK-031` (`[QA]`):** Write Backend Unit & Integration Tests (`JUnit 5 + Mockito / SpringBootTest`) covering `LeaveRequestService` (creation, overlap `409`, guardian ownership `403`, cancel `PENDING`, teacher approve/reject `RULE-008`) and Zalo CORS preflight (`AC-018`).
- **`TASK-032` (`[QA]`):** Write Frontend component/build verification and verify zero TypeScript compilation or bundler errors (`npm run build`).
- **`TASK-033` (`[INFRA]`):** Create `infra/docker-compose.yml`, `.env.example`, and Backend `Dockerfile` for one-command deployment of PostgreSQL + Spring Boot Backend.
- **`TASK-034` (`[INT]` / `[AUTO]`):** Configure Zalo Mini App manifest (`app-config.json`) and create automated API/E2E verification script (`scripts/verify-e2e.ps1`).
- **`TASK-035` (`[DOC]`):** Write comprehensive root `README.md` (15 standard sections) and `docs/demo-script.md` (10-minute Director Demo Script).
- **`TASK-036` (`[QA]`):** Execute full End-to-End Acceptance Verification (`AC-001` through `AC-019`) against live PostgreSQL instance and update `PROJECT_STATE.md`.

---

## 4. Master Engineering Task Tracking Matrix (`TASK-001` to `TASK-036`)

| Task ID | Discipline | Story / Epic | Milestone | Priority | Estimate | Upstream Dependencies | Current Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `TASK-001` | `[BE]` / `[INFRA]` | `US-001` | `EM-01` | `Must` | `30m` | None (Root Blocker) | `Ready for Implementation` |
| `TASK-002` | `[DB]` | `US-001` | `EM-01` | `Must` | `45m` | `TASK-001` | `Ready for Implementation` |
| `TASK-003` | `[DB]` / `[BE]` | `US-001` | `EM-01` | `Must` | `45m` | `TASK-002` | `Ready for Implementation` |
| `TASK-004` | `[BE]` | `US-001` | `EM-01` | `Must` | `40m` | `TASK-003` | `Ready for Implementation` |
| `TASK-005` | `[BE]` | `US-002` | `EM-01` | `Must` | `40m` | `TASK-004` | `Ready for Implementation` |
| `TASK-006` | `[BE]` / `[INT]` | `US-002` | `EM-01` | `Must` | `45m` | `TASK-003`, `TASK-005` | `Ready for Implementation` |
| `TASK-007` | `[FE]` | `US-002` | `EM-01` | `Must` | `45m` | `TASK-001` | `Ready for Implementation` |
| `TASK-008` | `[INT]` / `[FE]` | `US-002` | `EM-01` | `Must` | `40m` | `TASK-006`, `TASK-007` | `Ready for Implementation` |
| `TASK-009` | `[FE]` | `US-003` | `EM-01` | `Must` | `40m` | `TASK-007` | `Ready for Implementation` |
| `TASK-010` | `[INT]` / `[FE]` | `US-003` | `EM-01` | `Must` | `25m` | `TASK-008`, `TASK-009` | `Ready for Implementation` |
| `TASK-011` | `[BE]` | `US-004` | `EM-01` | `Must` | `30m` | `TASK-003`, `TASK-005` | `Ready for Implementation` |
| `TASK-012` | `[FE]` | `US-004` | `EM-01` | `Must` | `25m` | `TASK-010`, `TASK-011` | `Ready for Implementation` |
| `TASK-013` | `[BE]` | `US-005` | `EM-01` | `Must` | `40m` | `TASK-011` | `Ready for Implementation` |
| `TASK-014` | `[FE]` | `US-005` | `EM-01` | `Must` | `45m` | `TASK-012`, `TASK-013` | `Ready for Implementation` |
| `TASK-015` | `[BE]` | `US-006` | `EM-02` | `Must` | `30m` | `TASK-011` | `Ready for Implementation` |
| `TASK-016` | `[FE]` | `US-006` | `EM-02` | `Must` | `35m` | `TASK-014`, `TASK-015` | `Ready for Implementation` |
| `TASK-017` | `[BE]` | `US-007` | `EM-02` | `Must` | `30m` | `TASK-011` | `Ready for Implementation` |
| `TASK-018` | `[FE]` | `US-007` | `EM-02` | `Must` | `35m` | `TASK-014`, `TASK-017` | `Ready for Implementation` |
| `TASK-019` | `[BE]` | `US-008` | `EM-02` | `Must` | `25m` | `TASK-004` | `Ready for Implementation` |
| `TASK-020` | `[FE]` | `US-008` | `EM-02` | `Must` | `30m` | `TASK-014`, `TASK-019` | `Ready for Implementation` |
| `TASK-021` | `[BE]` | `US-009` | `EM-02` | `Must` | `25m` | `TASK-004` | `Ready for Implementation` |
| `TASK-022` | `[FE]` | `US-009` | `EM-02` | `Must` | `30m` | `TASK-014`, `TASK-021` | `Ready for Implementation` |
| `TASK-023` | `[BE]` | `US-010` | `EM-03` | `Must` | `50m` | `TASK-011` | `Ready for Implementation` |
| `TASK-024` | `[BE]` | `US-010` | `EM-03` | `Must` | `20m` | `TASK-023` | `Ready for Implementation` |
| `TASK-025` | `[FE]` | `US-010` | `EM-03` | `Must` | `50m` | `TASK-024` | `Ready for Implementation` |
| `TASK-026` | `[BE]` | `US-011` | `EM-03` | `Should` | `25m` | `TASK-023` | `Ready for Implementation` |
| `TASK-027` | `[FE]` | `US-011` | `EM-03` | `Should` | `20m` | `TASK-025`, `TASK-026` | `Ready for Implementation` |
| `TASK-028` | `[BE]` | `US-012` | `EM-03` | `Must` | `40m` | `TASK-023` | `Ready for Implementation` |
| `TASK-029` | `[BE]` | `US-012` | `EM-03` | `Must` | `20m` | `TASK-028` | `Ready for Implementation` |
| `TASK-030` | `[FE]` | `US-012` | `EM-03` | `Must` | `50m` | `TASK-010`, `TASK-029` | `Ready for Implementation` |
| `TASK-031` | `[QA]` | `EPIC-005` | `EM-04` | `Must` | `60m` | `TASK-029` | `Ready for Implementation` |
| `TASK-032` | `[QA]` | `All FE` | `EM-04` | `Must` | `25m` | `TASK-030` | `Ready for Implementation` |
| `TASK-033` | `[INFRA]` | `OPS-001` | `EM-04` | `Must` | `35m` | `TASK-029`, `TASK-032` | `Ready for Implementation` |
| `TASK-034` | `[INT]` / `[AUTO]` | `INT-001` | `EM-04` | `Must` | `30m` | `TASK-033` | `Ready for Implementation` |
| `TASK-035` | `[DOC]` | `NFR-010` | `EM-04` | `Must` | `40m` | `TASK-033` | `Ready for Implementation` |
| `TASK-036` | `[QA]` | `Release` | `EM-04` | `Must` | `30m` | `TASK-031`..`TASK-035` | `Ready for Implementation` |

