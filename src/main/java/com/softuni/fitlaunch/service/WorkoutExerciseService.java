package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.UserProgress;
import com.softuni.fitlaunch.model.entity.WorkoutExerciseEntity;
import com.softuni.fitlaunch.repository.WorkoutExerciseRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import lombok.Generated;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Generated
public class WorkoutExerciseService {
    private final WorkoutExerciseRepository workoutExerciseRepository;

    private final UserProgressService userProgressService;
    private final ModelMapper modelMapper;

    private final UserService userService;

    public WorkoutExerciseService(WorkoutExerciseRepository workoutExerciseRepository, UserProgressService userProgressService, ModelMapper modelMapper, UserService userService) {
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.userProgressService = userProgressService;
        this.modelMapper = modelMapper;
        this.userService = userService;
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

    public void completeExercise(Long exerciseId, Long workoutId, String username, Long weekId, Long programId) {
        WorkoutExerciseEntity exerciseToBeCompleted = workoutExerciseRepository.findByIdAndWorkoutId(exerciseId, workoutId).orElseThrow(() -> new ResourceNotFoundException("Exercise does not exist"));
        UserEntity user = userService.getUserEntityByUsername(username);
        UserProgress userProgress = userProgressService.getByUserIdAndWorkoutIdAndWeekIdAndProgramId(user.getId(), workoutId, weekId, programId);
        userProgress.setExerciseCompleted(true);
        userProgressService.saveUserProgress(userProgress);
    }
}
