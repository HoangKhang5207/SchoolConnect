--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5
-- Dumped by pg_dump version 17.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: announcements; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.announcements (
    id bigint NOT NULL,
    title character varying(200) NOT NULL,
    summary character varying(350) NOT NULL,
    content text NOT NULL,
    category character varying(24) NOT NULL,
    target_class character varying(64),
    is_pinned boolean DEFAULT false NOT NULL,
    author_name character varying(120) NOT NULL,
    published_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_announcement_cat CHECK (((category)::text = ANY ((ARRAY['SCHOOL'::character varying, 'CLASS'::character varying, 'HEALTH'::character varying, 'EVENT'::character varying])::text[])))
);


ALTER TABLE public.announcements OWNER TO postgres;

--
-- Name: announcements_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.announcements_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.announcements_id_seq OWNER TO postgres;

--
-- Name: announcements_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.announcements_id_seq OWNED BY public.announcements.id;


--
-- Name: attendance_records; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.attendance_records (
    id bigint NOT NULL,
    student_id bigint NOT NULL,
    attendance_date date NOT NULL,
    status character varying(24) NOT NULL,
    check_in_time time without time zone,
    check_out_time time without time zone,
    pickup_person character varying(120),
    note character varying(255),
    CONSTRAINT chk_attendance_status CHECK (((status)::text = ANY ((ARRAY['PRESENT'::character varying, 'ABSENT_EXCUSED'::character varying, 'ABSENT_UNEXCUSED'::character varying, 'LATE'::character varying, 'NOT_MARKED'::character varying])::text[])))
);


ALTER TABLE public.attendance_records OWNER TO postgres;

--
-- Name: attendance_records_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.attendance_records_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.attendance_records_id_seq OWNER TO postgres;

--
-- Name: attendance_records_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.attendance_records_id_seq OWNED BY public.attendance_records.id;


--
-- Name: daily_reports; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.daily_reports (
    id bigint NOT NULL,
    student_id bigint NOT NULL,
    report_date date NOT NULL,
    meal_status character varying(20) NOT NULL,
    meal_comment text NOT NULL,
    sleep_status character varying(20) NOT NULL,
    sleep_duration_mins integer,
    activity_summary text NOT NULL,
    mood_status character varying(20) NOT NULL,
    teacher_note text,
    recorded_by character varying(120) NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_report_meal CHECK (((meal_status)::text = ANY ((ARRAY['GOOD'::character varying, 'FAIR'::character varying, 'POOR'::character varying])::text[]))),
    CONSTRAINT chk_report_mood CHECK (((mood_status)::text = ANY ((ARRAY['HAPPY'::character varying, 'NORMAL'::character varying, 'TIRED'::character varying, 'FUSSY'::character varying])::text[]))),
    CONSTRAINT chk_report_sleep CHECK (((sleep_status)::text = ANY ((ARRAY['GOOD'::character varying, 'RESTLESS'::character varying, 'SHORT'::character varying])::text[]))),
    CONSTRAINT chk_sleep_mins CHECK (((sleep_duration_mins IS NULL) OR (sleep_duration_mins >= 0)))
);


ALTER TABLE public.daily_reports OWNER TO postgres;

--
-- Name: daily_reports_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.daily_reports_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.daily_reports_id_seq OWNER TO postgres;

--
-- Name: daily_reports_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.daily_reports_id_seq OWNED BY public.daily_reports.id;


--
-- Name: leave_requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.leave_requests (
    id bigint NOT NULL,
    request_code character varying(32) NOT NULL,
    student_id bigint NOT NULL,
    parent_id bigint NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    session_type character varying(20) NOT NULL,
    reason text NOT NULL,
    status character varying(20) DEFAULT 'PENDING'::character varying NOT NULL,
    teacher_note text,
    reviewed_by character varying(120),
    reviewed_at timestamp with time zone,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_leave_dates CHECK ((start_date <= end_date)),
    CONSTRAINT chk_leave_session CHECK (((session_type)::text = ANY ((ARRAY['FULL_DAY'::character varying, 'MORNING'::character varying, 'AFTERNOON'::character varying])::text[]))),
    CONSTRAINT chk_leave_status CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying, 'CANCELLED'::character varying])::text[])))
);


