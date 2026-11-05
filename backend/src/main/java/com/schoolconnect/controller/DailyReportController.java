package com.schoolconnect.controller;

import com.schoolconnect.dto.common.ApiResponse;
import com.schoolconnect.dto.dailyreport.DailyReportDto;
import com.schoolconnect.security.UserPrincipal;
import com.schoolconnect.service.DailyReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Daily care report journal endpoint (FR-006).
 */
@RestController
@RequestMapping("/api/v1/children/{studentId}/daily-reports")
@Tag(name = "Daily Reports", description = "Preschool daily care journal (Meals, Sleep, Activities, Mood, Teacher Note)")
public class DailyReportController {

    private final DailyReportService dailyReportService;

    public DailyReportController(DailyReportService dailyReportService) {
        this.dailyReportService = dailyReportService;
    }

    @GetMapping
    @Operation(summary = "Get daily care reports for a child")
    public ResponseEntity<ApiResponse<List<DailyReportDto>>> getDailyReports(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long studentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<DailyReportDto> reports = dailyReportService.getDailyReports(principal.id(), studentId, date);
        return ResponseEntity.ok(ApiResponse.ok(reports));
    }
}

