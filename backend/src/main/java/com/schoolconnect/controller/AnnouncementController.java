package com.schoolconnect.controller;

import com.schoolconnect.dto.announcement.AnnouncementDto;
import com.schoolconnect.dto.common.ApiResponse;
import com.schoolconnect.service.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * School-wide and class announcements endpoints (FR-008).
 */
@RestController
@RequestMapping("/api/v1/announcements")
@Tag(name = "Announcements", description = "Pinned and categorized school/class notices")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    @Operation(summary = "Get school and class announcements optionally filtered by category")
    public ResponseEntity<ApiResponse<List<AnnouncementDto>>> getAnnouncements(
            @RequestParam(required = false) String category
    ) {
        List<AnnouncementDto> announcements = announcementService.getAnnouncements(category);
        return ResponseEntity.ok(ApiResponse.ok(announcements));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get full announcement details by ID")
    public ResponseEntity<ApiResponse<AnnouncementDto>> getAnnouncementById(
            @PathVariable Long id
    ) {
        AnnouncementDto announcement = announcementService.getAnnouncementById(id);
        return ResponseEntity.ok(ApiResponse.ok(announcement));
    }
}

