package com.schoolconnect.repository;

import com.schoolconnect.entity.AnnouncementEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {
    List<AnnouncementEntity> findAllByOrderByIsPinnedDescPublishedAtDesc();
    List<AnnouncementEntity> findByCategoryIgnoreCaseOrderByIsPinnedDescPublishedAtDesc(String category);
}

