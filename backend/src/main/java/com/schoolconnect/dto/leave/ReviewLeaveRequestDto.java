package com.schoolconnect.dto.leave;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewLeaveRequestDto(
        @NotBlank(message = "Trạng thái duyệt không được để trống")
        String status,

        @Size(max = 500, message = "Ghi chú giáo viên không được vượt quá 500 ký tự")
        String teacherNote
) {
}

