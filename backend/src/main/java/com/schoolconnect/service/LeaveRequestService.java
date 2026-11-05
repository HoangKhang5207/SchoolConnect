package com.schoolconnect.service;

import com.schoolconnect.dto.leave.CreateLeaveRequestDto;
import com.schoolconnect.dto.leave.LeaveRequestDto;
import com.schoolconnect.dto.leave.ReviewLeaveRequestDto;
import com.schoolconnect.entity.LeaveRequestEntity;
import com.schoolconnect.entity.ParentEntity;
import com.schoolconnect.entity.StudentEntity;
import com.schoolconnect.exception.BadRequestException;
import com.schoolconnect.exception.BusinessConflictException;
import com.schoolconnect.exception.ForbiddenAccessException;
import com.schoolconnect.exception.ResourceNotFoundException;
import com.schoolconnect.repository.LeaveRequestRepository;
import com.schoolconnect.repository.ParentRepository;
import com.schoolconnect.repository.StudentRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * Transactional domain service for Leave Request submission, cancellation, and teacher approval/rejection.
 * Enforces RULE-001 through RULE-010.
 */
@Service
public class LeaveRequestService {

    private static final Set<String> VALID_SESSIONS = Set.of("FULL_DAY", "MORNING", "AFTERNOON");
    private static final Set<String> VALID_REVIEW_DECISIONS = Set.of("APPROVED", "REJECTED");
    private static final DateTimeFormatter CODE_DATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String DEFAULT_APPROVAL_NOTE = "Nhà trường đã ghi nhận đơn xin nghỉ của bé.";

    private final LeaveRequestRepository leaveRequestRepository;
    private final GuardianAccessService guardianAccessService;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final Clock clock;

