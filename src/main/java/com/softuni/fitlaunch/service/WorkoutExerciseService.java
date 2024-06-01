package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutCreationDTO;
import com.softuni.fitlaunch.model.entity.ExerciseEntity;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.entity.WorkoutExerciseEntity;
import com.softuni.fitlaunch.repository.WorkoutExerciseRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutExerciseService {
    private final WorkoutExerciseRepository workoutExerciseRepository;

    private final ExerciseService exerciseService;

    private final ModelMapper modelMapper;

    public WorkoutExerciseService(WorkoutExerciseRepository workoutExerciseRepository, ExerciseService exerciseService, ModelMapper modelMapper) {
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.exerciseService = exerciseService;
        this.modelMapper = modelMapper;
    }

    public List<WorkoutExerciseDTO> getAllExercises() {
        return workoutExerciseRepository.findAll()
                .stream()
                .map(exercise -> modelMapper.map(exercise, WorkoutExerciseDTO.class))
                .toList();
    }

    public List<WorkoutExerciseEntity> saveAll(List<WorkoutExerciseEntity> exercises) {
        return workoutExerciseRepository.saveAll(exercises);
    }

//    public void createWorkoutExercises(WorkoutCreationDTO workoutCreationDTO, WorkoutEntity workout) {
//        List<ExerciseEntity> exercises = workoutCreationDTO.getSelectedExerciseIds().stream().map(exerciseService::getById).toList();
//        List<WorkoutExerciseEntity> workoutExercises = exercises.stream().map(exerciseEntity -> modelMapper.map(exerciseEntity, WorkoutExerciseEntity.class)).toList();
//        for (WorkoutExerciseEntity workoutExercise : workoutExercises) {
//            workoutExercise.setWorkout(workout);
//            int exerciseSets = workoutCreationDTO.getSets().get((int) (workoutExercise.getId() - 1));
//            int exerciseReps = workoutCreationDTO.getReps().get((int) (workoutExercise.getId() - 1));
//            workoutExercise.setSets(exerciseSets);
//            workoutExercise.setReps(exerciseReps);
//        }
//
//        workoutExerciseRepository.saveAll(workoutExercises);
//
//    }

    public void completeExercise(Long exerciseId, Long workoutId) {
        WorkoutExerciseEntity exerciseToBeCompleted = workoutExerciseRepository.findByIdAndWorkoutId(exerciseId, workoutId).orElseThrow(() -> new ResourceNotFoundException("Exercise does not exist"));
        exerciseToBeCompleted.setCompleted(true);
        workoutExerciseRepository.save(exerciseToBeCompleted);
    }
}
