package com.softuni.fitlaunch.repository;

import com.softuni.fitlaunch.model.entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {
}
