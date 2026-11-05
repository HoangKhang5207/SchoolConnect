# Engineering Implementation Plan — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect (Zalo Mini App — Preschool Parent & Teacher Portal) |
| **Document ID** | `DOC-04-IMPLEMENTATION-PLAN` |
| **Phase** | Phase 04 — Implementation Planning & Engineering Breakdown |
| **Backend Architecture** | **Layered Architecture** (`controller` -> `service` -> `repository` -> `entity` / `dto`) |
| **Target Live Demo Deadline** | **2026-11-05 15:00 (GMT+7)** |
| **Last Updated** | 2026-11-04 |

---

## 2. Greenfield Project Repository Analysis

### 2.1. Current Workspace State
- **Repository Root:** `d:\Coding\TitKul_ProjectTest\SchoolConnect`
- **Current Contents:**
  - `Requirements.docx` (Input specification artifact)
  - `docs/00-discovery/` (`project-brief.md`, `source-register.md`, `assumptions.md`, `open-questions.md`)
  - `docs/01-product/` (`vision.md`, `user-personas.md`, `requirements.md`, `business-rules.md`, `user-flows.md`, `acceptance-criteria.md`, `mvp-scope.md`)
  - `docs/02-planning/` (`roadmap.md`, `product-backlog.md`, `sprint-plan.md`, `dependencies.md`, `risk-register.md`, `implementation-plan.md`)
  - `docs/03-architecture/` (`system-context.md`, `architecture.md`, `module-design.md`, `database-design.md`, `api-contracts.md`, `security-design.md`, `deployment-architecture.md`, `decisions/ADR-001..004.md`)
- **Application Code Status:** `0%` (Greenfield — zero application source files written prior to Phase 05).
- **Git Status:** Not yet initialized (`git init` scheduled as `TASK-001` in Foundation Setup).

### 2.2. Required Target Repository Topology & Tooling Stack

| Layer | Technology & Version | Build / Package Descriptor | Verified Local Host Binary |
| :--- | :--- | :--- | :--- |
| **Version Control** | Git `2.47.0.windows.2` | `.gitignore` | `D:\GitSrc\Git\cmd\git.exe` |
| **Database** | PostgreSQL `16` (Docker) / `17.0.5` (Local) | `backend/src/main/resources/schema.sql` & `data.sql` | `D:\DB_Design\PostgreSQL\pgsql\bin\psql.exe` |
| **Backend** | Java `21.0.5` + Spring Boot `3.3.5` | `backend/build.gradle` + Gradle Wrapper (`./gradlew`) | `C:\Program Files\Common Files\Oracle\Java\javapath\java.exe` |
| **Frontend** | Node.js `v24.16.0` + npm `11.13.0` + React `18.3` + TypeScript `5.x` + Vite + `zmp-sdk` + `zmp-ui` (ZaUI) + Lucide Icons | `frontend/package.json` + `frontend/app-config.json` | `D:\nodejs\node.exe`, `D:\nodejs\npm.ps1` |
| **Infrastructure** | Docker & Docker Compose | `infra/docker-compose.yml` + `backend/Dockerfile` | `C:\Program Files\Docker\Docker\resources\bin\docker.exe` |

### 2.3. Mandatory Foundation Tasks Before Feature Coding
Before any domain endpoint or screen is built, the following 5 foundation steps must complete (`Checkpoint 1 — Foundation Setup`):
1. Initialize Git repository and root `.gitignore` (`TASK-001`).
2. Scaffold `backend/` with Gradle Wrapper (`gradlew`, `build.gradle`, `application.yml`) and verify `./gradlew compileJava` (`TASK-001`).
3. Create idempotent `schema.sql` (8 tables + 7 indexes) and `data.sql` (synthetic preschool dataset + sequence synchronization) (`TASK-002`).
4. Implement all 8 JPA Entities (`entity/`) and 8 Spring Data Repositories (`repository/`) (`TASK-003`).
5. Implement Cross-Cutting Layer (`config/CorsConfig`, `config/ClockConfig`, `config/OpenApiConfig`, `exception/GlobalExceptionHandler`, `security/JwtTokenProvider`, `security/JwtAuthenticationFilter`, `security/SecurityConfig`, `controller/HealthController`) (`TASK-004`, `TASK-005`).

---

## 3. Categorized Engineering Task Breakdown (`TASK-001` to `TASK-036`)

All 36 engineering tasks are categorized across the 8 required engineering disciplines:

