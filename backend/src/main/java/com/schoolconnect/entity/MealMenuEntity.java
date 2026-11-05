package com.schoolconnect.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "meal_menus")
public class MealMenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_date", nullable = false)
    private LocalDate menuDate;

    @Column(name = "age_group", nullable = false, length = 32)
    private String ageGroup;

    @Column(name = "meal_type", nullable = false, length = 24)
    private String mealType;

    @Column(name = "dishes_json", nullable = false, columnDefinition = "TEXT")
    private String dishesJson;

    @Column(name = "calories_kcal")
    private Integer caloriesKcal;

    @Column(name = "nutrition_note", length = 255)
    private String nutritionNote;

    public MealMenuEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(LocalDate menuDate) {
        this.menuDate = menuDate;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getDishesJson() {
        return dishesJson;
    }

    public void setDishesJson(String dishesJson) {
        this.dishesJson = dishesJson;
    }

    public Integer getCaloriesKcal() {
        return caloriesKcal;
    }

    public void setCaloriesKcal(Integer caloriesKcal) {
        this.caloriesKcal = caloriesKcal;
    }

    public String getNutritionNote() {
        return nutritionNote;
    }

    public void setNutritionNote(String nutritionNote) {
        this.nutritionNote = nutritionNote;
    }
}

