# Technical Documentation — SchoolConnect

**Project:** SchoolConnect — Preschool Parent & Teacher Portal  
**Platform:** Zalo Mini App + Spring Boot Full-Stack Enterprise Platform  
**Document Version:** 1.0.0  
**Date:** 2026-11-05  
**Author:** Nguyen Vo Hoang Khang (Lead DevOps & Full-Stack Architect)  
**Security Classification:** TitKul Internal / Evaluator Confidential  

---

## 1. Executive Summary & System Overview

SchoolConnect is a specialized mobile-first preschool communication and administrative management portal engineered for **Trường Mầm Non Sao Mai**. Built as a high-performance **Zalo Mini App** running within Vietnam's pervasive Zalo messaging ecosystem, SchoolConnect bridges the communication divide between parents and homeroom teachers through real-time notifications, daily health and behavioral journaling, nutritional meal transparency, attendance monitoring, and a closed-loop absence request workflow.

The platform is designed with a strict **Layered Clean Architecture** adhering to enterprise SOLID design principles, Separation of Concerns (SoC), and high-traffic concurrency standards. It features a zero-cost hybrid cloud deployment architecture leveraging Zalo's cloud-edge CDN for static frontend delivery, local Spring Boot enterprise backend, PostgreSQL 17 relational database cluster, and Cloudflare Ingress Tunneling.

---

## 2. System Architecture & C4 Model

### 2.1 C4 Context Diagram

```mermaid
flowchart TD
    subgraph Users ["User Actors"]
        P["Preschool Parent (Trần Thu Hà)"]
        T["Homeroom Teacher (Cô Lê Thanh Thảo)"]
    end

    subgraph Container ["Zalo Ecosystem Platform"]
        ZMP["SchoolConnect Zalo Mini App (React 18 + TS)"]
        ZCDN["Zalo Cloud CDN (h5.zdn.vn)"]
        ZGRAPH["Zalo Open API / Graph API v2.0"]
    end

    subgraph BackendSys ["SchoolConnect Backend Host (Port 8080)"]
        API["Spring Boot 3.3.5 Backend (Java 21)"]
        SEC["Spring Security + JWT Engine"]
        TUNNEL["Cloudflare Ingress Tunnel (cloudflared)"]
        DB[("PostgreSQL 17.5 Database (Port 5432)")]
    end

    P -->|HTTPS / WSS| ZMP
    T -->|HTTPS / WSS| ZMP
    ZMP -->|Static Assets| ZCDN
    ZMP -->|Zalo User Token| ZGRAPH
    ZMP -->|REST API calls| TUNNEL
    TUNNEL -->|Loopback HTTP| API
    API -->|Auth Verification| SEC
    API -->|HikariCP JDBC| DB
```

### 2.2 C4 Container & Layered Architecture

The backend implements a classic enterprise **Layered Architecture**:

```
[ HTTP Ingress / Web Layer ]
       │
       ▼
Controllers (com.schoolconnect.controller.*)
  ├── AuthController          (/api/v1/auth/*)
  ├── HealthController        (/api/v1/health)
  ├── ParentController        (/api/v1/parent/*, /api/v1/children/*)
  ├── TeacherController       (/api/v1/teacher/*)
  ├── DailyReportController   (/api/v1/children/{id}/daily-reports)
  ├── AttendanceController    (/api/v1/children/{id}/attendance)
  ├── MealMenuController      (/api/v1/meal-menus)
  └── AnnouncementController  (/api/v1/announcements)
       │
       ▼
Security & Interceptor Layer (com.schoolconnect.security.*, com.schoolconnect.config.*)
  ├── JwtAuthenticationFilter (HMAC-SHA256 Token Validation)
  ├── CorsConfig              (Allowed Origin Patterns & Headers)
  └── GlobalExceptionHandler  (Structured RFC 7807 Error Envelopes)
       │
       ▼
Service / Business Logic Layer (com.schoolconnect.service.*)
  ├── AuthService, ParentService, TeacherService, ChildService, DailyReportService...
  └── Transaction Boundaries (@Transactional, Propagation.REQUIRED)
       │
       ▼
Data Access & Persistence Layer (com.schoolconnect.repository.*, entity.*)
  ├── 8 Spring Data JPA Repositories
  └── 8 Hibernate Entities (Audit timestamps, Enum Type-safe mapping)
       │
       ▼
Storage Layer: PostgreSQL 17.5 Database (schoolconnect)
```

---

## 3. Functional Modules & Vertical Slices

The system is delivered across 4 cohesive, independently verifiable Vertical Slices:

