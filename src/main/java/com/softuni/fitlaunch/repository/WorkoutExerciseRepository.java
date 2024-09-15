package com.softuni.fitlaunch.repository;


import com.softuni.fitlaunch.model.entity.WorkoutExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExerciseEntity, Long> {


    Optional<WorkoutExerciseEntity> findByIdAndWorkoutId(Long exerciseId, Long workoutId);
}
