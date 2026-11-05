package com.schoolconnect.dto.auth;

import com.schoolconnect.dto.student.StudentSummaryDto;
import java.util.List;

public record AuthResponseDto(
        String accessToken,
        String authMode,
        UserProfileDto user,
        List<StudentSummaryDto> linkedChildren
) {
    public record UserProfileDto(
            Long id,
            String fullName,
            String phone,
            String role,
            String avatarUrl
    ) {
    }
}

