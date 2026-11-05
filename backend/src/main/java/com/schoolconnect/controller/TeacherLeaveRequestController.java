package com.schoolconnect.controller;

import com.schoolconnect.dto.common.ApiResponse;
import com.schoolconnect.dto.leave.LeaveRequestDto;
import com.schoolconnect.dto.leave.ReviewLeaveRequestDto;
import com.schoolconnect.security.UserPrincipal;
import com.schoolconnect.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Teacher-facing endpoints for viewing and approving/rejecting student leave requests (FR-011, OQ-02).
 */
@RestController
@RequestMapping("/api/v1/teacher/leave-requests")
@Tag(name = "Teacher Leave Review", description = "Teacher queue for approving or rejecting student leave requests")
public class TeacherLeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public TeacherLeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @GetMapping
    @Operation(summary = "Get leave requests for teacher review optionally filtered by status")
    public ResponseEntity<ApiResponse<List<LeaveRequestDto>>> getLeaveRequestsForTeacher(
            @RequestParam(required = false) String status
    ) {
        List<LeaveRequestDto> requests = leaveRequestService.getAllLeaveRequestsForTeacher(status);
        return ResponseEntity.ok(ApiResponse.ok(requests));
    }

    @PatchMapping("/{id}/review")
    @Operation(summary = "Approve or reject a PENDING student leave request")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> reviewLeaveRequest(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody ReviewLeaveRequestDto request
    ) {
        LeaveRequestDto reviewed = leaveRequestService.reviewLeaveRequest(id, principal.fullName(), request);
        String msg = "APPROVED".equalsIgnoreCase(reviewed.status())
                ? "Đã phê duyệt đơn xin nghỉ phép"
                : "Đã từ chối đơn xin nghỉ phép";
        return ResponseEntity.ok(ApiResponse.ok(msg, reviewed));
    }
}

