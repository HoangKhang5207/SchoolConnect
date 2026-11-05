package com.schoolconnect.repository;

import com.schoolconnect.entity.StudentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

    @Query("""
        SELECT s FROM StudentEntity s
        JOIN ParentStudentEntity ps ON ps.studentId = s.id
        WHERE ps.parentId = :parentId AND s.isActive = true
        ORDER BY ps.isPrimary DESC, s.id ASC
        """)
    List<StudentEntity> findActiveStudentsByParentId(@Param("parentId") Long parentId);
}

