# Project State Register — SchoolConnect

## 1. Executive Status Summary

| Attribute | Current Value |
| :--- | :--- |
| **Project Name** | **SchoolConnect** (*Zalo Mini App — Preschool Parent & Teacher Portal*) |
| **Project Owner** | Nguyen Vo Hoang Khang (`Nguyễn Võ Hoàng Khang`) |
| **Target Live Demo Deadline** | **2026-11-05 15:00 (GMT+7)** |
| **Current Lifecycle Stage** | **Phase 12 Completed — Post-Deployment Monitoring & Project Handover Complete (`PROJECT_HANDOVER_COMPLETED` — Complete Technical & User Documentation Mirrored, 100% Ready for Live Defense at 15:00)** |
| **Backend Architecture** | **Layered Architecture** (`controller` -> `service` -> `repository` -> `entity` / `dto`, plus `security`, `config`, `exception`) |
| **Frontend Stack** | React 18 + TypeScript + Vite 5 + Zalo Mini App SDK (`zmp-sdk`) + Original SchoolConnect Emerald & Warm Cream Design System |
| **Database & Infra** | PostgreSQL 16/17 (8 Normalized Tables + 7 Indexes + Idempotent Seed Data Verified) + `infra/docker-compose.yml` |
| **Quality Status** | **41/41 Technical Checks Passed — 24/24 Live E2E Scenarios Passed — 23/23 Live UAT Scenarios Passed — 5/5 Smoke Tests Passed (`DEPLOYMENT_SUCCESSFUL` & `PROJECT_HANDOVER_COMPLETED`)** |
| **Active Blockers** | **`0` System Blockers** |
| **Last Updated** | `2026-11-05T14:15:00+07:00` |

---

## 2. Phase Progression & Gate History

| Phase | Phase Name | Status | Key Deliverables Completed |
| :--- | :--- | :--- | :--- |
| **Phase 00** | Project Initialization & Context Discovery | **`APPROVED & COMPLETED`** | `docs/00-discovery/project-brief.md`, `source-register.md`, `assumptions.md`, `open-questions.md` (`OQ-01`..`OQ-05` resolved). |
| **Phase 01** | Product Discovery & Requirement Analysis (SRS) | **`APPROVED & COMPLETED`** | `docs/01-product/vision.md`, `user-personas.md`, `requirements.md`, `business-rules.md`, `user-flows.md`, `acceptance-criteria.md`. |
| **Phase 02** | Product Scope, MVP & Delivery Planning | **`APPROVED & COMPLETED`** | `docs/01-product/mvp-scope.md`, `docs/02-planning/roadmap.md`, `product-backlog.md`, `sprint-plan.md`, `dependencies.md`, `risk-register.md`. |
| **Phase 03** | System Architecture & Technical Design | **`APPROVED & COMPLETED`** | `docs/03-architecture/system-context.md`, `architecture.md`, `module-design.md`, `database-design.md`, `api-contracts.md`, `security-design.md`, `deployment-architecture.md`, `decisions/ADR-001..004.md`. |
| **Phase 04** | Implementation Planning & Engineering Breakdown | **`APPROVED & COMPLETED`** | `docs/02-planning/implementation-plan.md`, updated `product-backlog.md`, `dependencies.md`, `sprint-plan.md`, and root `PROJECT_STATE.md`. |
| **Phase 05** | Project Foundation & Development Environment | **`COMPLETED & VERIFIED`** | Root `.gitignore`, `.env.example`, `Makefile`, `scripts/dev.ps1`, `.github/workflows/ci.yml`, `infra/docker-compose.yml`, `backend/Dockerfile`, Backend Layered Foundation (`Java 21 + Spring Boot 3.3.5 + Gradle 8.10.2`), PostgreSQL 8 tables + seed data (`schema.sql`, `data.sql`). |
| **Phase 06** | Feature Implementation & AI-Assisted Development | **`COMPLETED_VERIFIED`** | All 4 Vertical Slices (`VS-01`..`VS-04`), 36 tasks (`TASK-001`..`TASK-036`), 14 REST endpoints, 6 Parent screens + Teacher Review Queue, JUnit 5 & Vitest suites (`100% pass`), `README.md`, `docs/demo-script.md`. |
| **Phase 07A**| Independent Code Review | **`COMPLETED`** | Independent code review against 12 dimensions; identified `CR-001` to `CR-006` with `REQUEST_CHANGES`. |
| **Phase 07B**| Test Execution & Defect Analysis | **`COMPLETED`** | Comprehensive QA execution (`15/16` checks passed), `test-strategy.md`, `test-cases.md`, `test-results.md`, `bug-register.md` (`BUG-001`..`BUG-006`), `security-review.md`. |
| **Phase 07C**| Quality Remediation | **`COMPLETED_VERIFIED`** | All 6 defects (`BUG-001`..`BUG-006`) resolved and verified (`FIXED_VERIFIED`). 100% test pass rate across backend & frontend with zero test contamination. |
| **Phase 08** | System Integration & End-to-End Verification | **`COMPLETED_VERIFIED`** | Full inventory of 8 integration boundaries, 5 E2E core flows, 11 failure scenarios, data consistency, and 24/24 live E2E tests verified (`100% pass`, avg latency: 49.8ms). Documented in `docs/04-testing/integration-test-report.md`. |
| **Phase 09A**| User Acceptance Test Design (UAT) | **`COMPLETED_VERIFIED`** | User-centered acceptance test plan, business vs. technical testing framework, 23 realistic UAT scenarios (positive & negative), critical scenario prioritization, missing requirement analysis, and 5-step suggested execution roadmap in `docs/06-quality/uat-matrix.md`. |
| **Phase 09B**| UAT Execution Review & Acceptance Sign-Off | **`COMPLETED_VERIFIED`** | Live execution and evidence collection for all 23 scenarios (`100% pass rate`, avg latency 34.2ms). Generated `docs/06-quality/uat-review-report.md`, updated `uat-matrix.md` with concrete evidence, 0 open defects, official recommendation `ACCEPTANCE_READY_WITH_EXPLICIT_CONDITIONS`. |
| **Phase 10** | Release Readiness Review | **`COMPLETED_VERIFIED`** | Formal evaluation across 6 readiness dimensions in `docs/07-release/release-checklist.md`. Release Candidate `v1.0.0-rc1` approved with official recommendation **`CONDITIONAL GO`** (Development/Testing deployment authorized for 15:00 Live Demo). Awaiting user deployment authorization. |
| **Phase 11A**| Deployment Planning | **`COMPLETED_VERIFIED`** | Formulated 100% Free Zero-Cost Hybrid Hosting Strategy (Zalo Testing Cloud CDN + Local Spring Boot/Postgres + HTTPS Ingress Tunnel). Authored `docs/07-release/deployment-guide.md`, `docs/07-release/rollback-plan.md`, and updated `deployment-architecture.md`. Stopped and awaiting explicit user authorization. |
| **Phase 11B**| Authorized Deployment Execution | **`COMPLETED_VERIFIED`** | Executed 10-step deployment plan via `scripts/execute_deployment.ps1`. Backend JAR (PID: 9152, port 8080), Web Simulator (PID: 7860, port 3000), and PostgreSQL (port 5432) active and healthy. 5/5 Smoke tests passed (`100% PASS`). Documented in `docs/07-release/deployment-report.md`. Official Status: **`DEPLOYMENT_SUCCESSFUL`**. |
| **Phase 12** | Post-Deployment Monitoring & Project Handover | **`COMPLETED_VERIFIED`** | Authored comprehensive technical documentation (`technical-documentation.md`), user guide (`user-guide.md`), operations runbook (`operations-runbook.md`), release notes (`release-notes.md`), final project report (`final-report.md`), mirrored all artifacts to project root, and verified complete system readiness for 15:00 Live Demo. Official Status: **`PROJECT_HANDOVER_COMPLETED`**. |

