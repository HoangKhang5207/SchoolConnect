-- ==============================================================================
-- SchoolConnect — Idempotent Synthetic Seed Data (Trường Mầm Non Sao Mai)
-- 100% Synthetic Data per CF-06. Preserves existing records via ON CONFLICT DO NOTHING.
-- ==============================================================================

-- 1. Seed Parents & Teachers
INSERT INTO parents (id, zalo_user_id, full_name, phone, avatar_url, role, created_at)
VALUES
    (1, 'zalo_demo_parent_01', 'Trần Thu Hà', '0901234567', NULL, 'PARENT', '2026-09-01T08:00:00+07:00'),
    (2, 'zalo_demo_teacher_01', 'Cô Lê Thanh Thảo', '0909888777', NULL, 'TEACHER', '2026-09-01T08:00:00+07:00'),
    (3, 'zalo_demo_parent_02', 'Phạm Quốc Bảo', '0912345678', NULL, 'PARENT', '2026-09-01T08:00:00+07:00')
ON CONFLICT (id) DO NOTHING;

-- 2. Seed Preschool Students
INSERT INTO students (id, student_code, full_name, date_of_birth, gender, class_name, teacher_name, avatar_url, height_cm, weight_kg, blood_type, allergy_notes, is_active, created_at)
VALUES
    (1, 'HS-2026-001', 'Nguyễn Minh An', '2022-04-15', 'FEMALE', 'Lớp Chồi 1', 'Cô Lê Thanh Thảo', NULL, 104.0, 16.8, 'O', 'Không dị ứng thực phẩm', TRUE, '2026-09-01T08:00:00+07:00'),
    (2, 'HS-2026-002', 'Nguyễn Minh Khôi', '2024-02-10', 'MALE', 'Lớp Mầm 2', 'Cô Phạm Mai Anh', NULL, 91.5, 13.4, 'A', 'Hạn chế tôm cua biển có vỏ', TRUE, '2026-09-01T08:00:00+07:00'),
    (3, 'HS-2026-003', 'Phạm Gia Hân', '2022-08-20', 'FEMALE', 'Lớp Chồi 1', 'Cô Lê Thanh Thảo', NULL, 102.0, 15.9, 'B', 'Không dị ứng thực phẩm', TRUE, '2026-09-01T08:00:00+07:00')
ON CONFLICT (id) DO NOTHING;

-- 3. Seed Guardian-Student Relationships (Parent 1 owns Student 1 & 2; Parent 3 owns Student 3)
INSERT INTO parent_students (id, parent_id, student_id, relationship, is_primary)
VALUES
    (1, 1, 1, 'Mẹ', TRUE),
    (2, 1, 2, 'Mẹ', FALSE),
    (3, 3, 3, 'Bố', TRUE)
ON CONFLICT (id) DO NOTHING;

-- 4. Seed Attendance Records (For Student 1 - Minh An & Student 2 - Minh Khôi)
INSERT INTO attendance_records (id, student_id, attendance_date, status, check_in_time, check_out_time, pickup_person, note)
VALUES
    (1, 1, '2026-11-04', 'PRESENT', '07:35:00', NULL, NULL, 'Bé đến lớp vui vẻ, thân nhiệt 36.6°C'),
    (2, 1, '2026-11-03', 'PRESENT', '07:40:00', '16:35:00', 'Mẹ Trần Thu Hà', 'Bé ngoan, tự cất ba lô gọn gàng'),
    (3, 1, '2026-11-02', 'PRESENT', '07:32:00', '16:40:00', 'Mẹ Trần Thu Hà', 'Tham gia đầy đủ hoạt động ngoài trời'),
    (4, 1, '2026-10-30', 'LATE', '08:25:00', '16:30:00', 'Bố Nguyễn Hoàng Nam', 'Gia đình báo đến muộn do trời mưa lớn'),
    (5, 1, '2026-10-29', 'ABSENT_EXCUSED', NULL, NULL, NULL, 'Nghỉ có phép: Bé đi khám răng định kỳ'),
    (6, 1, '2026-10-28', 'PRESENT', '07:38:00', '16:25:00', 'Mẹ Trần Thu Hà', 'Sức khỏe bình thường'),
    (7, 2, '2026-11-04', 'PRESENT', '07:36:00', NULL, NULL, 'Bé Khôi vào lớp ngoan, thân nhiệt 36.5°C'),
    (8, 2, '2026-11-03', 'PRESENT', '07:42:00', '16:30:00', 'Mẹ Trần Thu Hà', 'Bé ăn ngủ tốt cả ngày'),
    (9, 2, '2026-11-02', 'ABSENT_EXCUSED', NULL, NULL, NULL, 'Nghỉ có phép theo đơn gia đình')
