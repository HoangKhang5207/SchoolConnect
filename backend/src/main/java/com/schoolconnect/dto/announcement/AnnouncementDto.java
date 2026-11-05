package com.schoolconnect.dto.announcement;

import java.time.OffsetDateTime;

public record AnnouncementDto(
        Long id,
        String title,
        String summary,
        String content,
        String category,
        String targetClass,
        Boolean isPinned,
        String authorName,
        OffsetDateTime publishedAt
) {
}

