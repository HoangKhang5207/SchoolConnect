import { useCallback, useEffect, useState } from 'react';
import { CheckCircle2, ClipboardCheck, Phone, User, XCircle } from 'lucide-react';
import { EmptyStateView, ErrorStateView, LoadingStateView } from '../components/AsyncStateViews';
import {
  fetchTeacherLeaveRequests,
  reviewTeacherLeaveRequest,
} from '../services/apiClient';
import { useAppContext } from '../store/AppContext';
import type { LeaveRequestDto } from '../types/api';
import {
  formatDateTimeShort,
  formatVietnameseDate,
  translateLeaveStatus,
  translateSessionType,
} from '../utils/formatters';

const STATUS_TABS = [
  { value: 'ALL', label: 'Tất cả đơn' },
  { value: 'PENDING', label: 'Chờ duyệt' },
  { value: 'APPROVED', label: 'Đã duyệt' },
  { value: 'REJECTED', label: 'Từ chối' },
];

export function TeacherReviewPage() {
  const { token } = useAppContext();
  const [statusFilter, setStatusFilter] = useState<string>('ALL');
  const [requests, setRequests] = useState<LeaveRequestDto[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const [activeReviewModal, setActiveReviewModal] = useState<{
    item: LeaveRequestDto;
    decision: 'APPROVED' | 'REJECTED';
  } | null>(null);
  const [teacherNote, setTeacherNote] = useState<string>('');
  const [modalError, setModalError] = useState<string | null>(null);
  const [submittingReview, setSubmittingReview] = useState<boolean>(false);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const loadTeacherQueue = useCallback(async () => {
    if (!token) {
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const res = await fetchTeacherLeaveRequests(token, statusFilter);
      setRequests(res.data);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Không thể tải danh sách đơn xin nghỉ');
    } finally {
      setLoading(false);
    }
  }, [token, statusFilter]);

  useEffect(() => {
    void loadTeacherQueue();
  }, [loadTeacherQueue]);

  const openModal = (item: LeaveRequestDto, decision: 'APPROVED' | 'REJECTED') => {
    setActiveReviewModal({ item, decision });
    setTeacherNote(
      decision === 'APPROVED'
        ? 'Cô Thảo đã ghi nhận đơn và báo bộ phận bán trú. Chúc bé nhiều sức khỏe!'
        : ''
    );
    setModalError(null);
  };

  const handleConfirmReview = async () => {
    if (!token || !activeReviewModal || submittingReview) {
      return;
    }
    const { item, decision } = activeReviewModal;
    const trimmed = teacherNote.trim();

    if (decision === 'REJECTED' && trimmed.length < 5) {
      setModalError('Vui lòng nhập lý do từ chối tối thiểu 5 ký tự (RULE-008).');
      return;
    }

    setSubmittingReview(true);
    setModalError(null);
    try {
      await reviewTeacherLeaveRequest(token, item.id, {
        status: decision,
        teacherNote: trimmed,
      });
      setActiveReviewModal(null);
      setToastMessage(
        decision === 'APPROVED'
          ? `Đã phê duyệt đơn #${item.requestCode} của bé ${item.studentName}`
          : `Đã từ chối đơn #${item.requestCode} của bé ${item.studentName}`
      );
      await loadTeacherQueue();
    } catch (err: unknown) {
      setModalError(err instanceof Error ? err.message : 'Không thể xử lý đơn xin nghỉ');
    } finally {
      setSubmittingReview(false);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
      <section className="sc-card" data-testid="teacher-review-header">
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px' }}>
          <ClipboardCheck size={20} color="#0f766e" />
          <h2 style={{ fontSize: '16px', fontWeight: 800 }}>
            Cổng Giáo Viên: Duyệt Đơn Xin Nghỉ Phép
          </h2>
        </div>
        <p style={{ fontSize: '12px', color: 'var(--sc-text-secondary)', marginBottom: '10px' }}>
          Kiểm tra và phản hồi trực tiếp đơn xin nghỉ phép của phụ huynh theo thời gian thực.
        </p>

        <div className="sc-filter-pills" role="group" aria-label="Lọc trạng thái đơn">
          {STATUS_TABS.map((tab) => (
            <button
              key={tab.value}
              type="button"
              data-testid={`teacher-filter-${tab.value}`}
              className={`sc-filter-pill ${statusFilter === tab.value ? 'active' : ''}`}
              onClick={() => setStatusFilter(tab.value)}
            >
              {tab.label}
            </button>
          ))}
        </div>
      </section>

      {toastMessage && (
        <div
          data-testid="teacher-toast-banner"
          style={{
            background: 'var(--sc-success-soft)',
            color: 'var(--sc-success)',
            padding: '10px 14px',
            borderRadius: '12px',
            fontSize: '13px',
            fontWeight: 700,
          }}
        >
          {toastMessage}
        </div>
      )}

      {loading ? (
        <LoadingStateView message="Đang tải danh sách đơn xin nghỉ của học sinh..." />
      ) : error ? (
        <ErrorStateView message={error} onRetry={() => void loadTeacherQueue()} />
      ) : requests.length === 0 ? (
        <EmptyStateView
          title="Không có đơn xin nghỉ nào"
          description="Hiện không có đơn xin nghỉ nào khớp với bộ lọc trạng thái đã chọn."
        />
      ) : (
        requests.map((req) => {
          const st = translateLeaveStatus(req.status);
          return (
            <article key={req.id} className="sc-card" data-testid={`teacher-leave-card-${req.id}`}>
              <div
                style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginBottom: '8px',
                }}
              >
                <span style={{ fontSize: '12px', fontWeight: 800, color: 'var(--sc-primary)' }}>
                  #{req.requestCode} • {req.className}
                </span>
                <span className={`sc-badge sc-badge-${st.tone}`}>{st.label}</span>
              </div>

              <h3 style={{ fontSize: '15px', fontWeight: 800 }}>Bé: {req.studentName}</h3>

              <div
                style={{
                  fontSize: '12px',
                  color: 'var(--sc-text-secondary)',
                  marginTop: '4px',
                  display: 'flex',
                  gap: '12px',
                }}
              >
                <span>
                  <User size={12} style={{ verticalAlign: 'middle', marginRight: '3px' }} />
                  PH: <strong>{req.parentName}</strong>
                </span>
                <span>
                  <Phone size={12} style={{ verticalAlign: 'middle', marginRight: '3px' }} />
                  {req.parentPhone}
                </span>
              </div>

              <div
                style={{
                  marginTop: '8px',
                  padding: '8px 10px',
                  borderRadius: '10px',
                  background: 'var(--sc-surface-muted)',
                  fontSize: '13px',
                }}
              >
                <div>
                  <strong>Thời gian nghỉ:</strong> {formatVietnameseDate(req.startDate)} →{' '}
                  {formatVietnameseDate(req.endDate)} ({translateSessionType(req.sessionType)})
                </div>
                <div style={{ marginTop: '4px' }}>
                  <strong>Lý do phụ huynh gửi:</strong> {req.reason}
                </div>
              </div>

              {req.teacherNote && (
                <div
                  style={{
                    marginTop: '8px',
                    fontSize: '12px',
                    color: 'var(--sc-primary-dark)',
                    background: 'var(--sc-primary-tint)',
                    padding: '8px 10px',
                    borderRadius: '10px',
                  }}
                >
                  <strong>Ghi chú duyệt ({req.reviewedBy}):</strong> {req.teacherNote}
                </div>
              )}

              <div
                style={{
                  marginTop: '10px',
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                }}
              >
                <span style={{ fontSize: '11px', color: 'var(--sc-text-secondary)' }}>
                  Gửi lúc: {formatDateTimeShort(req.createdAt)}
                </span>

                {req.status === 'PENDING' && (
                  <div style={{ display: 'flex', gap: '8px' }}>
                    <button
                      type="button"
                      className="sc-btn-danger-outline"
                      data-testid={`teacher-reject-btn-${req.id}`}
                      onClick={() => openModal(req, 'REJECTED')}
                    >
                      <XCircle size={14} style={{ verticalAlign: 'middle', marginRight: '4px' }} />
                      Từ chối
                    </button>
                    <button
                      type="button"
                      className="sc-btn-primary"
                      style={{ minHeight: '38px', padding: '6px 12px', fontSize: '12px' }}
                      data-testid={`teacher-approve-btn-${req.id}`}
                      onClick={() => openModal(req, 'APPROVED')}
                    >
                      <CheckCircle2 size={14} />
                      <span>Duyệt đơn</span>
                    </button>
                  </div>
                )}
              </div>
            </article>
          );
        })
      )}

      {/* Review Decision Modal */}
      {activeReviewModal && (
        <div className="sc-modal-backdrop" onClick={() => setActiveReviewModal(null)}>
          <div
            className="sc-modal-sheet"
            data-testid="teacher-review-modal"
            onClick={(e) => e.stopPropagation()}
          >
            <h3 style={{ fontSize: '16px', fontWeight: 800 }}>
              {activeReviewModal.decision === 'APPROVED'
                ? 'Xác nhận Phê duyệt Đơn xin nghỉ'
                : 'Từ chối Đơn xin nghỉ phép'}
            </h3>
            <p style={{ fontSize: '13px', color: 'var(--sc-text-secondary)', marginTop: '4px' }}>
              Học sinh: <strong>{activeReviewModal.item.studentName}</strong> (#
              {activeReviewModal.item.requestCode})
            </p>

            {modalError && (
              <div
                data-testid="teacher-modal-error"
                style={{
                  marginTop: '10px',
                  padding: '8px 10px',
                  borderRadius: '10px',
                  background: 'var(--sc-danger-soft)',
                  color: 'var(--sc-danger)',
                  fontSize: '12px',
                  fontWeight: 600,
                }}
              >
                {modalError}
              </div>
            )}

            <div style={{ marginTop: '12px' }}>
              <label
                htmlFor="teacher-review-note"
                style={{ display: 'block', fontSize: '12px', fontWeight: 700, marginBottom: '4px' }}
              >
                {activeReviewModal.decision === 'REJECTED'
                  ? 'Lý do từ chối (Bắt buộc tối thiểu 5 ký tự) *'
                  : 'Lời nhắn gửi Phụ huynh (Tùy chọn)'}
              </label>
              <textarea
                id="teacher-review-note"
                className="sc-textarea"
                rows={3}
                value={teacherNote}
                onChange={(e) => setTeacherNote(e.target.value)}
                placeholder="Nhập phản hồi của giáo viên chủ nhiệm..."
              />
            </div>

            <div style={{ display: 'flex', gap: '10px', marginTop: '14px' }}>
              <button
                type="button"
                className="sc-btn-outline"
                style={{ flex: 1 }}
                onClick={() => setActiveReviewModal(null)}
              >
                Đóng
              </button>
              <button
                type="button"
                className="sc-btn-primary"
                style={{ flex: 1 }}
                data-testid="confirm-review-submit-btn"
                disabled={submittingReview}
                onClick={() => void handleConfirmReview()}
              >
                {submittingReview ? 'Đang lưu...' : 'Xác nhận'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

