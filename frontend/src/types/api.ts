export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  errorCode?: string;
  fieldErrors?: Record<string, string>;
  timestamp: string;
}

export interface HealthStatusPayload {
  status: 'UP' | 'DEGRADED' | 'DOWN';
  service: string;
  architecture: string;
  database: 'UP' | 'DOWN';
  demoModeEnabled: boolean;
  serverDate: string;
}

export type AppRole = 'PARENT' | 'TEACHER';

export type AuthMode = 'DEMO_MODE' | 'ZALO_AUTH';

export type ParentScreenTab =
  | 'HOME'
  | 'DAILY_REPORT'
  | 'ATTENDANCE'
  | 'MEAL_MENU'
  | 'ANNOUNCEMENTS'
  | 'ABSENCE_REQUEST';

export interface UserProfileDto {
  id: number;
  fullName: string;
  phone: string;
  role: AppRole;
  avatarUrl: string | null;
}

export interface StudentSummaryDto {
  id: number;
  studentCode: string;
  fullName: string;
  className: string;
  teacherName: string;
  gender: string;
  dateOfBirth: string;
  avatarUrl: string | null;
}

export interface AuthResponseDto {
  accessToken: string;
  authMode: AuthMode;
  user: UserProfileDto;
  linkedChildren: StudentSummaryDto[];
}

export interface StudentProfileDetailDto {
  id: number;
  studentCode: string;
  fullName: string;
  dateOfBirth: string;
  gender: string;
  className: string;
  teacherName: string;
  avatarUrl: string | null;
  heightCm: number | null;
  weightKg: number | null;
  bmi: number | null;
  bmiStatus: string;
  bloodType: string | null;
  allergyNotes: string | null;
}

export interface AttendanceRecordDto {
  id: number;
  attendanceDate: string;
  status: 'PRESENT' | 'ABSENT_EXCUSED' | 'ABSENT_UNEXCUSED' | 'LATE' | 'NOT_MARKED';
  checkInTime: string | null;
  checkOutTime: string | null;
  pickupPerson: string | null;
  note: string | null;
}

export interface AttendanceSummaryMetricsDto {
  totalDays: number;
  presentCount: number;
  excusedAbsentCount: number;
  unexcusedAbsentCount: number;
  lateCount: number;
  attendanceRate: number;
}

export interface AttendanceSummaryResponseDto {
  studentId: number;
  studentName: string;
  summary: AttendanceSummaryMetricsDto;
  records: AttendanceRecordDto[];
}

export interface DailyReportDto {
  id: number;
  studentId: number;
  reportDate: string;
  mealStatus: 'GOOD' | 'FAIR' | 'POOR';
  mealComment: string;
  sleepStatus: 'GOOD' | 'RESTLESS' | 'SHORT';
  sleepDurationMins: number | null;
  activitySummary: string;
  moodStatus: 'HAPPY' | 'NORMAL' | 'TIRED' | 'FUSSY';
  teacherNote: string | null;
  recordedBy: string;
  updatedAt: string;
}

export interface MealMenuDto {
  id: number;
  menuDate: string;
  ageGroup: string;
  mealType: 'BREAKFAST' | 'LUNCH' | 'AFTERNOON_SNACK';
  dishes: string[];
  caloriesKcal: number | null;
  nutritionNote: string | null;
}

export interface AnnouncementDto {
  id: number;
  title: string;
  summary: string;
  content: string;
  category: 'SCHOOL' | 'CLASS' | 'HEALTH' | 'EVENT';
  targetClass: string | null;
  isPinned: boolean;
  authorName: string;
  publishedAt: string;
}

export interface DashboardResponseDto {
  student: StudentProfileDetailDto;
  todayAttendance: AttendanceRecordDto | null;
  latestDailyReport: DailyReportDto | null;
  pendingLeaveCount: number;
  recentAnnouncements: AnnouncementDto[];
}

export type LeaveSessionType = 'FULL_DAY' | 'MORNING' | 'AFTERNOON';
export type LeaveStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'CANCELLED';

export interface CreateLeaveRequestDto {
  startDate: string;
  endDate: string;
  sessionType: LeaveSessionType;
  reason: string;
}

export interface ReviewLeaveRequestDto {
  status: 'APPROVED' | 'REJECTED';
  teacherNote: string;
}

export interface LeaveRequestDto {
  id: number;
  requestCode: string;
  studentId: number;
  studentName: string;
  className: string;
  parentName: string;
  parentPhone: string;
  startDate: string;
  endDate: string;
  sessionType: LeaveSessionType;
  reason: string;
  status: LeaveStatus;
  teacherNote: string | null;
  reviewedBy: string | null;
  reviewedAt: string | null;
  createdAt: string;
}
