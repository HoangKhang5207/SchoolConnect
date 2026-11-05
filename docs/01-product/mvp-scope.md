# Scope Definition & MVP Strategy — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect (Zalo Mini App — Preschool Parent & Teacher Portal) |
| **Document ID** | `DOC-02-MVP-SCOPE` |
| **Phase** | Phase 02 — Product Scope, MVP & Delivery Planning |
| **Project Owner** | Nguyen Vo Hoang Khang (`Nguyễn Võ Hoàng Khang`) |
| **Backend Architecture** | **Layered Architecture** (`controller` -> `service` -> `repository` -> `entity` / `dto`) |
| **Target Demo Deadline** | **2026-11-05 15:00 (GMT+7)** |
| **Last Updated** | 2026-11-04 |

---

## 2. Comprehensive Scope Definition

Every scope boundary below includes explicit **Business Justification** explaining why it is included, deferred, or excluded.

### 2.1. In Scope (`Trong phạm vi`)

| Scope Item | Category | Business Justification |
| :--- | :--- | :--- |
| **Parent Portal Core (6 Screens)** | Functional | Directly demonstrates alignment with TitKul's flagship **H2School** preschool domain (`Home Dashboard`, `Daily Report`, `Attendance`, `Meal Menu`, `Announcements`, `Absence Request`). |
| **Closed-Loop Leave Request Workflow (Parent Submit/Cancel + Teacher Review)** | Functional | Proves real full-stack transactional engineering (`PostgreSQL` write + ownership check + state machine transition) rather than a static read-only UI. |
| **Interactive Role Switcher (`Parent Mode` <-> `Teacher Mode`)** | Functional / Demo | Enables the TitKul Director to experience both sides of the workflow (Parent submitting a request and Teacher approving/rejecting it) within a single 10-minute session (`OQ-02`). |
| **Multi-Child Profile Switcher (`2 Linked Students`)** | Functional | Proves relational data modeling (`parents` <-> `parent_students` <-> `students`) and strict guardian data isolation (`RULE-001`). |
| **Dual Authentication (`DEMO_MODE` + `ZALO_AUTH`)** | Technical Enabler | Eliminates platform onboarding friction during the live evaluation while proving production knowledge of Zalo Open API token verification (`appsecret_proof` + internal JWT) (`OQ-04`). |
| **Spring Boot 3.x Layered Architecture Backend** | Technical Enabler | Implements clean Separation of Concerns (`controller` -> `service` -> `repository` -> `entity`/`dto`), global exception handling, Jakarta validation, and Swagger UI (`OQ-05` & Owner Architecture Directive). |
| **Containerized PostgreSQL + Synthetic Seed Data** | Technical Enabler | Guarantees 1-command reproducibility via `docker-compose.yml` and 100% compliance with data privacy rules (`CF-06`). |

---

### 2.2. MVP Scope (`Phạm vi MVP`) vs. Non-MVP In-Scope Refinements
- **Core MVP (`Must Have`):**
  - Dual-Mode Auth (`DEMO_MODE` active out-of-the-box + `ZALO_AUTH` endpoint & SDK integration).
  - Role Switcher (`ROLE_PARENT` <-> `ROLE_TEACHER`).
  - Multi-Child Switcher on `01. Home Dashboard`.
  - Read flows backed by PostgreSQL: `01. Home Dashboard`, `02. Daily Report`, `03. Attendance`, `04. Meal Menu`, `05. Announcements`.
  - Transactional Write/Review flows backed by PostgreSQL: `06. Absence Request` (Parent Create) + `Teacher Leave Review Queue` (Teacher Approve/Reject).
  - Swagger UI + Health Check + Docker Compose + Unit/Integration Tests.
- **Value-Add Refinements (`Should Have` & `Could Have` within current release if schedule permits):**
  - Parent cancellation of `PENDING` leave requests (`FR-010` — `Should Have`).
  - Category filtering on Announcements and Status filtering on Teacher Review Queue (`Should Have`).
  - Student Height/Weight/BMI summary badge on Home Dashboard (`Could Have`).

---

