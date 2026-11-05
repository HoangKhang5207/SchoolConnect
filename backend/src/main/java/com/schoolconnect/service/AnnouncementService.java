package com.schoolconnect.service;

import com.schoolconnect.dto.announcement.AnnouncementDto;
import com.schoolconnect.entity.AnnouncementEntity;
import com.schoolconnect.exception.ResourceNotFoundException;
import com.schoolconnect.repository.AnnouncementRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Provides school and class announcements ordered by pinned status and publication timestamp (FR-008).
 */
@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @Transactional(readOnly = true)
    public List<AnnouncementDto> getAnnouncements(String category) {
        List<AnnouncementEntity> entities = (StringUtils.hasText(category) && !"ALL".equalsIgnoreCase(category.trim()))
                ? announcementRepository.findByCategoryIgnoreCaseOrderByIsPinnedDescPublishedAtDesc(category.trim())
                : announcementRepository.findAllByOrderByIsPinnedDescPublishedAtDesc();

        return entities.stream()
                .map(AnnouncementService::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public AnnouncementDto getAnnouncementById(Long id) {
        AnnouncementEntity entity = announcementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông báo với mã #" + id));
        return toDto(entity);
    }

    public static AnnouncementDto toDto(AnnouncementEntity entity) {
        return new AnnouncementDto(
                entity.getId(),
                entity.getTitle(),
                entity.getSummary(),
                entity.getContent(),
                entity.getCategory(),
                entity.getTargetClass(),
                entity.getIsPinned(),
                entity.getAuthorName(),
                entity.getPublishedAt()
        );
    }
}

