export function formatVietnameseDate(isoDate?: string | null): string {
  if (!isoDate) {
    return '—';
  }
  const parts = isoDate.split('T')[0].split('-');
  if (parts.length !== 3) {
    return isoDate;
  }
  return `${parts[2]}/${parts[1]}/${parts[0]}`;
}

export function formatDateTimeShort(isoDateTime?: string | null): string {
  if (!isoDateTime) {
    return '—';
  }
  try {
    const dt = new Date(isoDateTime);
    const day = String(dt.getDate()).padStart(2, '0');
    const month = String(dt.getMonth() + 1).padStart(2, '0');
    const year = dt.getFullYear();
    const hours = String(dt.getHours()).padStart(2, '0');
    const mins = String(dt.getMinutes()).padStart(2, '0');
    return `${hours}:${mins} - ${day}/${month}/${year}`;
  } catch {
    return isoDateTime;
  }
}

export function translateAttendanceStatus(status: string): {
  label: string;
  tone: 'success' | 'warning' | 'danger' | 'neutral';
} {
  switch (status) {
    case 'PRESENT':
      return { label: 'Có mặt đúng giờ', tone: 'success' };
    case 'LATE':
      return { label: 'Đi muộn', tone: 'warning' };
    case 'ABSENT_EXCUSED':
      return { label: 'Nghỉ có phép', tone: 'neutral' };
    case 'ABSENT_UNEXCUSED':
      return { label: 'Nghỉ không phép', tone: 'danger' };
    default:
      return { label: 'Chưa điểm danh', tone: 'neutral' };
  }
}

export function translateSessionType(sessionType: string): string {
  switch (sessionType) {
    case 'FULL_DAY':
      return 'Nghỉ cả ngày';
    case 'MORNING':
      return 'Nghỉ buổi sáng';
    case 'AFTERNOON':
      return 'Nghỉ buổi chiều';
    default:
      return sessionType;
  }
}

export function translateLeaveStatus(status: string): {
  label: string;
  tone: 'warning' | 'success' | 'danger' | 'neutral';
} {
  switch (status) {
    case 'PENDING':
      return { label: 'Chờ giáo viên duyệt', tone: 'warning' };
    case 'APPROVED':
      return { label: 'Đã duyệt', tone: 'success' };
    case 'REJECTED':
      return { label: 'Từ chối', tone: 'danger' };
    case 'CANCELLED':
      return { label: 'Đã hủy đơn', tone: 'neutral' };
    default:
      return { label: status, tone: 'neutral' };
  }
}

export function getTomorrowIsoDate(): string {
  const now = new Date();
  now.setDate(now.getDate() + 1);
  const yyyy = now.getFullYear();
  const mm = String(now.getMonth() + 1).padStart(2, '0');
  const dd = String(now.getDate()).padStart(2, '0');
  return `${yyyy}-${mm}-${dd}`;
}