ON CONFLICT (id) DO NOTHING;

-- 5. Seed Daily Care Reports
INSERT INTO daily_reports (id, student_id, report_date, meal_status, meal_comment, sleep_status, sleep_duration_mins, activity_summary, mood_status, teacher_note, recorded_by, updated_at)
VALUES
    (1, 1, '2026-11-04', 'GOOD', 'Bé ăn hết 100% suất cháo cá hồi bí đỏ buổi sáng và cơm gà hầm hạt sen buổi trưa, uống hết 180ml sữa hạt.', 'GOOD', 110, 'Tham gia giờ vẽ tranh chủ đề Mùa Thu, nhận biết màu sắc và nhảy vận động theo nhạc cùng các bạn.', 'HAPPY', 'Hôm nay Minh An rất ngoan, biết phụ cô xếp ghế và chia sẻ đồ chơi với bạn.', 'Cô Lê Thanh Thảo', '2026-11-04T14:00:00+07:00'),
    (2, 1, '2026-11-03', 'GOOD', 'Bé tự xúc ăn gọn gàng, ăn hết khẩu phần trưa và bữa xế sữa chua phô mai.', 'GOOD', 105, 'Học đếm số từ 1 đến 10 qua trò chơi xếp gỗ và nghe kể chuyện cổ tích.', 'HAPPY', 'Con tập trung tốt trong giờ học tạo hình.', 'Cô Lê Thanh Thảo', '2026-11-03T15:30:00+07:00'),
    (3, 1, '2026-11-02', 'FAIR', 'Buổi sáng bé ăn hơi chậm, buổi trưa ăn được 80% suất cơm và uống bù thêm sữa.', 'RESTLESS', 80, 'Hoạt động tưới cây trong vườn trường và tập thể dục nhịp điệu buổi sáng.', 'NORMAL', 'Cô đã nhắc bé uống thêm nước ấm vào buổi chiều.', 'Cô Lê Thanh Thảo', '2026-11-02T15:30:00+07:00'),
    (4, 2, '2026-11-04', 'GOOD', 'Bé Khôi ăn hết bát súp gà nấm và uống hết 150ml sữa công thức.', 'GOOD', 120, 'Tập nhận biết hình tròn - hình vuông và chơi lăn bóng vận động tinh.', 'HAPPY', 'Bé Khôi hôm nay không quấy khóc khi mẹ về, rất đáng khen!', 'Cô Phạm Mai Anh', '2026-11-04T14:10:00+07:00'),
    (5, 2, '2026-11-03', 'GOOD', 'Bé ăn ngon miệng, đặc biệt thích món bánh flan bữa xế.', 'GOOD', 115, 'Nghe nhạc thiếu nhi và tập vỗ tay theo nhịp cùng cô giáo.', 'HAPPY', 'Con ngủ trưa rất sâu giấc.', 'Cô Phạm Mai Anh', '2026-11-03T15:20:00+07:00')
ON CONFLICT (id) DO NOTHING;

