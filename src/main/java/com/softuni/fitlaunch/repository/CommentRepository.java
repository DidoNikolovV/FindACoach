package com.softuni.fitlaunch.repository;

import com.softuni.fitlaunch.model.entity.CommentEntity;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Optional<List<CommentEntity>> findAllByWorkout(WorkoutEntity workout);

    Optional<List<CommentEntity>> findByProgramIdAndWeekIdAndWorkoutId(Long programId, Long weekId, Long workoutId);

}
