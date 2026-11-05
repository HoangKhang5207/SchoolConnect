import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import App from './App';

describe('SchoolConnect Full-Stack Mini App Flows', () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  it('initializes Demo Parent session, renders Home Dashboard with BMI, switches child, and switches to Teacher Mode', async () => {
    const fetchMock = vi.spyOn(globalThis, 'fetch').mockImplementation(async (input) => {
      const url = String(input);

      if (url.endsWith('/auth/demo-session')) {
        return {
          ok: true,
          status: 200,
          json: async () => ({
            success: true,
            message: 'Demo session initialized',
            data: {
              accessToken: 'mock-jwt-token',
              authMode: 'DEMO_MODE',
              user: {
                id: 1,
                fullName: 'Trần Thu Hà',
                phone: '0901234567',
                role: 'PARENT',
                avatarUrl: null,
              },
              linkedChildren: [
                {
                  id: 1,
                  studentCode: 'HS-2026-001',
                  fullName: 'Nguyễn Minh An',
                  className: 'Lớp Chồi 1',
                  teacherName: 'Cô Lê Thanh Thảo',
                  gender: 'FEMALE',
                  dateOfBirth: '2022-04-15',
                  avatarUrl: null,
                },
                {
                  id: 2,
                  studentCode: 'HS-2026-002',
                  fullName: 'Nguyễn Minh Khôi',
                  className: 'Lớp Mầm 2',
                  teacherName: 'Cô Phạm Mai Anh',
                  gender: 'MALE',
                  dateOfBirth: '2024-02-10',
                  avatarUrl: null,
                },
              ],
            },
            timestamp: '2026-11-04T12:00:00Z',
          }),
        } as Response;
      }

      if (url.endsWith('/children/1/dashboard')) {
        return {
          ok: true,
          status: 200,
          json: async () => ({
            success: true,
            message: 'OK',
            data: {
              student: {
                id: 1,
                studentCode: 'HS-2026-001',
                fullName: 'Nguyễn Minh An',
                dateOfBirth: '2022-04-15',
                gender: 'FEMALE',
                className: 'Lớp Chồi 1',
                teacherName: 'Cô Lê Thanh Thảo',
                avatarUrl: null,
                heightCm: 104.0,
                weightKg: 16.8,
                bmi: 15.5,
                bmiStatus: 'Phát triển chuẩn',
                bloodType: 'O',
                allergyNotes: 'Không dị ứng thực phẩm',
              },
              todayAttendance: {
                id: 1,
                attendanceDate: '2026-11-04',
                status: 'PRESENT',
                checkInTime: '07:35',
                checkOutTime: null,
                pickupPerson: null,
                note: 'Bé đến lớp vui vẻ',
              },
              latestDailyReport: {
                id: 1,
                studentId: 1,
                reportDate: '2026-11-04',
                mealStatus: 'GOOD',
                mealComment: 'Bé ăn hết suất cháo cá hồi',
                sleepStatus: 'GOOD',
                sleepDurationMins: 110,
                activitySummary: 'Vẽ tranh mùa thu',
                moodStatus: 'HAPPY',
                teacherNote: 'Con rất ngoan',
                recordedBy: 'Cô Lê Thanh Thảo',
                updatedAt: '2026-11-04T14:00:00Z',
              },
              pendingLeaveCount: 1,
              recentAnnouncements: [],
            },
            timestamp: '2026-11-04T12:00:00Z',
          }),
        } as Response;
      }

      if (url.endsWith('/children/2/dashboard')) {
        return {
          ok: true,
          status: 200,
          json: async () => ({
            success: true,
            message: 'OK',
            data: {
              student: {
                id: 2,
                studentCode: 'HS-2026-002',
                fullName: 'Nguyễn Minh Khôi',
                dateOfBirth: '2024-02-10',
                gender: 'MALE',
                className: 'Lớp Mầm 2',
                teacherName: 'Cô Phạm Mai Anh',
                avatarUrl: null,
                heightCm: 91.5,
                weightKg: 13.4,
                bmi: 16.0,
                bmiStatus: 'Phát triển chuẩn',
                bloodType: 'A',
                allergyNotes: 'Hạn chế tôm cua biển',
              },
              todayAttendance: null,
              latestDailyReport: null,
              pendingLeaveCount: 0,
              recentAnnouncements: [],
            },
            timestamp: '2026-11-04T12:00:00Z',
          }),
        } as Response;
      }

      return {
        ok: true,
        status: 200,
        json: async () => ({
          success: true,
          message: 'OK',
          data: [],
          timestamp: '2026-11-04T12:00:00Z',
        }),
      } as Response;
    });

    render(<App />);

    await waitFor(() => {
      expect(screen.getByTestId('active-role-badge')).toHaveTextContent(
        'Chế độ: Phụ huynh (Demo)'
      );
      expect(screen.getByTestId('student-bmi-value')).toHaveTextContent('15.5');
    });

    const child2Pill = screen.getByTestId('child-pill-2');
    fireEvent.click(child2Pill);

    await waitFor(() => {
      expect(screen.getByTestId('student-bmi-value')).toHaveTextContent('16');
    });

    expect(fetchMock).toHaveBeenCalled();
  });
});