ALTER TABLE public.leave_requests OWNER TO postgres;

--
-- Name: leave_requests_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.leave_requests_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.leave_requests_id_seq OWNER TO postgres;

--
-- Name: leave_requests_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.leave_requests_id_seq OWNED BY public.leave_requests.id;


--
-- Name: meal_menus; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.meal_menus (
    id bigint NOT NULL,
    menu_date date NOT NULL,
    age_group character varying(32) DEFAULT 'ALL'::character varying NOT NULL,
    meal_type character varying(24) NOT NULL,
    dishes_json text NOT NULL,
    calories_kcal integer,
    nutrition_note character varying(255),
    CONSTRAINT chk_meal_type CHECK (((meal_type)::text = ANY ((ARRAY['BREAKFAST'::character varying, 'LUNCH'::character varying, 'AFTERNOON_SNACK'::character varying])::text[])))
);


ALTER TABLE public.meal_menus OWNER TO postgres;

--
-- Name: meal_menus_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.meal_menus_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.meal_menus_id_seq OWNER TO postgres;

--
-- Name: meal_menus_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.meal_menus_id_seq OWNED BY public.meal_menus.id;


--
-- Name: parent_students; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parent_students (
    id bigint NOT NULL,
    parent_id bigint NOT NULL,
    student_id bigint NOT NULL,
    relationship character varying(32) NOT NULL,
    is_primary boolean DEFAULT false NOT NULL
);


ALTER TABLE public.parent_students OWNER TO postgres;

--
-- Name: parent_students_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.parent_students_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.parent_students_id_seq OWNER TO postgres;

--
-- Name: parent_students_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.parent_students_id_seq OWNED BY public.parent_students.id;


--
-- Name: parents; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parents (
    id bigint NOT NULL,
    zalo_user_id character varying(64),
    full_name character varying(120) NOT NULL,
    phone character varying(20) NOT NULL,
    avatar_url character varying(255),
    role character varying(20) DEFAULT 'PARENT'::character varying NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_parents_role CHECK (((role)::text = ANY ((ARRAY['PARENT'::character varying, 'TEACHER'::character varying])::text[])))
);


ALTER TABLE public.parents OWNER TO postgres;

--
-- Name: parents_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.parents_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.parents_id_seq OWNER TO postgres;

--
-- Name: parents_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.parents_id_seq OWNED BY public.parents.id;


--
-- Name: students; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.students (
    id bigint NOT NULL,
    student_code character varying(32) NOT NULL,
    full_name character varying(120) NOT NULL,
    date_of_birth date NOT NULL,
    gender character varying(16) NOT NULL,
    class_name character varying(64) NOT NULL,
    teacher_name character varying(120) NOT NULL,
    avatar_url character varying(255),
    height_cm numeric(5,2),
    weight_kg numeric(5,2),
    blood_type character varying(8),
    allergy_notes text,
    is_active boolean DEFAULT true NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_students_height CHECK (((height_cm IS NULL) OR (height_cm > (0)::numeric))),
    CONSTRAINT chk_students_weight CHECK (((weight_kg IS NULL) OR (weight_kg > (0)::numeric)))
);


ALTER TABLE public.students OWNER TO postgres;

--
-- Name: students_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.students_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.students_id_seq OWNER TO postgres;

--
-- Name: students_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.students_id_seq OWNED BY public.students.id;


--
-- Name: announcements id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.announcements ALTER COLUMN id SET DEFAULT nextval('public.announcements_id_seq'::regclass);


--
-- Name: attendance_records id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attendance_records ALTER COLUMN id SET DEFAULT nextval('public.attendance_records_id_seq'::regclass);


