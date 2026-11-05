import React, { useCallback, useEffect, useState } from 'react';
import { CheckCircle2, FilePlus2, History, Send, XCircle } from 'lucide-react';
import { EmptyStateView, ErrorStateView, LoadingStateView } from '../components/AsyncStateViews';
import {
  cancelParentLeaveRequest,
  fetchParentLeaveRequests,
  submitParentLeaveRequest,
} from '../services/apiClient';
import { useAppContext } from '../store/AppContext';
import type { LeaveRequestDto, LeaveSessionType } from '../types/api';
import {
  formatDateTimeShort,
  formatVietnameseDate,
  getTomorrowIsoDate,
  translateLeaveStatus,
  translateSessionType,
} from '../utils/formatters';

export function AbsenceRequestPage() {
  const { token, selectedStudentId, selectedStudent } = useAppContext();
  const defaultDate = getTomorrowIsoDate();

  const [startDate, setStartDate] = useState<string>(defaultDate);
  const [endDate, setEndDate] = useState<string>(defaultDate);
  const [sessionType, setSessionType] = useState<LeaveSessionType>('FULL_DAY');
  const [reason, setReason] = useState<string>('');
  const [submitting, setSubmitting] = useState<boolean>(false);
  const [cancellingId, setCancellingId] = useState<number | null>(null);
  const [feedbackBanner, setFeedbackBanner] = useState<{
    type: 'success' | 'error';
    text: string;
  } | null>(null);

  const [history, setHistory] = useState<LeaveRequestDto[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const loadHistory = useCallback(async () => {
    if (!token) {
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const res = await fetchParentLeaveRequests(token, selectedStudentId);
      setHistory(res.data);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Không thể tải lịch sử đơn xin nghỉ');
    } finally {
      setLoading(false);
    }
  }, [token, selectedStudentId]);

  useEffect(() => {
    void loadHistory();
  }, [loadHistory]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!token || submitting) {
      return;
    }
    setFeedbackBanner(null);

    const trimmedReason = reason.trim();
    if (trimmedReason.length < 5) {
      setFeedbackBanner({
        type: 'error',
        text: 'Vui lòng nhập lý do xin nghỉ tối thiểu 5 ký tự.',
      });
      return;
    }
    if (endDate < startDate) {
      setFeedbackBanner({
        type: 'error',
        text: 'Ngày kết thúc nghỉ phải lớn hơn hoặc bằng ngày bắt đầu.',
      });
      return;
    }

    setSubmitting(true);
    try {
      const res = await submitParentLeaveRequest(token, selectedStudentId, {
        startDate,
        endDate,
        sessionType,
        reason: trimmedReason,
      });
      setReason('');
      setFeedbackBanner({
        type: 'success',
        text: `Đã gửi đơn thành công! Mã đơn: ${res.data.requestCode}`,
      });
      await loadHistory();
    } catch (err: unknown) {
      setFeedbackBanner({
        type: 'error',
        text: err instanceof Error ? err.message : 'Gửi đơn thất bại, vui lòng kiểm tra lại.',
      });
    } finally {
      setSubmitting(false);
    }
  };

  const handleCancelRequest = async (leaveId: number) => {
    if (!token || cancellingId !== null) {
      return;
    }
    setCancellingId(leaveId);
    setFeedbackBanner(null);
    try {
      await cancelParentLeaveRequest(token, leaveId);
      setFeedbackBanner({
        type: 'success',
        text: 'Đã hủy đơn xin nghỉ phép thành công.',
      });
      await loadHistory();
    } catch (err: unknown) {
      setFeedbackBanner({
        type: 'error',
        text: err instanceof Error ? err.message : 'Không thể hủy đơn xin nghỉ.',
      });
    } finally {
      setCancellingId(null);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
      {/* Leave Submission Form */}
      <section className="sc-card" data-testid="leave-request-form-card">
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '10px' }}>
          <FilePlus2 size={19} color="#0f766e" />
          <h2 style={{ fontSize: '16px', fontWeight: 800 }}>
            Tạo Đơn Xin Nghỉ Phép ({selectedStudent?.fullName})
          </h2>
        </div>

        {feedbackBanner && (
          <div
            data-testid="leave-feedback-banner"
            style={{
              padding: '10px 12px',
              borderRadius: '12px',
              marginBottom: '12px',
              fontSize: '13px',
              fontWeight: 600,
              background:
                feedbackBanner.type === 'success'
                  ? 'var(--sc-success-soft)'
                  : 'var(--sc-danger-soft)',
              color:
                feedbackBanner.type === 'success' ? 'var(--sc-success)' : 'var(--sc-danger)',
              display: 'flex',
              alignItems: 'center',
              gap: '6px',
            }}
          >
            {feedbackBanner.type === 'success' ? (
              <CheckCircle2 size={16} />
            ) : (
              <XCircle size={16} />
            )}
            <span>{feedbackBanner.text}</span>
          </div>
        )}

        <form
          onSubmit={(e) => void handleSubmit(e)}
          style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}
        >
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px' }}>
            <div>
              <label
                htmlFor="leave-start-date"
                style={{ display: 'block', fontSize: '12px', fontWeight: 700, marginBottom: '4px' }}
              >
                Từ ngày *
              </label>
              <input
                id="leave-start-date"
                type="date"
                className="sc-input"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
                required
              />
            </div>

            <div>
              <label
                htmlFor="leave-end-date"
                style={{ display: 'block', fontSize: '12px', fontWeight: 700, marginBottom: '4px' }}
              >
                Đến ngày *
              </label>
              <input
                id="leave-end-date"
                type="date"
                className="sc-input"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
                required
              />
            </div>
          </div>

          <div>
            <label
              htmlFor="leave-session-type"
              style={{ display: 'block', fontSize: '12px', fontWeight: 700, marginBottom: '4px' }}
            >
              Buổi xin nghỉ *
            </label>
            <select
              id="leave-session-type"
              className="sc-select"
              value={sessionType}
              onChange={(e) => setSessionType(e.target.value as LeaveSessionType)}
            >
              <option value="FULL_DAY">Nghỉ cả ngày (Sáng & Chiều)</option>
              <option value="MORNING">Chỉ nghỉ buổi sáng</option>
              <option value="AFTERNOON">Chỉ nghỉ buổi chiều</option>
            </select>
          </div>

          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
              <label htmlFor="leave-reason" style={{ fontSize: '12px', fontWeight: 700 }}>
                Lý do xin nghỉ *
              </label>
              <span style={{ fontSize: '11px', color: 'var(--sc-text-secondary)' }}>
                {reason.trim().length}/500 ký tự
              </span>
            </div>
            <textarea
              id="leave-reason"
              className="sc-textarea"
              rows={3}
              maxLength={500}
              placeholder="Nhập lý do chi tiết (tối thiểu 5 ký tự) để cô giáo chủ nhiệm và nhà bếp nắm thông tin..."
              value={reason}
              onChange={(e) => setReason(e.target.value)}
              required
            />
          </div>

          <button
            type="submit"
            className="sc-btn-primary"
            data-testid="submit-leave-btn"
            disabled={submitting}
          >
            <Send size={16} />
            <span>{submitting ? 'Đang gửi đơn...' : 'Gửi đơn xin nghỉ'}</span>
          </button>
        </form>
      </section>

      {/* Leave Request History List */}
      <section style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '6px', padding: '0 4px' }}>
          <History size={16} color="#0f766e" />
          <h3 style={{ fontSize: '15px', fontWeight: 800 }}>
            Lịch sử Đơn xin nghỉ ({history.length})
          </h3>
        </div>

        {loading ? (
          <LoadingStateView message="Đang tải danh sách đơn xin nghỉ..." />
        ) : error ? (
          <ErrorStateView message={error} onRetry={() => void loadHistory()} />
        ) : history.length === 0 ? (
          <EmptyStateView
            title="Chưa có đơn xin nghỉ nào"
            description="Các đơn xin nghỉ phép của bé sau khi gửi sẽ hiển thị tại đây."
          />
        ) : (
          history.map((item) => {
            const st = translateLeaveStatus(item.status);
            return (
              <article key={item.id} className="sc-card" data-testid={`leave-history-card-${item.id}`}>
                <div
                  style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    marginBottom: '6px',
                  }}
                >
                  <span style={{ fontSize: '12px', fontWeight: 800, color: 'var(--sc-primary)' }}>
                    #{item.requestCode}
                  </span>
                  <span className={`sc-badge sc-badge-${st.tone}`}>{st.label}</span>
                </div>

                <div style={{ fontSize: '13px', fontWeight: 700 }}>
                  {formatVietnameseDate(item.startDate)} → {formatVietnameseDate(item.endDate)} (
                  {translateSessionType(item.sessionType)})
                </div>

                <p
                  style={{
                    fontSize: '13px',
                    color: 'var(--sc-text-secondary)',
                    marginTop: '6px',
                    lineHeight: 1.5,
                  }}
                >
                  <strong>Lý do:</strong> {item.reason}
                </p>

                {item.teacherNote && (
                  <div
                    style={{
                      marginTop: '8px',
                      padding: '8px 10px',
                      borderRadius: '10px',
                      background: 'var(--sc-primary-tint)',
                      fontSize: '12px',
                    }}
                  >
                    <strong>Phản hồi của {item.reviewedBy || 'Giáo viên'}:</strong>{' '}
                    {item.teacherNote}
                    {item.reviewedAt && (
                      <div style={{ fontSize: '11px', opacity: 0.8, marginTop: '2px' }}>
                        Duyệt lúc: {formatDateTimeShort(item.reviewedAt)}
                      </div>
                    )}
                  </div>
                )}

                <div
                  style={{
                    marginTop: '10px',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    fontSize: '11px',
                    color: 'var(--sc-text-secondary)',
                  }}
                >
                  <span>Gửi lúc: {formatDateTimeShort(item.createdAt)}</span>
                  {item.status === 'PENDING' && (
                    <button
                      type="button"
                      className="sc-btn-danger-outline"
                      data-testid={`cancel-leave-btn-${item.id}`}
                      disabled={cancellingId === item.id}
                      onClick={() => void handleCancelRequest(item.id)}
                    >
                      {cancellingId === item.id ? 'Đang hủy...' : 'Hủy đơn'}
                    </button>
                  )}
                </div>
              </article>
            );
          })
        )}
      </section>
    </div>
  );
}

