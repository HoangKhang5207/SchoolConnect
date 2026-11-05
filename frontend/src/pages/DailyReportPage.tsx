import { useCallback, useEffect, useState } from 'react';
import { Activity, BookOpen, Calendar, Moon, Smile, Utensils } from 'lucide-react';
import { EmptyStateView, ErrorStateView, LoadingStateView } from '../components/AsyncStateViews';
import { fetchDailyReports } from '../services/apiClient';
import { useAppContext } from '../store/AppContext';
import type { DailyReportDto } from '../types/api';
import { formatVietnameseDate } from '../utils/formatters';

const DATE_OPTIONS = [
  { value: '', label: 'Tất cả ngày' },
  { value: '2026-11-04', label: 'Thứ Tư (04/11)' },
  { value: '2026-11-03', label: 'Thứ Ba (03/11)' },
  { value: '2026-11-02', label: 'Thứ Hai (02/11)' },
  { value: '2026-11-01', label: 'Chủ Nhật (01/11)' },
];

export function DailyReportPage() {
  const { token, selectedStudentId, selectedStudent } = useAppContext();
  const [selectedDate, setSelectedDate] = useState<string>('2026-11-04');
  const [reports, setReports] = useState<DailyReportDto[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const loadReports = useCallback(async () => {
    if (!token) {
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const res = await fetchDailyReports(
        token,
        selectedStudentId,
        selectedDate || undefined
      );
      setReports(res.data);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Không thể tải nhật ký sinh hoạt');
    } finally {
      setLoading(false);
    }
  }, [token, selectedStudentId, selectedDate]);

  useEffect(() => {
    void loadReports();
  }, [loadReports]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
      <section className="sc-card">
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
          <BookOpen size={19} color="#0f766e" />
          <h2 style={{ fontSize: '16px', fontWeight: 800 }}>
            Sổ Bé Ngoan & Nhật Ký Sinh Hoạt ({selectedStudent?.fullName})
          </h2>
        </div>

        <div className="sc-filter-pills" role="group" aria-label="Chọn ngày xem nhật ký">
          {DATE_OPTIONS.map((opt) => (
            <button
              key={opt.value || 'ALL'}
              type="button"
              data-testid={`report-date-pill-${opt.value || 'ALL'}`}
              className={`sc-filter-pill ${selectedDate === opt.value ? 'active' : ''}`}
              onClick={() => setSelectedDate(opt.value)}
            >
              <Calendar size={12} style={{ marginRight: '4px', verticalAlign: 'middle' }} />
              {opt.label}
            </button>
          ))}
        </div>
      </section>

      {loading ? (
        <LoadingStateView message="Đang tải nhật ký chăm sóc của bé..." />
      ) : error ? (
        <ErrorStateView message={error} onRetry={() => void loadReports()} />
      ) : reports.length === 0 ? (
        <EmptyStateView
          title="Chưa có dữ liệu cho ngày được chọn"
          description="Ngày nghỉ cuối tuần hoặc giáo viên chưa cập nhật sổ liên lạc cho ngày này."
        />
      ) : (
        reports.map((report) => (
          <article key={report.id} className="sc-card" data-testid="daily-report-item">
            <div
              style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                paddingBottom: '10px',
                borderBottom: '1px solid var(--sc-border)',
                marginBottom: '12px',
              }}
            >
              <div>
                <div style={{ fontSize: '14px', fontWeight: 800, color: 'var(--sc-primary)' }}>
                  Ngày {formatVietnameseDate(report.reportDate)}
                </div>
                <div style={{ fontSize: '12px', color: 'var(--sc-text-secondary)' }}>
                  Giáo viên phụ trách: <strong>{report.recordedBy}</strong>
                </div>
              </div>
              <span className="sc-badge sc-badge-success">Tâm trạng: {report.moodStatus}</span>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', fontSize: '13px' }}>
              {/* 1. Meal Evaluation */}
              <div
                style={{
                  background: 'var(--sc-surface-muted)',
                  padding: '10px 12px',
                  borderRadius: '12px',
                }}
              >
                <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontWeight: 700 }}>
                  <Utensils size={15} color="#0f766e" />
                  <span>1. Bữa ăn & Dinh dưỡng ({report.mealStatus})</span>
                </div>
                <p style={{ marginTop: '4px', color: 'var(--sc-text-secondary)', lineHeight: 1.5 }}>
                  {report.mealComment}
                </p>
              </div>

              {/* 2. Sleep Summary */}
              <div
                style={{
                  background: 'var(--sc-surface-muted)',
                  padding: '10px 12px',
                  borderRadius: '12px',
                }}
              >
                <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontWeight: 700 }}>
                  <Moon size={15} color="#0f766e" />
                  <span>
                    2. Giấc ngủ trưa ({report.sleepStatus} • {report.sleepDurationMins ?? 0} phút)
                  </span>
                </div>
              </div>

              {/* 3. Learning & Play Activities */}
              <div
                style={{
                  background: 'var(--sc-surface-muted)',
                  padding: '10px 12px',
                  borderRadius: '12px',
                }}
              >
                <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontWeight: 700 }}>
                  <Activity size={15} color="#0f766e" />
                  <span>3. Hoạt động Học tập & Vui chơi</span>
                </div>
                <p style={{ marginTop: '4px', color: 'var(--sc-text-secondary)', lineHeight: 1.5 }}>
                  {report.activitySummary}
                </p>
              </div>

              {/* 4. Teacher Note */}
              {report.teacherNote && (
                <div
                  style={{
                    background: 'var(--sc-primary-tint)',
                    border: '1px solid var(--sc-primary-soft)',
                    padding: '10px 12px',
                    borderRadius: '12px',
                  }}
                >
                  <div
                    style={{
                      display: 'flex',
                      alignItems: 'center',
                      gap: '6px',
                      fontWeight: 700,
                      color: 'var(--sc-primary-dark)',
                    }}
                  >
                    <Smile size={15} />
                    <span>4. Nhận xét của Cô giáo</span>
                  </div>
                  <p style={{ marginTop: '4px', lineHeight: 1.5 }}>{report.teacherNote}</p>
                </div>
              )}
            </div>
          </article>
        ))
      )}
    </div>
  );
}

