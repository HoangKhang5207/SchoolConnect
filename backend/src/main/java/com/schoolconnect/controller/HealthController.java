package com.schoolconnect.controller;

import com.schoolconnect.dto.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Clock;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * System health check controller verifying application readiness and PostgreSQL database connectivity.
 */
@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health", description = "System readiness and database connectivity diagnostics")
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    private final JdbcTemplate jdbcTemplate;
    private final Clock clock;
    private final boolean demoModeEnabled;

    public HealthController(
            JdbcTemplate jdbcTemplate,
            Clock clock,
            @Value("${app.auth.demo-mode:true}") boolean demoModeEnabled) {
        this.jdbcTemplate = jdbcTemplate;
        this.clock = clock;
        this.demoModeEnabled = demoModeEnabled;
    }

    @GetMapping
    @Operation(summary = "Check API and PostgreSQL database health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkHealth() {
        String dbStatus = "DOWN";
        try {
            Integer probe = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            if (probe != null && probe == 1) {
                dbStatus = "UP";
            }
        } catch (Exception ex) {
            log.error("Database connectivity probe failed: {}", ex.getMessage());
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("status", "UP".equals(dbStatus) ? "UP" : "DEGRADED");
        payload.put("service", "schoolconnect-backend");
        payload.put("architecture", "LAYERED");
        payload.put("database", dbStatus);
        payload.put("demoModeEnabled", demoModeEnabled);
        payload.put("serverDate", LocalDate.now(clock).toString());

        return ResponseEntity.ok(ApiResponse.ok("SchoolConnect backend is operational.", payload));
    }
}

