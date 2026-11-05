package com.schoolconnect.dto.student;

import java.time.LocalDate;

public record StudentSummaryDto(
        Long id,
        String studentCode,
        String fullName,
        String className,
        String teacherName,
        String gender,
        LocalDate dateOfBirth,
        String avatarUrl
) {
}