### 3.1. Database Tasks (`[DB]`)
| Task ID | Story | Target Files | Description | Estimate |
| :--- | :--- | :--- | :--- | :--- |
| **`TASK-002`** | `US-001` | `backend/src/main/resources/schema.sql`, `backend/src/main/resources/data.sql` | Write idempotent DDL for all 8 tables (`parents`, `students`, `parent_students`, `attendance_records`, `daily_reports`, `meal_menus`, `announcements`, `leave_requests`), 7 performance indexes, and synthetic seed data for *Trường Mầm Non Sao Mai* (Parent `Trần Thu Hà`, Teacher `Cô Lê Thanh Thảo`, Students `Nguyễn Minh An` & `Nguyễn Minh Khôi`, 7 days of records) + `setval` sequence sync. | `45m` |
| **`TASK-003`** | `US-001` | `backend/src/main/java/com/schoolconnect/entity/*.java`, `repository/*.java` | Implement 8 JPA `@Entity` classes and 8 Spring Data JPA `@Repository` interfaces (including custom overlap query in `LeaveRequestRepository` and guardian lookup in `ParentStudentRepository`). | `45m` |

### 3.2. Backend Development Tasks (`[BE]` — Layered Architecture)
| Task ID | Story | Target Files (`com.schoolconnect.*`) | Description | Estimate |
| :--- | :--- | :--- | :--- | :--- |
| **`TASK-001`** | `US-001` | `.gitignore`, `backend/build.gradle`, `backend/settings.gradle`, `backend/src/main/resources/application.yml`, `SchoolConnectApplication.java` | Initialize Git repository, configure Spring Boot 3.3.x Gradle build (Spring Web, Data JPA, Validation, Security, JJWT, Springdoc OpenAPI, PostgreSQL driver), and parameterized `application.yml`. | `30m` |
| **`TASK-004`** | `US-001` | `dto/common/ApiResponse.java`, `exception/*.java`, `config/CorsConfig.java`, `config/ClockConfig.java`, `config/OpenApiConfig.java`, `controller/HealthController.java` | Implement standardized `ApiResponse<T>`, `@RestControllerAdvice` global exception handler, `Asia/Ho_Chi_Minh` clock bean, Zalo CORS filter (`https://h5.zdn.vn` + `OPTIONS`), Swagger UI config, and `GET /api/v1/health`. | `40m` |
| **`TASK-005`** | `US-002` | `security/UserPrincipal.java`, `security/JwtTokenProvider.java`, `security/JwtAuthenticationFilter.java`, `security/SecurityConfig.java` | Implement HMAC-SHA256 JWT generation/validation, stateless Spring Security filter chain, and RBAC rules (`/api/v1/teacher/**` requires `ROLE_TEACHER`). | `40m` |
| **`TASK-006`** | `US-002` | `dto/auth/*.java`, `service/AuthService.java`, `controller/AuthController.java` | Implement `POST /api/v1/auth/demo-session` (`DEMO_MODE` for `PARENT` / `TEACHER`) and `POST /api/v1/auth/zalo-login` (`ZALO_AUTH` with HMAC-SHA256 `appsecret_proof` verification against `graph.zalo.me/v2.0/me`). | `45m` |
| **`TASK-011`** | `US-004` | `service/GuardianAccessService.java`, `controller/ParentStudentController.java` | Implement `GuardianAccessService.verifyParentOwnsStudent(parentId, studentId)` (`RULE-001`, throws `ForbiddenAccessException` -> `HTTP 403`) and `GET /api/v1/parent/children`. | `30m` |
| **`TASK-013`** | `US-005` | `dto/student/*.java`, `service/StudentDashboardService.java`, `controller/ParentStudentController.java` | Implement `GET /api/v1/children/{studentId}/dashboard` aggregating student profile, BMI calculation (`weight / height^2`), today's attendance, latest daily report, pending leave count, and top 2 announcements. | `40m` |
| **`TASK-015`** | `US-006` | `dto/attendance/*.java`, `service/AttendanceService.java`, `controller/AttendanceController.java` | Implement `GET /api/v1/children/{studentId}/attendance` with guardian check, monthly filter, summary counters (`present`, `excused`, `late`, `rate%`), and chronological log. | `30m` |
| **`TASK-017`** | `US-007` | `dto/dailyreport/*.java`, `service/DailyReportService.java`, `controller/DailyReportController.java` | Implement `GET /api/v1/children/{studentId}/daily-reports` with guardian check and optional `date` filter. | `30m` |
| **`TASK-019`** | `US-008` | `dto/meal/*.java`, `service/MealMenuService.java`, `controller/MealMenuController.java` | Implement `GET /api/v1/meal-menus` returning `BREAKFAST`, `LUNCH`, and `AFTERNOON_SNACK` sessions with parsed dish list, kcal, and nutrition notes. | `25m` |
| **`TASK-021`** | `US-009` | `dto/announcement/*.java`, `service/AnnouncementService.java`, `controller/AnnouncementController.java` | Implement `GET /api/v1/announcements` and `GET /api/v1/announcements/{id}` sorted by `isPinned DESC, publishedAt DESC` with category filtering. | `25m` |
| **`TASK-023`** | `US-010` | `dto/leave/*.java`, `service/LeaveRequestService.java` | Implement `@Transactional` `createLeaveRequest()` and `getLeaveRequestsForParent()` enforcing `RULE-001` (ownership), `RULE-002` (date validity in `GMT+7`), `RULE-003` (session/date overlap check -> `409 Conflict`), `RULE-009` (reason validation), and `RULE-010` (`LR-YYYYMMDD-XXXX` code generation). | `50m` |
| **`TASK-024`** | `US-010` | `controller/LeaveRequestController.java` | Expose `POST /api/v1/children/{studentId}/leave-requests` (`201 Created`) and `GET /api/v1/leave-requests`. | `20m` |
| **`TASK-026`** | `US-011` | `service/LeaveRequestService.java`, `controller/LeaveRequestController.java` | Implement `cancelLeaveRequest()` (`PATCH /api/v1/leave-requests/{id}/cancel`) enforcing `RULE-005` & `RULE-006` (only `PENDING` requests owned by parent can be cancelled). | `25m` |
| **`TASK-028`** | `US-012` | `service/LeaveRequestService.java` | Implement `getAllLeaveRequestsForTeacher(status)` and `@Transactional` `reviewLeaveRequest()` enforcing `RULE-005` (must be `PENDING`), `RULE-007` (`ROLE_TEACHER` audit `reviewedBy`, `reviewedAt`), and `RULE-008` (mandatory `teacherNote >= 5` chars on `REJECTED`, default polite note on `APPROVED`). | `40m` |
| **`TASK-029`** | `US-012` | `controller/TeacherLeaveRequestController.java` | Expose `GET /api/v1/teacher/leave-requests` and `PATCH /api/v1/teacher/leave-requests/{id}/review`. | `20m` |

