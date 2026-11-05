# ADR-004: PostgreSQL 8-Table Schema, Idempotent SQL Initialization & Docker Compose Packaging

## Status
**Accepted** (Confirmed via `OQ-05` on 2026-11-04)

## 1. Context
The Evaluator at TitKul will specifically verify whether data created in the Mini App (such as a Parent's leave request or a Teacher's approval) is genuinely persisted in a relational PostgreSQL database and survives application reloads (`SRC-001` `P183`, `P361`–`P364`, `P639`–`P640`). Additionally, the system must start reliably via `docker-compose` or local execution (`OQ-05`).

## 2. Problem
How should we manage database schema creation, synthetic data seeding, and container packaging so the system starts with zero manual SQL steps while preserving user-created records across restarts?

## 3. Alternatives Considered
1. **Option A — Hibernate `ddl-auto: create-drop`:** Destroys all newly created demo records whenever the Backend restarts.
2. **Option B — Idempotent SQL Initialization (`schema.sql` with `CREATE TABLE IF NOT EXISTS` + `data.sql` with `INSERT ... ON CONFLICT DO NOTHING`) & `ddl-auto: validate`:** Automatically creates the 8 tables and seeds initial synthetic data on first boot without wiping new records on restart.
3. **Option C — Manual SQL Execution by Evaluator:** Prone to human error during demo setup.

## 4. Decision
We select **Option B — Idempotent `schema.sql` + `data.sql` managed by Spring Boot SQL Init, paired with `infra/docker-compose.yml`**.

## 5. Rationale
- **Zero-Friction Startup:** Running either `docker compose up -d` or `./gradlew bootRun` automatically initializes all 8 tables (`parents`, `students`, `parent_students`, `attendance_records`, `daily_reports`, `meal_menus`, `announcements`, `leave_requests`) and seeds realistic synthetic data for *Trường Mầm Non Sao Mai*.
- **Durable Persistence:** Because `ON CONFLICT DO NOTHING` is used instead of `DROP TABLE`, any leave request created or approved during the demo persists across app reloads and Backend restarts.

## 6. Consequences
- **Positive:** 100% reproducible environment in `< 30 seconds`; satisfies both local PostgreSQL 17 and Dockerized PostgreSQL deployments.
- **Negative / Trade-off:** Schema changes during development require updating `schema.sql` alongside JPA entities.

## 7. Risks & Mitigations
- **Risk:** Primary key sequence (`BIGSERIAL`) out of sync after explicit ID inserts in `data.sql`.
- **Mitigation:** Include `SELECT setval(pg_get_serial_sequence('...', 'id'), COALESCE(MAX(id), 1))` at the end of `data.sql` so subsequent `INSERT` statements never collide with seeded IDs.

