package com.schoolconnect.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        String errorCode,
        Map<String, String> fieldErrors,
        OffsetDateTime timestamp
) {
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "OK", data, null, null, OffsetDateTime.now(DEFAULT_ZONE));
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, null, null, OffsetDateTime.now(DEFAULT_ZONE));
    }

    public static <T> ApiResponse<T> error(String errorCode, String message) {
        return new ApiResponse<>(false, message, null, errorCode, null, OffsetDateTime.now(DEFAULT_ZONE));
    }

    public static <T> ApiResponse<T> validationError(String message, Map<String, String> fieldErrors) {
        return new ApiResponse<>(false, message, null, "VALIDATION_ERROR", fieldErrors, OffsetDateTime.now(DEFAULT_ZONE));
    }
}

