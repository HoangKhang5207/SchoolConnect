# Component & Layered Module Design — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect |
| **Document ID** | `DOC-03-MODULE-DESIGN` |
| **Phase** | Phase 03 — System Architecture & Technical Design |
| **Backend Architecture** | **Layered Architecture** (`controller` -> `service` -> `repository` -> `entity` / `dto`) |
| **Last Updated** | 2026-11-04 |

---

## 2. Monorepo Directory Structure

```text
schoolconnect/
├── backend/
│   ├── src/main/java/com/schoolconnect/
│   │   ├── SchoolConnectApplication.java
│   │   ├── config/                  # OpenApiConfig, CorsConfig, JacksonConfig, ClockConfig
│   │   ├── security/                # SecurityConfig, JwtTokenProvider, JwtAuthenticationFilter, UserPrincipal
│   │   ├── controller/              # REST Controllers (Presentation Layer)
│   │   │   ├── AuthController.java
│   │   │   ├── ParentStudentController.java
│   │   │   ├── AttendanceController.java
│   │   │   ├── DailyReportController.java
│   │   │   ├── MealMenuController.java
│   │   │   ├── AnnouncementController.java
│   │   │   ├── LeaveRequestController.java
│   │   │   ├── TeacherLeaveRequestController.java
│   │   │   └── HealthController.java
│   │   ├── service/                 # Business Logic & Transaction Layer
│   │   │   ├── AuthService.java
│   │   │   ├── GuardianAccessService.java
│   │   │   ├── StudentDashboardService.java
│   │   │   ├── AttendanceService.java
│   │   │   ├── DailyReportService.java
│   │   │   ├── MealMenuService.java
│   │   │   ├── AnnouncementService.java
│   │   │   └── LeaveRequestService.java
│   │   ├── repository/              # Data Access Layer (Spring Data JPA)
│   │   │   ├── ParentRepository.java
│   │   │   ├── StudentRepository.java
│   │   │   ├── ParentStudentRepository.java
│   │   │   ├── AttendanceRecordRepository.java
│   │   │   ├── DailyReportRepository.java
│   │   │   ├── MealMenuRepository.java
│   │   │   ├── AnnouncementRepository.java
│   │   │   └── LeaveRequestRepository.java
│   │   ├── entity/                  # Persistence Domain Entities (8 PostgreSQL Tables)
│   │   │   ├── ParentEntity.java
│   │   │   ├── StudentEntity.java
│   │   │   ├── ParentStudentEntity.java
│   │   │   ├── AttendanceRecordEntity.java
│   │   │   ├── DailyReportEntity.java
│   │   │   ├── MealMenuEntity.java
│   │   │   ├── AnnouncementEntity.java
│   │   │   └── LeaveRequestEntity.java
│   │   ├── dto/                     # Request & Response Data Transfer Objects
│   │   │   ├── common/              # ApiResponse<T>, ErrorDetailDto
│   │   │   ├── auth/                # DemoLoginRequest, ZaloLoginRequest, AuthResponseDto
│   │   │   ├── student/             # StudentSummaryDto, DashboardResponseDto
│   │   │   ├── attendance/          # AttendanceRecordDto, AttendanceSummaryDto
│   │   │   ├── dailyreport/         # DailyReportDto
│   │   │   ├── meal/                # MealMenuDto, DishItemDto
│   │   │   ├── announcement/        # AnnouncementDto
│   │   │   └── leave/               # CreateLeaveRequestDto, ReviewLeaveRequestDto, LeaveRequestDto
│   │   └── exception/               # Custom Exceptions & GlobalExceptionHandler
│   │       ├── GlobalExceptionHandler.java
│   │       ├── ResourceNotFoundException.java
│   │       ├── ForbiddenAccessException.java
│   │       ├── BusinessConflictException.java
│   │       └── BadRequestException.java
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   ├── schema.sql               # Idempotent DDL for 8 PostgreSQL tables & indexes
│   │   └── data.sql                 # Idempotent Synthetic Seed Data
│   ├── src/test/java/com/schoolconnect/
│   │   └── service/                 # JUnit 5 + Mockito Unit & Integration Tests
│   ├── Dockerfile
│   └── build.gradle
├── frontend/
│   ├── src/
│   │   ├── assets/
│   │   ├── components/              # Reusable UI Components (RoleSwitcherBar, ChildSelector, StatusBadge, StateViews)
│   │   ├── layouts/                 # MobileShellLayout, BottomNavBar
│   │   ├── pages/                   # 6 Parent Pages + 1 Teacher Review Page
│   │   │   ├── HomeDashboardPage.tsx
│   │   │   ├── DailyReportPage.tsx
│   │   │   ├── AttendancePage.tsx
│   │   │   ├── MealMenuPage.tsx
│   │   │   ├── AnnouncementsPage.tsx
│   │   │   ├── AbsenceRequestPage.tsx
│   │   │   └── TeacherReviewPage.tsx
│   │   ├── services/                # HTTP Client, Zalo SDK Adapter & Domain API Services
│   │   ├── store/                   # Deterministic React Context / Zustand State Store
│   │   ├── types/                   # TypeScript Interfaces matching Backend DTOs
│   │   ├── utils/                   # Date formatting, validation helpers
│   │   ├── styles/                  # SchoolConnect Custom CSS Design System
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── app-config.json              # Zalo Mini App Configuration
│   └── package.json
├── infra/
│   └── docker-compose.yml
├── docs/                            # Phase 00, 01, 02, 03 Documentation
└── README.md
```

