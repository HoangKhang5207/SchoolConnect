# Open Questions & Pending Decisions — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect |
| **Document ID** | `DOC-00-QUESTIONS` |
| **Phase** | Phase 00 — Project Initialization & Context Discovery (Updated at Phase 01 Entry) |
| **Last Updated** | 2026-11-04 |

---

## 2. Resolved Phase 00 Decisions (Signed Off by Project Owner on 2026-11-04)

| Question ID | Category | Decision Summary | Owner Resolution | Status |
| :--- | :--- | :--- | :--- | :--- |
| **`OQ-01`** | **Product & Domain Confirmation** | Select between Part 1 (*TitKul Mini Hub – Service Booking*) and Part 2 (*SchoolConnect – Preschool Parent Portal*). | **CONFIRMED:** Select **Part 2 (`SchoolConnect` — Zalo Mini App Preschool Parent Portal, H2School-inspired)** and completely reject Part 1 (*TitKul Mini Hub*). | `RESOLVED / CONFIRMED` |
| **`OQ-02`** | **Target User Role Scope** | Parent Portal only (6 screens) vs. adding a Teacher/School demo mode. | **CONFIRMED:** Build the **Parent Portal (6 screens)** AND include a quick role switcher for **Teacher/School Mode (`Demo Admin/Teacher Mode`)** to review and approve/reject student leave requests directly within the Mini App UI during the demo. | `RESOLVED / CONFIRMED` |
| **`OQ-03`** | **Timeline & Submission Deadline** | Actual submission and live demo deadline. | **CONFIRMED:** Immediate execution required for live demo at **15:00 tomorrow (`2026-11-05 15:00 GMT+7`)**. | `RESOLVED / CONFIRMED` |
| **`OQ-04`** | **Zalo Developer Account & Mini App ID** | Status of Zalo Developer Account and Mini App ID. | **CONFIRMED:** Project Owner has a personal Zalo Developer account (no corporate App ID yet). Design dual authentication: **`DEMO_MODE`** (works standalone immediately) and **`ZALO_AUTH`** (ready for App ID binding upon deployment). | `RESOLVED / CONFIRMED` |
| **`OQ-05`** | **Deployment & Backend Hosting Target** | Backend + Database deployment strategy. | **CONFIRMED:** Package Backend + PostgreSQL using **`docker-compose`**, combined with Cloud PaaS or HTTPS Tunnel for Zalo Testing. | `RESOLVED / CONFIRMED` |

---

## 3. Secondary Clarifications (Addressed in Phase 01 SRS & Business Rules)

| Question ID | Category | Proposed Standard Adopted in Phase 01 | Status |
| :--- | :--- | :--- | :--- |
| **`OQ-06`** | **Multi-Child Support** | Seed 1 demo parent linked to 2 preschool children (`Nguyễn Minh An` – Lớp Chồi 1, and `Nguyễn Minh Khôi` – Lớp Mầm 2) with a **Child Switcher** on the Home Dashboard to demonstrate multi-student data isolation. | `ADOPTED IN SRS` |
| **`OQ-07`** | **Absence Request Business Rules** | Support date range (`start_date` to `end_date`), session (`FULL_DAY`, `MORNING`, `AFTERNOON`), reason text, overlapping leave detection, parent cancellation of `PENDING` requests, and Teacher approval/rejection (`APPROVED` / `REJECTED`) with teacher note. | `ADOPTED IN SRS` |
| **`OQ-08`** | **BMI & Health Metrics** | Include read-only height, weight, and BMI summary badge on the Student Profile/Dashboard card using seeded data (`Should/Could Have`), while full BMI charting remains `Deferred`. | `ADOPTED IN SRS` |
| **`OQ-09`** | **Build Tool for Spring Boot** | Standardize on **Gradle with Gradle Wrapper (`./gradlew`)** and **Java 21**. | `ADOPTED IN SRS` |