--
-- Name: daily_reports id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.daily_reports ALTER COLUMN id SET DEFAULT nextval('public.daily_reports_id_seq'::regclass);


--
-- Name: leave_requests id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.leave_requests ALTER COLUMN id SET DEFAULT nextval('public.leave_requests_id_seq'::regclass);


--
-- Name: meal_menus id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.meal_menus ALTER COLUMN id SET DEFAULT nextval('public.meal_menus_id_seq'::regclass);


--
-- Name: parent_students id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parent_students ALTER COLUMN id SET DEFAULT nextval('public.parent_students_id_seq'::regclass);


--
-- Name: parents id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parents ALTER COLUMN id SET DEFAULT nextval('public.parents_id_seq'::regclass);


--
-- Name: students id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students ALTER COLUMN id SET DEFAULT nextval('public.students_id_seq'::regclass);


--
-- Data for Name: announcements; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.announcements (id, title, summary, content, category, target_class, is_pinned, author_name, published_at) FROM stdin;
1	Thông báo Lịch khám sức khỏe định kỳ Học kỳ I năm học 2026-2027	Nhà trường phối hợp cùng Bác sĩ Nhi khoa tổ chức khám sức khỏe tổng quát cho toàn bộ các bé vào sáng Thứ Sáu (06/11/2026).	Kính gửi Quý Phụ huynh Trường Mầm Non Sao Mai,\\n\\nNhằm theo dõi sự phát triển thể chất của các con, Nhà trường tổ chức đợt khám sức khỏe định kỳ Học kỳ I vào lúc 08:00 sáng Thứ Sáu, ngày 06/11/2026.\\n\\nNội dung khám bao gồm: Đo chiều cao, cân nặng, đánh giá BMI, khám răng hàm mặt, tai mũi họng và tim phổi tổng quát. Kính mong Quý Phụ huynh đưa bé đến lớp đúng giờ để buổi khám diễn ra thuận lợi.	HEALTH	\N	t	Ban Giám Hiệu - Trường Mầm Non Sao Mai	2026-11-04 07:30:00+07
2	Hoạt động trải nghiệm: Ngày hội Làm bánh & Chia sẻ yêu thương	Các bé khối Chồi và Mầm sẽ tham gia tự tay nặn bánh quy bơ hạt dinh dưỡng vào chiều Thứ Năm (05/11/2026).	Chào bố mẹ Lớp Chồi 1,\\n\\nChiều mai (Thứ Năm, 05/11/2026), cô và các bé sẽ có tiết học trải nghiệm thực hành "Đầu bếp nhí tài ba". Toàn bộ nguyên liệu bột mì hữu cơ, bơ lạt và tạp dề đã được nhà trường chuẩn bị sẵn và đảm bảo vệ sinh ATTP.\\n\\nSau buổi học, mỗi bé sẽ mang về một túi bánh nhỏ dành tặng ông bà, bố mẹ!	CLASS	Lớp Chồi 1	t	Cô Lê Thanh Thảo	2026-11-03 17:00:00+07
3	Khuyến nghị giữ ấm và phòng ngừa cúm mùa giao mùa tháng 11	Phòng Y tế nhà trường gửi hướng dẫn phối hợp chăm sóc hô hấp cho trẻ khi thời tiết sáng sớm chuyển lạnh.	Kính gửi Quý Phụ huynh,\\n\\nHiện nay thời tiết buổi sáng sớm và chiều tối có sự chênh lệch nhiệt độ. Quý Phụ huynh vui lòng chuẩn bị thêm 01 áo khoác mỏng trong ba lô cho bé và thông báo ngay trên ứng dụng nếu bé có biểu hiện sốt hoặc ho để cô giáo phối hợp theo dõi.	HEALTH	\N	f	Phòng Y Tế Học Đường	2026-11-02 09:00:00+07
4	Thực đơn dinh dưỡng mùa Thu - Đông đã được cập nhật mới	Bếp ăn bán trú tăng cường các món súp ấm, hạt sen và trái cây giàu Vitamin C trong thực đơn tháng 11.	Nhà trường đã cập nhật thực đơn dinh dưỡng tháng 11/2026 được thẩm định bởi chuyên gia dinh dưỡng mầm non. Quý Phụ huynh có thể tra cứu chi tiết từng bữa Sáng - Trưa - Xế ngay tại mục Thực đơn trên ứng dụng SchoolConnect.	SCHOOL	\N	f	Bộ Phận Dinh Dưỡng Bán Trú	2026-11-01 10:00:00+07
\.


