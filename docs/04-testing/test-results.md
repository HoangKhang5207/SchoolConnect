# Test Execution & Verification Results — SchoolConnect

## 1. Document Control

| Attribute | Value |
| :--- | :--- |
| **Project Name** | SchoolConnect (*Zalo Mini App — Preschool Parent & Teacher Portal*) |
| **Document ID** | `DOC-04-TEST-RESULTS` |
| **Phase** | Phase 08 — System Integration & End-to-End Verification |
| **Execution Timestamp** | `2026-11-05T10:35:40+07:00` |
| **Test Environment** | Windows 10/11 x86_64, Java 21.0.5, Node.js 24.16.0, PostgreSQL 17.5 |
| **Overall Result** | **41 Passed, 0 Failed, 0 Skipped (100.0% Pass Rate across Unit, Integration, E2E, & Static Checks)** |

---

## 2. Comprehensive Test Execution Summary Matrix

| Test Suite / Harness | Level | Total | Passed | Failed | Skipped | Pass Rate | Execution Duration |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **`HealthControllerTest`** | Backend API & CORS Integration | 2 | 2 | 0 | 0 | 100.0% | ~1.4s |
| **`LeaveRequestServiceTest`** | Backend Service Unit Tests | 5 | 5 | 0 | 0 | 100.0% | ~0.8s |
| **`ControllerIntegrationTest`** | Backend PostgreSQL Integration | 4 | 4 | 0 | 0 | 100.0% | ~2.1s |
| **`App.test.tsx` (Vitest)** | Frontend Component & State Integration | 1 | 1 | 0 | 0 | 100.0% | ~0.2s |
| **Total Automated Code Tests** | **Automated Suite** | **12** | **12** | **0** | **0** | **100.0%** | **~4.5s** |
| **Static Code Analysis (ESLint)** | Static Quality | 1 | 1 | 0 | 0 | 100.0% | ~8.0s |
| **Type Check (`tsc --noEmit`)** | Static Typing | 1 | 1 | 0 | 0 | 100.0% | ~4.5s |
| **Production Bundle (`vite build`)**| Bundling & Assets | 1 | 1 | 0 | 0 | 100.0% | ~4.2s |
| **Live Database & Health Probe** | E2E Service Livelines | 1 | 1 | 0 | 0 | 100.0% | ~2.5s |
| **Comprehensive E2E Integration Suite** | Full-Stack End-to-End (`INT-01`..`INT-24`) | **24** | **24** | **0** | **0** | **100.0%** | **~1.2s** (Avg: 49.8ms) |
| **Consecutive Repeat Run Test** | Data Isolation Verification | 1 | 1 | 0 | 0 | 100.0% | ~17.0s |
| **Grand Total Quality Checks** | **Comprehensive Quality Gate** | **41** | **41** | **0** | **0** | **100.0%** | **~41.8s** |

---

## 3. Detailed E2E System Integration Test Results (Phase 08)

Executed via live automated test harness `run_e2e_integration_test.ps1` against Spring Boot 3.3.5 and PostgreSQL 17.5:

```text
==========================================================
                E2E TEST EXECUTION SUMMARY               
==========================================================
Total Scenarios Executed : 24
Passed Scenarios         : 24
Failed Scenarios         : 0
Average Response Time    : 49.8 ms
Success Rate             : 100%
==========================================================

Id     Name                                                                             Method  ExpectedStatus ActualStatus Latency
--     ----                                                                             ------  -------------- ------------ -------
INT-01 System Readiness & PostgreSQL Probe                                              GET                200          200  29.3ms
INT-02 CORS Preflight for Zalo Mini App WebView (https://h5.zdn.vn)                     OPTIONS            200          200  16.6ms
INT-03 Parent Instant Demo Session Login                                                POST               200          200 355.5ms
INT-04 Teacher Instant Demo Session Login (Role Switcher)                               POST               200          200  31.1ms
INT-05 Zalo Authentication with Mock Zalo Token                                         POST               200          200  45.0ms
INT-06 Negative: Zalo Authentication with Invalid Token returns 401                     POST               401          401  17.6ms
INT-07 Home Dashboard Retrieval (Child 1)                                               GET                200          200 193.5ms
INT-08 Multi-Child Switcher (Child 2)                                                   GET                200          200  28.7ms
INT-09 Security: Guardian Isolation (Parent accessing Child 3 returns 403)              GET                403          403  12.4ms
INT-10 Attendance History & Monthly Summary (Child 1, month=2026-11)                    GET                200          200  18.2ms
INT-11 Preschool Daily Care Reports (Child 1)                                           GET                200          200  19.8ms
INT-12 Weekly Nutrition Meal Menus                                                      GET                200          200  14.5ms
INT-13 School Announcements (Pinned & General)                                          GET                200          200  18.9ms
INT-14 Parent Creates Absence Request #1                                                POST               201          201  27.4ms
INT-15 Business Rule: Duplicate/Overlapping Leave Request returns 409 Conflict          POST               409          409  15.6ms
INT-16 Security: Parent attempting Teacher Review returns 403 Forbidden                 PATCH              403          403  11.2ms
INT-17 Teacher Reviews and Approves Absence Request #1                                  PATCH              200          200  22.1ms
INT-18 Business Rule: Cancelling APPROVED request violates terminal state (returns 409) PATCH              409          409   9.8ms
INT-19 Parent Creates Absence Request #2                                                POST               201          201  24.3ms
INT-20 Parent Cancels Absence Request #2 while PENDING                                  PATCH              200          200  21.6ms
INT-21 Parent Queries Leave Requests via /leave-requests?studentId=1                    GET                200          200  18.5ms
INT-22 Parent Queries Leave Requests via /children/1/leave-requests                     GET                200          200  16.4ms
INT-23 Negative: Non-existent Resource Route returns 404 Not Found                      GET                404          404  12.9ms
INT-24 Security: Unauthenticated request to protected endpoint returns 401              GET                401          401   5.9ms
```

---

## 4. Frontend Quality & Bundling Evidence

- **Linter:** `0 errors, 0 warnings`
- **TypeScript:** `0 errors`
- **Vitest:** `1 passed in 225ms`
- **Production Bundle:** Built in 4.17s (`assets/index.C5NpryBp.module.js` 60.55 KB gzip, `assets/index.es.Dx1Tc1z5.module.js` 62.78 KB gzip).
- Reference: Detailed full-length report available in [`docs/04-testing/integration-test-report.md`](file:///d:/Coding/TitKul_ProjectTest/SchoolConnect/docs/04-testing/integration-test-report.md).
