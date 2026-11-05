import React from 'react';
import {
  Bell,
  BookOpen,
  CalendarCheck,
  FilePlus2,
  Home,
  Utensils,
} from 'lucide-react';
import { useAppContext } from '../store/AppContext';
import { ChildSwitcherBar } from '../components/ChildSwitcherBar';
import { RoleSwitcherBar, SchoolBrandBanner } from '../components/RoleSwitcherBar';
import type { ParentScreenTab } from '../types/api';

const NAV_ITEMS: Array<{
  tab: ParentScreenTab;
  label: string;
  icon: React.ReactNode;
}> = [
  { tab: 'HOME', label: 'Trang chủ', icon: <Home size={18} /> },
  { tab: 'DAILY_REPORT', label: 'Nhật ký', icon: <BookOpen size={18} /> },
  { tab: 'ATTENDANCE', label: 'Điểm danh', icon: <CalendarCheck size={18} /> },
  { tab: 'MEAL_MENU', label: 'Thực đơn', icon: <Utensils size={18} /> },
  { tab: 'ANNOUNCEMENTS', label: 'Thông báo', icon: <Bell size={18} /> },
  { tab: 'ABSENCE_REQUEST', label: 'Xin nghỉ', icon: <FilePlus2 size={18} /> },
];

export function MobileShellLayout({ children }: { children: React.ReactNode }) {
  const { currentUser, activeTab, navigateTab } = useAppContext();
  const isParent = currentUser?.role === 'PARENT';

  return (
    <div className="sc-shell">
      <header className="sc-top-banner">
        <RoleSwitcherBar />
        <SchoolBrandBanner />
        <ChildSwitcherBar />
      </header>

      <main className="sc-main">{children}</main>

      {isParent && (
        <nav className="sc-bottom-nav" aria-label="Điều hướng chính">
          {NAV_ITEMS.map((item) => (
            <button
              key={item.tab}
              type="button"
              data-testid={`nav-tab-${item.tab}`}
              className={`sc-nav-item ${activeTab === item.tab ? 'active' : ''}`}
              onClick={() => navigateTab(item.tab)}
            >
              {item.icon}
              <span>{item.label}</span>
            </button>
          ))}
        </nav>
      )}
    </div>
  );
}