--
-- Data for Name: attendance_records; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.attendance_records (id, student_id, attendance_date, status, check_in_time, check_out_time, pickup_person, note) FROM stdin;
1	1	2026-11-04	PRESENT	07:35:00	\N	\N	Bé đến lớp vui vẻ, thân nhiệt 36.6°C
2	1	2026-11-03	PRESENT	07:40:00	16:35:00	Mẹ Trần Thu Hà	Bé ngoan, tự cất ba lô gọn gàng
3	1	2026-11-02	PRESENT	07:32:00	16:40:00	Mẹ Trần Thu Hà	Tham gia đầy đủ hoạt động ngoài trời
4	1	2026-10-30	LATE	08:25:00	16:30:00	Bố Nguyễn Hoàng Nam	Gia đình báo đến muộn do trời mưa lớn
5	1	2026-10-29	ABSENT_EXCUSED	\N	\N	\N	Nghỉ có phép: Bé đi khám răng định kỳ
6	1	2026-10-28	PRESENT	07:38:00	16:25:00	Mẹ Trần Thu Hà	Sức khỏe bình thường
7	2	2026-11-04	PRESENT	07:36:00	\N	\N	Bé Khôi vào lớp ngoan, thân nhiệt 36.5°C
8	2	2026-11-03	PRESENT	07:42:00	16:30:00	Mẹ Trần Thu Hà	Bé ăn ngủ tốt cả ngày
9	2	2026-11-02	ABSENT_EXCUSED	\N	\N	\N	Nghỉ có phép theo đơn gia đình
\.


--
-- Data for Name: daily_reports; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.daily_reports (id, student_id, report_date, meal_status, meal_comment, sleep_status, sleep_duration_mins, activity_summary, mood_status, teacher_note, recorded_by, updated_at) FROM stdin;
1	1	2026-11-04	GOOD	Bé ăn hết 100% suất cháo cá hồi bí đỏ buổi sáng và cơm gà hầm hạt sen buổi trưa, uống hết 180ml sữa hạt.	GOOD	110	Tham gia giờ vẽ tranh chủ đề Mùa Thu, nhận biết màu sắc và nhảy vận động theo nhạc cùng các bạn.	HAPPY	Hôm nay Minh An rất ngoan, biết phụ cô xếp ghế và chia sẻ đồ chơi với bạn.	Cô Lê Thanh Thảo	2026-11-04 14:00:00+07
2	1	2026-11-03	GOOD	Bé tự xúc ăn gọn gàng, ăn hết khẩu phần trưa và bữa xế sữa chua phô mai.	GOOD	105	Học đếm số từ 1 đến 10 qua trò chơi xếp gỗ và nghe kể chuyện cổ tích.	HAPPY	Con tập trung tốt trong giờ học tạo hình.	Cô Lê Thanh Thảo	2026-11-03 15:30:00+07
3	1	2026-11-02	FAIR	Buổi sáng bé ăn hơi chậm, buổi trưa ăn được 80% suất cơm và uống bù thêm sữa.	RESTLESS	80	Hoạt động tưới cây trong vườn trường và tập thể dục nhịp điệu buổi sáng.	NORMAL	Cô đã nhắc bé uống thêm nước ấm vào buổi chiều.	Cô Lê Thanh Thảo	2026-11-02 15:30:00+07
4	2	2026-11-04	GOOD	Bé Khôi ăn hết bát súp gà nấm và uống hết 150ml sữa công thức.	GOOD	120	Tập nhận biết hình tròn - hình vuông và chơi lăn bóng vận động tinh.	HAPPY	Bé Khôi hôm nay không quấy khóc khi mẹ về, rất đáng khen!	Cô Phạm Mai Anh	2026-11-04 14:10:00+07
5	2	2026-11-03	GOOD	Bé ăn ngon miệng, đặc biệt thích món bánh flan bữa xế.	GOOD	115	Nghe nhạc thiếu nhi và tập vỗ tay theo nhịp cùng cô giáo.	HAPPY	Con ngủ trưa rất sâu giấc.	Cô Phạm Mai Anh	2026-11-03 15:20:00+07
\.


