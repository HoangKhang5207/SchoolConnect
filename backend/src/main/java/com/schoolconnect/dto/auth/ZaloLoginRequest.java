package com.schoolconnect.dto.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;

public record ZaloLoginRequest(
                @NotBlank(message = "Zalo Access Token không được để trống") @JsonAlias({
                                "zaloAccessToken" }) String accessToken) {
}