### 2.3. Technical Enablers (`Các hạng mục nền tảng kỹ thuật`)
1. **`TE-01` — Monorepo & Build Scaffolding:** Standardized directory structure (`frontend/`, `backend/`, `database/`, `infra/`, `docs/`) with Gradle Wrapper (`./gradlew`) for Java 21 and Vite/ZMP CLI for React + TypeScript.
2. **`TE-02` — Layered Backend Foundation:** Standard packages (`com.schoolconnect.controller`, `service`, `repository`, `entity`, `dto`, `security`, `config`, `exception`) with unified `ApiResponse<T>` envelope and `@RestControllerAdvice` global error handler.
3. **`TE-03` — Database Migration & Seed Engine:** Automated schema creation and idempotent synthetic seed data for 2 preschool classes (*Lớp Chồi 1*, *Lớp Mầm 2*), 2 students (*Nguyễn Minh An*, *Nguyễn Minh Khôi*), 7 days of attendance/menus/reports, 5 announcements, and sample leave requests.
4. **`TE-04` — Zalo WebView & CORS Security Layer:** Spring Security filter chain configured for stateless JWT + `DEMO_MODE` header/token support and explicit CORS support for `https://h5.zdn.vn` and local development origins.
5. **`TE-05` — Original SchoolConnect Design System:** Custom CSS variables, typography, color palette, and reusable mobile components (strictly original, non-imitating per `CF-05`).

---

### 2.4. Future Enhancements (`Các cải tiến trong tương lai` — Post-Technical Test)
- **`FE-01`:** Multi-month WHO BMI percentile growth charts and vaccination history logs (`FR-013`).
- **`FE-02`:** Teacher mobile forms for creating/editing Daily Reports, marking daily class Attendance, and uploading activity photos.
- **`FE-03`:** Zalo Official Account (Zalo OA) ZNS push notifications when a teacher approves/rejects a leave request or marks attendance.
- **`FE-04`:** Medication reminder (*Nhắc thuốc*) and afternoon pickup authorization (*Đón muộn / Ủy quyền đón trẻ*).

---

### 2.5. Out of Scope & Explicitly Excluded Functionality (`Ngoài phạm vi & Loại trừ rõ ràng`)

| Excluded Item | Reason for Explicit Exclusion |
| :--- | :--- |
| **TitKul Mini Hub (Service Discovery & Booking — Part 1 of `Requirements.docx`)** | Explicitly rejected by Project Owner (`OQ-01`) in favor of `SchoolConnect`. |
| **Online Tuition Fee Payment (`Thanh toán học phí`)** | Requires merchant payment gateway onboarding (ZaloPay/VietQR/VNPay) and PCI/financial reconciliation outside MVP scope (`SRC-001` `T509`). |
| **AI Nutrition Chatbot & AI Smart Scale (`Cân AI / Trợ lý dinh dưỡng`)** | High complexity peripheral features requiring external LLM/IoT integrations that dilute focus from core full-stack CRUD verification (`SRC-001` `P510`). |
| **Facial Recognition Attendance (`Face ID / Điểm danh khuôn mặt`)** | Requires biometric hardware/camera pipeline and specialized privacy compliance (`SRC-001` `P510`). |
| **Department of Education (`Sở Giáo dục`) Data Sync** | Requires government API credentials and proprietary institutional schemas (`SRC-001` `P510`). |
| **Enterprise Distributed Infra (Microservices, Redis, Kafka, Elasticsearch, K8s)** | Over-engineering that adds operational risk without adding evaluation value for a single-app MVP (`SRC-001` `P67`). |

---

## 3. MoSCoW Prioritization Matrix

To ensure realistic delivery before **15:00 on 2026-11-05** without classifying everything as `Must Have`, features are strictly differentiated across all four MoSCoW tiers based on Business Value, User Impact, Effort, Risk, and Acceptance Importance:

