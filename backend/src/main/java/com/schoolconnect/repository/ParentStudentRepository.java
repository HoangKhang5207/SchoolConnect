package com.schoolconnect.repository;

import com.schoolconnect.entity.ParentStudentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentStudentRepository extends JpaRepository<ParentStudentEntity, Long> {
    boolean existsByParentIdAndStudentId(Long parentId, Long studentId);
    List<ParentStudentEntity> findByParentIdOrderByIsPrimaryDescIdAsc(Long parentId);
}

