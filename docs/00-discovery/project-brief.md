# Project Brief — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect |
| **Document ID** | `DOC-00-BRIEF` |
| **Phase** | Phase 00 — Project Initialization & Context Discovery |
| **Project Owner** | Nguyen Vo Hoang Khang (`Nguyễn Võ Hoàng Khang`) |
| **Classification** | Type 1 — Completely New Application (Greenfield Project) |
| **Current Status** | Discovery Completed — Pending Approval Gate for Phase 01 |
| **Last Updated** | 2026-11-04 |

---

## 2. Executive Summary & Project Context

**SchoolConnect** is a pre-probation Technical Test project developed for **TitKul**. The primary objective of this project is to demonstrate end-to-end software engineering capability across platform research, UI/UX design, frontend development on the **Zalo Mini App** ecosystem, backend RESTful API engineering with **Java Spring Boot**, relational data persistence with **PostgreSQL**, authentication/authorization design, and real-world deployment/demonstration.

While the input document (`Requirements.docx`) contains two chronological planning iterations:
1. **Iteration 1 (Superseded):** *TitKul Mini Hub* — a generic Service Discovery & Booking MVP.
2. **Iteration 2 (Active Direction):** *SchoolConnect* — a **Zalo Mini App Parent Portal (H2School-inspired Preschool Edition)**.

Aligned with the explicit workspace name (`SchoolConnect`), the user prompt, and TitKul's core EdTech product portfolio (`H2School` for preschools and `HDSchool` for K-12), **SchoolConnect** is established as an independent concept MVP that enables preschool parents to monitor their children's attendance, daily activities, nutrition/meal menus, school announcements, and submit/track leave (absence) requests directly within Zalo.

---

## 3. Project Information Matrix

| Dimension | Extracted / Confirmed Details | Status | Source Reference |
| :--- | :--- | :--- | :--- |
| **Project Name** | `SchoolConnect` (Zalo Mini App – Parent Portal) | `CONFIRMED` | User Prompt, `SRC-001` (Part 2, P478) |
| **Project Type** | Completely New Application (Greenfield MVP for Technical Test) | `CONFIRMED` | User Prompt, Workspace Inspection |
| **Project Owner** | Nguyen Vo Hoang Khang (`Nguyễn Võ Hoàng Khang`) | `CONFIRMED` | User Prompt |
| **Business Domain** | EdTech — Preschool Management & Parent-School Communication (`H2School`-inspired) | `CONFIRMED` | `SRC-001` (P457–P488), `SRC-004` |
| **Target Users** | Primary: **Parents / Guardians** of preschool students.<br>Secondary (Demo/Evaluator): **TitKul Director / Technical Evaluator**. | `INFERRED` | `SRC-001` (P473, P479–P481), User Prompt |
| **Preferred Tech Stack** | - **Frontend:** React + TypeScript, Zalo Mini App SDK (`zmp-sdk`), ZaUI (`zmp-ui`), ZMP CLI / Vite, Custom CSS<br>- **Backend:** Java 21 + Spring Boot 3.x (Spring Web, Spring Data JPA/Hibernate, Jakarta Validation, Spring Security, Springdoc OpenAPI)<br>- **Database:** PostgreSQL<br>- **DevOps & Tools:** Docker (`docker-compose`), Git + GitHub/GitLab, Swagger UI, Postman | `PROPOSED / INFERRED` | `SRC-001` (P513–P558, T737) |
| **Architecture Pattern** | Layered Architecture Backend (`Controller -> Service -> Repository -> Entity/DTO`) + Zalo Mini App Frontend + PostgreSQL Database | `CONFIRMED` | Project Owner Directive, `SRC-001` (P77–P81) |
| **Budget Constraints** | Not explicitly stated in monetary terms; constrained to lightweight Dev/Testing resources suitable for an individual technical test (no paid enterprise cloud infra like Kafka/Redis/K8s; avoid Zalo Live publication overhead). | `INFERRED / UNKNOWN` | `SRC-001` (P67, P661) |
| **Deadline / Timeline** | Proposed **6 focused working days** (or 5 days in Part 1). Exact calendar submission deadline from TitKul is not specified in the document. | `ASSUMED / UNKNOWN` | `SRC-001` (P626, T627, T737) |
| **Deployment Target** | - **Mini App:** Zalo Testing Version (accessed via QR Code / Deep Link for authorized Developer/Admin accounts, or live screen mirroring)<br>- **Backend:** HTTPS Dev Server (with CORS configured for `https://h5.zdn.vn`)<br>- **Database:** PostgreSQL Dev Instance | `PROPOSED / INFERRED` | `SRC-001` (P644–P663, T737) |
| **Repository Root** | `d:\Coding\TitKul_ProjectTest\SchoolConnect` (Proposed monorepo structure containing `frontend/`, `backend/`, `database/`, `infra/`, `docs/`) | `CONFIRMED` | User Prompt, `SRC-001` (P665–P706) |

---

## 4. Initial MVP Scope Summary (Proposed in `SRC-001` Part 2)

### 4.1. In-Scope Core Screens & Features (`Must Have`)
1. **Home Dashboard (`01. Home Dashboard`):** Child profile switcher/summary, class info, today's attendance status, recent school announcements.
2. **Daily Report (`02. Daily Report`):** Daily activity log covering meals, nap/sleep quality, learning/play activities, and child's mood.
3. **Attendance (`03. Attendance`):** Attendance calendar/history, present/absent status, check-in/check-out records.
4. **Meal Menu (`04. Meal Menu`):** Daily/weekly nutrition and meal menu breakdown (Breakfast, Lunch, Afternoon Snack).
5. **Announcements (`05. Announcements`):** School-wide and class-level notifications from the school and teachers.
6. **Absence Request (`06. Absence Request`):** End-to-end write flow allowing parents to submit a leave request for their child, persisted in PostgreSQL with guardian-student ownership validation, and view leave request history/status (`PENDING`, `CONFIRMED`/`APPROVED`, `CANCELLED`).

### 4.2. Deferred / Out-of-Scope Items
- **Deferred (`Later`):** BMI tracking (`Theo dõi BMI`).
- **Explicitly Out of Scope (`Out of scope`):** Tuition fee payment (`Thanh toán học phí`), AI nutrition analysis (`AI phân tích dinh dưỡng`), Facial Recognition attendance (`Face ID`), Department of Education (`Sở Giáo dục`) data synchronization, and complex distributed infrastructure (Microservices, Redis, Kafka, Elasticsearch, Kubernetes).

---

## 5. Key Design & Compliance Constraints
1. **Strict Visual Originality:** The 5 reference screenshots provided by the Project Owner (`SRC-002` to `SRC-006`) are strictly for context and pattern awareness. The UI/UX of **SchoolConnect** must **never** copy or imitate their visual layout or branding.
2. **Synthetic Data & Privacy:** All student, parent, health, and school records in the demo environment must be 100% synthetic. No real PII or proprietary TitKul customer/school data may be used.
3. **Brand & Account Governance:** Do not register or publish a public Live Zalo Mini App using TitKul's corporate identity without explicit authorization.
4. **True End-to-End Persistence:** All mutations (such as submitting an absence request) must execute real transactions against PostgreSQL via the Spring Boot REST API and survive app reloads.