### 3.3. Frontend Development Tasks (`[FE]` — React + TypeScript + ZaUI)
| Task ID | Story | Target Files (`frontend/src/*`) | Description | Estimate |
| :--- | :--- | :--- | :--- | :--- |
| **`TASK-007`** | `US-002` | `frontend/package.json`, `vite.config.ts`, `tsconfig.json`, `app-config.json`, `src/styles/design-system.css`, `src/types/api.ts` | Scaffold React + TypeScript + Vite/ZMP project, define TypeScript DTO interfaces, and create the original **SchoolConnect Emerald & Warm Cream Design System** (`CF-05` non-imitation compliance). | `45m` |
| **`TASK-009`** | `US-003` | `src/components/RoleSwitcherBar.tsx`, `src/components/AsyncStateViews.tsx`, `src/layouts/MobileShellLayout.tsx` | Build the sticky Role Switcher header (`Phụ huynh` ⇄ `Giáo viên duyệt đơn`), Bottom Navigation bar, and reusable `Loading` / `Empty` / `Error` state components. | `40m` |
| **`TASK-012`** | `US-004` | `src/components/ChildSwitcherBar.tsx` | Build the interactive Multi-Child selector pill bar (`Bé Nguyễn Minh An - Lớp Chồi 1` ⇄ `Bé Nguyễn Minh Khôi - Lớp Mầm 2`). | `25m` |
| **`TASK-014`** | `US-005` | `src/pages/HomeDashboardPage.tsx` | Build `01. Home Dashboard` displaying student profile card, Height/Weight/BMI badge, today's attendance card, today's daily report snapshot, quick action grid, and recent announcements. | `45m` |
| **`TASK-016`** | `US-006` | `src/pages/AttendancePage.tsx` | Build `03. Attendance` screen with monthly attendance rate & counter cards (`Có mặt`, `Nghỉ có phép`, `Đi muộn`) and daily check-in/out timeline list. | `35m` |
| **`TASK-018`** | `US-007` | `src/pages/DailyReportPage.tsx` | Build `02. Daily Report` screen with date selector pills, 4 care cards (Meals, Sleep, Activities, Mood + Teacher Note), and Empty State on unrecorded dates. | `35m` |
| **`TASK-020`** | `US-008` | `src/pages/MealMenuPage.tsx` | Build `04. Meal Menu` screen with weekday selector and 3 session cards (`Bữa sáng`, `Bữa trưa`, `Bữa xế`) showing dishes, kcal, and nutrition notes. | `30m` |
| **`TASK-022`** | `US-009` | `src/pages/AnnouncementsPage.tsx` | Build `05. Announcements` screen with category filter pills (`Tất cả`, `Trường`, `Lớp`, `Y tế`, `Sự kiện`), pinned notice highlights, and full detail modal. | `30m` |
| **`TASK-025`** | `US-010` | `src/pages/AbsenceRequestPage.tsx` | Build `06. Absence Request` screen with validated creation form (Date range, Session selector `Cả ngày`/`Sáng`/`Chiều`, Reason input with char counter, double-submit guard) and Leave Request History cards showing status & teacher feedback. | `50m` |
| **`TASK-027`** | `US-011` | `src/pages/AbsenceRequestPage.tsx` | Add "Hủy đơn" (`Cancel`) action with confirmation on `PENDING` leave request cards in Parent Mode. | `20m` |
| **`TASK-030`** | `US-012` | `src/pages/TeacherReviewPage.tsx` | Build **Teacher Leave Review Dashboard** (`Quản lý & Duyệt đơn xin nghỉ`) with status filter tabs (`Chờ duyệt`, `Đã duyệt`, `Từ chối`, `Tất cả`), student/parent metadata cards, and Approve/Reject modal enforcing mandatory rejection note (`RULE-008`). | `50m` |

