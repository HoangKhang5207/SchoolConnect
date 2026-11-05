package com.schoolconnect.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record DemoLoginRequest(
        @NotBlank(message = "Vai trò đăng nhập không được để trống")
        String role
) {
}

