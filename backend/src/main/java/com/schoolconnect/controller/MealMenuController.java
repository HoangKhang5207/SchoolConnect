package com.schoolconnect.controller;

import com.schoolconnect.dto.common.ApiResponse;
import com.schoolconnect.dto.meal.MealMenuDto;
import com.schoolconnect.service.MealMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Preschool daily and weekly meal menus endpoint (FR-007).
 */
@RestController
@RequestMapping("/api/v1/meal-menus")
@Tag(name = "Meal Menus", description = "Preschool daily and weekly nutritional menus (Breakfast, Lunch, Afternoon Snack)")
public class MealMenuController {

    private final MealMenuService mealMenuService;

    public MealMenuController(MealMenuService mealMenuService) {
        this.mealMenuService = mealMenuService;
    }

    @GetMapping
    @Operation(summary = "Get meal menus optionally filtered by date")
    public ResponseEntity<ApiResponse<List<MealMenuDto>>> getMealMenus(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<MealMenuDto> menus = mealMenuService.getMealMenus(date);
        return ResponseEntity.ok(ApiResponse.ok(menus));
    }
}