### 3.4. Integration & AI/Automation Tasks (`[INT]` & `[AUTO]`)
| Task ID | Story | Target Files | Description | Estimate |
| :--- | :--- | :--- | :--- | :--- |
| **`TASK-008`** | `US-002` | `frontend/src/services/apiClient.ts`, `src/services/zaloSdkAdapter.ts`, `src/store/AppContext.tsx` | Implement HTTP client with Bearer JWT attachment, Zalo Mini App SDK adapter (`getAccessToken()` with automatic `DEMO_MODE` fallback outside Zalo), and global `AppContext` store. | `40m` |
| **`TASK-010`** | `US-003` | `frontend/src/App.tsx`, `src/store/AppContext.tsx` | Wire end-to-end Role Switching (`ROLE_PARENT` ⇄ `ROLE_TEACHER`) and Child Switching (`studentId=1` ⇄ `studentId=2`) to automatic data re-fetching across screens. | `25m` |
| **`TASK-034`** | `M4` | `frontend/app-config.json`, `scripts/verify-e2e.ps1` | Configure Zalo Mini App manifest (`app-config.json`) and create automated API/E2E verification script (`[AUTO]`) that tests all 14 endpoints and seed data automatically. | `30m` |

### 3.5. Infrastructure, QA & Documentation Tasks (`[INFRA]`, `[QA]`, `[DOC]`)
| Task ID | Discipline | Target Files | Description | Estimate |
| :--- | :--- | :--- | :--- | :--- |
| **`TASK-031`** | `[QA]` | `backend/src/test/java/com/schoolconnect/service/LeaveRequestServiceTest.java`, `ControllerIntegrationTest.java` | Write JUnit 5 + Mockito / MockMvc tests verifying `AC-001`..`AC-018` (`LeaveRequestService` creation, `409` overlap, `403` guardian isolation, parent cancel, teacher approve/reject `RULE-008`, and Zalo CORS `OPTIONS` preflight). | `60m` |
| **`TASK-032`** | `[QA]` | `frontend/` | Execute TypeScript strict check (`tsc --noEmit`) and production bundle build (`npm run build`), verifying zero errors and mobile viewport compliance (`AC-019`). | `25m` |
| **`TASK-033`** | `[INFRA]` | `backend/Dockerfile`, `infra/docker-compose.yml`, `.env.example` | Create multi-stage `Dockerfile` for Spring Boot (Java 21) and `infra/docker-compose.yml` orchestrating PostgreSQL + Backend with healthchecks. | `35m` |
| **`TASK-035`** | `[DOC]` | `README.md`, `docs/demo-script.md` | Author comprehensive 15-section root `README.md` and step-by-step 10-minute Director `docs/demo-script.md`. | `40m` |
| **`TASK-036`** | `[QA]` | All layers + `PROJECT_STATE.md` | Execute full end-to-end acceptance gate checklist (`AC-001` through `AC-019`) against live PostgreSQL instance and update `PROJECT_STATE.md`. | `30m` |

