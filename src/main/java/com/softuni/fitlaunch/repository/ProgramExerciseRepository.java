package com.softuni.fitlaunch.repository;

import com.softuni.fitlaunch.model.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramExerciseRepository extends JpaRepository<WorkoutExercise, Long> {
    Optional<WorkoutExercise> findByExerciseIdAndWorkoutId(Long exerciseId, Long workoutId);
}
