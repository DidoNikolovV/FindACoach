package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.entity.WorkoutExerciseEntity;
import com.softuni.fitlaunch.repository.ExerciseRepository;
import com.softuni.fitlaunch.repository.WorkoutExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutExerciseService {

    private final WorkoutExerciseRepository workoutExerciseRepository;

    private final ExerciseRepository exerciseRepository;


    public WorkoutExerciseService(WorkoutExerciseRepository workoutExerciseRepository, ExerciseRepository exerciseRepository) {
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public List<WorkoutExerciseEntity> getAllWorkoutExercisesByWorkoutId(Long id) {
        List<WorkoutExerciseEntity> exercises = workoutExerciseRepository.findByWorkoutId(id).stream().toList();
//        for (WorkoutExerciseEntity exercise : exercises) {
//            exercise.setVideoUrl(exerciseRepository.findById(exercise.getId()).get().getVideoUrl());
//        }

        return exercises;
    }


    public void saveWorkoutExercise(WorkoutExerciseEntity workoutExercise) {
        workoutExerciseRepository.save(workoutExercise);
    }

    public List<WorkoutExerciseEntity> getWorkoutDetails(Long id) {
        return workoutExerciseRepository.findByWorkoutId(id);
    }


}
