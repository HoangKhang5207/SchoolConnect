import { useCallback, useEffect, useState } from 'react';
import { Bell, Pin, X } from 'lucide-react';
import { EmptyStateView, ErrorStateView, LoadingStateView } from '../components/AsyncStateViews';
import { fetchAnnouncements } from '../services/apiClient';
import { useAppContext } from '../store/AppContext';
import type { AnnouncementDto } from '../types/api';
import { formatDateTimeShort } from '../utils/formatters';

const CATEGORIES = [
  { value: 'ALL', label: 'Tất cả' },
  { value: 'SCHOOL', label: 'Nhà trường' },
  { value: 'CLASS', label: 'Lớp học' },
  { value: 'HEALTH', label: 'Y tế & Dinh dưỡng' },
  { value: 'EVENT', label: 'Sự kiện' },
];

export function AnnouncementsPage() {
  const { token } = useAppContext();
  const [category, setCategory] = useState<string>('ALL');
  const [items, setItems] = useState<AnnouncementDto[]>([]);
  const [selectedItem, setSelectedItem] = useState<AnnouncementDto | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const loadAnnouncements = useCallback(async () => {
    if (!token) {
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const res = await fetchAnnouncements(token, category);
      setItems(res.data);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Không thể tải danh sách thông báo');
    } finally {
      setLoading(false);
    }
  }, [token, category]);

  useEffect(() => {
    void loadAnnouncements();
  }, [loadAnnouncements]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
      <section className="sc-card">
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
          <Bell size={19} color="#0f766e" />
          <h2 style={{ fontSize: '16px', fontWeight: 800 }}>Bản Tin & Thông Báo Nhà Trường</h2>
        </div>

        <div className="sc-filter-pills" role="group" aria-label="Lọc danh mục thông báo">
          {CATEGORIES.map((c) => (
            <button
              key={c.value}
              type="button"
              className={`sc-filter-pill ${category === c.value ? 'active' : ''}`}
              onClick={() => setCategory(c.value)}
            >
              {c.label}
            </button>
          ))}
        </div>
      </section>

      {loading ? (
        <LoadingStateView message="Đang tải bản tin nhà trường..." />
      ) : error ? (
        <ErrorStateView message={error} onRetry={() => void loadAnnouncements()} />
      ) : items.length === 0 ? (
        <EmptyStateView
          title="Chưa có thông báo trong danh mục này"
          description="Hiện chưa có thông báo mới nào thuộc phân loại bạn đang chọn."
        />
      ) : (
        items.map((item) => (
          <article
            key={item.id}
            className="sc-card"
            data-testid="announcement-card"
            style={{ cursor: 'pointer' }}
            onClick={() => setSelectedItem(item)}
          >
            <div style={{ display: 'flex', gap: '6px', marginBottom: '6px', flexWrap: 'wrap' }}>
              {item.isPinned && (
                <span className="sc-badge sc-badge-warning">
                  <Pin size={11} />
                  Ghim quan trọng
                </span>
              )}
              <span className="sc-badge sc-badge-success">{item.category}</span>
              {item.targetClass && (
                <span className="sc-badge sc-badge-neutral">{item.targetClass}</span>
              )}
            </div>

            <h3 style={{ fontSize: '15px', fontWeight: 800, lineHeight: 1.4 }}>{item.title}</h3>
            <p
              style={{
                fontSize: '13px',
                color: 'var(--sc-text-secondary)',
                marginTop: '6px',
                lineHeight: 1.5,
              }}
            >
              {item.summary}
            </p>
            <div
              style={{
                marginTop: '10px',
                fontSize: '11px',
                color: 'var(--sc-text-secondary)',
                display: 'flex',
                justifyContent: 'space-between',
              }}
            >
              <span>Người đăng: {item.authorName}</span>
              <span>{formatDateTimeShort(item.publishedAt)}</span>
            </div>
          </article>
        ))
      )}

      {selectedItem && (
        <div className="sc-modal-backdrop" onClick={() => setSelectedItem(null)}>
          <div
            className="sc-modal-sheet"
            data-testid="announcement-detail-modal"
            onClick={(e) => e.stopPropagation()}
          >
            <div
              style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'flex-start',
                gap: '12px',
              }}
            >
              <h3 style={{ fontSize: '16px', fontWeight: 800 }}>{selectedItem.title}</h3>
              <button
                type="button"
                onClick={() => setSelectedItem(null)}
                style={{
                  border: 'none',
                  background: 'var(--sc-surface-muted)',
                  borderRadius: '999px',
                  padding: '6px',
                  cursor: 'pointer',
                }}
              >
                <X size={18} />
              </button>
            </div>

            <div
              style={{
                fontSize: '12px',
                color: 'var(--sc-text-secondary)',
                marginTop: '6px',
                paddingBottom: '10px',
                borderBottom: '1px solid var(--sc-border)',
              }}
            >
              {selectedItem.authorName} • {formatDateTimeShort(selectedItem.publishedAt)}
            </div>

            <div
              style={{
                marginTop: '12px',
                fontSize: '14px',
                lineHeight: 1.65,
                whiteSpace: 'pre-line',
              }}
            >
              {selectedItem.content}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