---

## 3. Milestone & Task Readiness Tracker (`36 Engineering Tasks`)

| Milestone | Scope Summary | Tasks | Status |
| :--- | :--- | :--- | :--- |
| **`EM-01` (`VS-01`)** | Git Init, PostgreSQL 8 Tables + Seed Data, Layered Backend Core, Dual Auth (`DEMO_MODE` + `ZALO_AUTH`), Role Switcher, Child Switcher, `01. Home Dashboard` | `TASK-001` .. `TASK-014` (`14 tasks`) | **`COMPLETED_VERIFIED`** |
| **`EM-02` (`VS-02`, `VS-03`)** | Preschool Daily Care & Communication Read Modules (`02. Daily Report`, `03. Attendance`, `04. Meal Menu`, `05. Announcements`) | `TASK-015` .. `TASK-022` (`8 tasks`) | **`COMPLETED_VERIFIED`** |
| **`EM-03` (`VS-04`)** | Closed-Loop Transactional Leave Workflow (`06. Absence Request` Parent Create/Cancel + `Teacher Leave Review Queue` Approve/Reject) | `TASK-023` .. `TASK-030` (`8 tasks`) | **`COMPLETED_VERIFIED`** |
| **`EM-04` (`Release`)** | JUnit 5 Unit/Integration Tests, Frontend Production Build, `docker-compose.yml`, Zalo `app-config.json`, `README.md`, `demo-script.md`, E2E Verification | `TASK-031` .. `TASK-036` (`6 tasks`) | **`COMPLETED_VERIFIED`** |

---

## 4. Known Limitations & Active Guardrails

1. **Zalo Testing Access Constraint (`CF-07`):** Standard Zalo `Development` and `Testing` builds are only accessible to accounts added to the Mini App's Developer/Admin group; therefore, `DEMO_MODE` (with interactive Parent/Teacher Role Switcher) is enabled by default for zero-friction evaluation. In addition, real Zalo accounts automatically map to Demo Parent `id=1` (`Trần Thu Hà`) during demonstration.
2. **Visual Non-Imitation Rule (`CF-05`):** The Frontend implements the original **SchoolConnect Emerald & Warm Cream Design System** (`#0F766E` / `#FAFAF7`), avoiding any imitation of the 5 reference screenshots.
3. **100% Synthetic Data (`CF-06`):** All seeded records in `data.sql` use fictional personas (*Trường Mầm Non Sao Mai*, *Chị Trần Thu Hà*, *Bé Nguyễn Minh An*, *Bé Nguyễn Minh Khôi*, *Cô Lê Thanh Thảo*).
