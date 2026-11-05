package com.schoolconnect.repository;

import com.schoolconnect.entity.LeaveRequestEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequestEntity, Long> {

    long countByStudentIdAndStatus(Long studentId, String status);

    List<LeaveRequestEntity> findByParentIdOrderByCreatedAtDesc(Long parentId);

    List<LeaveRequestEntity> findByStudentIdAndParentIdOrderByCreatedAtDesc(Long studentId, Long parentId);

    List<LeaveRequestEntity> findAllByOrderByCreatedAtDesc();

    List<LeaveRequestEntity> findByStatusIgnoreCaseOrderByCreatedAtDesc(String status);

    @Query("""
        SELECT lr FROM LeaveRequestEntity lr
        WHERE lr.studentId = :studentId
          AND lr.status IN ('PENDING', 'APPROVED')
          AND lr.startDate <= :endDate
          AND lr.endDate >= :startDate
        """)
    List<LeaveRequestEntity> findActiveOverlappingByDateRange(
            @Param("studentId") Long studentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}

