# Source Register & Input Inventory — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect |
| **Document ID** | `DOC-00-SOURCES` |
| **Phase** | Phase 00 — Project Initialization & Context Discovery |
| **Last Updated** | 2026-11-04 |

---

## 2. Summary of Discovered Inputs

All project artifacts and input materials have been cataloged and treated strictly as **Project Evidence**. No existing application source code, database schemas, or deployment scripts exist in the repository root (`d:\Coding\TitKul_ProjectTest\SchoolConnect`) as of Phase 00 initialization.

| Source ID | Filename / Reference | Type | Description | Access / Read Status |
| :--- | :--- | :--- | :--- | :--- |
| `SRC-001` | `d:\Coding\TitKul_ProjectTest\SchoolConnect\Requirements.docx` | Document (`DOCX`) | Technical Test implementation plan containing two chronological proposals: Part 1 (*TitKul Mini Hub* - Service Booking) and Part 2 (*SchoolConnect* - Preschool Parent Portal). | **Fully Read & Extracted** (956 text/table nodes + 1 embedded chart image + hyperlinks) |
| `SRC-001-IMG1` | `word/media/image1.png` (inside `Requirements.docx`) | Embedded Chart (`PNG`) | Bar chart illustrating estimated 5-day effort distribution across Research (15%), Frontend (25%), Backend (25%), Integration (20%), and Deploy & Demo (15%). | **Fully Read & Inspected** |
| `SRC-002` | `media_1793776335275_63f61063.jpg` | Reference Image (`JPG`) | Screenshot of Zalo Mini App "Dầu Nhớt NPoil" (Home screen showing user greeting "Khách hàng Dinh Titkul", account verification banner, quick actions, product categories, and 5-tab bottom navigation). | **Fully Read & Inspected** |
| `SRC-003` | `media_1793776335277_17eda2be.jpg` | Reference Image (`JPG`) | Screenshot of Zalo Mini App "Phú Lâm TKSmart" (Internal facial attendance registration screen, Step 1 personal info form with "Lấy số điện thoại từ Zalo" button, role/department selectors, and 2-tab bottom navigation). | **Fully Read & Inspected** |
| `SRC-004` | `media_1793776335278_0cd4890c.jpg` | Reference Image (`JPG`) | Screenshot of Zalo Mini App "Trường Mầm Non 11" (Teacher portal greeting "Titkul T11 - Giáo viên", morning/afternoon weather summary, 10 preschool feature icons including Đơn nghỉ phép, Điểm danh, Sổ bé ngoan, Thực đơn, Chỉ số BMI, Biểu phí, Cân AI, Quản lý bán trú, Nhắc thuốc, ATTP, plus floating "Trợ lý dinh dưỡng" assistant and 5-tab bottom nav). | **Fully Read & Inspected** |
| `SRC-005` | `media_1793776335280_cb1a6b9e.jpg` | Reference Image (`JPG`) | Screenshot of Zalo Mini App "Xã Truông Mít" (Local commune information & tourism portal with hero banner, search bar, and categorized icon cards). | **Fully Read & Inspected** |
| `SRC-006` | `media_1793776335281_6786ac81.jpg` | Reference Image (`JPG`) | Screenshot of Zalo Mini App "Hội Doanh Nghiệp Phường Bảy Hiền" (Business association portal with group photo banner, Zalo OA/phone linking prompt card, activity news list, and 5-tab bottom navigation). | **Fully Read & Inspected** |
| `SRC-007` | User Prompt (`<USER_REQUEST>`) | Text Directive | Phase 00 instructions, project metadata, role definitions, safety constraints, and explicit visual non-imitation rule (`Tuyệt đối không được bắt chước giống`). | **Fully Read & Inspected** |

---

## 3. Detailed Source Evaluation

### 3.1. `SRC-001` — `Requirements.docx`
- **Relevant Locations:**
  - **Part 1 (`P0`–`P444`, Tables `T66`, `T129`, `T134`, `T136`, `T138`, `T151`, `T246`, `T286`, `T350`, `T372`, `T391`):** Initial proposal for *TitKul Mini Hub* (Service Discovery & Booking MVP).
  - **Part 2 (`P446`–`P744`, Tables `T485`, `T509`, `T561`, `T576`, `T627`, `T708`, `T728`, `T737`):** Revised proposal after researching TitKul's EdTech ecosystem (`HDSchool`, `H2School`, `TitKul MiniApp`), pivoting to **SchoolConnect** (Preschool Parent Portal).