---

## 4. Independently Demonstrable Vertical Slices Plan (`VS-01` to `VS-04`)

| Slice ID | User-Visible Outcome | Database Changes | Backend Changes (`Layered`) | Frontend Changes | Integration & Tests | Demo Evidence & Completion Criteria |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **`VS-01`**<br>*Foundation, Dual Auth, Role Switcher & Home Dashboard* | User opens app, auto-logs into `DEMO_MODE` (`Trần Thu Hà`), views `Minh An`'s Dashboard + BMI badge, switches to `Minh Khôi`, and toggles Teacher Mode. | `schema.sql` & `data.sql` initialize all 8 tables + seed data (`TASK-002`, `TASK-003`). | `CorsConfig`, `SecurityConfig`, `JwtTokenProvider`, `AuthController`/`Service`, `GuardianAccessService`, `ParentStudentController`, `StudentDashboardService`, `HealthController`. | App shell, `RoleSwitcherBar`, `ChildSwitcherBar`, `HomeDashboardPage`, `zaloSdkAdapter`, `apiClient`. | Zalo SDK fallback integration; test `GET /api/v1/health`, `/auth/demo-session`, `/children/1/dashboard`, and `403` on `/children/999/dashboard`. | **Evidence:** Live Child Switcher updates class/BMI/attendance cards; Swagger UI shows `200 OK` and `403 Forbidden` for unlinked student.<br>**Exit:** `AC-001`..`AC-006`, `AC-018` pass. |
| **`VS-02`**<br>*Preschool Daily Care (Attendance & Daily Report)* | Parent views child's monthly attendance stats + check-in/out log (`03. Attendance`) and daily meal/sleep/activity/mood report (`02. Daily Report`). | Query `attendance_records` and `daily_reports` via indexed `(student_id, date DESC)`. | `AttendanceController` / `Service` / `Dto`, `DailyReportController` / `Service` / `Dto`. | `AttendancePage.tsx` and `DailyReportPage.tsx` with date picker and Empty State UI. | Connect Frontend pages to `/attendance` and `/daily-reports` APIs; verify `AC-007`, `AC-008`, `AC-009`. | **Evidence:** Inspecting `Minh An`'s 110-min nap & salmon porridge report; selecting Sunday displays Empty State.<br>**Exit:** `AC-007`..`AC-009` pass. |
| **`VS-03`**<br>*School Nutrition & Notices (Meal Menu & Announcements)* | Parent views 3-session daily/weekly meal menus (`04. Meal Menu`) and pinned/categorized school announcements (`05. Announcements`). | Query `meal_menus` and `announcements`. | `MealMenuController` / `Service` / `Dto`, `AnnouncementController` / `Service` / `Dto`. | `MealMenuPage.tsx` and `AnnouncementsPage.tsx` with category filters & detail modal. | Connect Frontend pages to `/meal-menus` and `/announcements`; verify `AC-010`, `AC-011`. | **Evidence:** Switching weekday tabs on Meal Menu; tapping a pinned announcement to view full content.<br>**Exit:** `AC-010`, `AC-011` pass. |
| **`VS-04`**<br>*Closed-Loop Leave Request Workflow (Parent & Teacher)* | Parent creates/cancels a leave request (`06. Absence Request`); Teacher switches to Teacher Mode and approves/rejects it with a note; Parent reloads and sees `APPROVED` + teacher note. | Transactional `INSERT` and `UPDATE` on `leave_requests`. | `LeaveRequestController`, `TeacherLeaveRequestController`, `@Transactional` `LeaveRequestService` (`RULE-001`..`RULE-010`). | `AbsenceRequestPage.tsx` (form + history + cancel) and `TeacherReviewPage.tsx` (filter tabs + Approve/Reject modal). | Full E2E mutation loop + JUnit 5 service/controller tests (`TASK-031`). | **Evidence:** Live creation of `LR-20261105-XXXX` -> Teacher approves -> Reload app & inspect PostgreSQL row.<br>**Exit:** `AC-012`..`AC-017` pass. |