---

## 3. Backend Layered Design (`Java 21 + Spring Boot 3.x`)

### 3.1. Layer Responsibilities & Dependency Rules
Strict unidirectional call flow is enforced across layers:
`Controller Layer` $\rightarrow$ `Service Layer` $\rightarrow$ `Repository Layer` $\rightarrow$ `Entity Layer`

| Layer (`Package`) | Responsibilities | Forbidden Actions (Anti-Patterns Prevented) |
| :--- | :--- | :--- |
| **`controller/`** | - Expose REST endpoints (`/api/v1/...`).<br>- Parse HTTP parameters, headers, and `@Valid` request DTOs.<br>- Extract authenticated `UserPrincipal` from `SecurityContext`.<br>- Wrap outputs in `ApiResponse<T>` with appropriate HTTP status (`200`, `201`). | - **Never** inject or call `Repository` classes directly.<br>- **Never** return raw JPA `Entity` objects to the client.<br>- **Never** contain business rules or SQL queries. |
| **`service/`** | - Enforce all business rules (`RULE-001` through `RULE-014`).<br>- Enforce Guardian-Student ownership via `GuardianAccessService`.<br>- Manage `@Transactional` boundaries.<br>- Map between JPA `Entity` objects and `DTO` records. | - **Never** depend on `HttpServletRequest` or HTTP status codes directly (throw domain exceptions instead). |
| **`repository/`** | - Extend `JpaRepository<Entity, Long>`.<br>- Execute parameterized JPQL/SQL queries (e.g., overlap detection for leave requests). | - **Never** contain business state transition logic. |
| **`entity/`** | - Represent PostgreSQL tables, columns, primary/foreign keys, and audit timestamps (`created_at`, `updated_at`). | - **Never** expose entities directly in Jackson JSON responses (prevents N+1 lazy-loading serialization bugs and mass-assignment vulnerabilities). |
| **`dto/`** | - Define immutable Java `record` classes for API requests and responses with Jakarta Validation annotations (`@NotBlank`, `@NotNull`, `@Size`). | - **Never** include database persistence annotations. |
| **`exception/`** | - Catch all exceptions centrally in `GlobalExceptionHandler` (`@RestControllerAdvice`) and translate them into deterministic HTTP status codes and `ApiResponse` error payloads. | - **Never** swallow exceptions silently or leak raw SQL/stack traces to clients. |

### 3.2. Transaction Boundaries (`@Transactional`)
- **Read Operations (`StudentDashboardService`, `AttendanceService`, `DailyReportService`, `MealMenuService`, `AnnouncementService`):** Annotated with `@Transactional(readOnly = true)` to optimize Hibernate dirty-checking performance and connection usage.
- **Write Operations (`LeaveRequestService.createLeaveRequest`, `cancelLeaveRequest`, `reviewLeaveRequest`):** Annotated with `@Transactional` (default `Propagation.REQUIRED`, `Isolation.READ_COMMITTED`). Any runtime or domain exception (`BusinessConflictException`, `BadRequestException`, `ForbiddenAccessException`) triggers an immediate, complete rollback.