| Priority Tier | Req / Feature ID | Feature Name | Business Value | Effort | Acceptance Importance | Rationale |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **`Must Have`** | `FR-001`, `FR-002` | Dual Auth (`DEMO_MODE` + `ZALO_AUTH`) & Role Switcher (`Parent` <-> `Teacher`) | Critical | Medium | Mandatory Gate | Without instant auth and role switching, the live closed-loop demo cannot run smoothly. |
| **`Must Have`** | `FR-003`, `FR-004` | `01. Home Dashboard` & Multi-Child Switcher (`Minh An` / `Minh Khôi`) | High | Medium | Mandatory Gate | Entry point of the Mini App; proves guardian-student relational isolation (`RULE-001`). |
| **`Must Have`** | `FR-005`, `FR-006` | `03. Attendance` & `02. Daily Report` (Meals, Sleep, Activity, Mood) | High | Medium | High | Core H2School preschool value proposition for daily parent peace of mind. |
| **`Must Have`** | `FR-007`, `FR-008` | `04. Meal Menu` (Breakfast/Lunch/Snack) & `05. Announcements` | High | Low-Med | High | Completes the 6 required Parent screens from `SRC-001` Part 2. |
| **`Must Have`** | `FR-009`, `FR-011` | `06. Absence Request` (Parent Create/List) & Teacher Review (`Approve`/`Reject`) | Critical | High | Mandatory Gate | Primary transactional end-to-end write flow proving PostgreSQL persistence & state machine rules. |
| **`Must Have`** | `FR-012`, `OPS-001` | Health Check, Swagger UI & Docker Compose Packaging | High | Low | Mandatory Gate | Required by Evaluator to verify Backend, API contracts, and Database persistence. |
| **`Should Have`** | `FR-010` | Parent Cancellation of `PENDING` Leave Request (`PATCH /cancel`) | Medium | Low | Medium | Completes the Parent-side lifecycle of a leave request before teacher review (`OQ-07`). |
| **`Should Have`** | `REP-001` | Attendance Monthly Summary Counters & Teacher Status Filter Tabs | Medium | Low | Medium | Enhances usability and scannability on Attendance and Teacher Queue screens. |
| **`Could Have`** | `FR-003-BMI` | Student Height, Weight & BMI Summary Badge on Home Dashboard | Low-Med | Very Low | Low | Nice domain touch referencing `H2School` health tracking (`OQ-08`) with minimal effort using seeded fields. |
| **`Could Have`** | `FE-SEARCH` | Client-side Keyword Search / Category Pill Filter on Announcements | Low | Very Low | Low | Useful UX polish if ahead of schedule; not required if announcement list is concise. |
| **`Won't Have`** | `FR-013`, `FR-014` | Longitudinal BMI Charts, Tuition Payment, AI Bot, Face ID, Teacher CRUD for Menus/Reports | Low (for Test) | Very High | None | Explicitly deferred/excluded to protect the 24-hour delivery deadline. |

---

## 4. Vertical Slice MVP Strategy

Instead of building all database tables, then all backend endpoints, and finally all frontend screens (horizontal layering that risks having nothing demonstrable until the final hour), implementation is structured into **4 End-to-End Vertical Slices**:

### Slice 1 — Foundation, Auth & Multi-Child Home Dashboard (`VS-01`)
- **Target User:** Parent (`ROLE_PARENT`) & Evaluator (`EVALUATOR`).
- **Problem Addressed:** Unblock immediate app access, establish the Layered Backend + PostgreSQL connection, and prove multi-child data isolation.
- **Minimum Workflow:** Open App -> Auto-login in `DEMO_MODE` (or verify `ZALO_AUTH`) -> View Child 1 (`Nguyễn Minh An`) Dashboard -> Switch to Child 2 (`Nguyễn Minh Khôi`) -> Toggle Role Switcher.
- **Required Components:**
  - *Database:* `parents`, `students`, `parent_students` tables + seed data.
  - *Backend (Layered):* `AuthController`/`AuthService`, `ParentController`/`StudentService`, `ParentRepository`, `StudentRepository`, Security/CORS config, `/api/v1/health`.
  - *Frontend:* App Shell, Role Switcher Header, Child Switcher, `Home Dashboard` screen.
- **Acceptance Criteria:** `AC-001`, `AC-002`, `AC-003`, `AC-004`, `AC-005`, `AC-006`, `AC-018`.
- **Demo Evidence:** Live switching between `Minh An` and `Minh Khôi` updates class/teacher/BMI cards; requesting an unlinked student ID in Swagger returns `403 Forbidden`.

