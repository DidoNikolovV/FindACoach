package com.softuni.fitlaunch.repository;


import com.softuni.fitlaunch.model.entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<MealEntity, Long> {
}
