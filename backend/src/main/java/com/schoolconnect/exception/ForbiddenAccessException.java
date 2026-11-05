package com.schoolconnect.exception;

public class ForbiddenAccessException extends RuntimeException {
    private final String errorCode;

    public ForbiddenAccessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

