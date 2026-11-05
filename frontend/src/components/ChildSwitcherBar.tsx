import { Baby } from 'lucide-react';
import { useAppContext } from '../store/AppContext';

export function ChildSwitcherBar() {
  const { currentUser, linkedChildren, selectedStudentId, selectStudent } = useAppContext();

  if (currentUser?.role !== 'PARENT' || linkedChildren.length === 0) {
    return null;
  }

  return (
    <div className="sc-child-pills" role="tablist" aria-label="Chọn hồ sơ bé">
      {linkedChildren.map((child) => {
        const isActive = child.id === selectedStudentId;
        return (
          <button
            key={child.id}
            type="button"
            role="tab"
            aria-selected={isActive}
            data-testid={`child-pill-${child.id}`}
            className={`sc-child-pill ${isActive ? 'active' : ''}`}
            onClick={() => selectStudent(child.id)}
          >
            <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
              <Baby size={15} />
              <span style={{ fontSize: '13px', fontWeight: 700 }}>{child.fullName}</span>
            </div>
            <div style={{ fontSize: '11px', opacity: 0.85, marginTop: '2px' }}>
              {child.className} • {child.studentCode}
            </div>
          </button>
        );
      })}
    </div>
  );
}

