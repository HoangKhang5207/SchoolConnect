# Assumptions, Inferences & Uncertainty Log — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect |
| **Document ID** | `DOC-00-ASSUMPTIONS` |
| **Phase** | Phase 00 — Project Initialization & Context Discovery |
| **Last Updated** | 2026-11-04 |

---

## 2. Classification Taxonomy

- **`CONFIRMED`**: Explicitly verified by project evidence (`SRC-001` through `SRC-007`) or workspace inspection.
- **`INFERRED`**: Logically derived with high confidence from available project evidence, but not yet formally signed off as a hard requirement.
- **`ASSUMED`**: Working assumption adopted temporarily to enable structured planning; requires Project Owner review before becoming an official requirement.
- **`UNKNOWN`**: Information currently absent from all provided sources.
- **`CONFLICTING`**: Contradictory statements or models across different sections or sources.

---

## 3. Confirmed Facts (`CONFIRMED`)

| ID | Category | Statement | Evidence / Source |
| :--- | :--- | :--- | :--- |
| `CF-01` | Project Nature | The project is a pre-probation **Technical Test** for **TitKul**, aimed at demonstrating practical end-to-end delivery (`Build -> Deploy -> Demo`). | `SRC-001` (`P0`, `P1`, `P20`, `P22`) |
| `CF-02` | Project State | The workspace `d:\Coding\TitKul_ProjectTest\SchoolConnect` is a **Greenfield** directory containing only `Requirements.docx` prior to Phase 00 initialization. No Git repository or source code exists yet. | Workspace & Git Inspection |
| `CF-03` | Product Pivot | The initial *TitKul Mini Hub* (Service Booking) concept in Part 1 of `Requirements.docx` was explicitly superseded in Part 2 in favor of **SchoolConnect** (a Zalo Mini App simulating TitKul's school management ecosystem). | `SRC-001` (`P447`, `P472`–`P482`), Workspace Name |
| `CF-04` | Reference Domain | TitKul's core commercial products include **HDSchool** (Primary/Secondary/High School management), **H2School** (Preschool management), and **TitKul MiniApp** (Zalo channel for parents & teachers). | `SRC-001` (`P450`–`P469`), `SRC-004` |
| `CF-05` | Visual Constraint | The 5 uploaded screenshots (`SRC-002` to `SRC-006`) are strictly for reference. Imitating or copying their UI is strictly prohibited (`Tuyệt đối không được bắt chước giống`). | `SRC-007` (User Prompt) |
| `CF-06` | Data Privacy | All demo data must be 100% synthetic. No real student, parent, or school data from TitKul may be used. | `SRC-001` (`P574`) |
| `CF-07` | Platform Constraint | Zalo Mini App `Development` and `Testing` versions can only be opened by Zalo accounts added to the Mini App's Developer/Admin group; general public access requires Zalo `Live` review and approval. | `SRC-001` (`P266`, `P656`–`P660`) |
| `CF-08` | Network/CORS Constraint | When running inside the Zalo Mini App container, Backend API requests require **HTTPS** and explicit **CORS** configuration allowing origin `https://h5.zdn.vn` (including `OPTIONS` preflight requests). | `SRC-001` (`P662`) |

---

## 4. Inferred Information (`INFERRED`)

| ID | Category | Statement | Rationale & Source |
| :--- | :--- | :--- | :--- |
| `INF-01` | Target Product Scope | **SchoolConnect** (Preschool Parent Portal inspired by `H2School`) is the intended product to build, rather than *TitKul Mini Hub* (Booking) or a Teacher Portal. | Workspace folder is named `SchoolConnect`; `SRC-001` Part 2 (`P478`–`P744`) details `SchoolConnect` as the final recommended direction. |
| `INF-02` | Target Users | Primary end-users of the Mini App are **Preschool Parents / Guardians** (`Phụ huynh`), with a secondary persona being the **TitKul Evaluator / Director** testing the flow. | `SRC-001` (`P473`, `P479`–`P481`, `P710`–`P725`). |
| `INF-03` | Core Tech Stack | The confirmed stack is **React + TypeScript + ZaUI + Zalo Mini App SDK** (Frontend), **Java 21 + Spring Boot 3.x** using **Layered Architecture (`Controller -> Service -> Repository -> Entity/DTO`)** (Backend), and **PostgreSQL** (Database). | Confirmed by Project Owner Directive at Phase 02 Entry & `SRC-001` (`P77`–`P81`, `P514`–`P536`). |
| `INF-04` | Core MVP Screens & Flows | The MVP comprises 6 screens (`Home Dashboard`, `Daily Report`, `Attendance`, `Meal Menu`, `Announcements`, `Absence Request`), with **Absence Request (`leave_requests`)** serving as the primary transactional write flow. | `SRC-001` (`P491`–`P509`, `P577`–`P603`). |
| `INF-05` | Dual Auth Mode | Supporting both `DEMO_MODE` (pre-seeded parent session for instant evaluator testing) and `ZALO_AUTH` (Zalo token exchange -> internal JWT + guardian-student check) provides the best balance between reliability during demo and technical depth. | `SRC-001` (`P604`–`P624`, `T737`). |

---

## 5. Temporary Working Assumptions (`ASSUMED`)

> **Important Rule:** None of the assumptions below may be treated as official business requirements until reviewed and approved by the Project Owner (`Nguyễn Võ Hoàng Khang`).

| ID | Category | Working Assumption | Impact if Invalidated |
| :--- | :--- | :--- | :--- |
| `ASM-01` | Repository Layout | A single monorepo at `d:\Coding\TitKul_ProjectTest\SchoolConnect` containing `frontend/`, `backend/`, `database/`, `infra/`, and `docs/` will be used. | Would require splitting into multiple repositories if separate Git repos are preferred. |
| `ASM-02` | Build Tooling | Backend uses **Gradle** (`build.gradle`, as shown in `P111` and `P690`) or **Maven** depending on local environment readiness; Database schema migrations use Flyway/Liquibase or SQL init scripts via Docker Compose. | Minor adjustment to Backend project scaffolding in Phase 02/03. |
| `ASM-03` | Scope Boundary | Teacher/Admin data entry screens (creating menus, recording daily reports, approving leave requests) are **out of scope** for the Mini App UI and will be pre-populated via PostgreSQL seed data and/or demonstrable via Swagger UI. | If a Teacher/Admin UI is also required inside the Mini App, frontend scope will increase by ~40–60%. |
| `ASM-04` | Budget & Hosting | Zero-cost or existing personal dev hosting (e.g., local Docker + cloud tunnel / VPS / PaaS free-to-low tier with HTTPS) will be used for the Backend & PostgreSQL demo environment. | Requires provisioning a specific server/domain before Sprint 4/5 deployment. |

---

## 6. Unknowns (`UNKNOWN`)

| ID | Category | Missing / Unknown Information |
| :--- | :--- | :--- |
| `UNK-01` | Schedule / Deadline | Exact calendar deadline for submitting the Technical Test to TitKul (`Requirements.docx` proposes a 5-day or 6-day effort plan, but does not state the actual due date). |
| `UNK-02` | Zalo Platform Credentials | Whether the Project Owner already has a registered **Zalo Developer Account** and a **Zalo Mini App ID**, or if TitKul provided one (`P430`–`P434`). |
| `UNK-03` | Demo Delivery Mode | Whether the TitKul Director will provide their Zalo account to be added as a Developer (`Option A`) or if the evaluation will be conducted via live screen sharing / video (`Option B`) (`P645`–`P655`). |
| `UNK-04` | Hosting Infrastructure | Whether the Project Owner has an existing VPS, cloud account (Render, Railway, Fly.io, AWS, etc.), or custom domain with HTTPS for deploying the Spring Boot Backend. |
| `UNK-05` | Detailed Field-Level Rules | Exact field definitions, validation rules (e.g., past-date leave request restrictions, multi-day absence ranges, attachment support), and status transition rules for the 8 `SchoolConnect` database tables (`T561`). |

---

## 7. Conflicting Information (`CONFLICTING`)

| ID | Conflict Topic | Source A | Source B | Resolution Status |
| :--- | :--- | :--- | :--- | :--- |
| `CNF-01` | **Product Concept & Domain** | `SRC-001` Part 1 (`P0`–`P444`): Proposes **TitKul Mini Hub** (Service Discovery & Booking App with 3 tables: `users`, `services`, `bookings` and 5-day roadmap). | `SRC-001` Part 2 (`P446`–`P744`) & User Prompt: Explicitly rejects Service Booking and proposes **SchoolConnect** (Preschool Parent Portal inspired by `H2School` with 8 tables and 6-day roadmap). | **Pending Owner Confirmation** (Strong evidence favors Source B / `SchoolConnect`, but requires formal confirmation before Phase 01). |
| `CNF-02` | **User Role in Preschool Mini App** | `SRC-001` Part 2 (`P478`–`P509`): Defines `SchoolConnect` strictly as a **Parent Portal** (`Phụ huynh` viewing their child's dashboard, daily report, attendance, meal menu, announcements, and submitting leave requests). | `SRC-004` (`Trường Mầm Non 11` screenshot): Displays a **Teacher Portal** (`Xin chào giáo viên Titkul T11`) with 10 modules (`Đơn nghỉ phép`, `Điểm danh`, `Sổ bé ngoan`, `Thực đơn`, `Chỉ số BMI`, `Biểu phí`, `Cân AI`, `Quản lý bán trú`, `Nhắc thuốc`, `ATTP`). | **Pending Owner Confirmation** (Need to confirm whether `SchoolConnect` is strictly Parent Portal as in `SRC-001` Part 2, or needs a Teacher/Admin role toggle). |
| `CNF-03` | **Implementation Timeline** | `SRC-001` Part 1 (`P185`, `P425`): 5 working days (Sprint 0 to Final Sprint). | `SRC-001` Part 2 (`P626`, `T627`, `T737`): 6 working days (Day 1 to Day 6). | **Pending Owner Confirmation** (Depends on actual submission deadline `UNK-01`). |

