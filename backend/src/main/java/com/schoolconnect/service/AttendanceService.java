package com.schoolconnect.service;

import com.schoolconnect.dto.attendance.AttendanceRecordDto;
import com.schoolconnect.dto.attendance.AttendanceSummaryResponseDto;
import com.schoolconnect.entity.AttendanceRecordEntity;
import com.schoolconnect.entity.StudentEntity;
import com.schoolconnect.exception.BadRequestException;
import com.schoolconnect.repository.AttendanceRecordRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Provides student attendance logs and summary counters (FR-005, RULE-011).
 */
@Service
public class AttendanceService {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private final GuardianAccessService guardianAccessService;
    private final AttendanceRecordRepository attendanceRecordRepository;

    public AttendanceService(
            GuardianAccessService guardianAccessService,
            AttendanceRecordRepository attendanceRecordRepository
    ) {
        this.guardianAccessService = guardianAccessService;
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    @Transactional(readOnly = true)
    public AttendanceSummaryResponseDto getStudentAttendance(Long parentId, Long studentId, String month) {
        StudentEntity student = guardianAccessService.verifyParentOwnsStudent(parentId, studentId);

        List<AttendanceRecordEntity> entities;
        if (StringUtils.hasText(month)) {
            try {
                YearMonth ym = YearMonth.parse(month.trim());
                LocalDate start = ym.atDay(1);
                LocalDate end = ym.atEndOfMonth();
                entities = attendanceRecordRepository
                        .findByStudentIdAndAttendanceDateBetweenOrderByAttendanceDateDesc(studentId, start, end);
            } catch (DateTimeParseException ex) {
                throw new BadRequestException("VALIDATION_ERROR", "Định dạng tháng không hợp lệ (yêu cầu YYYY-MM).");
            }
        } else {
            entities = attendanceRecordRepository.findByStudentIdOrderByAttendanceDateDesc(studentId);
        }

        int totalDays = entities.size();
        int presentCount = 0;
        int excusedCount = 0;
        int unexcusedCount = 0;
        int lateCount = 0;

        for (AttendanceRecordEntity item : entities) {
            String st = item.getStatus();
            if ("PRESENT".equalsIgnoreCase(st)) {
                presentCount++;
            } else if ("ABSENT_EXCUSED".equalsIgnoreCase(st)) {
                excusedCount++;
            } else if ("ABSENT_UNEXCUSED".equalsIgnoreCase(st)) {
                unexcusedCount++;
            } else if ("LATE".equalsIgnoreCase(st)) {
                lateCount++;
            }
        }

        double rate = totalDays == 0
                ? 0.0
                : BigDecimal.valueOf(((double) (presentCount + lateCount) / totalDays) * 100.0)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();

        AttendanceSummaryResponseDto.AttendanceSummaryMetricsDto summary =
                new AttendanceSummaryResponseDto.AttendanceSummaryMetricsDto(
                        totalDays,
                        presentCount,
                        excusedCount,
                        unexcusedCount,
                        lateCount,
                        rate
                );

        List<AttendanceRecordDto> records = entities.stream()
                .map(AttendanceService::toDto)
                .toList();

        return new AttendanceSummaryResponseDto(
                student.getId(),
                student.getFullName(),
                summary,
                records
        );
    }

    public static AttendanceRecordDto toDto(AttendanceRecordEntity entity) {
        return new AttendanceRecordDto(
                entity.getId(),
                entity.getAttendanceDate(),
                entity.getStatus(),
                entity.getCheckInTime() != null ? entity.getCheckInTime().format(TIME_FMT) : null,
                entity.getCheckOutTime() != null ? entity.getCheckOutTime().format(TIME_FMT) : null,
                entity.getPickupPerson(),
                entity.getNote()
        );
    }
}

