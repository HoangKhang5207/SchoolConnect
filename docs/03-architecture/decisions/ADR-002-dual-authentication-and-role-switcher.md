# ADR-002: Dual-Mode Authentication (`DEMO_MODE` + `ZALO_AUTH`) & Interactive Role Switcher

## Status
**Accepted** (Confirmed via `OQ-02` & `OQ-04` on 2026-11-04)

## 1. Context
Zalo Mini Apps running in `Development` or `Testing` mode restrict access to Zalo accounts explicitly added to the Mini App's Developer/Admin group (`CF-07`), and certain Zalo APIs require enterprise verification. Meanwhile, the Project Owner has a personal Zalo Developer account (`OQ-04`) and must demonstrate a complete closed-loop workflow (Parent submits leave request -> Teacher approves/rejects it -> Parent sees updated status) to the TitKul Director at **15:00 on 2026-11-05** (`OQ-02`, `OQ-03`).

## 2. Problem
How do we design authentication and authorization so that:
1. The system enforces real JWT security, role-based access (`ROLE_PARENT` vs `ROLE_TEACHER`), and guardian-student ownership (`RULE-001`),
2. The system supports real Zalo token verification (`ZALO_AUTH`) when an App ID is bound, AND
3. The live evaluation is 100% immune to Zalo enterprise approval delays and allows seamless switching between Parent and Teacher roles?

## 3. Alternatives Considered
1. **Option A — Pure `ZALO_AUTH` Only:** Requires every user to authenticate via `zmp-sdk` `getAccessToken()` and requires two separate Zalo accounts to test Parent and Teacher flows.
2. **Option B — No Authentication (Hardcoded User IDs in Frontend):** Insecure, fails to demonstrate Spring Security, JWT, or `RULE-001` ownership checks (`SRC-001` `P148`).
3. **Option C — Dual-Mode Authentication (`DEMO_MODE` + `ZALO_AUTH`) with Signed Internal JWTs & Role Switcher:** Backend issues real HMAC-SHA256 JWTs for both `POST /api/v1/auth/demo-session` (`PARENT` or `TEACHER`) and `POST /api/v1/auth/zalo-login` (`graph.zalo.me/v2.0/me` + `appsecret_proof`).

## 4. Decision
We select **Option C — Dual-Mode Authentication (`DEMO_MODE` + `ZALO_AUTH`) issuing internal signed JWTs, paired with a Frontend Role Switcher (`Parent` <-> `Teacher`)**.

## 5. Rationale
- **Zero-Blocker Live Demo:** Whether opened inside Zalo Testing or a desktop/mobile browser, `DEMO_MODE` authenticates immediately and lets the Director switch between *Phụ huynh Trần Thu Hà* (`ROLE_PARENT`) and *Cô Lê Thanh Thảo* (`ROLE_TEACHER`) in one tap.
- **Real Backend Security:** Even in `DEMO_MODE`, every subsequent API call carries a real `Authorization: Bearer <JWT>` header validated by `JwtAuthenticationFilter`, enforcing `@PreAuthorize` and `GuardianAccessService` (`RULE-001`).
- **Zalo Platform Compliance:** Implements the exact Zalo server-side verification flow (`https://graph.zalo.me/v2.0/me` with HMAC-SHA256 `appsecret_proof`) required by Zalo's documentation.

## 6. Consequences
- **Positive:** Eliminates external platform risk for tomorrow's 15:00 demo while proving production-grade security architecture.
- **Negative / Trade-off:** `APP_AUTH_DEMO_MODE` must be explicitly disabled (`false`) if the application is ever promoted to a public production environment.

## 7. Risks & Mitigations
- **Risk:** `DEMO_MODE` left enabled in a real production environment.
- **Mitigation:** Gate `/api/v1/auth/demo-session` behind `@ConditionalOnProperty` / environment configuration `APP_AUTH_DEMO_MODE` and report its state transparently in `/api/v1/health`.

