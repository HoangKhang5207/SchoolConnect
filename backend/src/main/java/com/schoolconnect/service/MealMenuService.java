package com.schoolconnect.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolconnect.dto.meal.MealMenuDto;
import com.schoolconnect.entity.MealMenuEntity;
import com.schoolconnect.repository.MealMenuRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides daily and weekly preschool nutritional menus ordered by BREAKFAST -> LUNCH -> AFTERNOON_SNACK (FR-007, RULE-012).
 */
@Service
public class MealMenuService {

    private final MealMenuRepository mealMenuRepository;
    private final ObjectMapper objectMapper;

    public MealMenuService(MealMenuRepository mealMenuRepository, ObjectMapper objectMapper) {
        this.mealMenuRepository = mealMenuRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public List<MealMenuDto> getMealMenus(LocalDate date) {
        List<MealMenuEntity> entities = (date != null)
                ? mealMenuRepository.findByMenuDateOrderByIdAsc(date)
                : mealMenuRepository.findAllByOrderByMenuDateDescIdAsc();

        return entities.stream()
                .sorted(Comparator
                        .comparing(MealMenuEntity::getMenuDate).reversed()
                        .thenComparingInt(e -> mealTypeOrder(e.getMealType())))
                .map(this::toDto)
                .toList();
    }

    private int mealTypeOrder(String mealType) {
        if ("BREAKFAST".equalsIgnoreCase(mealType)) {
            return 1;
        }
        if ("LUNCH".equalsIgnoreCase(mealType)) {
            return 2;
        }
        if ("AFTERNOON_SNACK".equalsIgnoreCase(mealType)) {
            return 3;
        }
        return 99;
    }

    private MealMenuDto toDto(MealMenuEntity entity) {
        List<String> dishes;
        try {
            dishes = objectMapper.readValue(entity.getDishesJson(), new TypeReference<List<String>>() {});
        } catch (Exception ex) {
            dishes = List.of(entity.getDishesJson());
        }
        return new MealMenuDto(
                entity.getId(),
                entity.getMenuDate(),
                entity.getAgeGroup(),
                entity.getMealType(),
                dishes,
                entity.getCaloriesKcal(),
                entity.getNutritionNote()
        );
    }
}

