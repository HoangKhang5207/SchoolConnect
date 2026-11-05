package com.schoolconnect.dto.attendance;

import java.util.List;

public record AttendanceSummaryResponseDto(
        Long studentId,
        String studentName,
        AttendanceSummaryMetricsDto summary,
        List<AttendanceRecordDto> records
) {
    public record AttendanceSummaryMetricsDto(
            int totalDays,
            int presentCount,
            int excusedAbsentCount,
            int unexcusedAbsentCount,
            int lateCount,
            double attendanceRate
    ) {
    }
}