---

## 5. Implementation Order, Critical Path & Shared-State Coordination Rules

### 5.1. Critical Path (`Đường găng`)
$$\text{TASK-001..004 (DB \& Core Config)} \longrightarrow \text{TASK-005..006 (JWT \& Auth)} \longrightarrow \text{TASK-011 (GuardianAccessService)} \longrightarrow \text{TASK-023..029 (Leave Request \& Teacher Review API)} \longrightarrow \text{TASK-025..030 (Leave \& Teacher UI)} \longrightarrow \text{TASK-031..036 (Tests, Docker \& Rehearsal)}$$

### 5.2. Blocking Tasks (`Các công việc mang tính chặn`)
- **`TASK-001`..`TASK-005`:** Blocks all Backend controllers and Frontend API integration.
- **`TASK-007`..`TASK-008`:** Blocks all Frontend feature screens (`TASK-014`..`TASK-030`).
- **`TASK-011` (`GuardianAccessService`):** Blocks all student-scoped Backend services (`StudentDashboardService`, `AttendanceService`, `DailyReportService`, `LeaveRequestService`).

### 5.3. Shared Components & Integration Points
- **Backend Shared Components:** `ApiResponse<T>`, `GlobalExceptionHandler`, `SecurityConfig`, `UserPrincipal`, `GuardianAccessService`, `LeaveRequestService`.
- **Frontend Shared Components:** `AppContext` (`store/AppContext.tsx`), `apiClient.ts`, `MobileShellLayout.tsx`, `RoleSwitcherBar.tsx`, `ChildSwitcherBar.tsx`, `AsyncStateViews.tsx`, `design-system.css`.

### 5.4. Parallelization & Anti-Conflict Coordination Policy
To prevent file-write collisions or inconsistent shared state:
1. **Sequential Lock on Shared Foundations:** `TASK-001` through `TASK-012` (which establish `build.gradle`, `schema.sql`, `SecurityConfig`, `AppContext.tsx`, and `App.tsx`) must be executed **strictly sequentially**.
2. **Safe Parallelization Window (`VS-02` & `VS-03`):** Once `VS-01` is locked, the 4 read modules (`Attendance`, `DailyReport`, `MealMenu`, `Announcement`) touch completely disjoint Controller, Service, DTO, and Page files (`Attendance*`, `DailyReport*`, `MealMenu*`, `Announcement*`) and can be implemented in rapid parallel pairs without touching shared state.
3. **Single-Owner Lock on `LeaveRequestService` & `AbsenceRequestPage`:** In `VS-04`, `US-010` (Create), `US-011` (Cancel), and `US-012` (Teacher Review) all share `LeaveRequestService.java`. Therefore, `LeaveRequestService.java` is implemented in a single cohesive pass (`TASK-023` + `TASK-026` + `TASK-028`) rather than split across concurrent edits.

### 5.5. High-Risk Changes & Safeguards
- **Risk 1 — Overlap Detection Logic (`RULE-003`):** Date range intersection (`existing.startDate <= req.endDate AND existing.endDate >= req.startDate`) combined with session intersection (`FULL_DAY` vs `MORNING`/`AFTERNOON`). *Safeguard:* Encapsulate in a dedicated method in `LeaveRequestService` and verify with 4 explicit JUnit test cases (`TASK-031`).
- **Risk 2 — Sequence Desynchronization in PostgreSQL (`ADR-004`):** *Safeguard:* Execute `SELECT setval(pg_get_serial_sequence('leave_requests', 'id'), COALESCE(MAX(id), 1))` at the end of `data.sql`.

---

## 6. Engineering Milestones (`EM-01` to `EM-04`) & Checkpoints

### 6.1. Engineering Milestones Matrix

