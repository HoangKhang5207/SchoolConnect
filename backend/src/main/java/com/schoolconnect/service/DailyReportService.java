package com.schoolconnect.service;

import com.schoolconnect.dto.dailyreport.DailyReportDto;
import com.schoolconnect.entity.DailyReportEntity;
import com.schoolconnect.repository.DailyReportRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides daily care reports (Meals, Sleep, Activities, Mood, Teacher Note) for a child (FR-006).
 */
@Service
public class DailyReportService {

    private final GuardianAccessService guardianAccessService;
    private final DailyReportRepository dailyReportRepository;

    public DailyReportService(
            GuardianAccessService guardianAccessService,
            DailyReportRepository dailyReportRepository
    ) {
        this.guardianAccessService = guardianAccessService;
        this.dailyReportRepository = dailyReportRepository;
    }

    @Transactional(readOnly = true)
    public List<DailyReportDto> getDailyReports(Long parentId, Long studentId, LocalDate date) {
        guardianAccessService.verifyParentOwnsStudent(parentId, studentId);

        List<DailyReportEntity> entities = (date != null)
                ? dailyReportRepository.findByStudentIdAndReportDateOrderByReportDateDesc(studentId, date)
                : dailyReportRepository.findByStudentIdOrderByReportDateDesc(studentId);

        return entities.stream()
                .map(DailyReportService::toDto)
                .toList();
    }

    public static DailyReportDto toDto(DailyReportEntity entity) {
        return new DailyReportDto(
                entity.getId(),
                entity.getStudentId(),
                entity.getReportDate(),
                entity.getMealStatus(),
                entity.getMealComment(),
                entity.getSleepStatus(),
                entity.getSleepDurationMins(),
                entity.getActivitySummary(),
                entity.getMoodStatus(),
                entity.getTeacherNote(),
                entity.getRecordedBy(),
                entity.getUpdatedAt()
        );
    }
}

