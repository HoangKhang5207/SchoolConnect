import { ArrowLeftRight, GraduationCap, ShieldCheck, UserCheck } from 'lucide-react';
import { useAppContext } from '../store/AppContext';

export function RoleSwitcherBar() {
  const { currentUser, switchRole, isInitializing } = useAppContext();
  const isTeacher = currentUser?.role === 'TEACHER';

  const handleToggleRole = () => {
    const nextRole = isTeacher ? 'PARENT' : 'TEACHER';
    void switchRole(nextRole);
  };

  return (
    <div className="sc-role-bar" data-testid="role-switcher-bar">
      <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '12px' }}>
        {isTeacher ? <GraduationCap size={16} /> : <UserCheck size={16} />}
        <span style={{ fontWeight: 700 }} data-testid="active-role-badge">
          {isTeacher
            ? 'Chế độ: Giáo viên / Nhà trường (Demo)'
            : 'Chế độ: Phụ huynh (Demo)'}
        </span>
      </div>

      <button
        type="button"
        className="sc-role-switch-btn"
        data-testid="role-switch-btn"
        disabled={isInitializing}
        onClick={handleToggleRole}
      >
        <ArrowLeftRight size={13} />
        <span>
          {isTeacher ? 'Chuyển sang: Phụ huynh' : 'Chuyển sang: Giáo viên duyệt đơn'}
        </span>
      </button>
    </div>
  );
}

export function SchoolBrandBanner() {
  const { currentUser } = useAppContext();
  return (
    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
      <div>
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: '6px',
            fontSize: '11px',
            fontWeight: 700,
            letterSpacing: '0.04em',
            opacity: 0.92,
          }}
        >
          <ShieldCheck size={14} />
          <span>TRƯỜNG MẦM NON SAO MAI</span>
        </div>
        <h1 style={{ fontSize: '18px', fontWeight: 800, marginTop: '2px' }}>
          SchoolConnect — Sổ Liên Lạc Mầm Non
        </h1>
      </div>
      {currentUser && (
        <div
          style={{
            textAlign: 'right',
            fontSize: '11px',
            background: 'rgba(255,255,255,0.16)',
            padding: '6px 10px',
            borderRadius: '10px',
          }}
        >
          <div style={{ opacity: 0.85 }}>Xin chào,</div>
          <div style={{ fontWeight: 700 }}>{currentUser.fullName}</div>
        </div>
      )}
    </div>
  );
}