--
-- Data for Name: leave_requests; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.leave_requests (id, request_code, student_id, parent_id, start_date, end_date, session_type, reason, status, teacher_note, reviewed_by, reviewed_at, created_at, updated_at) FROM stdin;
1	LR-20261029-0001	1	1	2026-10-29	2026-10-29	FULL_DAY	Gia đình đưa bé Minh An đi khám và bôi vecni fluor ngừa sâu răng định kỳ tại nha khoa.	APPROVED	Cô Thảo đã ghi nhận và báo bếp nghỉ ăn ngày 29/10. Chúc bé đi khám răng ngoan nhé!	Cô Lê Thanh Thảo	2026-10-28 19:15:00+07	2026-10-28 18:30:00+07	2026-10-28 19:15:00+07
2	LR-20261104-0002	1	1	2026-11-07	2026-11-07	MORNING	Gia đình xin cho bé nghỉ buổi sáng Thứ Bảy để về thăm ông bà ngoại, buổi chiều bé tự học ở nhà.	PENDING	\N	\N	\N	2026-11-04 08:30:00+07	2026-11-04 08:30:00+07
3	LR-20261102-0001	2	1	2026-11-02	2026-11-02	FULL_DAY	Bé Minh Khôi hơi sổ mũi nhẹ sau ngày cuối tuần nên mẹ xin cho con nghỉ ở nhà theo dõi 1 ngày.	APPROVED	Nhà trường đã duyệt đơn nghỉ phép. Chúc bé Khôi mau khỏe để mai đến lớp cùng các bạn!	Cô Lê Thanh Thảo	2026-11-01 20:10:00+07	2026-11-01 19:45:00+07	2026-11-01 20:10:00+07
\.


--
-- Data for Name: meal_menus; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.meal_menus (id, menu_date, age_group, meal_type, dishes_json, calories_kcal, nutrition_note) FROM stdin;
1	2026-11-04	ALL	BREAKFAST	["Cháo cá hồi bí đỏ tươi", "Sữa hạt óc chó hạnh nhân (180ml)"]	320	Giàu Omega-3, DHA và Vitamin A hỗ trợ phát triển thị giác
2	2026-11-04	ALL	LUNCH	["Cơm mềm hữu cơ", "Thịt gà hầm hạt sen cà rốt", "Canh rau ngót thịt bằm", "Chuối chín tráng miệng"]	485	Cân bằng đạm dễ tiêu hóa, sắt và chất xơ tự nhiên
3	2026-11-04	ALL	AFTERNOON_SNACK	["Sữa chua phô mai tươi", "Bánh flan trứng sữa nhà làm"]	215	Bổ sung lợi khuẩn Probiotics và Canxi cho xương chắc khỏe
4	2026-11-05	ALL	BREAKFAST	["Nui sao nấu thịt bằm & rau củ", "Sữa tươi tiệt trùng ít đường (180ml)"]	310	Cung cấp năng lượng khởi đầu ngày mới và Vitamin nhóm B
5	2026-11-05	ALL	LUNCH	["Cơm mềm", "Cá lóc hấp bầu non", "Canh bí xanh nấu mọc", "Đu đủ chín ngọt"]	460	Đạm cá lành tính kết hợp Vitamin C tăng đề kháng
6	2026-11-05	ALL	AFTERNOON_SNACK	["Chè đậu xanh hạt sen đường phèn", "Sữa hạt sen (150ml)"]	205	Thanh nhiệt, giàu chất xơ và khoáng chất
7	2026-11-03	ALL	BREAKFAST	["Súp gà xé nấm hương ngô ngọt", "Sữa tươi ấm (180ml)"]	305	Dễ tiêu hóa, giữ ấm bụng buổi sáng
8	2026-11-03	ALL	LUNCH	["Cơm mềm", "Thịt bò xào bông cải xanh", "Canh khoai mỡ nấu thịt", "Cam sành ngọt"]	490	Giàu sắt, kẽm và Vitamin C tự nhiên
9	2026-11-03	ALL	AFTERNOON_SNACK	["Bánh bông lan trứng muối nhẹ", "Nước ép táo tươi"]	220	Bổ sung năng lượng cho hoạt động vui chơi buổi chiều
\.


