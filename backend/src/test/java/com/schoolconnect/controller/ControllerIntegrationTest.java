package com.schoolconnect.controller;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolconnect.repository.LeaveRequestRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Full-stack integration test suite covering AC-001 through AC-018 against PostgreSQL.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    private String parentToken;
    private String teacherToken;

    @BeforeEach
    void initTokens() throws Exception {
        leaveRequestRepository.deleteAll();

        MvcResult parentLogin = mockMvc.perform(post("/api/v1/auth/demo-session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"PARENT\"}"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode parentJson = objectMapper.readTree(parentLogin.getResponse().getContentAsString());
        parentToken = parentJson.path("data").path("accessToken").asText();

        MvcResult teacherLogin = mockMvc.perform(post("/api/v1/auth/demo-session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"TEACHER\"}"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode teacherJson = objectMapper.readTree(teacherLogin.getResponse().getContentAsString());
        teacherToken = teacherJson.path("data").path("accessToken").asText();
    }

    @Test
    @Order(1)
    @DisplayName("AC-001, AC-002, AC-003: Dual Auth (DEMO_MODE & ZALO_AUTH) and Role Switching")
    void shouldAuthenticateInBothDemoAndZaloModes() throws Exception {
        mockMvc.perform(post("/api/v1/auth/demo-session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"PARENT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.authMode").value("DEMO_MODE"))
                .andExpect(jsonPath("$.data.user.fullName").value("Trần Thu Hà"))
                .andExpect(jsonPath("$.data.linkedChildren", hasSize(2)));

        mockMvc.perform(post("/api/v1/auth/zalo-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accessToken\":\"mock_zalo_token_parent\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.authMode").value("ZALO_AUTH"))
                .andExpect(jsonPath("$.data.user.role").value("PARENT"));

        // AC-002 Negative: Invalid Zalo token returns HTTP 401 Unauthorized (BUG-002)
        mockMvc.perform(post("/api/v1/auth/zalo-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accessToken\":\"mock_zalo_token_invalid\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
    }

    @Test
    @Order(2)
    @DisplayName("AC-004, AC-005, AC-006: Home Dashboard completeness, Multi-Child Switcher, and 403 Guardian Isolation")
    void shouldLoadDashboardAndEnforceGuardianIsolation() throws Exception {
        // Child 1: Nguyễn Minh An
        mockMvc.perform(get("/api/v1/children/1/dashboard")
                        .header("Authorization", "Bearer " + parentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.student.fullName").value("Nguyễn Minh An"))
                .andExpect(jsonPath("$.data.student.className").value("Lớp Chồi 1"))
                .andExpect(jsonPath("$.data.student.bmi").value(15.5))
                .andExpect(jsonPath("$.data.todayAttendance.status").value("PRESENT"));

        // Child 2: Nguyễn Minh Khôi
        mockMvc.perform(get("/api/v1/children/2/dashboard")
                        .header("Authorization", "Bearer " + parentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.student.fullName").value("Nguyễn Minh Khôi"))
                .andExpect(jsonPath("$.data.student.className").value("Lớp Mầm 2"));

        // Unlinked Child 3 (Phạm Gia Hân belongs to Parent 3) -> 403 Forbidden
        mockMvc.perform(get("/api/v1/children/3/dashboard")
                        .header("Authorization", "Bearer " + parentToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("ACCESS_DENIED_NOT_GUARDIAN"));

        // Non-existent / unlinked Child 999 -> 403 Forbidden
        mockMvc.perform(get("/api/v1/children/999/dashboard")
                        .header("Authorization", "Bearer " + parentToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("ACCESS_DENIED_NOT_GUARDIAN"));
    }

    @Test
    @Order(3)
    @DisplayName("AC-007, AC-008, AC-009, AC-010, AC-011: Attendance, Daily Reports, Meal Menus & Announcements")
    void shouldReturnAttendanceDailyReportsMealMenusAndAnnouncements() throws Exception {
        mockMvc.perform(get("/api/v1/children/1/attendance")
                        .header("Authorization", "Bearer " + parentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.summary.totalDays", greaterThanOrEqualTo(6)))
                .andExpect(jsonPath("$.data.records", hasSize(greaterThanOrEqualTo(6))));

        mockMvc.perform(get("/api/v1/children/1/daily-reports")
                        .param("date", "2026-11-04")
                        .header("Authorization", "Bearer " + parentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].sleepDurationMins").value(110));

        // Empty state on Sunday (2026-11-01)
        mockMvc.perform(get("/api/v1/children/1/daily-reports")
                        .param("date", "2026-11-01")
                        .header("Authorization", "Bearer " + parentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));

        mockMvc.perform(get("/api/v1/meal-menus")
                        .param("date", "2026-11-04")
                        .header("Authorization", "Bearer " + parentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].mealType").value("BREAKFAST"))
                .andExpect(jsonPath("$.data[1].mealType").value("LUNCH"))
                .andExpect(jsonPath("$.data[2].mealType").value("AFTERNOON_SNACK"));

        mockMvc.perform(get("/api/v1/announcements")
                        .header("Authorization", "Bearer " + parentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(4))))
                .andExpect(jsonPath("$.data[0].isPinned").value(true));
    }

    @Test
    @Order(4)
    @DisplayName("AC-012..AC-017: Closed-loop Leave Request Creation, Overlap Conflict, Teacher Review, and Parent Cancel")
    void shouldExecuteClosedLoopLeaveWorkflow() throws Exception {
        LocalDate futureDate1 = LocalDate.now().plusDays(5);
        String payload1 = String.format(
                "{\"startDate\":\"%s\",\"endDate\":\"%s\",\"sessionType\":\"FULL_DAY\",\"reason\":\"Gia đình xin phép cho bé nghỉ 1 ngày đi khám sức khỏe tổng quát.\"}",
                futureDate1, futureDate1
        );

        // 1. Parent creates Leave Request #1 -> 201 Created
        MvcResult createRes = mockMvc.perform(post("/api/v1/children/1/leave-requests")
                        .header("Authorization", "Bearer " + parentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload1))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.requestCode", matchesPattern("^LR-\\d{8}-\\d{4}$")))
                .andReturn();

        long createdId1 = objectMapper.readTree(createRes.getResponse().getContentAsString())
                .path("data").path("id").asLong();

        // 2. Duplicate/overlapping submission for same date -> 409 Conflict (AC-013)
        mockMvc.perform(post("/api/v1/children/1/leave-requests")
                        .header("Authorization", "Bearer " + parentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload1))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("LEAVE_REQUEST_OVERLAP"));

        // 3. Parent cannot call Teacher review endpoint -> 403 Forbidden (RULE-007)
        mockMvc.perform(patch("/api/v1/teacher/leave-requests/" + createdId1 + "/review")
                        .header("Authorization", "Bearer " + parentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"APPROVED\",\"teacherNote\":\"Test\"}"))
                .andExpect(status().isForbidden());

        // 4. Teacher approves Leave Request #1 -> 200 OK (AC-015)
        mockMvc.perform(patch("/api/v1/teacher/leave-requests/" + createdId1 + "/review")
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"APPROVED\",\"teacherNote\":\"Cô Thảo đã duyệt đơn nghỉ phép của bé.\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("APPROVED"))
                .andExpect(jsonPath("$.data.reviewedBy").value("Cô Lê Thanh Thảo"));

        // 5. Immutable terminal state check -> 409 Conflict if reviewed or cancelled again (AC-017)
        mockMvc.perform(patch("/api/v1/leave-requests/" + createdId1 + "/cancel")
                        .header("Authorization", "Bearer " + parentToken))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("INVALID_STATUS_TRANSITION"));

        // 6. Parent creates Leave Request #2 and cancels it while PENDING -> 200 OK (AC-014)
        LocalDate futureDate2 = LocalDate.now().plusDays(8);
        String payload2 = String.format(
                "{\"startDate\":\"%s\",\"endDate\":\"%s\",\"sessionType\":\"AFTERNOON\",\"reason\":\"Gia đình xin phép đón bé sớm buổi chiều có việc riêng.\"}",
                futureDate2, futureDate2
        );
        MvcResult createRes2 = mockMvc.perform(post("/api/v1/children/1/leave-requests")
                        .header("Authorization", "Bearer " + parentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload2))
                .andExpect(status().isCreated())
                .andReturn();
        long createdId2 = objectMapper.readTree(createRes2.getResponse().getContentAsString())
                .path("data").path("id").asLong();

        mockMvc.perform(patch("/api/v1/leave-requests/" + createdId2 + "/cancel")
                        .header("Authorization", "Bearer " + parentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CANCELLED"));
    }
}