### Slice 2 — Preschool Daily Care Read Modules: Attendance & Daily Report (`VS-02`)
- **Target User:** Parent (`ROLE_PARENT`).
- **Problem Addressed:** Give parents transparent visibility into daily check-in/out times, attendance statistics, meals eaten, nap duration, and mood.
- **Minimum Workflow:** From Home Dashboard -> Open `02. Daily Report` (switch dates, inspect meal/sleep/activity/mood cards & empty state) -> Open `03. Attendance` (inspect summary counters & daily check-in/out log).
- **Required Components:**
  - *Database:* `attendance_records`, `daily_reports` tables + 7-day seed data for both children.
  - *Backend (Layered):* `AttendanceController`/`AttendanceService`/`AttendanceRepository`, `DailyReportController`/`DailyReportService`/`DailyReportRepository`.
  - *Frontend:* `Daily Report` screen & `Attendance` screen with date/month selectors and Empty/Loading/Error states.
- **Acceptance Criteria:** `AC-007`, `AC-008`, `AC-009`.
- **Demo Evidence:** Inspecting `Minh An`'s check-in time (`07:35`) and daily report comments, then selecting a Sunday date to demonstrate the Empty State UI.

### Slice 3 — School Nutrition & Communication: Meal Menu & Announcements (`VS-03`)
- **Target User:** Parent (`ROLE_PARENT`).
- **Problem Addressed:** Inform parents of daily/weekly nutritional menus (Breakfast, Lunch, Afternoon Snack) and official school/class notices.
- **Minimum Workflow:** Navigate to `04. Meal Menu` (switch weekdays, inspect 3 meal sessions & calories) -> Navigate to `05. Announcements` (view pinned notices, filter by category, open detail modal).
- **Required Components:**
  - *Database:* `meal_menus`, `announcements` tables + weekly menu and school/class announcement seed data.
  - *Backend (Layered):* `MealMenuController`/`MealMenuService`/`MealMenuRepository`, `AnnouncementController`/`AnnouncementService`/`AnnouncementRepository`.
  - *Frontend:* `Meal Menu` screen & `Announcements` screen with detail sheet.
- **Acceptance Criteria:** `AC-010`, `AC-011`.
- **Demo Evidence:** Viewing weekly preschool dishes with calorie counts and opening a pinned school health announcement.

### Slice 4 — Closed-Loop Transactional Leave Request Workflow: Parent & Teacher (`VS-04`)
- **Target User:** Parent (`ROLE_PARENT`) & Homeroom Teacher (`ROLE_TEACHER`).
- **Problem Addressed:** Replace unstructured chat absence messages with a validated, persistent, auditable leave request and teacher approval workflow.
- **Minimum Workflow:**
  1. Parent opens `06. Absence Request`, submits a new leave request -> Persisted in PostgreSQL with code `LR-YYYYMMDD-XXXX` and status `PENDING`.
  2. Evaluator taps **Role Switcher** -> Switches to `Teacher Mode` -> Views `PENDING` request -> Taps **Approve (`Duyệt đơn`)** with teacher note -> Persisted in PostgreSQL as `APPROVED`.
  3. Evaluator switches back to `Parent Mode` (and reloads app) -> Verifies `APPROVED` badge and teacher note.
- **Required Components:**
  - *Database:* `leave_requests` table + indexes & seed records.
  - *Backend (Layered):* `LeaveRequestController`, `TeacherLeaveRequestController`, `LeaveRequestService` (`@Transactional` overlap check + state machine), `LeaveRequestRepository`.
  - *Frontend:* `Absence Request` screen (form + history + cancel action) & `Teacher Leave Review` screen (filter tabs + Approve/Reject modal).
- **Acceptance Criteria:** `AC-012`, `AC-013`, `AC-014`, `AC-015`, `AC-016`, `AC-017`.
- **Demo Evidence:** Creating a real leave request during the demo, verifying the row in PostgreSQL/Swagger, approving it in Teacher Mode, and reloading the Mini App to prove end-to-end persistence.

