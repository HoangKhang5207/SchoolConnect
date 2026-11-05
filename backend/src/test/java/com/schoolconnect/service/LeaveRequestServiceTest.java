package com.schoolconnect.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.schoolconnect.dto.leave.CreateLeaveRequestDto;
import com.schoolconnect.dto.leave.LeaveRequestDto;
import com.schoolconnect.dto.leave.ReviewLeaveRequestDto;
import com.schoolconnect.entity.LeaveRequestEntity;
import com.schoolconnect.entity.ParentEntity;
import com.schoolconnect.entity.StudentEntity;
import com.schoolconnect.exception.BadRequestException;
import com.schoolconnect.exception.BusinessConflictException;
import com.schoolconnect.exception.ForbiddenAccessException;
import com.schoolconnect.repository.LeaveRequestRepository;
import com.schoolconnect.repository.ParentRepository;
import com.schoolconnect.repository.StudentRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for LeaveRequestService verifying RULE-001 through RULE-010.
 */
@ExtendWith(MockitoExtension.class)
class LeaveRequestServiceTest {

    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2026-11-04T09:00:00Z"), VN_ZONE);

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private GuardianAccessService guardianAccessService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ParentRepository parentRepository;

    private LeaveRequestService leaveRequestService;
    private StudentEntity student;
    private ParentEntity parent;

    @BeforeEach
    void setUp() {
        leaveRequestService = new LeaveRequestService(
                leaveRequestRepository,
                guardianAccessService,
                studentRepository,
                parentRepository,
                FIXED_CLOCK
        );

        student = new StudentEntity();
        student.setId(1L);
        student.setFullName("Nguyễn Minh An");
        student.setClassName("Lớp Chồi 1");

        parent = new ParentEntity();
        parent.setId(1L);
        parent.setFullName("Trần Thu Hà");
        parent.setPhone("0901234567");
        parent.setRole("PARENT");
    }