--
-- Data for Name: parent_students; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.parent_students (id, parent_id, student_id, relationship, is_primary) FROM stdin;
1	1	1	Mẹ	t
2	1	2	Mẹ	f
3	3	3	Bố	t
\.


--
-- Data for Name: parents; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.parents (id, zalo_user_id, full_name, phone, avatar_url, role, created_at) FROM stdin;
1	zalo_demo_parent_01	Trần Thu Hà	0901234567	\N	PARENT	2026-09-01 08:00:00+07
2	zalo_demo_teacher_01	Cô Lê Thanh Thảo	0909888777	\N	TEACHER	2026-09-01 08:00:00+07
3	zalo_demo_parent_02	Phạm Quốc Bảo	0912345678	\N	PARENT	2026-09-01 08:00:00+07
\.


--
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.students (id, student_code, full_name, date_of_birth, gender, class_name, teacher_name, avatar_url, height_cm, weight_kg, blood_type, allergy_notes, is_active, created_at) FROM stdin;
1	HS-2026-001	Nguyễn Minh An	2022-04-15	FEMALE	Lớp Chồi 1	Cô Lê Thanh Thảo	\N	104.00	16.80	O	Không dị ứng thực phẩm	t	2026-09-01 08:00:00+07
2	HS-2026-002	Nguyễn Minh Khôi	2024-02-10	MALE	Lớp Mầm 2	Cô Phạm Mai Anh	\N	91.50	13.40	A	Hạn chế tôm cua biển có vỏ	t	2026-09-01 08:00:00+07
3	HS-2026-003	Phạm Gia Hân	2022-08-20	FEMALE	Lớp Chồi 1	Cô Lê Thanh Thảo	\N	102.00	15.90	B	Không dị ứng thực phẩm	t	2026-09-01 08:00:00+07
\.


--
-- Name: announcements_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.announcements_id_seq', 4, true);


--
-- Name: attendance_records_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.attendance_records_id_seq', 9, true);


--
-- Name: daily_reports_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.daily_reports_id_seq', 5, true);


--
-- Name: leave_requests_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.leave_requests_id_seq', 3, true);


--
-- Name: meal_menus_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.meal_menus_id_seq', 9, true);


--
-- Name: parent_students_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.parent_students_id_seq', 3, true);


--
-- Name: parents_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.parents_id_seq', 3, true);


--
-- Name: students_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.students_id_seq', 3, true);


--
-- Name: announcements announcements_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.announcements
    ADD CONSTRAINT announcements_pkey PRIMARY KEY (id);


--
-- Name: attendance_records attendance_records_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attendance_records
    ADD CONSTRAINT attendance_records_pkey PRIMARY KEY (id);


--
-- Name: daily_reports daily_reports_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.daily_reports
    ADD CONSTRAINT daily_reports_pkey PRIMARY KEY (id);


--
-- Name: leave_requests leave_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.leave_requests
    ADD CONSTRAINT leave_requests_pkey PRIMARY KEY (id);


--
-- Name: leave_requests leave_requests_request_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.leave_requests
    ADD CONSTRAINT leave_requests_request_code_key UNIQUE (request_code);


