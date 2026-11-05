# ADR-001: Adoption of Layered Architecture for Spring Boot Backend

## Status
**Accepted** (Confirmed by Project Owner on 2026-11-04)

## 1. Context
SchoolConnect is a full-stack Zalo Mini App and Java 21 Spring Boot 3.x Backend with a hard live demonstration deadline at **15:00 on 2026-11-05**. The initial draft in `Requirements.docx` Part 2 mentioned a *Modular Monolith* layout, while Part 1 (`P77`–`P81`), Global Engineering Rules (`Rule 3.A`), and the Project Owner's explicit directive at Phase 02 specified **Layered Architecture (`Controller -> Service -> Repository -> Entity/DTO`)**.

## 2. Problem
How should we structure the Backend packages, responsibilities, and dependency boundaries so that the 8 preschool domain entities and 14 REST endpoints can be implemented, tested, and reviewed rapidly with maximum clarity and zero cross-module boilerplate?

## 3. Alternatives Considered
1. **Option A — Layered Architecture (`controller`, `service`, `repository`, `entity`, `dto`, `security`, `config`, `exception`):** Organizes classes by technical layer with strict top-down dependency rules.
2. **Option B — Modular Monolith (Package-by-Feature with strict module facades):** Isolates `auth`, `parent`, `student`, `attendance`, `dailyreport`, `meal`, `announcement`, and `leave` into independent modules communicating only via public interfaces/events.
3. **Option C — Microservices Architecture:** Splits Student, Attendance, and Leave Request into separate deployable Spring Boot services.

## 4. Decision
We select **Option A — Layered Architecture (`Controller -> Service -> Repository -> Entity/DTO`)** inside a single Spring Boot 3.x application.

## 5. Rationale
- **Alignment with Project Owner Directive & Engineering Standards:** Directly fulfills the Owner's explicit requirement and `Rule 3.A` (*Use proper Layered Architecture: Controller -> Service -> Repository/DAO*).
- **Cohesive Transactional Joins:** Core features like `StudentDashboardService` (which aggregates student profile, today's attendance, today's daily report, pending leave count, and announcements) and `LeaveRequestService` (which validates guardian-student mapping and overlapping dates) can directly collaborate with repositories inside a single `@Transactional` service boundary without inter-module adapter overhead.
- **Evaluator Readability:** Any technical evaluator inspecting the repository can immediately locate all REST endpoints under `controller/`, business rules under `service/`, and database queries under `repository/`.

## 6. Consequences
- **Positive:** High development speed; clean Separation of Concerns (SoC); straightforward unit testing of `Service` classes using JUnit 5 and Mockito.
- **Negative / Trade-off:** Requires developer discipline not to bypass the `Service` layer from `Controller` classes.

## 7. Risks & Mitigations
- **Risk:** Anemic services or leaking JPA entities to controllers.
- **Mitigation:** Enforce strict code review rules: Controllers accept/return only `dto/` records wrapped in `ApiResponse<T>`, and all business rules (`RULE-001`..`RULE-013`) reside exclusively in `service/`.

