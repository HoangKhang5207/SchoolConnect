package com.schoolconnect.dto.attendance;

import java.time.LocalDate;

public record AttendanceRecordDto(
        Long id,
        LocalDate attendanceDate,
        String status,
        String checkInTime,
        String checkOutTime,
        String pickupPerson,
        String note
) {
}

