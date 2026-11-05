package com.schoolconnect.dto.meal;

import java.time.LocalDate;
import java.util.List;

public record MealMenuDto(
        Long id,
        LocalDate menuDate,
        String ageGroup,
        String mealType,
        List<String> dishes,
        Integer caloriesKcal,
        String nutritionNote
) {
}

