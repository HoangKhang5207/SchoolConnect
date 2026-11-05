package com.schoolconnect.controller;

import com.schoolconnect.dto.common.ApiResponse;
import com.schoolconnect.dto.leave.CreateLeaveRequestDto;
import com.schoolconnect.dto.leave.LeaveRequestDto;
import com.schoolconnect.security.UserPrincipal;
import com.schoolconnect.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Parent-facing endpoints for submitting, listing, and cancelling student leave requests (FR-009, FR-010).
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Parent Leave Requests", description = "Submit, view, and cancel student leave requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @PostMapping("/children/{studentId}/leave-requests")
    @Operation(summary = "Submit a new leave request for a linked child")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> createLeaveRequest(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long studentId,
            @Valid @RequestBody CreateLeaveRequestDto request
    ) {
        LeaveRequestDto created = leaveRequestService.createLeaveRequest(principal.id(), studentId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Đã gửi đơn xin nghỉ phép thành công", created));
    }

    @GetMapping({"/leave-requests", "/children/{studentId}/leave-requests"})
    @Operation(summary = "Get leave request history for the authenticated parent")
    public ResponseEntity<ApiResponse<List<LeaveRequestDto>>> getParentLeaveRequests(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable(required = false) Long studentId,
            @RequestParam(name = "studentId", required = false) Long queryStudentId
    ) {
        Long targetStudentId = studentId != null ? studentId : queryStudentId;
        List<LeaveRequestDto> list = leaveRequestService.getLeaveRequestsForParent(principal.id(), targetStudentId);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PatchMapping("/leave-requests/{id}/cancel")
    @Operation(summary = "Cancel a PENDING leave request owned by the authenticated parent")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> cancelLeaveRequest(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        LeaveRequestDto cancelled = leaveRequestService.cancelLeaveRequest(principal.id(), id);
        return ResponseEntity.ok(ApiResponse.ok("Đã hủy đơn xin nghỉ phép thành công", cancelled));
    }
}

