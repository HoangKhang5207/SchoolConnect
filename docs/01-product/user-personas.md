# Stakeholder & User Personas Analysis — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect |
| **Document ID** | `DOC-01-PERSONAS` |
| **Phase** | Phase 01 — Product Discovery & Requirement Analysis |
| **Last Updated** | 2026-11-04 |

---

## 2. Stakeholder Catalog & Role Matrix

| Stakeholder ID | Role / Title | Category | Primary Representation in System |
| :--- | :--- | :--- | :--- |
| **`STK-01`** | **TitKul Director / Technical Evaluator** | Business Owner & Evaluator | Evaluates the live system at 15:00 on 2026-11-05; uses the **Role Switcher** (`Parent Mode` <-> `Teacher Mode`), Swagger UI, and Database viewer to verify end-to-end engineering quality. |
| **`STK-02`** | **Preschool Parent / Guardian (`Phụ huynh`)** | External End-User / Customer | Primary user of the **Parent Portal** (`ROLE_PARENT`), linked to one or more preschool children (`Nguyễn Minh An`, `Nguyễn Minh Khôi`). |
| **`STK-03`** | **Homeroom Teacher / School Reviewer (`Giáo viên / Nhà trường`)** | Internal Staff | User of the **Teacher Demo Mode** (`ROLE_TEACHER`), responsible for viewing class leave requests and approving/rejecting (`APPROVED` / `REJECTED`) pending absence requests with a response note. |
| **`STK-04`** | **Candidate / Project Owner (`Nguyễn Võ Hoàng Khang`)** | System Developer & Administrator | Configures environment (`DEMO_MODE` / `ZALO_AUTH`), manages Docker/Database deployment, and conducts the 10–15 minute live demonstration. |
| **`STK-05`** | **Zalo Platform (`Zalo Mini App Container & Open API`)** | External System Actor | Hosts the Mini App runtime (`https://h5.zdn.vn`) and provides `getAccessToken` / user profile verification when `ZALO_AUTH` mode is active. |

---

## 3. Detailed User Personas

### 3.1. Persona 1 — Preschool Parent (`Chị Trần Thu Hà`) — *Primary External User*
- **Role Identifier:** `ROLE_PARENT`
- **Demographics & Context:** 32-year-old working mother with two children attending *Trường Mầm Non Sao Mai*:
  - Child 1: **Nguyễn Minh An** (4 years old, *Lớp Chồi 1*).
  - Child 2: **Nguyễn Minh Khôi** (2.5 years old, *Lớp Mầm 2*).
- **Goals:**
  - Quickly check in the morning whether both children have arrived safely at school and been marked present.
  - Check what her children ate for breakfast, lunch, and afternoon snack, how long they napped, and whether the teacher left any health/mood notes.
  - Submit an absence request in under 30 seconds when a child has a fever or family trip, and know immediately when the teacher approves it.
- **Responsibilities:**
  - Provide accurate absence dates, session (`Full Day`, `Morning`, `Afternoon`), and clear reasons when requesting leave.
  - Review school announcements regarding vaccinations, holidays, or events.
- **Permissions & Access Control:**
  - **Read Access:** Only her own profile, her linked children's profiles (`parent_students` mapping), her children's attendance records, her children's daily reports, school meal menus, and school/class announcements.
  - **Write Access:** Create new leave requests (`POST /api/v1/children/{id}/leave-requests`) and cancel her own `PENDING` leave requests (`PATCH /api/v1/leave-requests/{id}/cancel`).
  - **Restrictions:** Strictly prohibited from viewing or modifying any unlinked student's data or approving leave requests.
- **Main Interactions:**
  - Switching active child (`Minh An` <-> `Minh Khôi`) on the Home Dashboard.
  - Viewing Daily Report cards (meals, sleep, mood, teacher note).
  - Browsing monthly/weekly Attendance logs and Daily Meal Menus.
  - Submitting a Leave Request and checking its live status (`PENDING` -> `APPROVED` / `REJECTED`).
- **Pain Points:**
  - Group Zalo messages from the class are noisy (50+ messages/day); she misses important school notices.
  - Having to manage two children in different classes makes it confusing to track which update belongs to which child.
- **Expected Outcomes:**
  - One-tap child switching, clear visual status badges, and instant confirmation that her child's leave request was recorded and approved by the homeroom teacher.

---

### 3.2. Persona 2 — Preschool Homeroom Teacher (`Cô Lê Thanh Thảo`) — *Internal Staff (Demo Admin/Teacher Mode)*
- **Role Identifier:** `ROLE_TEACHER`
- **Demographics & Context:** Homeroom teacher at *Trường Mầm Non Sao Mai*, managing daily class operations and parent communications.
- **Goals:**
  - View all incoming student leave requests in a single structured list filtered by status (`PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`).
  - Approve or reject a parent's leave request with one tap and an optional teacher confirmation note, immediately notifying the parent via status update.
