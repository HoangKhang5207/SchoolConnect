# ADR-003: Frontend Zalo Mini App Stack (`React + TypeScript + ZaUI`) & Original Design System

## Status
**Accepted** (2026-11-04)

## 1. Context
SchoolConnect targets the Zalo Mini App ecosystem (`https://h5.zdn.vn`) and must also run smoothly in mobile-responsive browser previews during evaluation. Furthermore, the Project Owner mandated (`CF-05`): *"Các hình ảnh trên chỉ mang tính chất tham khảo. Tuyệt đối không được bắt chước giống"* (strictly do not imitate the 5 reference screenshots).

## 2. Problem
Which UI framework, state management strategy, and visual design system should be used for the Frontend to ensure compatibility with Zalo Mini App CLI/WebView, fast implementation, and 100% visual originality?

## 3. Alternatives Considered
1. **Option A — React 18 + TypeScript + Vite/ZMP + ZaUI (`zmp-ui` / `zmp-sdk`) + Custom SchoolConnect CSS Design System:** Native to the Zalo Mini App ecosystem, lightweight bundle size, mobile-first touch ergonomics.
2. **Option B — React + Heavy Desktop UI Library (Ant Design / Material UI):** Large bundle sizes (`>1MB`), desktop-oriented components that break inside `360px` Zalo WebViews (`SRC-001` `P533`).
3. **Option C — Vue / Angular SPA:** Not officially supported by Zalo Mini App's standard React templates and ZaUI components.

## 4. Decision
We select **Option A — React 18 + TypeScript + Vite/ZMP + ZaUI/Custom Mobile CSS Design System** with deterministic React Context state management.

## 5. Rationale
- **Zalo Ecosystem Alignment:** Recommended directly by Zalo Platform Documentation and `Requirements.docx` (`P533`–`P536`).
- **Strict Visual Originality (`CF-05`):** By pairing clean mobile structural primitives with a bespoke **SchoolConnect Preschool Emerald & Warm Cream Design System**, we create a modern, cohesive interface (featuring a top Student Switcher bar, Role Switcher pill, and timeline cards) that shares zero visual similarity with the 3x3 icon grid or color schemes of the reference screenshots.

## 6. Consequences
- **Positive:** Fast rendering on mobile viewports (`360px`–`430px`), zero horizontal overflow, clean TypeScript type safety across all API DTOs.
- **Negative / Trade-off:** Requires wrapping `zmp-sdk` calls in a safe environment adapter so the app works identically inside a regular browser and inside the Zalo app container.

## 7. Risks & Mitigations
- **Risk:** `zmp-sdk` throwing runtime errors when opened in Chrome/Edge outside the Zalo WebView.
- **Mitigation:** Centralize all Zalo SDK calls in `src/services/zaloSdkAdapter.ts` with environment detection and automatic fallback.

