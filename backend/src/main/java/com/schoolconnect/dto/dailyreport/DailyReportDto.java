package com.schoolconnect.dto.dailyreport;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record DailyReportDto(
        Long id,
        Long studentId,
        LocalDate reportDate,
        String mealStatus,
        String mealComment,
        String sleepStatus,
        Integer sleepDurationMins,
        String activitySummary,
        String moodStatus,
        String teacherNote,
        String recordedBy,
        OffsetDateTime updatedAt
) {
}

