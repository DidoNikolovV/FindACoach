package com.softuni.fitlaunch.repository;

import com.softuni.fitlaunch.model.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUserIdAndWorkoutId(Long userId, Long workoutId);

    Optional<UserProgress> findByUserIdAndWeekId(Long userId, Long workoutId);

    List<UserProgress> findByUserUsernameAndWorkoutCompletedTrue(String clientName);

    List<UserProgress> findByUserIdAndProgramId(Long userId, Long programId);

    Optional<UserProgress> findByUserIdAndWorkoutIdAndWeekIdAndProgramId(Long userId, Long dayWorkoutId, Long weekId, Long programId);

    List<UserProgress> findByUserIdAndProgramIdAndWeekId(Long userId, Long programId, Long weekId);

    Optional<UserProgress> findByUserIdAndWorkoutIdAndWeekId(Long userId, Long workoutId, Long weekId);

    Optional<UserProgress> findByUserIdAndWorkoutIdAndWeekIdAndProgramIdAndExerciseId(Long id, Long workoutId, Long weekId, Long programId, Long exerciseId);

    Optional<UserProgress> findByUserIdAndWorkoutIdAndWeekIdAndDayName(Long userId, Long workoutId, Long weekId, String dayName);
}
