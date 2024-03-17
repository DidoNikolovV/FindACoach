package com.softuni.fitlaunch.repository;


import com.softuni.fitlaunch.model.entity.WorkoutExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExerciseEntity, Long> {

    List<WorkoutExerciseEntity> findAllByWorkoutId(Long workoutId);
}
