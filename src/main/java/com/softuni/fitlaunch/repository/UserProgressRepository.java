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

}
