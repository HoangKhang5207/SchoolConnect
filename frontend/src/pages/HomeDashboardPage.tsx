import { useCallback, useEffect, useState } from 'react';
import {
  Activity,
  Bell,
  BookOpen,
  CalendarCheck,
  ChevronRight,
  Clock,
  FilePlus2,
  HeartPulse,
  Moon,
  Smile,
  Utensils,
} from 'lucide-react';
import { EmptyStateView, ErrorStateView, LoadingStateView } from '../components/AsyncStateViews';
import { fetchStudentDashboard } from '../services/apiClient';
import { useAppContext } from '../store/AppContext';
import type { DashboardResponseDto } from '../types/api';
import { formatVietnameseDate, translateAttendanceStatus } from '../utils/formatters';

export function HomeDashboardPage() {
  const { token, selectedStudentId, navigateTab } = useAppContext();
  const [data, setData] = useState<DashboardResponseDto | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const loadDashboard = useCallback(async () => {
    if (!token) {
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const res = await fetchStudentDashboard(token, selectedStudentId);
      setData(res.data);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Không thể tải bảng tin của bé');
    } finally {
      setLoading(false);
    }
  }, [token, selectedStudentId]);

  useEffect(() => {
    void loadDashboard();
  }, [loadDashboard]);

  if (loading) {
    return <LoadingStateView message="Đang tải hồ sơ & hoạt động hôm nay của bé..." />;
  }

  if (error) {
    return <ErrorStateView message={error} onRetry={() => void loadDashboard()} />;
  }

  if (!data) {
    return <EmptyStateView />;
  }

  const { student, todayAttendance, latestDailyReport, pendingLeaveCount, recentAnnouncements } =
    data;
  const attBadge = translateAttendanceStatus(todayAttendance?.status || 'NOT_MARKED');

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
      {/* 1. Student Profile & Physical BMI Card */}
      <section className="sc-card" data-testid="dashboard-student-card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
          <div>
            <span className="sc-badge sc-badge-success">{student.className}</span>
            <h2 style={{ fontSize: '18px', fontWeight: 800, marginTop: '6px' }}>
              {student.fullName}
            </h2>
            <p style={{ fontSize: '12px', color: 'var(--sc-text-secondary)', marginTop: '2px' }}>
              Mã HS: <strong>{student.studentCode}</strong> • GVCN:{' '}
              <strong>{student.teacherName}</strong>
            </p>
          </div>
          <div
            style={{
              background: 'var(--sc-primary-tint)',
              border: '1px solid var(--sc-primary-soft)',
              borderRadius: '12px',
              padding: '8px 10px',
              textAlign: 'center',
            }}
          >
            <div style={{ fontSize: '10px', color: 'var(--sc-primary-dark)', fontWeight: 700 }}>
              CHỈ SỐ BMI
            </div>
            <div
              style={{ fontSize: '16px', fontWeight: 800, color: 'var(--sc-primary)' }}
              data-testid="student-bmi-value"
            >
              {student.bmi ?? '—'}
            </div>
            <div style={{ fontSize: '10px', color: 'var(--sc-text-secondary)' }}>
              {student.bmiStatus}
            </div>
          </div>
        </div>

        <div
          style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(3, 1fr)',
            gap: '8px',
            marginTop: '12px',
            paddingTop: '12px',
            borderTop: '1px dashed var(--sc-border)',
            fontSize: '12px',
          }}
        >
          <div>
            <span style={{ color: 'var(--sc-text-secondary)' }}>Chiều cao:</span>
            <div style={{ fontWeight: 700 }}>{student.heightCm ?? '—'} cm</div>
          </div>
          <div>
            <span style={{ color: 'var(--sc-text-secondary)' }}>Cân nặng:</span>
            <div style={{ fontWeight: 700 }}>{student.weightKg ?? '—'} kg</div>
          </div>
          <div>
            <span style={{ color: 'var(--sc-text-secondary)' }}>Lưu ý y tế:</span>
            <div style={{ fontWeight: 700, fontSize: '11px' }}>
              {student.allergyNotes || 'Bình thường'}
            </div>
          </div>
        </div>
      </section>

      {/* 2. Today's Attendance Status Card */}
      <section className="sc-card" data-testid="dashboard-attendance-card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <CalendarCheck size={19} color="#0f766e" />
            <h3 style={{ fontSize: '15px', fontWeight: 700 }}>Điểm danh hôm nay</h3>
          </div>
          <span className={`sc-badge sc-badge-${attBadge.tone}`}>{attBadge.label}</span>
        </div>

        {todayAttendance ? (
          <div style={{ marginTop: '10px', fontSize: '13px', color: 'var(--sc-text-secondary)' }}>
            <div style={{ display: 'flex', gap: '16px', marginBottom: '4px' }}>
              <span>
                <Clock size={13} style={{ verticalAlign: 'middle', marginRight: '4px' }} />
                Giờ đến lớp: <strong>{todayAttendance.checkInTime || '—'}</strong>
              </span>
              <span>
                Giờ đón về: <strong>{todayAttendance.checkOutTime || 'Chưa đón'}</strong>
              </span>
            </div>
            {todayAttendance.note && <p>Ghi chú: {todayAttendance.note}</p>}
          </div>
        ) : (
          <p style={{ marginTop: '8px', fontSize: '13px', color: 'var(--sc-text-secondary)' }}>
            Hôm nay chưa có bản ghi điểm danh.
          </p>
        )}
      </section>

      {/* 3. Quick Navigation Pill Grid */}
      <section className="sc-card">
        <h3 style={{ fontSize: '14px', fontWeight: 700, marginBottom: '10px' }}>
          Tiện ích Sổ Liên Lạc Điện Tử
        </h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '10px' }}>
          <button
            type="button"
            className="sc-btn-outline"
            onClick={() => navigateTab('DAILY_REPORT')}
          >
            <BookOpen size={16} />
            <span>Nhật ký Bé ngoan</span>
          </button>
          <button
            type="button"
            className="sc-btn-outline"
            onClick={() => navigateTab('ATTENDANCE')}
          >
            <CalendarCheck size={16} />
            <span>Lịch sử Điểm danh</span>
          </button>
          <button type="button" className="sc-btn-outline" onClick={() => navigateTab('MEAL_MENU')}>
            <Utensils size={16} />
            <span>Thực đơn Dinh dưỡng</span>
          </button>
          <button
            type="button"
            className="sc-btn-primary"
            onClick={() => navigateTab('ABSENCE_REQUEST')}
          >
            <FilePlus2 size={16} />
            <span>
              Xin nghỉ phép {pendingLeaveCount > 0 ? `(${pendingLeaveCount} chờ)` : ''}
            </span>
          </button>
        </div>
      </section>

      {/* 4. Latest Daily Report Snapshot */}
      <section className="sc-card" data-testid="dashboard-daily-report-card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <HeartPulse size={19} color="#0f766e" />
            <h3 style={{ fontSize: '15px', fontWeight: 700 }}>Tóm tắt Sinh hoạt gần nhất</h3>
          </div>
          <button
            type="button"
            onClick={() => navigateTab('DAILY_REPORT')}
            style={{
              background: 'none',
              border: 'none',
              color: 'var(--sc-primary)',
              fontSize: '12px',
              fontWeight: 700,
              cursor: 'pointer',
              display: 'inline-flex',
              alignItems: 'center',
            }}
          >
            <span>Chi tiết</span>
            <ChevronRight size={15} />
          </button>
        </div>

        {latestDailyReport ? (
          <div
            style={{
              marginTop: '10px',
              display: 'flex',
              flexDirection: 'column',
              gap: '8px',
              fontSize: '13px',
            }}
          >
            <div style={{ fontSize: '12px', color: 'var(--sc-text-secondary)' }}>
              Ngày ghi nhận: <strong>{formatVietnameseDate(latestDailyReport.reportDate)}</strong> •{' '}
              {latestDailyReport.recordedBy}
            </div>
            <div style={{ display: 'flex', alignItems: 'flex-start', gap: '6px' }}>
              <Utensils size={15} color="#0f766e" style={{ marginTop: '2px', flexShrink: 0 }} />
              <span>
                <strong>Ăn uống:</strong> {latestDailyReport.mealComment}
              </span>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
              <Moon size={15} color="#0f766e" />
              <span>
                <strong>Giấc ngủ trưa:</strong> {latestDailyReport.sleepDurationMins ?? 0} phút
              </span>
            </div>
            <div style={{ display: 'flex', alignItems: 'flex-start', gap: '6px' }}>
              <Activity size={15} color="#0f766e" style={{ marginTop: '2px', flexShrink: 0 }} />
              <span>
                <strong>Hoạt động:</strong> {latestDailyReport.activitySummary}
              </span>
            </div>
            {latestDailyReport.teacherNote && (
              <div
                style={{
                  background: 'var(--sc-primary-tint)',
                  padding: '8px 10px',
                  borderRadius: '10px',
                  fontSize: '12px',
                }}
              >
                <Smile size={14} style={{ verticalAlign: 'middle', marginRight: '5px' }} />
                <strong>Lời nhắn cô giáo:</strong> {latestDailyReport.teacherNote}
              </div>
            )}
          </div>
        ) : (
          <p style={{ marginTop: '8px', fontSize: '13px', color: 'var(--sc-text-secondary)' }}>
            Chưa có nhật ký sinh hoạt.
          </p>
        )}
      </section>

      {/* 5. Recent Announcements */}
      <section className="sc-card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Bell size={18} color="#0f766e" />
            <h3 style={{ fontSize: '15px', fontWeight: 700 }}>Thông báo mới từ Nhà trường</h3>
          </div>
          <button
            type="button"
            onClick={() => navigateTab('ANNOUNCEMENTS')}
            style={{
              background: 'none',
              border: 'none',
              color: 'var(--sc-primary)',
              fontSize: '12px',
              fontWeight: 700,
              cursor: 'pointer',
            }}
          >
            Xem tất cả
          </button>
        </div>

        <div style={{ marginTop: '10px', display: 'flex', flexDirection: 'column', gap: '10px' }}>
          {recentAnnouncements.map((ann) => (
            <div
              key={ann.id}
              style={{
                padding: '10px 12px',
                borderRadius: '12px',
                background: 'var(--sc-surface-muted)',
              }}
            >
              <div style={{ display: 'flex', gap: '6px', marginBottom: '4px' }}>
                {ann.isPinned && <span className="sc-badge sc-badge-warning">Ghim quan trọng</span>}
                <span className="sc-badge sc-badge-neutral">{ann.category}</span>
              </div>
              <div style={{ fontSize: '13px', fontWeight: 700 }}>{ann.title}</div>
              <p
                style={{
                  fontSize: '12px',
                  color: 'var(--sc-text-secondary)',
                  marginTop: '3px',
                }}
              >
                {ann.summary}
              </p>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}

