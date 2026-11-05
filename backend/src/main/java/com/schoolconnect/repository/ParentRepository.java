package com.schoolconnect.repository;

import com.schoolconnect.entity.ParentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<ParentEntity, Long> {
    Optional<ParentEntity> findByZaloUserId(String zaloUserId);
    Optional<ParentEntity> findFirstByRoleOrderByIdAsc(String role);
}

