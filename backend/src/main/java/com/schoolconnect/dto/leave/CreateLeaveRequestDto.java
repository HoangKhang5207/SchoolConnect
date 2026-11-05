package com.schoolconnect.dto.leave;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateLeaveRequestDto(
        @NotNull(message = "Ngày bắt đầu nghỉ không được để trống")
        LocalDate startDate,

        @NotNull(message = "Ngày kết thúc nghỉ không được để trống")
        LocalDate endDate,

        @NotBlank(message = "Buổi nghỉ không được để trống")
        String sessionType,

        @NotBlank(message = "Lý do xin nghỉ không được để trống")
        @Size(min = 5, max = 500, message = "Lý do xin nghỉ phải từ 5 đến 500 ký tự")
        String reason
) {
}