| Vertical Slice | Scope | Implemented Endpoints | Business Capabilities |
| :--- | :--- | :--- | :--- |
| **`VS-01` Core Foundation & Parent Home** | Database, Dual Auth, Dashboard | `POST /auth/demo-session`<br>`POST /auth/zalo-login`<br>`GET /parent/children`<br>`GET /children/{id}/dashboard` | - Instant Parent/Teacher Role Switcher<br>- Linked Child Selector (Minh An / Minh Khôi)<br>- Dynamic BMI Calculator with WHO pediatric health status badge<br>- Quick-action navigation grid |
| **`VS-02` Daily Care & Health Modules** | Daily journal & Attendance | `GET /children/{id}/daily-reports`<br>`GET /children/{id}/attendance` | - Sổ bé ngoan: Meal intake, Nap hours, Hygiene & Homeroom Teacher's daily remarks<br>- Attendance history calendar, monthly rate |
| **`VS-03` School Communication** | Menus & Announcements | `GET /meal-menus`<br>`GET /announcements` | - Nutritional preschool meal menu (Breakfast, Lunch, Afternoon snack, Calorie counts)<br>- Categorized school announcements (Pinned notices, Emergency events) |
| **`VS-04` Closed-Loop Absence Workflow** | Transactional Leave Management | `POST /children/{id}/leave-requests`<br>`GET /leave-requests`<br>`PATCH /leave-requests/{id}/cancel`<br>`GET /teacher/leave-requests`<br>`PATCH /teacher/leave-requests/{id}/review` | - Parent submits multi-day absence request with session type (`FULL_DAY`, `MORNING`, `AFTERNOON`)<br>- Parent cancels pending requests<br>- Teacher review queue with status filter (`PENDING`, `APPROVED`, `REJECTED`)<br>- Teacher approval or rejection with mandatory feedback note |

---

## 4. RESTful API Specification (OpenAPI 3.0 Contract)

All API responses strictly implement a standard unified response envelope:
```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": { ... },
  "errorCode": null,
  "fieldErrors": null,
  "timestamp": "2026-11-05T14:00:00.000+07:00"
}
```

### Complete Endpoints Inventory

```
+--------+-------------------------------------------------+------------------------+-----------------------------------------+
| METHOD | ENDPOINT                                        | AUTHENTICATION         | PURPOSE                                 |
+--------+-------------------------------------------------+------------------------+-----------------------------------------+
| GET    | /api/v1/health                                  | Public                 | Infrastructure & Database Health Check  |
| POST   | /api/v1/auth/demo-session                       | Public                 | Instant Demo Login (PARENT/TEACHER)     |
| POST   | /api/v1/auth/zalo-login                         | Public                 | Zalo Mini App OAuth Token Verification  |
| GET    | /api/v1/parent/children                         | Bearer JWT (PARENT)    | List children linked to authenticated PH|
| GET    | /api/v1/children/{id}/dashboard                 | Bearer JWT (PARENT)    | Home Dashboard summary, attendance, BMI |
| GET    | /api/v1/children/{id}/daily-reports             | Bearer JWT (PARENT)    | Sổ bé ngoan daily journal by date       |
| GET    | /api/v1/children/{id}/attendance                | Bearer JWT (PARENT)    | Monthly attendance records & percentage |
| GET    | /api/v1/meal-menus                              | Bearer JWT (PARENT)    | Daily nutrition menu & calories         |
| GET    | /api/v1/announcements                           | Bearer JWT (PARENT)    | Broadcast notices & categorized alerts  |
| POST   | /api/v1/children/{id}/leave-requests            | Bearer JWT (PARENT)    | Submit new absence leave request        |
| GET    | /api/v1/leave-requests                          | Bearer JWT (PARENT)    | View submitted absence history          |
| PATCH  | /api/v1/leave-requests/{id}/cancel              | Bearer JWT (PARENT)    | Cancel pending absence request          |
| GET    | /api/v1/teacher/leave-requests                  | Bearer JWT (TEACHER)   | Homeroom teacher review queue           |
| PATCH  | /api/v1/teacher/leave-requests/{id}/review      | Bearer JWT (TEACHER)   | Approve or Reject leave request         |
+--------+-------------------------------------------------+------------------------+-----------------------------------------+
```

Swagger UI documentation is accessible at: `http://localhost:8080/swagger-ui.html`.

---

## 5. Database Schema & Data Dictionary

The database consists of **8 normalized relational tables** in third normal form (3NF), indexed for sub-50ms query latency:

```mermaid
erDiagram
    USERS ||--o{ PARENTS : "profile"
    USERS ||--o{ TEACHERS : "profile"
    CLASSES ||--|{ TEACHERS : "supervised_by"
    CLASSES ||--o{ STUDENTS : "enrolled_in"
    PARENTS ||--|{ PARENT_STUDENTS : "guardian_of"
    STUDENTS ||--|{ PARENT_STUDENTS : "associated_with"
    STUDENTS ||--o{ DAILY_REPORTS : "receives"
    STUDENTS ||--o{ ATTENDANCE_RECORDS : "tracks"
    STUDENTS ||--o{ LEAVE_REQUESTS : "applies"
    TEACHERS ||--o{ LEAVE_REQUESTS : "reviews"
    CLASSES ||--o{ ANNOUNCEMENTS : "targets"
```

### Table Definitions & Key Constraints

