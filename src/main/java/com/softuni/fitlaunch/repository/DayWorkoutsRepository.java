package com.softuni.fitlaunch.repository;

import com.softuni.fitlaunch.model.entity.DayWorkoutsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DayWorkoutsRepository extends JpaRepository<DayWorkoutsEntity, Long> {
    Optional<DayWorkoutsEntity> findByWorkoutIdAndWeekIdAndName(Long workoutId, Long weekId, String dayName);
}
