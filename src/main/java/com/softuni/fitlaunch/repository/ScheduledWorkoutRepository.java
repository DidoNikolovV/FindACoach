package com.softuni.fitlaunch.repository;

import com.softuni.fitlaunch.model.entity.ScheduledWorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScheduledWorkoutRepository extends JpaRepository<ScheduledWorkoutEntity, Long> {

    List<ScheduledWorkoutEntity> findAllByCoachId(Long id);

    List<ScheduledWorkoutEntity> findAllByClientId(Long id);
}
