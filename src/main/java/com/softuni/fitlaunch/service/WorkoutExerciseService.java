package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutCreationDTO;
import com.softuni.fitlaunch.model.entity.ExerciseEntity;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.entity.WorkoutExerciseEntity;
import com.softuni.fitlaunch.repository.WorkoutExerciseRepository;
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

    public List<WorkoutExerciseEntity> createWorkoutExercises(WorkoutCreationDTO workoutCreationDTO, WorkoutEntity workout) {
        List<ExerciseEntity> exercises = workoutCreationDTO.getSelectedExerciseIds().stream().map(exerciseService::getById).toList();
        List<WorkoutExerciseEntity> workoutExercises = exercises.stream().map(exerciseEntity -> modelMapper.map(exerciseEntity, WorkoutExerciseEntity.class)).toList();
        for (WorkoutExerciseEntity workoutExercise : workoutExercises) {
            workoutExercise.setWorkout(workout);
            int exerciseSets = workoutCreationDTO.getSets().get((int) (workoutExercise.getId() - 1));
            int exerciseReps = workoutCreationDTO.getReps().get((int) (workoutExercise.getId() - 1));
            workoutExercise.setSets(exerciseSets);
            workoutExercise.setReps(exerciseReps);
        }


        return workoutExercises;
    }

    public List<WorkoutExerciseDTO> getAllByWorkoutId(Long workoutId) {
        return workoutExerciseRepository.findAllByWorkoutId(workoutId).stream().map(exerciseEntity -> modelMapper.map(exerciseEntity, WorkoutExerciseDTO.class)).toList();
    }

}
