# Sprint Plan, Effort Estimation & Quality Delivery Policy — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect |
| **Document ID** | `DOC-02-SPRINT-PLAN` |
| **Phase** | Phase 04 — Implementation Planning & Engineering Breakdown (Updated) |
| **Target Demo Deadline** | **2026-11-05 15:00 (GMT+7)** |
| **Last Updated** | 2026-11-04 |

---

## 2. Compressed Sprint Execution Plan (4 Focused Time-Boxed Sprints)

Because the live demonstration is scheduled for **15:00 tomorrow (2026-11-05)** (~22.5 wall-clock hours remaining, representing **12.0 to 16.0 active AI-assisted engineering hours**), execution is organized into **4 Focused Micro-Sprints** aligned 1-to-1 with Milestones `M1`–`M4`:

| Sprint | Time Window (GMT+7) | Target Milestone & Vertical Slice | Included User Stories & Tasks | Sprint Goal & Deliverable |
| :--- | :--- | :--- | :--- | :--- |
| **Sprint 1** | `04/11 17:00 – 20:30` *(3.0 – 3.5 hrs)* | **`M1` / `VS-01`**: Foundation, Layered Backend, DB Seed, Dual Auth & Dashboard | `US-001`, `US-002`, `US-003`, `US-004`, `US-005` (`TASK-001`..`TASK-014`) | Running PostgreSQL + Spring Boot Layered Backend + React Mini App with `DEMO_MODE`, Role Switcher, Multi-Child Switcher, and `01. Home Dashboard`. |
| **Sprint 2** | `04/11 20:30 – 23:30` *(2.5 – 3.0 hrs)* | **`M2` / `VS-02 & VS-03`**: Preschool Care & School Communication Read Modules | `US-006`, `US-007`, `US-008`, `US-009` (`TASK-015`..`TASK-022`) | End-to-end delivery of `02. Daily Report`, `03. Attendance`, `04. Meal Menu`, and `05. Announcements` with full Empty/Loading/Error states. |
| **Sprint 3** | `05/11 06:30 – 10:00` *(3.0 – 3.5 hrs)* | **`M3` / `VS-04`**: Closed-Loop Transactional Leave Workflow (Parent + Teacher) | `US-010`, `US-011`, `US-012` (`TASK-023`..`TASK-030`) | End-to-end Parent Leave Request submission/cancellation (`06. Absence Request`) and Teacher Leave Review Queue (`Approve`/`Reject` with note). |
| **Sprint 4** | `05/11 10:00 – 13:30` *(2.5 – 3.5 hrs)* | **`M4`**: Automated Tests, Docker Compose, Zalo Build, Docs & Demo Rehearsal | Cross-cutting (`TASK-031`..`TASK-036`) | JUnit 5 tests green, `docker-compose.yml` verified, Zalo Mini App config ready, `README.md` & `demo-script.md` finalized, 90-min buffer before 15:00 demo. |

---

## 3. Estimation & Resource Planning

Estimates are expressed in **realistic effort ranges (hours)** and **relative sizing (`S`, `M`, `L`)** based on AI-assisted pair programming between the Project Owner (`Nguyễn Võ Hoàng Khang`) and the AI Software Engineering Assistant.

### 3.1. Effort Breakdown by Activity Category

| Activity Category | Estimated Effort Range (Hours) | Share of Total (%) | Confidence Level | Key Tasks Covered |
| :--- | :--- | :--- | :--- | :--- |
| **1. Development Effort** | `6.5 – 8.5 hrs` | ~52% | **High (85%)** | Layered Backend (`controller`, `service`, `repository`, `entity`, `dto`, `security`), PostgreSQL schema/seed, 6 Parent screens + 1 Teacher Review screen + Role/Child Switchers (`TASK-001`..`TASK-030`). |
| **2. Integration Effort** | `1.5 – 2.0 hrs` | ~12% | **High (85%)** | Frontend-to-Backend REST API wiring, JWT/Demo session interceptors, Zalo SDK wrapper with browser fallback, CORS preflight verification. |
| **3. Testing Effort** | `1.5 – 2.0 hrs` | ~12% | **High (90%)** | JUnit 5 + Mockito service/controller tests (`LeaveRequestService`, `GuardianAccessService`), Frontend build & typecheck, E2E verification of `AC-001`..`AC-019` (`TASK-031`, `TASK-032`, `TASK-036`). |
| **4. Documentation Effort** | `1.0 – 1.5 hrs` | ~9% | **High (95%)** | OpenAPI/Swagger annotations, root `README.md` (15 sections), `docs/demo-script.md`, architecture/ERD docs (`TASK-035`). |
| **5. Deployment Effort** | `1.0 – 1.5 hrs` | ~8% | **Medium-High (80%)** | `infra/docker-compose.yml`, Backend `Dockerfile`, Zalo Mini App `app-config.json` & HTTPS Tunnel / Testing build (`TASK-033`, `TASK-034`). |
| **6. Contingency for Uncertainty** | `1.0 – 2.0 hrs` | ~7% | **Buffer** | Reserved buffer (`13:30 – 15:00` on 05/11 + intra-sprint buffer) for unexpected Gradle/npm cache issues, port conflicts, or Zalo CLI quirks. |
| **TOTAL ESTIMATED EFFORT** | **`12.5 – 17.5 hrs`** | **100%** | **High (85%)** | Fits comfortably within the available window prior to **2026-11-05 15:00 (GMT+7)**. |

