# Kế hoạch Quay lui & Khôi phục Hệ thống (System Rollback & Recovery Plan) — SchoolConnect
## Phiên bản: `1.0.0` | Mục tiêu: RTO $\le 30$ giây, RPO $= 0$ giây

> Tài liệu kế hoạch quay lui và quy trình khôi phục chi tiết đã được lưu trữ tại:
> 👉 [**`docs/07-release/rollback-plan.md`**](file:///d:/Coding/TitKul_ProjectTest/SchoolConnect/docs/07-release/rollback-plan.md)

### Tóm tắt Nhanh:
1. **Lỗi Mạng Di động Zalo:** Chuyển ngay sang Kênh dự phòng Web Simulator tại `http://localhost:3000` (Mobile Viewport F12) trên laptop máy chiếu (< 5 giây).
2. **Dữ liệu Bị Nhiễm Bẩn Thử Nghiệm:** Chạy lệnh dọn dẹp tức thì `< 2 giây`:
   ```powershell
   & "D:\DB_Design\PostgreSQL\pgsql\bin\psql.exe" -U postgres -d schoolconnect -c "DELETE FROM leave_requests WHERE id > 2;"
   ```
3. **Lỗi Ứng dụng Backend:** Khởi động lại Spring Boot JAR v1.0.0 trong 10 giây.
4. **Lỗi Phiên bản Zalo Frontend:** Chọn lại bản build cũ trong Zalo Developer Center (1 click).