### 3.3. Background Jobs Assessment
- In accordance with YAGNI and MVP scope discipline, **no asynchronous background schedulers (`@Scheduled` or message queues)** are needed for the core MVP workflows, as all Parent and Teacher interactions require immediate synchronous confirmation.

---

## 4. Frontend Design (`React + TypeScript + ZaUI + ZMP SDK`)

### 4.1. Component & State Management Architecture
- **Unidirectional State Store (`src/store/AppContext.tsx`):**
  - Holds global session state:
    - `authMode`: `'DEMO_MODE' | 'ZALO_AUTH'`
    - `currentUser`: `{ id, fullName, phone, role: 'PARENT' | 'TEACHER', avatarUrl }`
    - `linkedChildren`: `StudentSummaryDto[]` (`Nguyễn Minh An` – `id=1`, `Nguyễn Minh Khôi` – `id=2`)
    - `activeStudentId`: `number` (defaults to `1`)
  - Local screen data (e.g., selected date on Daily Report, selected weekday on Meal Menu, form fields on Absence Request) is kept strictly in **local component state** (`useState`) to minimize unnecessary re-renders.

### 4.2. Routing & Role-Aware Navigation
- **Top Sticky Context Header (`RoleSwitcherBar`):**
  - Displays the active persona badge (`Phụ huynh: Trần Thu Hà` or `Giáo viên: Cô Lê Thanh Thảo`) and a prominent one-tap switch button (`"Chuyển sang: Giáo viên duyệt đơn"` / `"Chuyển sang: Phụ huynh"`).
  - When `role === 'PARENT'`, displays the **Child Selector Bar** (`Bé Nguyễn Minh An - Lớp Chồi 1` | `Bé Nguyễn Minh Khôi - Lớp Mầm 2`).
- **Role-Aware Views & Bottom Navigation:**
  - **Parent Mode (`ROLE_PARENT`):** 5-tab bottom navigation (`Trang chủ`, `Nhật ký`, `Điểm danh`, `Thực đơn`, `Xin nghỉ`) plus direct access to `Thông báo` from the Home Dashboard header/section, covering all 6 Parent screens (`01` to `06`).
  - **Teacher Mode (`ROLE_TEACHER`):** Switches view directly to the **Teacher Leave Review Dashboard (`Quản lý & Duyệt đơn xin nghỉ phép`)**, allowing status filtering (`Chờ duyệt`, `Đã duyệt`, `Từ chối`, `Tất cả`) and one-tap **Approve (`Duyệt đơn`)** / **Reject (`Từ chối`)** with note.

### 4.3. Standardized 4-State UI Pattern (`Loading / Success / Empty / Error`)
Every data-driven page implements a reusable `<AsyncStateBoundary>` pattern:
1. **`Loading`:** Smooth shimmer skeleton cards matching the target layout height (prevents layout shift).
2. **`Error`:** Friendly warning card displaying the error message and an interactive **"Thử lại (Retry)"** button.
3. **`Empty`:** Custom illustration card with clear Vietnamese explanation (e.g., *"Chưa có dữ liệu nhật ký cho ngày nghỉ cuối tuần"*).
4. **`Success`:** Renders the actual domain cards.

### 4.4. Original SchoolConnect Visual Design System (`Non-Imitation Guarantee`)
To strictly comply with `CF-05` (`Tuyệt đối không được bắt chước giống` the 5 reference screenshots):
- **Color Palette:** Warm Preschool Emerald & Mint theme (`Primary: #0F766E / #14B8A6`, `Accent Warm Amber: #F59E0B`, `Background: #F8FAFC / #F0FDFA`, `Surface: #FFFFFF`) — completely distinct from the royal blue of `SRC-003`/`SRC-004`/`SRC-006`, sky blue of `SRC-005`, and orange of `SRC-002`.
- **Layout Architecture:** Uses a **Unified Student Context Header + Interactive Timeline Cards + Segmented Pill Navigation** rather than the 3x3 square icon grid seen in `SRC-004`.

