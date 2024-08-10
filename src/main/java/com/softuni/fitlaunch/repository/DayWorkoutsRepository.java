package com.softuni.fitlaunch.repository;

import com.softuni.fitlaunch.model.entity.DayWorkoutsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DayWorkoutsRepository extends JpaRepository<DayWorkoutsEntity, Long> {
    Optional<DayWorkoutsEntity> findByNameAndWeekId(String name, Long weekId);

    Optional<DayWorkoutsEntity> findByNameAndWorkoutIdAndWeekId(String dayName, Long workoutId, Long weekId);

    Optional<DayWorkoutsEntity> findByWorkoutIdAndWeekId(Long workoutId, Long weekId);
}
