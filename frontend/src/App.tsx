import { ErrorStateView, LoadingStateView } from './components/AsyncStateViews';
import { MobileShellLayout } from './layouts/MobileShellLayout';
import { AbsenceRequestPage } from './pages/AbsenceRequestPage';
import { AnnouncementsPage } from './pages/AnnouncementsPage';
import { AttendancePage } from './pages/AttendancePage';
import { DailyReportPage } from './pages/DailyReportPage';
import { HomeDashboardPage } from './pages/HomeDashboardPage';
import { MealMenuPage } from './pages/MealMenuPage';
import { TeacherReviewPage } from './pages/TeacherReviewPage';
import { AppProvider, useAppContext } from './store/AppContext';

function AppRouterView() {
  const { currentUser, activeTab, isInitializing, authError, retryAuth } = useAppContext();

  if (isInitializing && !currentUser) {
    return (
      <MobileShellLayout>
        <LoadingStateView message="Đang khởi tạo phiên làm việc SchoolConnect..." />
      </MobileShellLayout>
    );
  }

  if (authError && !currentUser) {
    return (
      <MobileShellLayout>
        <ErrorStateView message={authError} onRetry={() => void retryAuth()} />
      </MobileShellLayout>
    );
  }

  if (currentUser?.role === 'TEACHER') {
    return (
      <MobileShellLayout>
        <TeacherReviewPage />
      </MobileShellLayout>
    );
  }

  return (
    <MobileShellLayout>
      {activeTab === 'HOME' && <HomeDashboardPage />}
      {activeTab === 'DAILY_REPORT' && <DailyReportPage />}
      {activeTab === 'ATTENDANCE' && <AttendancePage />}
      {activeTab === 'MEAL_MENU' && <MealMenuPage />}
      {activeTab === 'ANNOUNCEMENTS' && <AnnouncementsPage />}
      {activeTab === 'ABSENCE_REQUEST' && <AbsenceRequestPage />}
    </MobileShellLayout>
  );
}

export function App() {
  return (
    <AppProvider>
      <AppRouterView />
    </AppProvider>
  );
}

export default App;
