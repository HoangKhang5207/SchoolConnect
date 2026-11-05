package com.schoolconnect.repository;

import com.schoolconnect.entity.DailyReportEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReportEntity, Long> {
    Optional<DailyReportEntity> findFirstByStudentIdOrderByReportDateDesc(Long studentId);
    List<DailyReportEntity> findByStudentIdOrderByReportDateDesc(Long studentId);
    List<DailyReportEntity> findByStudentIdAndReportDateOrderByReportDateDesc(Long studentId, LocalDate reportDate);
}

