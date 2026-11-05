package com.schoolconnect.security;

public record UserPrincipal(
        Long id,
        String fullName,
        String phone,
        String role
) {
    public boolean isTeacher() {
        return "TEACHER".equalsIgnoreCase(role);
    }

    public boolean isParent() {
        return "PARENT".equalsIgnoreCase(role);
    }
}

