import { useCallback, useEffect, useState } from 'react';
import { CalendarCheck, Clock, UserCheck } from 'lucide-react';
import { EmptyStateView, ErrorStateView, LoadingStateView } from '../components/AsyncStateViews';
import { fetchStudentAttendance } from '../services/apiClient';
import { useAppContext } from '../store/AppContext';
import type { AttendanceSummaryResponseDto } from '../types/api';
import { formatVietnameseDate, translateAttendanceStatus } from '../utils/formatters';

export function AttendancePage() {
  const { token, selectedStudentId, selectedStudent } = useAppContext();
  const [data, setData] = useState<AttendanceSummaryResponseDto | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const loadAttendance = useCallback(async () => {
    if (!token) {
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const res = await fetchStudentAttendance(token, selectedStudentId);
      setData(res.data);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Không thể tải dữ liệu điểm danh');
    } finally {
      setLoading(false);
    }
  }, [token, selectedStudentId]);

  useEffect(() => {
    void loadAttendance();
  }, [loadAttendance]);

  if (loading) {
    return <LoadingStateView message="Đang tải thống kê chuyên cần & lịch sử điểm danh..." />;
  }

  if (error) {
    return <ErrorStateView message={error} onRetry={() => void loadAttendance()} />;
  }

  if (!data || data.records.length === 0) {
    return <EmptyStateView title="Chưa có lịch sử điểm danh" />;
  }

  const { summary, records } = data;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
      <section className="sc-card" data-testid="attendance-summary-card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <CalendarCheck size={19} color="#0f766e" />
            <h2 style={{ fontSize: '16px', fontWeight: 800 }}>
              Chuyên Cần ({selectedStudent?.fullName})
            </h2>
          </div>
          <span className="sc-badge sc-badge-success">Tỷ lệ: {summary.attendanceRate}%</span>
        </div>

        <div
          style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(4, 1fr)',
            gap: '8px',
            marginTop: '12px',
            textAlign: 'center',
          }}
        >
          <div
            style={{
              background: 'var(--sc-success-soft)',
              padding: '10px 6px',
              borderRadius: '12px',
            }}
          >
            <div style={{ fontSize: '18px', fontWeight: 800, color: 'var(--sc-success)' }}>
              {summary.presentCount}
            </div>
            <div style={{ fontSize: '11px', fontWeight: 600 }}>Có mặt</div>
          </div>
          <div
            style={{
              background: 'var(--sc-surface-muted)',
              padding: '10px 6px',
              borderRadius: '12px',
            }}
          >
            <div style={{ fontSize: '18px', fontWeight: 800 }}>
              {summary.excusedAbsentCount}
            </div>
            <div style={{ fontSize: '11px', fontWeight: 600 }}>Nghỉ phép</div>
          </div>
          <div
            style={{
              background: 'var(--sc-warning-soft)',
              padding: '10px 6px',
              borderRadius: '12px',
            }}
          >
            <div style={{ fontSize: '18px', fontWeight: 800, color: 'var(--sc-warning)' }}>
              {summary.lateCount}
            </div>
            <div style={{ fontSize: '11px', fontWeight: 600 }}>Đi muộn</div>
          </div>
          <div
            style={{
              background: 'var(--sc-danger-soft)',
              padding: '10px 6px',
              borderRadius: '12px',
            }}
          >
            <div style={{ fontSize: '18px', fontWeight: 800, color: 'var(--sc-danger)' }}>
              {summary.unexcusedAbsentCount}
            </div>
            <div style={{ fontSize: '11px', fontWeight: 600 }}>Không phép</div>
          </div>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        {records.map((rec) => {
          const statusMeta = translateAttendanceStatus(rec.status);
          return (
            <div key={rec.id} className="sc-card" data-testid="attendance-record-item">
              <div
                style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginBottom: '8px',
                }}
              >
                <strong style={{ fontSize: '14px' }}>
                  Ngày {formatVietnameseDate(rec.attendanceDate)}
                </strong>
                <span className={`sc-badge sc-badge-${statusMeta.tone}`}>
                  {statusMeta.label}
                </span>
              </div>

              <div
                style={{
                  display: 'flex',
                  flexDirection: 'column',
                  gap: '4px',
                  fontSize: '12px',
                  color: 'var(--sc-text-secondary)',
                }}
              >
                <div style={{ display: 'flex', gap: '16px' }}>
                  <span>
                    <Clock size={13} style={{ verticalAlign: 'middle', marginRight: '4px' }} />
                    Giờ vào: <strong>{rec.checkInTime || '—'}</strong>
                  </span>
                  <span>
                    Giờ ra: <strong>{rec.checkOutTime || '—'}</strong>
                  </span>
                </div>
                {rec.pickupPerson && (
                  <div>
                    <UserCheck
                      size={13}
                      style={{ verticalAlign: 'middle', marginRight: '4px' }}
                    />
                    Người đón: <strong>{rec.pickupPerson}</strong>
                  </div>
                )}
                {rec.note && <div>Ghi chú: {rec.note}</div>}
              </div>
            </div>
          );
        })}
      </section>
    </div>
  );
}

