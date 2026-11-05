package com.schoolconnect.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for HealthController and CORS configuration.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/v1/health returns HTTP 200 OK with database UP and LAYERED architecture")
    void shouldReturnHealthStatusUp() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("SchoolConnect backend is operational."))
            .andExpect(jsonPath("$.data.status").value("UP"))
            .andExpect(jsonPath("$.data.database").value("UP"))
            .andExpect(jsonPath("$.data.architecture").value("LAYERED"))
            .andExpect(jsonPath("$.data.demoModeEnabled").value(true));
    }

    @Test
    @DisplayName("OPTIONS /api/v1/health allows Zalo Mini App WebView origin https://h5.zdn.vn")
    void shouldAllowZaloWebViewCorsOrigin() throws Exception {
        mockMvc.perform(options("/api/v1/health")
                .header("Origin", "https://h5.zdn.vn")
                .header("Access-Control-Request-Method", "GET"))
            .andExpect(status().isOk())
            .andExpect(header().string("Access-Control-Allow-Origin", "https://h5.zdn.vn"));
    }
}