-- 6. Seed Meal Menus (Daily & Weekly Breakdown: Breakfast, Lunch, Afternoon Snack)
INSERT INTO meal_menus (id, menu_date, age_group, meal_type, dishes_json, calories_kcal, nutrition_note)
VALUES
    (1, '2026-11-04', 'ALL', 'BREAKFAST', '["Cháo cá hồi bí đỏ tươi", "Sữa hạt óc chó hạnh nhân (180ml)"]', 320, 'Giàu Omega-3, DHA và Vitamin A hỗ trợ phát triển thị giác'),
    (2, '2026-11-04', 'ALL', 'LUNCH', '["Cơm mềm hữu cơ", "Thịt gà hầm hạt sen cà rốt", "Canh rau ngót thịt bằm", "Chuối chín tráng miệng"]', 485, 'Cân bằng đạm dễ tiêu hóa, sắt và chất xơ tự nhiên'),
    (3, '2026-11-04', 'ALL', 'AFTERNOON_SNACK', '["Sữa chua phô mai tươi", "Bánh flan trứng sữa nhà làm"]', 215, 'Bổ sung lợi khuẩn Probiotics và Canxi cho xương chắc khỏe'),
    (4, '2026-11-05', 'ALL', 'BREAKFAST', '["Nui sao nấu thịt bằm & rau củ", "Sữa tươi tiệt trùng ít đường (180ml)"]', 310, 'Cung cấp năng lượng khởi đầu ngày mới và Vitamin nhóm B'),
    (5, '2026-11-05', 'ALL', 'LUNCH', '["Cơm mềm", "Cá lóc hấp bầu non", "Canh bí xanh nấu mọc", "Đu đủ chín ngọt"]', 460, 'Đạm cá lành tính kết hợp Vitamin C tăng đề kháng'),
    (6, '2026-11-05', 'ALL', 'AFTERNOON_SNACK', '["Chè đậu xanh hạt sen đường phèn", "Sữa hạt sen (150ml)"]', 205, 'Thanh nhiệt, giàu chất xơ và khoáng chất'),
    (7, '2026-11-03', 'ALL', 'BREAKFAST', '["Súp gà xé nấm hương ngô ngọt", "Sữa tươi ấm (180ml)"]', 305, 'Dễ tiêu hóa, giữ ấm bụng buổi sáng'),
    (8, '2026-11-03', 'ALL', 'LUNCH', '["Cơm mềm", "Thịt bò xào bông cải xanh", "Canh khoai mỡ nấu thịt", "Cam sành ngọt"]', 490, 'Giàu sắt, kẽm và Vitamin C tự nhiên'),
    (9, '2026-11-03', 'ALL', 'AFTERNOON_SNACK', '["Bánh bông lan trứng muối nhẹ", "Nước ép táo tươi"]', 220, 'Bổ sung năng lượng cho hoạt động vui chơi buổi chiều')
ON CONFLICT (id) DO NOTHING;

-- 7. Seed School & Class Announcements
INSERT INTO announcements (id, title, summary, content, category, target_class, is_pinned, author_name, published_at)
VALUES
    (1, 'Thông báo Lịch khám sức khỏe định kỳ Học kỳ I năm học 2026-2027', 'Nhà trường phối hợp cùng Bác sĩ Nhi khoa tổ chức khám sức khỏe tổng quát cho toàn bộ các bé vào sáng Thứ Sáu (06/11/2026).', 'Kính gửi Quý Phụ huynh Trường Mầm Non Sao Mai,\n\nNhằm theo dõi sự phát triển thể chất của các con, Nhà trường tổ chức đợt khám sức khỏe định kỳ Học kỳ I vào lúc 08:00 sáng Thứ Sáu, ngày 06/11/2026.\n\nNội dung khám bao gồm: Đo chiều cao, cân nặng, đánh giá BMI, khám răng hàm mặt, tai mũi họng và tim phổi tổng quát. Kính mong Quý Phụ huynh đưa bé đến lớp đúng giờ để buổi khám diễn ra thuận lợi.', 'HEALTH', NULL, TRUE, 'Ban Giám Hiệu - Trường Mầm Non Sao Mai', '2026-11-04T07:30:00+07:00'),
    (2, 'Hoạt động trải nghiệm: Ngày hội Làm bánh & Chia sẻ yêu thương', 'Các bé khối Chồi và Mầm sẽ tham gia tự tay nặn bánh quy bơ hạt dinh dưỡng vào chiều Thứ Năm (05/11/2026).', 'Chào bố mẹ Lớp Chồi 1,\n\nChiều mai (Thứ Năm, 05/11/2026), cô và các bé sẽ có tiết học trải nghiệm thực hành "Đầu bếp nhí tài ba". Toàn bộ nguyên liệu bột mì hữu cơ, bơ lạt và tạp dề đã được nhà trường chuẩn bị sẵn và đảm bảo vệ sinh ATTP.\n\nSau buổi học, mỗi bé sẽ mang về một túi bánh nhỏ dành tặng ông bà, bố mẹ!', 'CLASS', 'Lớp Chồi 1', TRUE, 'Cô Lê Thanh Thảo', '2026-11-03T17:00:00+07:00'),
    (3, 'Khuyến nghị giữ ấm và phòng ngừa cúm mùa giao mùa tháng 11', 'Phòng Y tế nhà trường gửi hướng dẫn phối hợp chăm sóc hô hấp cho trẻ khi thời tiết sáng sớm chuyển lạnh.', 'Kính gửi Quý Phụ huynh,\n\nHiện nay thời tiết buổi sáng sớm và chiều tối có sự chênh lệch nhiệt độ. Quý Phụ huynh vui lòng chuẩn bị thêm 01 áo khoác mỏng trong ba lô cho bé và thông báo ngay trên ứng dụng nếu bé có biểu hiện sốt hoặc ho để cô giáo phối hợp theo dõi.', 'HEALTH', NULL, FALSE, 'Phòng Y Tế Học Đường', '2026-11-02T09:00:00+07:00'),
    (4, 'Thực đơn dinh dưỡng mùa Thu - Đông đã được cập nhật mới', 'Bếp ăn bán trú tăng cường các món súp ấm, hạt sen và trái cây giàu Vitamin C trong thực đơn tháng 11.', 'Nhà trường đã cập nhật thực đơn dinh dưỡng tháng 11/2026 được thẩm định bởi chuyên gia dinh dưỡng mầm non. Quý Phụ huynh có thể tra cứu chi tiết từng bữa Sáng - Trưa - Xế ngay tại mục Thực đơn trên ứng dụng SchoolConnect.', 'SCHOOL', NULL, FALSE, 'Bộ Phận Dinh Dưỡng Bán Trú', '2026-11-01T10:00:00+07:00')
