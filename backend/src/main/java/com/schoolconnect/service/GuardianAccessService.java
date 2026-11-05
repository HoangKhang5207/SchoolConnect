package com.schoolconnect.service;

import com.schoolconnect.dto.student.StudentSummaryDto;
import com.schoolconnect.entity.StudentEntity;
import com.schoolconnect.exception.ForbiddenAccessException;
import com.schoolconnect.exception.ResourceNotFoundException;
import com.schoolconnect.repository.ParentStudentRepository;
import com.schoolconnect.repository.StudentRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Enforces RULE-001 (Guardian-Student Data Isolation).
 */
@Service
public class GuardianAccessService {

    private final ParentStudentRepository parentStudentRepository;
    private final StudentRepository studentRepository;

    public GuardianAccessService(
            ParentStudentRepository parentStudentRepository,
            StudentRepository studentRepository
    ) {
        this.parentStudentRepository = parentStudentRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public StudentEntity verifyParentOwnsStudent(Long parentId, Long studentId) {
        boolean isGuardian = parentStudentRepository.existsByParentIdAndStudentId(parentId, studentId);
        if (!isGuardian) {
            throw new ForbiddenAccessException(
                    "ACCESS_DENIED_NOT_GUARDIAN",
                    "Bạn không có quyền truy cập dữ liệu của học sinh này."
            );
        }
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ học sinh."));
    }

    @Transactional(readOnly = true)
    public List<StudentSummaryDto> getLinkedChildren(Long parentId) {
        return studentRepository.findActiveStudentsByParentId(parentId)
                .stream()
                .map(s -> new StudentSummaryDto(
                        s.getId(),
                        s.getStudentCode(),
                        s.getFullName(),
                        s.getClassName(),
                        s.getTeacherName(),
                        s.getGender(),
                        s.getDateOfBirth(),
                        s.getAvatarUrl()
                ))
                .toList();
    }
}