| Milestone | Target Period (GMT+7) | Task IDs | Deliverables | Test Requirements | Exit Criteria |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **`EM-01`**<br>Foundation & First Vertical Slice (`VS-01`) | `04/11 17:30 – 20:30`<br>*(3.0h)* | `TASK-001` to `TASK-014` | Git repo, PostgreSQL 8 tables + seed data, Layered Backend core, Dual Auth, Role Switcher, Child Switcher, `01. Home Dashboard`. | Verify `/api/v1/health`, `/auth/demo-session`, `/children/1/dashboard`, `403` guard, and CORS preflight. | `Checkpoint 1` & `Checkpoint 2` passed; `AC-001`..`AC-006`, `AC-018` green. |
| **`EM-02`**<br>Daily Care & School Info Slices (`VS-02`, `VS-03`) | `04/11 20:30 – 23:30`<br>*(3.0h)* | `TASK-015` to `TASK-022` | Backend APIs & Frontend pages for `02. Daily Report`, `03. Attendance`, `04. Meal Menu`, `05. Announcements`. | Verify date/month/category filters and Empty State rendering on Sunday dates. | `Checkpoint 3` passed for all 4 read modules; `AC-007`..`AC-011` green. |
| **`EM-03`**<br>Closed-Loop Leave Request Slice (`VS-04`) | `05/11 06:30 – 10:00`<br>*(3.5h)* | `TASK-023` to `TASK-030` | Parent Leave Submit/Cancel (`06. Absence Request`) + Teacher Leave Review Queue (`Approve`/`Reject` with note). | Verify `201 Created`, `409` overlap, Parent `CANCELLED`, Teacher `APPROVED`/`REJECTED` (`RULE-008`), reload persistence. | `Checkpoint 4` & `Checkpoint 5` passed; `AC-012`..`AC-017` green. |
| **`EM-04`**<br>Release Candidate, QA & Demo Package | `05/11 10:00 – 13:30`<br>*(3.5h)* | `TASK-031` to `TASK-036` | JUnit 5 test suite, `docker-compose.yml`, Zalo `app-config.json`, `README.md`, `demo-script.md`, E2E rehearsal. | `./gradlew test` 100% pass; `npm run build` 0 errors; all 19 ACs verified. | `Checkpoint 6 (Release Candidate)` passed; ready 90 mins before **15:00 demo**. |

---

### 6.2. Mandatory Implementation Checkpoints (`Các điểm kiểm soát triển khai`)

1. **Checkpoint 1 — Foundation Setup (`Thiết lập nền tảng ban đầu`):**
   - [ ] Git initialized with clean `.gitignore` (excluding `node_modules/`, `build/`, `.gradle/`, `.env`).
   - [ ] PostgreSQL database `schoolconnect` reachable; all 8 tables and synthetic seed rows created via `schema.sql` and `data.sql`.
   - [ ] `./gradlew compileJava` succeeds; `GET /api/v1/health` returns `HTTP 200 OK` with `database: "UP"`.
2. **Checkpoint 2 — First Vertical Slice (`Lát cắt chức năng đầu tiên — VS-01`):**
   - [ ] Frontend boots in `< 2s`, authenticates via `/api/v1/auth/demo-session`, and renders `01. Home Dashboard` with real PostgreSQL data for `Nguyễn Minh An`.
   - [ ] Switching to `Nguyễn Minh Khôi` dynamically updates class, teacher, BMI, and attendance summary.
   - [ ] Role Switcher toggles cleanly between `ROLE_PARENT` and `ROLE_TEACHER`.
3. **Checkpoint 3 — Each Major Feature (`Mỗi chức năng lớn — VS-02, VS-03, VS-04`):**
   - [ ] All 4 UI states (`Loading`, `Success`, `Empty`, `Error` with Retry) verified on every screen.
   - [ ] All business rules (`RULE-001` to `RULE-014`) enforced in Backend `service/` layer.
4. **Checkpoint 4 — API Integration (`Tích hợp API`):**
   - [ ] All 14 REST endpoints documented in Swagger UI (`/swagger-ui.html`) and returning standardized `ApiResponse<T>` JSON.
   - [ ] `OPTIONS` preflight request from `Origin: https://h5.zdn.vn` returns `200 OK` with required CORS headers (`AC-018`).
5. **Checkpoint 5 — Database Integration (`Tích hợp cơ sở dữ liệu`):**
   - [ ] Creating a leave request, cancelling a pending request, and approving/rejecting a request as Teacher commit durably to PostgreSQL and survive full browser reload and Backend restart.
   - [ ] Invalid or conflicting operations roll back cleanly (`@Transactional`) without leaving partial state.
6. **Checkpoint 6 — Release Candidate (`Phiên bản ứng viên phát hành`):**
   - [ ] `./gradlew test` passes 100% of unit and integration tests.
   - [ ] `npm run build` completes with zero TypeScript or bundler errors.
   - [ ] `docker-compose.yml`, `README.md`, and `docs/demo-script.md` are complete and verified.