ON CONFLICT (id) DO NOTHING;

-- 8. Seed Leave Requests (Includes APPROVED and PENDING records for live demo)
INSERT INTO leave_requests (id, request_code, student_id, parent_id, start_date, end_date, session_type, reason, status, teacher_note, reviewed_by, reviewed_at, created_at, updated_at)
VALUES
    (1, 'LR-20261029-0001', 1, 1, '2026-10-29', '2026-10-29', 'FULL_DAY', 'Gia đình đưa bé Minh An đi khám và bôi vecni fluor ngừa sâu răng định kỳ tại nha khoa.', 'APPROVED', 'Cô Thảo đã ghi nhận và báo bếp nghỉ ăn ngày 29/10. Chúc bé đi khám răng ngoan nhé!', 'Cô Lê Thanh Thảo', '2026-10-28T19:15:00+07:00', '2026-10-28T18:30:00+07:00', '2026-10-28T19:15:00+07:00'),
    (2, 'LR-20261104-0002', 1, 1, '2026-11-07', '2026-11-07', 'MORNING', 'Gia đình xin cho bé nghỉ buổi sáng Thứ Bảy để về thăm ông bà ngoại, buổi chiều bé tự học ở nhà.', 'PENDING', NULL, NULL, NULL, '2026-11-04T08:30:00+07:00', '2026-11-04T08:30:00+07:00'),
    (3, 'LR-20261102-0001', 2, 1, '2026-11-02', '2026-11-02', 'FULL_DAY', 'Bé Minh Khôi hơi sổ mũi nhẹ sau ngày cuối tuần nên mẹ xin cho con nghỉ ở nhà theo dõi 1 ngày.', 'APPROVED', 'Nhà trường đã duyệt đơn nghỉ phép. Chúc bé Khôi mau khỏe để mai đến lớp cùng các bạn!', 'Cô Lê Thanh Thảo', '2026-11-01T20:10:00+07:00', '2026-11-01T19:45:00+07:00', '2026-11-01T20:10:00+07:00')
ON CONFLICT (id) DO NOTHING;

-- Synchronize BIGSERIAL sequences so subsequent INSERTs never collide with seeded IDs (ADR-004)
SELECT setval(pg_get_serial_sequence('parents', 'id'), COALESCE((SELECT MAX(id) FROM parents), 1));
SELECT setval(pg_get_serial_sequence('students', 'id'), COALESCE((SELECT MAX(id) FROM students), 1));
SELECT setval(pg_get_serial_sequence('parent_students', 'id'), COALESCE((SELECT MAX(id) FROM parent_students), 1));
SELECT setval(pg_get_serial_sequence('attendance_records', 'id'), COALESCE((SELECT MAX(id) FROM attendance_records), 1));
SELECT setval(pg_get_serial_sequence('daily_reports', 'id'), COALESCE((SELECT MAX(id) FROM daily_reports), 1));
SELECT setval(pg_get_serial_sequence('meal_menus', 'id'), COALESCE((SELECT MAX(id) FROM meal_menus), 1));
SELECT setval(pg_get_serial_sequence('announcements', 'id'), COALESCE((SELECT MAX(id) FROM announcements), 1));
SELECT setval(pg_get_serial_sequence('leave_requests', 'id'), COALESCE((SELECT MAX(id) FROM leave_requests), 1));