- **Responsibilities:**
  - Verify the student name, class, absence date range, session, and parent's reason before approving or rejecting a leave request.
- **Permissions & Access Control:**
  - **Read Access:** View all student leave requests submitted for the school/classes under management (`GET /api/v1/teacher/leave-requests`).
  - **Write Access:** Approve or reject a `PENDING` leave request (`PATCH /api/v1/teacher/leave-requests/{id}/review`) with status (`APPROVED` or `REJECTED`) and `teacher_note`.
  - **Restrictions:** Cannot alter a parent's original reason text or review a request that has already been `CANCELLED` by the parent.
- **Main Interactions:**
  - Switching into **Teacher Mode (`Chế độ Giáo viên / Nhà trường`)** via the quick demo role switcher.
  - Reviewing pending leave requests, inspecting student/parent details, and clicking **Approve (`Duyệt đơn`)** or **Reject (`Từ chối`)**.
- **Pain Points:**
  - Parents message absence requests at 6:30 AM in personal Zalo chats while the teacher is commuting, leading to missed attendance updates.
- **Expected Outcomes:**
  - Centralized queue of leave requests with clear status indicators and audit timestamps (`reviewed_at`, `reviewed_by`).

---

### 3.3. Persona 3 — TitKul Director / Technical Evaluator — *Business Owner & Evaluator*
- **Role Identifier:** `EVALUATOR` (operates both `ROLE_PARENT` and `ROLE_TEACHER` via Demo Switcher)
- **Goals:**
  - Assess within 10–15 minutes whether the candidate can deliver a working, well-architected, production-grade MVP aligned with TitKul's EdTech domain.
  - Verify that the application is not a static frontend mockup by creating a leave request as a Parent, switching to Teacher Mode to approve it, refreshing the app, and inspecting Swagger UI / PostgreSQL records.
- **Pain Points:**
  - Candidates who deliver broken demos, require manual code edits during the presentation, or get blocked by Zalo platform permission gates without a fallback plan.
- **Expected Outcomes:**
  - Smooth, zero-friction access via `DEMO_MODE` (and QR/Testing version support), clean code organization, real database transactions, and clear technical documentation.

---

## 4. Stakeholder Conflict Analysis & Resolution

| Conflict ID | Conflicting Stakeholders / Needs | Description of Tension | Architectural / Product Resolution |
| :--- | :--- | :--- | :--- |
| **`SCONF-01`** | **Strict Zalo Security (`STK-05`) vs. Frictionless Live Evaluation (`STK-01`, `STK-04`)** | Zalo restricts `Development`/`Testing` Mini Apps to whitelisted Developer accounts and requires enterprise verification for sensitive Zalo APIs, whereas the Evaluator needs immediate, hassle-free testing by 15:00 tomorrow. | Implement **Dual Auth Architecture (`DEMO_MODE` + `ZALO_AUTH`)**: `DEMO_MODE` provides pre-authenticated JWT/session switching between Parent and Teacher accounts out-of-the-box, while `ZALO_AUTH` implements the real Zalo token verification pipeline when `App ID` is bound. |
| **`SCONF-02`** | **Parent Simplicity (`STK-02`) vs. Closed-Loop Demo Proof (`STK-01`, `STK-03`)** | A pure Parent App only lets the user create a `PENDING` leave request, leaving the Evaluator unable to see how the request transitions to `APPROVED` in the UI. | Add a non-intrusive **Demo Role Switcher Banner/Pill** (`Phụ huynh` <-> `Giáo viên (Demo)`) at the top/profile bar (`OQ-02`), allowing the Evaluator to act as the Teacher, approve the request in the UI, and switch back to Parent Mode to see the `APPROVED` status. |
| **`SCONF-03`** | **Rich Domain Scope (`H2School`) vs. 24-Hour Delivery Deadline (`OQ-03`)** | Real preschool software includes tuition billing, AI scale integration, camera streaming, and full teacher CRUD for menus/reports, which cannot be built cleanly in 24 hours. | Enforce strict **MoSCoW Prioritization**: Pre-seed rich, realistic synthetic data for read-heavy modules (`Dashboard`, `Attendance`, `Daily Report`, `Meal Menu`, `Announcements`) and focus full transactional write/review engineering on the **Leave Request closed-loop workflow** (`Parent Submit/Cancel` <-> `Teacher Approve/Reject`). |

