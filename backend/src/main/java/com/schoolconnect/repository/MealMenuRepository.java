package com.schoolconnect.repository;

import com.schoolconnect.entity.MealMenuEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealMenuRepository extends JpaRepository<MealMenuEntity, Long> {
    List<MealMenuEntity> findByMenuDateOrderByIdAsc(LocalDate menuDate);
    List<MealMenuEntity> findAllByOrderByMenuDateDescIdAsc();
}

