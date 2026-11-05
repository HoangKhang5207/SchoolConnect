import { AlertTriangle, Inbox, Loader2, RefreshCw } from 'lucide-react';

export function LoadingStateView({ message = 'Đang tải dữ liệu từ hệ thống...' }: { message?: string }) {
  return (
    <div
      className="sc-card"
      data-testid="loading-state"
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        padding: '32px 18px',
        textAlign: 'center',
        gap: '10px',
      }}
    >
      <Loader2 size={28} color="#0f766e" />
      <p style={{ fontSize: '13px', fontWeight: 600, color: 'var(--sc-text-secondary)' }}>
        {message}
      </p>
    </div>
  );
}

export function ErrorStateView({
  message,
  onRetry,
}: {
  message: string;
  onRetry?: () => void;
}) {
  return (
    <div
      className="sc-card"
      data-testid="error-state"
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        textAlign: 'center',
        padding: '26px 18px',
        gap: '10px',
        borderColor: 'rgba(185, 28, 28, 0.25)',
      }}
    >
      <AlertTriangle size={28} color="#b91c1c" />
      <h3 style={{ fontSize: '15px', fontWeight: 700, color: '#b91c1c' }}>
        Không thể tải dữ liệu
      </h3>
      <p style={{ fontSize: '13px', color: 'var(--sc-text-secondary)', lineHeight: 1.5 }}>
        {message}
      </p>
      {onRetry && (
        <button type="button" className="sc-btn-outline" onClick={onRetry}>
          <RefreshCw size={15} />
          <span>Thử lại</span>
        </button>
      )}
    </div>
  );
}

export function EmptyStateView({
  title = 'Chưa có dữ liệu cho ngày được chọn',
  description = 'Nhà trường chưa cập nhật bản ghi hoặc đây là ngày nghỉ của bé.',
}: {
  title?: string;
  description?: string;
}) {
  return (
    <div
      className="sc-card"
      data-testid="empty-state"
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        textAlign: 'center',
        padding: '30px 18px',
        gap: '8px',
      }}
    >
      <Inbox size={30} color="#0f766e" />
      <h3 style={{ fontSize: '15px', fontWeight: 700 }}>{title}</h3>
      <p style={{ fontSize: '13px', color: 'var(--sc-text-secondary)', lineHeight: 1.5 }}>
        {description}
      </p>
    </div>
  );
}