    public LeaveRequestService(
            LeaveRequestRepository leaveRequestRepository,
            GuardianAccessService guardianAccessService,
            StudentRepository studentRepository,
            ParentRepository parentRepository,
            Clock clock
    ) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.guardianAccessService = guardianAccessService;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.clock = clock;
    }

    @Transactional
    public LeaveRequestDto createLeaveRequest(Long parentId, Long studentId, CreateLeaveRequestDto request) {
        // RULE-001: Verify guardian-student ownership
        StudentEntity student = guardianAccessService.verifyParentOwnsStudent(parentId, studentId);
        ParentEntity parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản phụ huynh."));

        // RULE-002: Validate dates against current local date (Asia/Ho_Chi_Minh)
        LocalDate today = LocalDate.now(clock);
        LocalDate startDate = request.startDate();
        LocalDate endDate = request.endDate();

        if (startDate == null || endDate == null) {
            throw new BadRequestException("VALIDATION_ERROR", "Ngày bắt đầu và ngày kết thúc không được để trống.");
        }
        if (startDate.isBefore(today)) {
            throw new BadRequestException("VALIDATION_ERROR", "Ngày bắt đầu nghỉ không được nhỏ hơn ngày hiện tại.");
        }
        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("VALIDATION_ERROR", "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu.");
        }
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (totalDays > 14) {
            throw new BadRequestException("VALIDATION_ERROR", "Một đơn xin nghỉ không được vượt quá 14 ngày liên tiếp.");
        }

        String sessionType = request.sessionType() != null ? request.sessionType().trim().toUpperCase() : "";
        if (!VALID_SESSIONS.contains(sessionType)) {
            throw new BadRequestException("VALIDATION_ERROR", "Buổi nghỉ không hợp lệ (chỉ chấp nhận FULL_DAY, MORNING, AFTERNOON).");
        }

        // RULE-009: Trim, validate length, and sanitize reason
        String sanitizedReason = sanitizeText(request.reason());
        if (sanitizedReason.length() < 5 || sanitizedReason.length() > 500) {
            throw new BadRequestException("VALIDATION_ERROR", "Lý do xin nghỉ phải từ 5 đến 500 ký tự.");
        }

        // RULE-003: Overlap check on date range AND session
        List<LeaveRequestEntity> dateOverlaps = leaveRequestRepository
                .findActiveOverlappingByDateRange(studentId, startDate, endDate);
        boolean hasSessionConflict = dateOverlaps.stream()
                .anyMatch(existing -> sessionsOverlap(existing.getSessionType(), sessionType));

        if (hasSessionConflict) {
            throw new BusinessConflictException(
                    "LEAVE_REQUEST_OVERLAP",
                    "Học sinh đã có đơn xin nghỉ trùng lặp trong khoảng thời gian này"
            );
        }

        // RULE-010: Generate unique human-readable request code LR-YYYYMMDD-XXXX
        String requestCode = generateUniqueRequestCode(today);
        OffsetDateTime now = OffsetDateTime.now(clock);

        LeaveRequestEntity entity = new LeaveRequestEntity();
        entity.setRequestCode(requestCode);
        entity.setStudentId(studentId);
        entity.setParentId(parentId);
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setSessionType(sessionType);
        entity.setReason(sanitizedReason);
        entity.setStatus("PENDING");
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        LeaveRequestEntity saved = leaveRequestRepository.save(entity);
        return toDto(saved, student, parent);
    }

    @Transactional(readOnly = true)
    public List<LeaveRequestDto> getLeaveRequestsForParent(Long parentId, Long studentId) {
        List<LeaveRequestEntity> entities;
        if (studentId != null) {
            guardianAccessService.verifyParentOwnsStudent(parentId, studentId);
            entities = leaveRequestRepository.findByStudentIdAndParentIdOrderByCreatedAtDesc(studentId, parentId);
        } else {
            entities = leaveRequestRepository.findByParentIdOrderByCreatedAtDesc(parentId);
        }
        return mapEntitiesToDtos(entities);
    }

    @Transactional
    public LeaveRequestDto cancelLeaveRequest(Long parentId, Long leaveRequestId) {
        LeaveRequestEntity entity = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn xin nghỉ phép #" + leaveRequestId));

        // RULE-006: Verify parent owns this leave request / student
        if (!entity.getParentId().equals(parentId)) {
            guardianAccessService.verifyParentOwnsStudent(parentId, entity.getStudentId());
            throw new ForbiddenAccessException("ACCESS_DENIED_NOT_GUARDIAN", "Bạn không có quyền hủy đơn xin nghỉ này.");
        }

        // RULE-005: Only PENDING requests can be cancelled
        if (!"PENDING".equalsIgnoreCase(entity.getStatus())) {
            throw new BusinessConflictException(
                    "INVALID_STATUS_TRANSITION",
                    "Chỉ có thể hủy đơn xin nghỉ đang ở trạng thái Chờ duyệt (PENDING)."
            );
        }

        entity.setStatus("CANCELLED");
        entity.setUpdatedAt(OffsetDateTime.now(clock));
        LeaveRequestEntity saved = leaveRequestRepository.save(entity);

        StudentEntity student = studentRepository.findById(saved.getStudentId()).orElse(null);
        ParentEntity parent = parentRepository.findById(saved.getParentId()).orElse(null);
        return toDto(saved, student, parent);
    }

    @Transactional(readOnly = true)
    public List<LeaveRequestDto> getAllLeaveRequestsForTeacher(String status) {
        List<LeaveRequestEntity> entities = (StringUtils.hasText(status) && !"ALL".equalsIgnoreCase(status.trim()))
                ? leaveRequestRepository.findByStatusIgnoreCaseOrderByCreatedAtDesc(status.trim())
                : leaveRequestRepository.findAllByOrderByCreatedAtDesc();
        return mapEntitiesToDtos(entities);
    }

    @Transactional
    public LeaveRequestDto reviewLeaveRequest(Long leaveRequestId, String teacherName, ReviewLeaveRequestDto reviewDto) {
        LeaveRequestEntity entity = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn xin nghỉ phép #" + leaveRequestId));

        // RULE-005: Terminal statuses are immutable
        if (!"PENDING".equalsIgnoreCase(entity.getStatus())) {
            throw new BusinessConflictException(
                    "INVALID_STATUS_TRANSITION",
                    "Đơn xin nghỉ này đã được xử lý hoặc đã hủy trước đó, không thể duyệt lại."
            );
        }

        String decision = reviewDto.status() != null ? reviewDto.status().trim().toUpperCase() : "";
        if (!VALID_REVIEW_DECISIONS.contains(decision)) {
            throw new BadRequestException("VALIDATION_ERROR", "Trạng thái duyệt chỉ chấp nhận APPROVED hoặc REJECTED.");
        }

        String rawNote = reviewDto.teacherNote() != null ? sanitizeText(reviewDto.teacherNote()) : "";

        // RULE-008: Mandatory rejection reason (>= 5 chars) when REJECTED; default polite note when APPROVED
        if ("REJECTED".equals(decision)) {
            if (rawNote.length() < 5) {
                throw new BadRequestException(
                        "VALIDATION_ERROR",
                        "Vui lòng nhập lý do từ chối đơn tối thiểu 5 ký tự để phụ huynh nắm rõ thông tin."
                );
            }
            entity.setTeacherNote(rawNote);
        } else {
            entity.setTeacherNote(StringUtils.hasText(rawNote) ? rawNote : DEFAULT_APPROVAL_NOTE);
        }

        OffsetDateTime now = OffsetDateTime.now(clock);
        entity.setStatus(decision);
        entity.setReviewedBy(StringUtils.hasText(teacherName) ? teacherName : "Cô Lê Thanh Thảo");
        entity.setReviewedAt(now);
        entity.setUpdatedAt(now);

        LeaveRequestEntity saved = leaveRequestRepository.save(entity);
        StudentEntity student = studentRepository.findById(saved.getStudentId()).orElse(null);
        ParentEntity parent = parentRepository.findById(saved.getParentId()).orElse(null);
        return toDto(saved, student, parent);
    }

    public static boolean sessionsOverlap(String existingSession, String requestedSession) {
        if ("FULL_DAY".equalsIgnoreCase(existingSession) || "FULL_DAY".equalsIgnoreCase(requestedSession)) {
            return true;
        }
        return existingSession != null && existingSession.equalsIgnoreCase(requestedSession);
    }

    private String sanitizeText(String input) {
        if (input == null) {
            return "";
        }
        String trimmed = input.trim();
        return HtmlUtils.htmlEscape(trimmed);
    }

    private String generateUniqueRequestCode(LocalDate today) {
        String datePart = today.format(CODE_DATE_FMT);
        int randomSuffix = ThreadLocalRandom.current().nextInt(1000, 10000);
        return String.format("LR-%s-%04d", datePart, randomSuffix);
    }

    private List<LeaveRequestDto> mapEntitiesToDtos(List<LeaveRequestEntity> entities) {
        if (entities.isEmpty()) {
            return List.of();
        }
        Map<Long, StudentEntity> studentMap = studentRepository.findAllById(
                entities.stream().map(LeaveRequestEntity::getStudentId).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(StudentEntity::getId, Function.identity()));

        Map<Long, ParentEntity> parentMap = parentRepository.findAllById(
                entities.stream().map(LeaveRequestEntity::getParentId).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(ParentEntity::getId, Function.identity()));

        return entities.stream()
                .map(e -> toDto(e, studentMap.get(e.getStudentId()), parentMap.get(e.getParentId())))
                .toList();
    }

    private LeaveRequestDto toDto(LeaveRequestEntity entity, StudentEntity student, ParentEntity parent) {
        return new LeaveRequestDto(
                entity.getId(),
                entity.getRequestCode(),
                entity.getStudentId(),
                student != null ? student.getFullName() : "Học sinh #" + entity.getStudentId(),
                student != null ? student.getClassName() : "",
                parent != null ? parent.getFullName() : "Phụ huynh #" + entity.getParentId(),
                parent != null ? parent.getPhone() : "",
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getSessionType(),
                HtmlUtils.htmlUnescape(entity.getReason()),
                entity.getStatus(),
                entity.getTeacherNote() != null ? HtmlUtils.htmlUnescape(entity.getTeacherNote()) : null,
                entity.getReviewedBy(),
                entity.getReviewedAt(),
                entity.getCreatedAt()
        );
    }
}