1. **`users`**: Master authentication identity table. Stores `phone`, `role` (`PARENT`, `TEACHER`, `ADMIN`), and optional `zalo_user_id`.
2. **`parents`**: Parent profile (`full_name`, `email`, `relationship_type`).
3. **`teachers`**: Teacher profile (`full_name`, `qualification`, `employee_code`).
4. **`classes`**: Preschool classrooms (`Lớp Chồi 1`, `Lớp Mầm 2`, academic year `2025-2026`).
5. **`students`**: Student records (`student_code`, `full_name`, `gender`, `date_of_birth`, `height_cm`, `weight_kg`).
6. **`parent_students`**: Many-to-Many junction mapping parents to their respective biological or adopted children.
7. **`daily_reports`**: Sổ bé ngoan journal entries (`meal_status`, `sleep_duration_minutes`, `nap_status`, `hygiene_notes`, `teacher_notes`, `report_date`).
8. **`attendance_records`**: Timestamped check-in/check-out logs and status (`PRESENT`, `ABSENT_EXCUSED`, `ABSENT_UNEXCUSED`).
9. **`meal_menus`**: Daily preschool nutrition schedule (`breakfast`, `lunch`, `afternoon_snack`, `total_calories`, `menu_date`).
10. **`announcements`**: School notices (`category`, `title`, `content`, `is_pinned`, `publish_date`).
11. **`leave_requests`**: Transactional absence records (`request_code`, `start_date`, `end_date`, `session_type`, `reason`, `status`, `reviewed_by`, `teacher_note`).

---

## 6. Security, Authentication & Authorization Architecture

### 6.1 Dual Authentication Mechanism
- **Mode 1 (`DEMO_MODE=true`):** Allows immediate evaluator evaluation without requiring Zalo Official Account (OA) binding or KYB enterprise registration. Evaluators switch effortlessly between Parent (`id=1` - *Trần Thu Hà*) and Teacher (`id=2` - *Cô Lê Thanh Thảo*) via an interactive header toggle.
- **Mode 2 (`ZALO_AUTH`):** Obtains the Zalo Access Token via `zmp-sdk.getAccessToken()`, validates the signature against Zalo Graph API (`https://graph.zalo.me/v2.0/me`), resolves the user identity, and automatically falls back to Demo Parent if external network verification is constrained.

### 6.2 Token Security
- All authenticated sessions issue signed **JSON Web Tokens (JWT)** using HMAC-SHA256 (`HS256`).
- Cryptographic keys are configured via the `JWT_SECRET` environment variable with a minimum 256-bit entropy requirement.
- Tokens enforce an expiration window (`JWT_EXPIRATION_MS=86400000`, 24 hours).
- Passwords and confidential credentials are never hardcoded or emitted to log streams.

### 6.3 CORS & Tunneling Protection
- The backend Spring Security filter chain validates origin headers using regex patterns matching `https://*.zdn.vn`, `https://*.loca.lt`, `https://*.trycloudflare.com`, and `http://localhost:*`.
- Frontend API client dynamically scopes tunnel bypass headers (`Bypass-Tunnel-Reminder: true`) exclusively to `loca.lt` domains to protect against CORS preflight header rejection.

---

## 7. System Configuration & Environment Variables

| Variable | Target Component | Default Value | Description |
| :--- | :--- | :--- | :--- |
| `SERVER_PORT` | Backend | `8080` | HTTP listening port for Spring Boot Tomcat |
| `DB_URL` | Backend | `jdbc:postgresql://localhost:5432/schoolconnect` | PostgreSQL connection string |
| `DB_USERNAME` | Backend | `postgres` | Database superuser account |
| `DB_PASSWORD` | Backend | `123456` | Database access credential |
| `JWT_SECRET` | Backend | `DFAEIsbSzUY3J//cJsH55Qa8lL2+...` | HMAC-SHA256 256-bit signing key |
| `JWT_EXPIRATION_MS` | Backend | `86400000` | JWT validity window (24 hours) |
| `APP_AUTH_DEMO_MODE` | Backend | `true` | Enables zero-friction dual demo login |
| `ZALO_APP_ID` | Backend/Frontend | `4485440593554869533` | Registered Zalo Application Identifier |
| `ZALO_MINI_APP_ID` | Backend/Frontend | `3878294804116801168` | Registered Zalo Mini App Identifier |
| `VITE_API_BASE_URL` | Frontend | `https://xxxx.trycloudflare.com/api/v1` | Public HTTPS Ingress Tunnel endpoint |
| `VITE_AUTH_MODE` | Frontend | `DEMO_MODE` | Frontend client authentication driver |

---

## 8. Third-Party Integrations & External Boundaries

1. **Zalo Mini App SDK (`zmp-sdk` v2.43+):** Manages webview lifecycle, header styling, safe-area insets, and user token handoff.
2. **Cloudflare Tunnel (`cloudflared` v2026.10+):** Multiplexes local port 8080 traffic over high-speed QUIC/HTTP2 tunnels to Cloudflare Edge PoPs (Hanoi/HCMC) providing automatic TLS 1.3 termination.
3. **Zalo Cloud CDN (`zmp-cli` v4.0.5):** Compiles and distributes React bundles to `zmp-3rd-resources.zmncdn.me` for instant mobile delivery.