---

### 3.2. Estimation Assumptions & Sensitivity Factors

#### Estimation Assumptions
1. **Local Tooling Readiness:** Verified in Phase 00 that Node.js `v24.16.0`, npm `11.13.0`, JDK `21.0.5`, PostgreSQL `17.0.5`, Docker, and Git `2.47.0` are already installed on the machine.
2. **Scope Discipline:** Scope is strictly locked to the approved MVP (`6 Parent Screens + 1 Teacher Review Screen + Dual Auth + 8 DB Tables`). No unapproved scope additions will be introduced during Sprints 1–4.
3. **Layered Architecture Simplicity:** Using standard **Layered Architecture** (`controller -> service -> repository -> entity/dto`) eliminates cross-module boundary boilerplate and accelerates Spring Boot implementation.

#### Factors That Could Cause Estimates to Change
- **Network / Package Registry Latency:** Slow Maven Central or npm registry downloads when initializing Gradle Wrapper or Frontend packages (mitigated by keeping dependencies minimal and standard).
- **Zalo Testing Environment Access:** If binding a live Zalo App ID takes extra time on the Zalo Developer portal, `DEMO_MODE` ensures 100% of business workflows remain demonstrable without delay.

---

## 4. Quality & Delivery Governance Policy

### 4.1. Definition of Ready (`DoR` — Định nghĩa sẵn sàng)
A User Story or Task may only enter active implementation when:
1. Its business objective, input/output contract, and business rules (`RULE-xxx`) are clearly defined in `docs/01-product/requirements.md` and `docs/01-product/business-rules.md`.
2. Its testable `Given / When / Then` Acceptance Criteria (`AC-xxx`) are documented in `docs/01-product/acceptance-criteria.md`.
3. All upstream dependencies (e.g., database tables or auth filters) are either completed or scheduled first in the same vertical slice.
4. No `BLOCKING` open questions remain for that item.

### 4.2. Definition of Done (`DoD` — Định nghĩa hoàn thành)
A User Story or Milestone is considered **Done** only when ALL of the following hold true:
1. **Complete Code (Zero Placeholders):** Implementation is 100% functional with zero `// TODO` comments or mocked hardcodes where database persistence is required.
2. **Layered Architecture Compliance:** Backend code strictly follows `Controller -> Service -> Repository -> Entity/DTO` separation with Jakarta Validation and structured error responses.
3. **English Code & Documentation Standard:** All source code, variable/method/class names, SQL column names, log messages, and code comments are written in English (while UI labels displayed to Vietnamese end-users are in natural Vietnamese).
4. **UX State Completeness:** Frontend screen handles `Loading`, `Success`, `Empty`, and `Error` (with Retry) states cleanly on mobile viewports (`360px`–`430px`).
5. **Automated Verification:** Backend compiles and passes JUnit 5 tests (`./gradlew test`); Frontend passes TypeScript typechecking and production build (`npm run build`).
6. **Acceptance Criteria Verified:** All mapped `AC-xxx` criteria pass against a running PostgreSQL instance.

### 4.3. Testing Policy
- **Unit Tests (`JUnit 5 + Mockito`):** Mandatory for core domain logic in `LeaveRequestService` (valid creation, date validation `RULE-002`, overlap detection `RULE-003`, parent cancel `RULE-006`, teacher approve/reject `RULE-007`/`RULE-008`) and `GuardianAccessService` (`RULE-001`).
- **Integration / API Tests:** Verify REST controllers, HTTP status codes (`200`, `201`, `400`, `401`, `403`, `409`), and CORS headers (`https://h5.zdn.vn`).
- **End-to-End UI Verification:** Verify complete user journeys in browser/Zalo simulator and confirm persistence across page reloads.

### 4.4. Git Branching, Commit & Code Review Policy
- **Repository Structure:** Single Monorepo (`d:\Coding\TitKul_ProjectTest\SchoolConnect`) with `main` branch and feature/milestone commits.
- **Conventional Commits:** All commits must follow English Conventional Commits format (`feat:`, `fix:`, `docs:`, `test:`, `chore:`, `refactor:`).
- **Code Review Gate:** Every vertical slice undergoes automated static analysis (TypeScript compiler, Java compiler, linter) and self-review against Security Rules (`Zero Hardcoding`, `Input Validation`, `Least Privilege`) before milestone sign-off.

### 4.5. Sprint Demo Expectations & Reporting Cadence
- **End-of-Milestone Checkpoints:** At the completion of each Milestone (`M1`, `M2`, `M3`, `M4`), build status, test results, and verified Acceptance Criteria are reported transparently.
- **Live Demo Standard:** The final demo at **15:00 on 2026-11-05** will operate on live services (React Mini App -> Spring Boot API -> PostgreSQL) with Swagger UI and database inspection open alongside the app, backed by a recorded demo video as contingency.

