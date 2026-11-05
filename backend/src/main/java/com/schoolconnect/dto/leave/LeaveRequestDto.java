package com.schoolconnect.dto.leave;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record LeaveRequestDto(
        Long id,
        String requestCode,
        Long studentId,
        String studentName,
        String className,
        String parentName,
        String parentPhone,
        LocalDate startDate,
        LocalDate endDate,
        String sessionType,
        String reason,
        String status,
        String teacherNote,
        String reviewedBy,
        OffsetDateTime reviewedAt,
        OffsetDateTime createdAt
) {
}

