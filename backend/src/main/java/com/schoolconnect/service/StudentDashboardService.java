package com.schoolconnect.service;

import com.schoolconnect.dto.announcement.AnnouncementDto;
import com.schoolconnect.dto.attendance.AttendanceRecordDto;
import com.schoolconnect.dto.dailyreport.DailyReportDto;
import com.schoolconnect.dto.student.DashboardResponseDto;
import com.schoolconnect.entity.AttendanceRecordEntity;
import com.schoolconnect.entity.StudentEntity;
import com.schoolconnect.repository.AnnouncementRepository;
import com.schoolconnect.repository.AttendanceRecordRepository;
import com.schoolconnect.repository.DailyReportRepository;
import com.schoolconnect.repository.LeaveRequestRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Aggregates student profile, BMI metrics, latest attendance, latest daily report, and announcements for Home Dashboard.
 */
@Service
public class StudentDashboardService {

    private final GuardianAccessService guardianAccessService;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final DailyReportRepository dailyReportRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final AnnouncementRepository announcementRepository;

    public StudentDashboardService(
            GuardianAccessService guardianAccessService,
            AttendanceRecordRepository attendanceRecordRepository,
            DailyReportRepository dailyReportRepository,
            LeaveRequestRepository leaveRequestRepository,
            AnnouncementRepository announcementRepository
    ) {
        this.guardianAccessService = guardianAccessService;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.dailyReportRepository = dailyReportRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.announcementRepository = announcementRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponseDto getStudentDashboard(Long parentId, Long studentId) {
        StudentEntity student = guardianAccessService.verifyParentOwnsStudent(parentId, studentId);

        Double bmiValue = calculateBmi(student.getHeightCm(), student.getWeightKg());
        String bmiStatus = evaluateBmiStatus(bmiValue);

        DashboardResponseDto.StudentProfileDetailDto studentDto =
                new DashboardResponseDto.StudentProfileDetailDto(
                        student.getId(),
                        student.getStudentCode(),
                        student.getFullName(),
                        student.getDateOfBirth(),
                        student.getGender(),
                        student.getClassName(),
                        student.getTeacherName(),
                        student.getAvatarUrl(),
                        student.getHeightCm(),
                        student.getWeightKg(),
                        bmiValue,
                        bmiStatus,
                        student.getBloodType(),
                        student.getAllergyNotes()
                );

        List<AttendanceRecordEntity> attendanceList =
                attendanceRecordRepository.findByStudentIdOrderByAttendanceDateDesc(studentId);
        AttendanceRecordDto latestAttendance = attendanceList.isEmpty()
                ? null
                : AttendanceService.toDto(attendanceList.get(0));

        DailyReportDto latestReport = dailyReportRepository
                .findFirstByStudentIdOrderByReportDateDesc(studentId)
                .map(DailyReportService::toDto)
                .orElse(null);

        long pendingLeaves = leaveRequestRepository.countByStudentIdAndStatus(studentId, "PENDING");

        List<AnnouncementDto> recentAnnouncements = announcementRepository
                .findAllByOrderByIsPinnedDescPublishedAtDesc()
                .stream()
                .filter(a -> a.getTargetClass() == null || a.getTargetClass().equalsIgnoreCase(student.getClassName()))
                .limit(2)
                .map(AnnouncementService::toDto)
                .toList();

        return new DashboardResponseDto(
                studentDto,
                latestAttendance,
                latestReport,
                pendingLeaves,
                recentAnnouncements
        );
    }

    public static Double calculateBmi(BigDecimal heightCm, BigDecimal weightKg) {
        if (heightCm == null || weightKg == null || heightCm.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        double heightMeters = heightCm.doubleValue() / 100.0;
        double rawBmi = weightKg.doubleValue() / (heightMeters * heightMeters);
        return BigDecimal.valueOf(rawBmi).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    public static String evaluateBmiStatus(Double bmi) {
        if (bmi == null) {
            return "Chưa cập nhật";
        }
        if (bmi < 14.0) {
            return "Cần bổ sung dinh dưỡng";
        }
        if (bmi <= 17.5) {
            return "Phát triển chuẩn";
        }
        return "Thừa cân nhẹ";
    }
}

