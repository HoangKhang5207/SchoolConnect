package com.schoolconnect.controller;

import com.schoolconnect.dto.attendance.AttendanceSummaryResponseDto;
import com.schoolconnect.dto.common.ApiResponse;
import com.schoolconnect.security.UserPrincipal;
import com.schoolconnect.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Student attendance records and monthly metrics endpoint (FR-005).
 */
@RestController
@RequestMapping("/api/v1/children/{studentId}/attendance")
@Tag(name = "Attendance", description = "Student attendance summary metrics and check-in/out history")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    @Operation(summary = "Get attendance summary and history for a child")
    public ResponseEntity<ApiResponse<AttendanceSummaryResponseDto>> getStudentAttendance(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long studentId,
            @RequestParam(required = false) String month
    ) {
        AttendanceSummaryResponseDto response =
                attendanceService.getStudentAttendance(principal.id(), studentId, month);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}

