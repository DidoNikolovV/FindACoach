package com.softuni.fitlaunch.repository;

import com.softuni.fitlaunch.model.entity.MealPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MealPlanRepository extends JpaRepository<MealPlanEntity, Long> {
}
