# Product Vision & Problem Discovery — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect (Zalo Mini App — Preschool Parent & Teacher Portal) |
| **Document ID** | `DOC-01-VISION` |
| **Phase** | Phase 01 — Product Discovery & Requirement Analysis |
| **Project Owner** | Nguyen Vo Hoang Khang (`Nguyễn Võ Hoàng Khang`) |
| **Target Demo Deadline** | **2026-11-05 15:00 (GMT+7)** |
| **Status** | `Confirmed` (Pending Phase 01 Approval Gate) |

---

## 2. Problem Discovery & Analysis

### 2.1. Core Business Problems
In the preschool education sector in Vietnam (the core market of TitKul's **H2School** ecosystem), daily communication between preschools and parents suffers from fragmentation, high friction, and lack of real-time visibility:

1. **Fragmented Communication Channels:** Parents and teachers rely on ad-hoc Zalo chat groups, paper communication books (*Sổ liên lạc giấy*), or phone calls. Critical messages (such as a child's absence request or medication/dietary note) easily get buried under dozens of group messages.
2. **High App-Install Friction for Parents:** Traditional native mobile apps (iOS/Android) require parents to remember App Store/Google Play passwords, download large binaries, and manage separate login credentials—resulting in low adoption rates among busy parents and grandparents.
3. **Opaque Daily Child Care Visibility:** Parents of preschool children (ages 18 months to 5 years) experience high anxiety during the day because they cannot easily see whether their child checked in safely, what meals they ate, how they slept during naptime, or what activities they participated in.
4. **Unstructured Leave Request Processing:** When a child is sick or taking leave, parents message teachers informally via chat or call the school hotline. Teachers must manually record and reconcile these requests against daily attendance sheets, leading to lost requests or inaccurate meal-portion counts for the school kitchen.

### 2.2. Dual Context: The Technical Test Problem Statement
Alongside the domain problem above, this project solves a **Strategic Evaluation Problem** for the Project Owner and TitKul's Leadership:
- **Evaluator's Problem (TitKul Director):** Needs objective, verifiable proof within a 10–15 minute live review session (by **15:00 on 2026-11-05**) that the candidate understands TitKul's domain (EdTech / Preschool Digital Transformation) and can architect, build, secure, and deploy a full-stack Zalo Mini App backed by Java Spring Boot and PostgreSQL—without getting blocked by corporate Zalo App verification hurdles.

### 2.3. Current Workarounds & Their Limitations

| Current Solution / Workaround | Limitations & Failure Modes |
| :--- | :--- |
| **Zalo Group Chats / Personal Messages** | No structured data persistence; absence requests get lost; zero privacy when sharing student-specific feedback in group chats; cannot query historical attendance or daily reports. |
| **Paper Notebooks (*Sổ bé ngoan / Sổ liên lạc*)** | Weekly or delayed feedback; prone to physical loss; no real-time check-in/out alerts or daily meal updates. |
| **Standalone Native Apps (iOS/Android)** | High onboarding friction; parents resist installing dedicated apps or forget credentials; slower release cycles compared to Zalo Mini Apps. |
| **Static/Mock Frontend Demos (in Technical Tests)** | Fail to prove backend engineering, relational data modeling, transaction handling, ownership authorization, or real persistence across reloads. |

---

## 3. Business Objectives vs. Proposed Technical Solutions

To avoid conflating *what the business needs to achieve* with *how the software implements it*, the table below separates **Business Objectives** from **Proposed Technical Solutions**:

| Dimension | Business Objective (Why & What Outcome) | Proposed Technical Solution (How We Deliver It) |
| :--- | :--- | :--- |
| **Frictionless Parent Access** | Maximize parent engagement by meeting parents on the platform they already use every day without requiring native app installation. | Build **SchoolConnect** as a **Zalo Mini App** (`React + TypeScript + ZaUI + ZMP SDK`) running directly inside the Zalo super-app container. |
| **Transparent Daily Child Tracking** | Give parents peace of mind regarding their child's daily attendance, meals, sleep, and school announcements. | Implement structured read modules (`Dashboard`, `Attendance`, `Daily Report`, `Meal Menu`, `Announcements`) backed by normalized **PostgreSQL** tables and **Spring Boot REST APIs**. |
| **Auditable, End-to-End Leave Workflow** | Replace informal chat-based absence requests with a structured, verifiable workflow between Parent and Teacher. | Implement a transactional `Leave Request` module where **Parents** submit/cancel requests and **Teachers** (via `Demo Teacher Mode`) approve/reject them in real time with PostgreSQL persistence. |
| **Zero-Blocker Live Evaluation** | Guarantee a 100% reliable live demo at **15:00 tomorrow** regardless of Zalo enterprise account approval delays. | Architect a **Dual Authentication & Role Switcher Engine**: `DEMO_MODE` (instant pre-seeded Parent & Teacher sessions) alongside `ZALO_AUTH` (Zalo token verification + internal JWT). |
| **Visual & Brand Integrity** | Demonstrate original product design tailored to preschool parents without copying existing TitKul client Mini Apps. | Create an original, warm, parent-friendly **SchoolConnect Design System** strictly adhering to the non-imitation rule (`CF-05`). |

---

## 4. Product Vision Statement

> **For** preschool parents and homeroom teachers in Vietnam,
> **Who** need a reliable, instant, and structured way to track children's daily care, attendance, nutrition, and absence requests without installing separate mobile apps,
> **SchoolConnect** is a **Zalo Mini App Parent & Teacher Portal** (inspired by TitKul's *H2School* domain),
> **That** centralizes daily child reports, attendance calendars, nutritional menus, school announcements, and closed-loop leave request approvals directly inside Zalo,
> **Unlike** unstructured chat groups or high-friction native applications,
> **Our product** delivers a real-time, role-aware, full-stack experience backed by a secure Java 21 Spring Boot Layered Architecture (`Controller -> Service -> Repository`) and PostgreSQL database, featuring an interactive Parent-to-Teacher live demo mode.

---

## 5. Success Criteria & Measurable Outcomes

### 5.1. Technical Test & Business Evaluation Success Metrics
1. **End-to-End Closed-Loop Persistence (100% Verifiable):** A leave request created by a Parent is immediately persisted in PostgreSQL, survives full application reloads, appears in the Teacher's pending review queue, and updates its status to `APPROVED` or `REJECTED` across both roles when acted upon by the Teacher.
2. **Multi-Child Data Isolation (100% Accurate):** Switching between Child 1 (`Nguyễn Minh An` – Lớp Chồi 1) and Child 2 (`Nguyễn Minh Khôi` – Lớp Mầm 2) dynamically refreshes Dashboard, Attendance, Daily Report, and Leave Request views with strict backend ownership enforcement.
3. **Deadline Adherence:** Complete, tested, documented, and deployable package ready prior to **2026-11-05 15:00 (GMT+7)**.
4. **12/12 MVP Acceptance Criteria Passed:** All 12 acceptance criteria from `Requirements.docx` Part 2 plus the newly confirmed **Teacher Approval Mode (`OQ-02`)** pass without code modifications during the live demonstration.