--
-- Name: meal_menus meal_menus_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.meal_menus
    ADD CONSTRAINT meal_menus_pkey PRIMARY KEY (id);


--
-- Name: parent_students parent_students_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parent_students
    ADD CONSTRAINT parent_students_pkey PRIMARY KEY (id);


--
-- Name: parents parents_phone_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parents
    ADD CONSTRAINT parents_phone_key UNIQUE (phone);


--
-- Name: parents parents_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parents
    ADD CONSTRAINT parents_pkey PRIMARY KEY (id);


--
-- Name: parents parents_zalo_user_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parents
    ADD CONSTRAINT parents_zalo_user_id_key UNIQUE (zalo_user_id);


--
-- Name: students students_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (id);


--
-- Name: students students_student_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_student_code_key UNIQUE (student_code);


--
-- Name: meal_menus uq_menu_date_group_type; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.meal_menus
    ADD CONSTRAINT uq_menu_date_group_type UNIQUE (menu_date, age_group, meal_type);


--
-- Name: parent_students uq_parent_student; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parent_students
    ADD CONSTRAINT uq_parent_student UNIQUE (parent_id, student_id);


--
-- Name: attendance_records uq_student_attendance_date; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attendance_records
    ADD CONSTRAINT uq_student_attendance_date UNIQUE (student_id, attendance_date);


--
-- Name: daily_reports uq_student_report_date; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.daily_reports
    ADD CONSTRAINT uq_student_report_date UNIQUE (student_id, report_date);


--
-- Name: idx_announcements_pinned_pub; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_announcements_pinned_pub ON public.announcements USING btree (is_pinned DESC, published_at DESC);


--
-- Name: idx_attendance_student_date; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_attendance_student_date ON public.attendance_records USING btree (student_id, attendance_date DESC);


--
-- Name: idx_daily_reports_student_date; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_daily_reports_student_date ON public.daily_reports USING btree (student_id, report_date DESC);


--
-- Name: idx_leave_requests_status_created; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_leave_requests_status_created ON public.leave_requests USING btree (status, created_at DESC);


--
-- Name: idx_leave_requests_student_dates; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_leave_requests_student_dates ON public.leave_requests USING btree (student_id, status, start_date, end_date);


--
-- Name: idx_meal_menus_date; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_meal_menus_date ON public.meal_menus USING btree (menu_date);


--
-- Name: idx_parent_students_parent; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_parent_students_parent ON public.parent_students USING btree (parent_id, student_id);


--
-- Name: attendance_records attendance_records_student_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attendance_records
    ADD CONSTRAINT attendance_records_student_id_fkey FOREIGN KEY (student_id) REFERENCES public.students(id) ON DELETE RESTRICT;


--
-- Name: daily_reports daily_reports_student_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.daily_reports
    ADD CONSTRAINT daily_reports_student_id_fkey FOREIGN KEY (student_id) REFERENCES public.students(id) ON DELETE RESTRICT;


--
-- Name: leave_requests leave_requests_parent_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.leave_requests
    ADD CONSTRAINT leave_requests_parent_id_fkey FOREIGN KEY (parent_id) REFERENCES public.parents(id) ON DELETE RESTRICT;


--
-- Name: leave_requests leave_requests_student_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.leave_requests
    ADD CONSTRAINT leave_requests_student_id_fkey FOREIGN KEY (student_id) REFERENCES public.students(id) ON DELETE RESTRICT;


--
-- Name: parent_students parent_students_parent_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parent_students
    ADD CONSTRAINT parent_students_parent_id_fkey FOREIGN KEY (parent_id) REFERENCES public.parents(id) ON DELETE RESTRICT;


--
-- Name: parent_students parent_students_student_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parent_students
    ADD CONSTRAINT parent_students_student_id_fkey FOREIGN KEY (student_id) REFERENCES public.students(id) ON DELETE RESTRICT;


--
-- PostgreSQL database dump complete
--

