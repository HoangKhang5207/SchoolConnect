-- ==============================================================================
-- SchoolConnect — Idempotent PostgreSQL Schema Definition (8 Tables + 7 Indexes)
-- ==============================================================================

-- 1. PARENTS (Includes Parent and Teacher Demo/Auth Accounts)
CREATE TABLE IF NOT EXISTS parents (
    id BIGSERIAL PRIMARY KEY,
    zalo_user_id VARCHAR(64) UNIQUE,
    full_name VARCHAR(120) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    avatar_url VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'PARENT'
        CONSTRAINT chk_parents_role CHECK (role IN ('PARENT', 'TEACHER')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. STUDENTS (Preschool Student Profiles)
CREATE TABLE IF NOT EXISTS students (
    id BIGSERIAL PRIMARY KEY,
    student_code VARCHAR(32) NOT NULL UNIQUE,
    full_name VARCHAR(120) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(16) NOT NULL,
    class_name VARCHAR(64) NOT NULL,
    teacher_name VARCHAR(120) NOT NULL,
    avatar_url VARCHAR(255),
    height_cm NUMERIC(5, 2) CONSTRAINT chk_students_height CHECK (height_cm IS NULL OR height_cm > 0),
    weight_kg NUMERIC(5, 2) CONSTRAINT chk_students_weight CHECK (weight_kg IS NULL OR weight_kg > 0),
    blood_type VARCHAR(8),
    allergy_notes TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. PARENT_STUDENTS (Many-to-Many Guardian-Student Mapping for RULE-001)
CREATE TABLE IF NOT EXISTS parent_students (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT NOT NULL REFERENCES parents(id) ON DELETE RESTRICT,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE RESTRICT,
    relationship VARCHAR(32) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_parent_student UNIQUE (parent_id, student_id)
);

-- 4. ATTENDANCE_RECORDS (Daily Check-in / Check-out History)
CREATE TABLE IF NOT EXISTS attendance_records (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE RESTRICT,
    attendance_date DATE NOT NULL,
    status VARCHAR(24) NOT NULL
        CONSTRAINT chk_attendance_status CHECK (status IN ('PRESENT', 'ABSENT_EXCUSED', 'ABSENT_UNEXCUSED', 'LATE', 'NOT_MARKED')),
    check_in_time TIME,
    check_out_time TIME,
    pickup_person VARCHAR(120),
    note VARCHAR(255),
    CONSTRAINT uq_student_attendance_date UNIQUE (student_id, attendance_date)
);

-- 5. DAILY_REPORTS (Daily Meals, Sleep, Activities, Mood & Teacher Note)
CREATE TABLE IF NOT EXISTS daily_reports (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE RESTRICT,
    report_date DATE NOT NULL,
    meal_status VARCHAR(20) NOT NULL
        CONSTRAINT chk_report_meal CHECK (meal_status IN ('GOOD', 'FAIR', 'POOR')),
    meal_comment TEXT NOT NULL,
    sleep_status VARCHAR(20) NOT NULL
        CONSTRAINT chk_report_sleep CHECK (sleep_status IN ('GOOD', 'RESTLESS', 'SHORT')),
    sleep_duration_mins INTEGER CONSTRAINT chk_sleep_mins CHECK (sleep_duration_mins IS NULL OR sleep_duration_mins >= 0),
    activity_summary TEXT NOT NULL,
    mood_status VARCHAR(20) NOT NULL
        CONSTRAINT chk_report_mood CHECK (mood_status IN ('HAPPY', 'NORMAL', 'TIRED', 'FUSSY')),
    teacher_note TEXT,
    recorded_by VARCHAR(120) NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_student_report_date UNIQUE (student_id, report_date)
);

-- 6. MEAL_MENUS (Preschool Daily Nutritional Menus: Breakfast, Lunch, Afternoon Snack)
CREATE TABLE IF NOT EXISTS meal_menus (
    id BIGSERIAL PRIMARY KEY,
    menu_date DATE NOT NULL,
    age_group VARCHAR(32) NOT NULL DEFAULT 'ALL',
    meal_type VARCHAR(24) NOT NULL
        CONSTRAINT chk_meal_type CHECK (meal_type IN ('BREAKFAST', 'LUNCH', 'AFTERNOON_SNACK')),
    dishes_json TEXT NOT NULL,
    calories_kcal INTEGER,
    nutrition_note VARCHAR(255),
    CONSTRAINT uq_menu_date_group_type UNIQUE (menu_date, age_group, meal_type)
);

-- 7. ANNOUNCEMENTS (School-wide & Class-specific Notices)
CREATE TABLE IF NOT EXISTS announcements (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    summary VARCHAR(350) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(24) NOT NULL
        CONSTRAINT chk_announcement_cat CHECK (category IN ('SCHOOL', 'CLASS', 'HEALTH', 'EVENT')),
    target_class VARCHAR(64),
    is_pinned BOOLEAN NOT NULL DEFAULT FALSE,
    author_name VARCHAR(120) NOT NULL,
    published_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 8. LEAVE_REQUESTS (Transactional Parent Leave Submission & Teacher Approval Queue)
CREATE TABLE IF NOT EXISTS leave_requests (
    id BIGSERIAL PRIMARY KEY,
    request_code VARCHAR(32) NOT NULL UNIQUE,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE RESTRICT,
    parent_id BIGINT NOT NULL REFERENCES parents(id) ON DELETE RESTRICT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    session_type VARCHAR(20) NOT NULL
        CONSTRAINT chk_leave_session CHECK (session_type IN ('FULL_DAY', 'MORNING', 'AFTERNOON')),
    reason TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
        CONSTRAINT chk_leave_status CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED')),
    teacher_note TEXT,
    reviewed_by VARCHAR(120),
    reviewed_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_leave_dates CHECK (start_date <= end_date)
);

-- Performance Indexes
CREATE INDEX IF NOT EXISTS idx_parent_students_parent ON parent_students (parent_id, student_id);
CREATE INDEX IF NOT EXISTS idx_attendance_student_date ON attendance_records (student_id, attendance_date DESC);
CREATE INDEX IF NOT EXISTS idx_daily_reports_student_date ON daily_reports (student_id, report_date DESC);
CREATE INDEX IF NOT EXISTS idx_meal_menus_date ON meal_menus (menu_date);
CREATE INDEX IF NOT EXISTS idx_announcements_pinned_pub ON announcements (is_pinned DESC, published_at DESC);
CREATE INDEX IF NOT EXISTS idx_leave_requests_student_dates ON leave_requests (student_id, status, start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_leave_requests_status_created ON leave_requests (status, created_at DESC);