    @Test
    @DisplayName("createLeaveRequest generates LR-YYYYMMDD-XXXX code and PENDING status when valid (AC-012, RULE-010)")
    void shouldCreateLeaveRequestSuccessfully() {
        LocalDate tomorrow = LocalDate.of(2026, 11, 5);
        CreateLeaveRequestDto req = new CreateLeaveRequestDto(
                tomorrow,
                tomorrow,
                "FULL_DAY",
                "Bé đi khám sức khỏe định kỳ cùng mẹ tại bệnh viện Nhi."
        );

        when(guardianAccessService.verifyParentOwnsStudent(1L, 1L)).thenReturn(student);
        when(parentRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(leaveRequestRepository.findActiveOverlappingByDateRange(1L, tomorrow, tomorrow)).thenReturn(List.of());
        when(leaveRequestRepository.save(any(LeaveRequestEntity.class))).thenAnswer(inv -> {
            LeaveRequestEntity e = inv.getArgument(0);
            e.setId(99L);
            return e;
        });

        LeaveRequestDto result = leaveRequestService.createLeaveRequest(1L, 1L, req);

        assertThat(result.id()).isEqualTo(99L);
        assertThat(result.requestCode()).matches("^LR-20261104-\\d{4}$");
        assertThat(result.status()).isEqualTo("PENDING");
        assertThat(result.studentName()).isEqualTo("Nguyễn Minh An");
    }

    @Test
    @DisplayName("createLeaveRequest rejects startDate in the past or endDate < startDate (RULE-002, AC-013)")
    void shouldRejectInvalidDates() {
        when(guardianAccessService.verifyParentOwnsStudent(1L, 1L)).thenReturn(student);
        when(parentRepository.findById(1L)).thenReturn(Optional.of(parent));

        CreateLeaveRequestDto pastDateReq = new CreateLeaveRequestDto(
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 2),
                "FULL_DAY",
                "Xin nghỉ ngày quá khứ"
        );
        assertThatThrownBy(() -> leaveRequestService.createLeaveRequest(1L, 1L, pastDateReq))
                .isInstanceOf(BadRequestException.class);

        CreateLeaveRequestDto invertedDateReq = new CreateLeaveRequestDto(
                LocalDate.of(2026, 11, 6),
                LocalDate.of(2026, 11, 5),
                "FULL_DAY",
                "Ngày kết thúc nhỏ hơn ngày bắt đầu"
        );
        assertThatThrownBy(() -> leaveRequestService.createLeaveRequest(1L, 1L, invertedDateReq))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("createLeaveRequest rejects overlapping FULL_DAY and MORNING sessions with 409 Conflict (RULE-003, AC-013)")
    void shouldRejectOverlappingSessionLeaveRequest() {
        LocalDate targetDate = LocalDate.of(2026, 11, 7);
        LeaveRequestEntity existingMorning = new LeaveRequestEntity();
        existingMorning.setId(2L);
        existingMorning.setStudentId(1L);
        existingMorning.setStartDate(targetDate);
        existingMorning.setEndDate(targetDate);
        existingMorning.setSessionType("MORNING");
        existingMorning.setStatus("PENDING");

        when(guardianAccessService.verifyParentOwnsStudent(1L, 1L)).thenReturn(student);
        when(parentRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(leaveRequestRepository.findActiveOverlappingByDateRange(1L, targetDate, targetDate))
                .thenReturn(List.of(existingMorning));

        CreateLeaveRequestDto fullDayConflict = new CreateLeaveRequestDto(
                targetDate,
                targetDate,
                "FULL_DAY",
                "Xin nghỉ cả ngày trùng với buổi sáng đã đăng ký"
        );

        assertThatThrownBy(() -> leaveRequestService.createLeaveRequest(1L, 1L, fullDayConflict))
                .isInstanceOf(BusinessConflictException.class)
                .hasMessageContaining("trùng lặp");
    }

    @Test
    @DisplayName("reviewLeaveRequest requires teacherNote >= 5 chars when REJECTED and sets default note when APPROVED (RULE-008, AC-015, AC-016)")
    void shouldEnforceTeacherNoteRuleOnReview() {
        LeaveRequestEntity pending = new LeaveRequestEntity();
        pending.setId(2L);
        pending.setRequestCode("LR-20261104-0002");
        pending.setStudentId(1L);
        pending.setParentId(1L);
        pending.setStatus("PENDING");
        pending.setReason("Xin nghỉ buổi sáng");

        when(leaveRequestRepository.findById(2L)).thenReturn(Optional.of(pending));

        ReviewLeaveRequestDto emptyReject = new ReviewLeaveRequestDto("REJECTED", "   ");
        assertThatThrownBy(() -> leaveRequestService.reviewLeaveRequest(2L, "Cô Lê Thanh Thảo", emptyReject))
                .isInstanceOf(BadRequestException.class);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(parentRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(leaveRequestRepository.save(any(LeaveRequestEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        ReviewLeaveRequestDto validApprove = new ReviewLeaveRequestDto("APPROVED", "");
        LeaveRequestDto approved = leaveRequestService.reviewLeaveRequest(2L, "Cô Lê Thanh Thảo", validApprove);

        assertThat(approved.status()).isEqualTo("APPROVED");
        assertThat(approved.reviewedBy()).isEqualTo("Cô Lê Thanh Thảo");
        assertThat(approved.teacherNote()).isEqualTo("Nhà trường đã ghi nhận đơn xin nghỉ của bé.");
    }

    @Test
    @DisplayName("cancelLeaveRequest blocks cancelling non-PENDING or unowned requests (RULE-005, RULE-006, AC-017)")
    void shouldPreventCancellingAlreadyApprovedRequest() {
        LeaveRequestEntity approvedEntity = new LeaveRequestEntity();
        approvedEntity.setId(1L);
        approvedEntity.setStudentId(1L);
        approvedEntity.setParentId(1L);
        approvedEntity.setStatus("APPROVED");

        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(approvedEntity));

        assertThatThrownBy(() -> leaveRequestService.cancelLeaveRequest(1L, 1L))
                .isInstanceOf(BusinessConflictException.class);

        LeaveRequestEntity otherParentEntity = new LeaveRequestEntity();
        otherParentEntity.setId(3L);
        otherParentEntity.setStudentId(3L);
        otherParentEntity.setParentId(3L);
        otherParentEntity.setStatus("PENDING");

        when(leaveRequestRepository.findById(3L)).thenReturn(Optional.of(otherParentEntity));
        when(guardianAccessService.verifyParentOwnsStudent(1L, 3L))
                .thenThrow(new ForbiddenAccessException("ACCESS_DENIED_NOT_GUARDIAN", "Forbidden"));

        assertThatThrownBy(() -> leaveRequestService.cancelLeaveRequest(1L, 3L))
                .isInstanceOf(ForbiddenAccessException.class);
    }
}

