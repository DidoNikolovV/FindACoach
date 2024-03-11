package com.softuni.fitlaunch.repository;


import com.softuni.fitlaunch.model.entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {

    List<ExerciseEntity> findAllByWorkoutId(Long workoutId);
}
