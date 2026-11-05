# Technical Debt & Future Enhancements Register — SchoolConnect

## 1. Overview
During the Phase 05 and Phase 06 implementation of the **SchoolConnect MVP**, strict engineering standards (Layered Architecture, `@Transactional` integrity, Jakarta Validation, and automated testing) were enforced. Below is the transparent log of conscious MVP trade-offs and post-MVP evolution items.

---

## 2. Recorded Items

| ID | Category | Description & Rationale for MVP | Planned Post-MVP Resolution | Severity |
| :--- | :--- | :--- | :--- | :--- |
| **`TD-001`** | **Database Migration Tooling** | Uses idempotent Spring SQL initialization (`schema.sql` + `data.sql` with `ON CONFLICT DO NOTHING` and `setval`) for zero-friction single-command evaluation (`ADR-004`). | Introduce Flyway (`V1__init_schema.sql`, `V2__seed_data.sql`) when multi-environment schema evolution begins. | Low |
| **`TD-002`** | **Pagination on Historical Lists** | Attendance, Daily Reports, and Leave Requests return full student-scoped lists (bounded to a single preschool academic year per child). | Add cursor or `Pageable` pagination (`page`, `size`) when multi-year archival data grows beyond 300 rows per student. | Low |
| **`TD-003`** | **Push Notifications via Zalo ZNS** | Parent sees updated leave approval status immediately upon screen load or role switch; outbound Zalo ZNS push notification requires an enterprise-verified Zalo Official Account (OA). | Integrate Zalo ZNS webhook/API inside `LeaveRequestService.reviewLeaveRequest()` once enterprise OA credentials are provisioned. | Low |

