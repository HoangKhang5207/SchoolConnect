package com.schoolconnect.controller;

import com.schoolconnect.dto.auth.AuthResponseDto;
import com.schoolconnect.dto.auth.DemoLoginRequest;
import com.schoolconnect.dto.auth.ZaloLoginRequest;
import com.schoolconnect.dto.common.ApiResponse;
import com.schoolconnect.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication endpoints supporting both DEMO_MODE and ZALO_AUTH (FR-001, FR-002).
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Dual authentication (DEMO_MODE & ZALO_AUTH) and role switching")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/demo-session")
    @Operation(summary = "Initialize stateless JWT session for Demo Parent or Demo Teacher")
    public ResponseEntity<ApiResponse<AuthResponseDto>> createDemoSession(
            @Valid @RequestBody DemoLoginRequest request
    ) {
        AuthResponseDto response = authService.createDemoSession(request.role());
        return ResponseEntity.ok(ApiResponse.ok("Demo session initialized", response));
    }

    @PostMapping("/zalo-login")
    @Operation(summary = "Verify Zalo Mini App Access Token and issue internal JWT")
    public ResponseEntity<ApiResponse<AuthResponseDto>> zaloLogin(
            @Valid @RequestBody ZaloLoginRequest request
    ) {
        AuthResponseDto response = authService.authenticateWithZalo(request.accessToken());
        return ResponseEntity.ok(ApiResponse.ok("Zalo authentication successful", response));
    }
}