- **Extractable Information:**
  - Context: Pre-probation Technical Test for TitKul (`P1`).
  - Domain analysis comparing `HDSchool` (K-12) vs. `H2School` (Preschool) (`T485`).
  - Selected MVP concept: **SchoolConnect — Zalo Mini App Parent Portal (H2School-inspired Preschool Edition)** (`P478`–`P482`).
  - Proposed 6 core screens: `01. Home Dashboard`, `02. Daily Report`, `03. Attendance`, `04. Meal Menu`, `05. Announcements`, `06. Absence Request` (`P491`–`P506`).
  - Feature prioritization table (`T509`) with Must, Later, and Out-of-scope classifications.
  - Proposed Technology Stack: React + TypeScript + ZMP SDK + ZaUI + CSS; Java 21 + Spring Boot 3.x (Modular Monolith); PostgreSQL; Docker; OpenAPI/Swagger (`P514`–`P558`, `T737`).
  - Proposed Database Schema (8 tables): `parents`, `students`, `parent_students`, `attendance_records`, `daily_reports`, `meal_menus`, `announcements`, `leave_requests` (`T561`).
  - Proposed REST API endpoints (8 endpoints under `/api/v1/...`) (`T576`).
  - Dual-mode Authentication strategy: `DEMO_MODE` (simulated parent session for frictionless demo/testing) and `ZALO_AUTH` (`getAccessToken` -> Backend verification via `graph.zalo.me/v2.0/me` with `appsecret_proof` -> Internal JWT + Guardian-Student ownership check) (`P604`–`P624`).
  - 6-Day Sprint Roadmap (`T627`), 12 MVP Acceptance Criteria (`P629`–`P643`), Deployment & Director Access Plan (`P644`–`P663`), Monorepo folder structure (`P666`–`P706`), and 10-minute Demo Scenario (`P709`–`P725`).
- **Missing or Unreadable Information:**
  - The 3 decision questions posed at the end of Part 1 (`P429`–`P443`: Zalo App ID availability, evaluation focus, and exact time budget) do not have explicit written answers from the candidate/owner inside the document.
  - Detailed column definitions, data types, and constraints for the 8 tables in Part 2 (`T561` lists only table names and roles, unlike Part 1 which listed columns for the 3 booking tables).
  - Detailed request/response JSON payloads for the 8 SchoolConnect endpoints in Part 2 (`T576`).
  - External web links and ChatGPT static image URLs embedded in `word/_rels/document.xml.rels` are external references; however, all text captions and structural tables in `word/document.xml` were 100% extracted.
- **Potential Conflicts with Other Sources:**
  - **Internal Conflict (Part 1 vs. Part 2 within `SRC-001`):** Part 1 proposes *TitKul Mini Hub* (Service Booking, 3 tables, Java Spring Boot 3.x, 5-day plan), whereas Part 2 explicitly supersedes Part 1 (`P447`, `P472`) and proposes *SchoolConnect* (Preschool Parent Portal, 8 tables, Java 21 + Spring Boot 3.x, 6-day plan).
  - **User Role Perspective (`SRC-001` Part 2 vs. `SRC-004`):** `SRC-001` Part 2 focuses on the **Parent Portal** (`Phụ huynh`), whereas `SRC-004` (`Trường Mầm Non 11`) shows a **Teacher Portal** (`Xin chào giáo viên`).

### 3.2. `SRC-002` to `SRC-006` — Uploaded Reference Screenshots
- **Relevant Locations:** Visual UI layouts of 5 live Zalo Mini Apps (`Dầu Nhớt NPoil`, `Phú Lâm TKSmart`, `Trường Mầm Non 11`, `Xã Truông Mít`, `Hội Doanh Nghiệp Phường Bảy Hiền`).
- **Extractable Information:**
  - Confirms TitKul's real-world Zalo Mini App conventions: clean mobile-first cards, Zalo header bar compatibility, bottom navigation bar (2 to 5 tabs), Zalo phone number linking (`Lấy số điện thoại từ Zalo` / `Liên kết số điện thoại và quan tâm OA`), and preschool domain terminology in `SRC-004` (`Đơn nghỉ phép`, `Điểm danh`, `Sổ bé ngoan`, `Thực đơn`, `Chỉ số BMI`, `Biểu phí`, `Quản lý bán trú`, `Nhắc thuốc`, `ATTP`).
- **Constraints & Conflicts:**
  - `SRC-007` (User Prompt) mandates: **"Các hình ảnh trên chỉ mang tính chất tham khảo. Tuyệt đối không được bắt chước giống"** (The images above are for reference only. Strictly do not imitate/copy them). Therefore, these images must only inform domain context and Zalo platform ergonomics, never serve as UI templates to copy.

