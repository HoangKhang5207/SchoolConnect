package com.schoolconnect.controller;

import com.schoolconnect.dto.common.ApiResponse;
import com.schoolconnect.dto.student.DashboardResponseDto;
import com.schoolconnect.dto.student.StudentSummaryDto;
import com.schoolconnect.security.UserPrincipal;
import com.schoolconnect.service.GuardianAccessService;
import com.schoolconnect.service.StudentDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints for parent's linked children and aggregated student Home Dashboard (FR-003, FR-004).
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Parent & Student Dashboard", description = "Linked children list and Home Dashboard aggregation")
public class ParentStudentController {

    private final GuardianAccessService guardianAccessService;
    private final StudentDashboardService studentDashboardService;

    public ParentStudentController(
            GuardianAccessService guardianAccessService,
            StudentDashboardService studentDashboardService
    ) {
        this.guardianAccessService = guardianAccessService;
        this.studentDashboardService = studentDashboardService;
    }

    @GetMapping("/parent/children")
    @Operation(summary = "Get list of preschool students linked to the authenticated parent")
    public ResponseEntity<ApiResponse<List<StudentSummaryDto>>> getLinkedChildren(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        List<StudentSummaryDto> children = guardianAccessService.getLinkedChildren(principal.id());
        return ResponseEntity.ok(ApiResponse.ok(children));
    }

    @GetMapping("/children/{studentId}/dashboard")
    @Operation(summary = "Get aggregated Home Dashboard for a specific child")
    public ResponseEntity<ApiResponse<DashboardResponseDto>> getDashboard(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long studentId
    ) {
        DashboardResponseDto dashboard = studentDashboardService.getStudentDashboard(principal.id(), studentId);
        return ResponseEntity.ok(ApiResponse.ok(dashboard));
    }
}

