import { useCallback, useEffect, useState } from 'react';
import { Flame, Sparkles, Utensils } from 'lucide-react';
import { EmptyStateView, ErrorStateView, LoadingStateView } from '../components/AsyncStateViews';
import { fetchMealMenus } from '../services/apiClient';
import { useAppContext } from '../store/AppContext';
import type { MealMenuDto } from '../types/api';
import { formatVietnameseDate } from '../utils/formatters';

const MENU_DATES = [
  { value: '2026-11-04', label: 'Thứ Tư (04/11)' },
  { value: '2026-11-05', label: 'Thứ Năm (05/11)' },
  { value: '2026-11-03', label: 'Thứ Ba (03/11)' },
  { value: '2026-11-01', label: 'Chủ Nhật (01/11)' },
];

function translateMealType(mealType: string): string {
  switch (mealType) {
    case 'BREAKFAST':
      return 'Bữa Sáng (07:45 - 08:15)';
    case 'LUNCH':
      return 'Bữa Trưa Bán Trú (11:00 - 11:45)';
    case 'AFTERNOON_SNACK':
      return 'Bữa Xế Chiều (14:30 - 15:00)';
    default:
      return mealType;
  }
}

export function MealMenuPage() {
  const { token } = useAppContext();
  const [selectedDate, setSelectedDate] = useState<string>('2026-11-04');
  const [menus, setMenus] = useState<MealMenuDto[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const loadMenus = useCallback(async () => {
    if (!token) {
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const res = await fetchMealMenus(token, selectedDate);
      setMenus(res.data);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Không thể tải thực đơn dinh dưỡng');
    } finally {
      setLoading(false);
    }
  }, [token, selectedDate]);

  useEffect(() => {
    void loadMenus();
  }, [loadMenus]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
      <section className="sc-card">
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
          <Utensils size={19} color="#0f766e" />
          <h2 style={{ fontSize: '16px', fontWeight: 800 }}>
            Thực Đơn Dinh Dưỡng Mầm Non
          </h2>
        </div>

        <div className="sc-filter-pills" role="group" aria-label="Chọn ngày xem thực đơn">
          {MENU_DATES.map((d) => (
            <button
              key={d.value}
              type="button"
              data-testid={`menu-date-pill-${d.value}`}
              className={`sc-filter-pill ${selectedDate === d.value ? 'active' : ''}`}
              onClick={() => setSelectedDate(d.value)}
            >
              {d.label}
            </button>
          ))}
        </div>
      </section>

      {loading ? (
        <LoadingStateView message="Đang tải thực đơn dinh dưỡng..." />
      ) : error ? (
        <ErrorStateView message={error} onRetry={() => void loadMenus()} />
      ) : menus.length === 0 ? (
        <EmptyStateView
          title="Chưa có dữ liệu cho ngày được chọn"
          description="Nhà bếp không phục vụ bán trú vào ngày nghỉ hoặc chưa cập nhật thực đơn."
        />
      ) : (
        menus.map((menu) => (
          <article key={menu.id} className="sc-card" data-testid={`meal-card-${menu.mealType}`}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <h3 style={{ fontSize: '15px', fontWeight: 800, color: 'var(--sc-primary)' }}>
                {translateMealType(menu.mealType)}
              </h3>
              {menu.caloriesKcal && (
                <span className="sc-badge sc-badge-warning">
                  <Flame size={12} />
                  {menu.caloriesKcal} kcal
                </span>
              )}
            </div>

            <div style={{ fontSize: '11px', color: 'var(--sc-text-secondary)', marginTop: '2px' }}>
              Áp dụng ngày: {formatVietnameseDate(menu.menuDate)}
            </div>

            <ul
              style={{
                marginTop: '10px',
                paddingLeft: '18px',
                fontSize: '13px',
                lineHeight: 1.7,
                fontWeight: 600,
              }}
            >
              {menu.dishes.map((dish, idx) => (
                <li key={idx}>{dish}</li>
              ))}
            </ul>

            {menu.nutritionNote && (
              <div
                style={{
                  marginTop: '10px',
                  background: 'var(--sc-primary-tint)',
                  padding: '8px 10px',
                  borderRadius: '10px',
                  fontSize: '12px',
                  color: 'var(--sc-primary-dark)',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '6px',
                }}
              >
                <Sparkles size={14} />
                <span>{menu.nutritionNote}</span>
              </div>
            )}
          </article>
        ))
      )}
    </div>
  );
}

