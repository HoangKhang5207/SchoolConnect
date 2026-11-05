import type {
  AnnouncementDto,
  ApiResponse,
  AppRole,
  AttendanceSummaryResponseDto,
  AuthResponseDto,
  CreateLeaveRequestDto,
  DailyReportDto,
  DashboardResponseDto,
  HealthStatusPayload,
  LeaveRequestDto,
  MealMenuDto,
  ReviewLeaveRequestDto,
  StudentSummaryDto,
} from '../types/api';

const DEFAULT_API_BASE_URL = 'http://localhost:8080/api/v1';

export class ApiClientError extends Error {
  public readonly status: number;
  public readonly errorCode?: string;
  public readonly fieldErrors?: Record<string, string>;

  constructor(
    message: string,
    status: number,
    errorCode?: string,
    fieldErrors?: Record<string, string>
  ) {
    super(message);
    this.name = 'ApiClientError';
    this.status = status;
    this.errorCode = errorCode;
    this.fieldErrors = fieldErrors;
  }
}

export function getApiBaseUrl(): string {
  // If running inside a local browser simulator (e.g. localhost or 127.0.0.1),
  // connect directly to local backend port 8080 to guarantee flawless offline/local demo.
  if (
    typeof window !== 'undefined' &&
    (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1')
  ) {
    return DEFAULT_API_BASE_URL;
  }

  const configured = import.meta.env.VITE_API_BASE_URL as string | undefined;
  return configured && configured.trim().length > 0 ? configured.trim() : DEFAULT_API_BASE_URL;
}

export async function requestJson<T>(
  endpoint: string,
  options: RequestInit = {},
  jwtToken?: string | null
): Promise<ApiResponse<T>> {
  const headers = new Headers(options.headers || {});
  if (!headers.has('Content-Type')) {
    headers.set('Content-Type', 'application/json');
  }
  const baseUrl = getApiBaseUrl();
  // Only inject Bypass-Tunnel-Reminder when connecting through Localtunnel
  if (baseUrl.includes('loca.lt')) {
    headers.set('Bypass-Tunnel-Reminder', 'true');
  }

  if (jwtToken) {
    headers.set('Authorization', `Bearer ${jwtToken}`);
  }

  const url = `${baseUrl}${endpoint}`;
  const response = await fetch(url, {
    ...options,
    headers,
  });

  const contentType = response.headers?.get?.('content-type') || '';
  if (contentType && !contentType.includes('application/json')) {
    const rawText = await response.text();
    if (response.status === 511 || rawText.includes('localtunnel')) {
      throw new ApiClientError(
        'Đường hầm Localtunnel yêu cầu xác thực bảo mật. Hãy sử dụng Cloudflare Tunnel (cloudflared) hoặc mở đường link trên trình duyệt để kích hoạt.',
        response.status
      );
    }
    throw new ApiClientError(
      `Phản hồi máy chủ không hợp lệ (HTTP ${response.status}). Nội dung nhận được là HTML thay vì JSON.`,
      response.status
    );
  }

  let payload: ApiResponse<T>;
  try {
    payload = (await response.json()) as ApiResponse<T>;
  } catch {
    throw new ApiClientError(
      `Không thể giải mã dữ liệu JSON từ máy chủ (HTTP ${response.status}).`,
      response.status
    );
  }

  if (!response.ok || !payload.success) {
    throw new ApiClientError(
      payload.message || `HTTP Error ${response.status}`,
      response.status,
      payload.errorCode,
      payload.fieldErrors
    );
  }
  return payload;
}

export async function fetchBackendHealth(): Promise<ApiResponse<HealthStatusPayload>> {
  return requestJson<HealthStatusPayload>('/health', { method: 'GET' });
}

export async function loginDemoSession(role: AppRole): Promise<ApiResponse<AuthResponseDto>> {
  return requestJson<AuthResponseDto>('/auth/demo-session', {
    method: 'POST',
    body: JSON.stringify({ role }),
  });
}

export async function loginZaloToken(accessToken: string): Promise<ApiResponse<AuthResponseDto>> {
  return requestJson<AuthResponseDto>('/auth/zalo-login', {
    method: 'POST',
    body: JSON.stringify({ accessToken }),
  });
}

export async function fetchLinkedChildren(
  token: string
): Promise<ApiResponse<StudentSummaryDto[]>> {
  return requestJson<StudentSummaryDto[]>('/parent/children', { method: 'GET' }, token);
}

export async function fetchStudentDashboard(
  token: string,
  studentId: number
): Promise<ApiResponse<DashboardResponseDto>> {
  return requestJson<DashboardResponseDto>(
    `/children/${studentId}/dashboard`,
    { method: 'GET' },
    token
  );
}

export async function fetchStudentAttendance(
  token: string,
  studentId: number,
  month?: string
): Promise<ApiResponse<AttendanceSummaryResponseDto>> {
  const query = month ? `?month=${encodeURIComponent(month)}` : '';
  return requestJson<AttendanceSummaryResponseDto>(
    `/children/${studentId}/attendance${query}`,
    { method: 'GET' },
    token
  );
}

export async function fetchDailyReports(
  token: string,
  studentId: number,
  date?: string
): Promise<ApiResponse<DailyReportDto[]>> {
  const query = date ? `?date=${encodeURIComponent(date)}` : '';
  return requestJson<DailyReportDto[]>(
    `/children/${studentId}/daily-reports${query}`,
    { method: 'GET' },
    token
  );
}

export async function fetchMealMenus(
  token: string,
  date?: string
): Promise<ApiResponse<MealMenuDto[]>> {
  const query = date ? `?date=${encodeURIComponent(date)}` : '';
  return requestJson<MealMenuDto[]>(`/meal-menus${query}`, { method: 'GET' }, token);
}

export async function fetchAnnouncements(
  token: string,
  category?: string
): Promise<ApiResponse<AnnouncementDto[]>> {
  const query =
    category && category !== 'ALL' ? `?category=${encodeURIComponent(category)}` : '';
  return requestJson<AnnouncementDto[]>(`/announcements${query}`, { method: 'GET' }, token);
}

export async function submitParentLeaveRequest(
  token: string,
  studentId: number,
  body: CreateLeaveRequestDto
): Promise<ApiResponse<LeaveRequestDto>> {
  return requestJson<LeaveRequestDto>(
    `/children/${studentId}/leave-requests`,
    {
      method: 'POST',
      body: JSON.stringify(body),
    },
    token
  );
}

export async function fetchParentLeaveRequests(
  token: string,
  studentId?: number
): Promise<ApiResponse<LeaveRequestDto[]>> {
  const query = studentId ? `?studentId=${studentId}` : '';
  return requestJson<LeaveRequestDto[]>(`/leave-requests${query}`, { method: 'GET' }, token);
}

export async function cancelParentLeaveRequest(
  token: string,
  leaveRequestId: number
): Promise<ApiResponse<LeaveRequestDto>> {
  return requestJson<LeaveRequestDto>(
    `/leave-requests/${leaveRequestId}/cancel`,
    { method: 'PATCH' },
    token
  );
}

export async function fetchTeacherLeaveRequests(
  token: string,
  status?: string
): Promise<ApiResponse<LeaveRequestDto[]>> {
  const query = status && status !== 'ALL' ? `?status=${encodeURIComponent(status)}` : '';
  return requestJson<LeaveRequestDto[]>(
    `/teacher/leave-requests${query}`,
    { method: 'GET' },
    token
  );
}

export async function reviewTeacherLeaveRequest(
  token: string,
  leaveRequestId: number,
  body: ReviewLeaveRequestDto
): Promise<ApiResponse<LeaveRequestDto>> {
  return requestJson<LeaveRequestDto>(
    `/teacher/leave-requests/${leaveRequestId}/review`,
    {
      method: 'PATCH',
      body: JSON.stringify(body),
    },
    token
  );
}
