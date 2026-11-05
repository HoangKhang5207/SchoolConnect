package com.schoolconnect.repository;

import com.schoolconnect.entity.AttendanceRecordEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecordEntity, Long> {
    Optional<AttendanceRecordEntity> findByStudentIdAndAttendanceDate(Long studentId, LocalDate attendanceDate);
    List<AttendanceRecordEntity> findByStudentIdOrderByAttendanceDateDesc(Long studentId);
    List<AttendanceRecordEntity> findByStudentIdAndAttendanceDateBetweenOrderByAttendanceDateDesc(
            Long studentId, LocalDate startDate, LocalDate endDate);
}

