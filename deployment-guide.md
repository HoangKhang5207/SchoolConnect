# Hướng dẫn Triển khai Hệ thống (System Deployment Guide) — SchoolConnect
## Phiên bản: `1.0.0` | Môi trường: `Development / Testing`

> Tài liệu hướng dẫn triển khai đầy đủ, chi tiết từng bước và bảng lệnh chính xác đã được lưu trữ tại:
> 👉 [**`docs/07-release/deployment-guide.md`**](file:///d:/Coding/TitKul_ProjectTest/SchoolConnect/docs/07-release/deployment-guide.md)

### Tóm tắt Nhanh:
1. **Chiến lược:** Kiến trúc Kép Cục bộ & Edge Ingress Tunneling (100% Miễn phí, 0đ, Không Cold-Start, Độ trễ < 35ms).
2. **Frontend:** Triển khai lên Zalo Mini App Testing Cloud Server thông qua `npx zmp-cli deploy --mode=testing` $\rightarrow$ sinh mã QR cho Giám đốc quét trên điện thoại.
3. **Backend & DB:** Chạy Spring Boot JAR v1.0.0 (Port 8080) + PostgreSQL 17.5 (Port 5432) trên máy trạm.
4. **Ingress HTTPS:** Mở đường hầm bảo mật qua Cloudflare Tunnel (`cloudflared.exe tunnel --url http://127.0.0.1:8080`) hoặc Localtunnel (`npx localtunnel --port 8080`).
5. **Kênh Dự phòng Máy chiếu:** Chạy đồng thời bản Web Simulator tại `http://localhost:3000` (F12 Mobile Viewport) sẵn sàng chiếu lên màn hình lớn.

