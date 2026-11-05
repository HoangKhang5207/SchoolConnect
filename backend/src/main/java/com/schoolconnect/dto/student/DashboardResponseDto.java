package com.schoolconnect.dto.student;

import com.schoolconnect.dto.announcement.AnnouncementDto;
import com.schoolconnect.dto.attendance.AttendanceRecordDto;
import com.schoolconnect.dto.dailyreport.DailyReportDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DashboardResponseDto(
        StudentProfileDetailDto student,
        AttendanceRecordDto todayAttendance,
        DailyReportDto latestDailyReport,
        long pendingLeaveCount,
        List<AnnouncementDto> recentAnnouncements
) {
    public record StudentProfileDetailDto(
            Long id,
            String studentCode,
            String fullName,
            LocalDate dateOfBirth,
            String gender,
            String className,
            String teacherName,
            String avatarUrl,
            BigDecimal heightCm,
            BigDecimal weightKg,
            Double bmi,
            String bmiStatus,
            String bloodType,
            String allergyNotes
    ) {
    }
}

