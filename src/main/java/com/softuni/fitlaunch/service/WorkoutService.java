package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.ExerciseDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekWorkoutDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWorkoutExerciseDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDetailsDTO;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.entity.WorkoutExerciseEntity;
import com.softuni.fitlaunch.model.enums.LevelEnum;
import com.softuni.fitlaunch.repository.ExerciseRepository;
import com.softuni.fitlaunch.repository.ProgramWeekWorkoutRepository;
import com.softuni.fitlaunch.repository.UserRepository;
import com.softuni.fitlaunch.repository.WorkoutExerciseRepository;
import com.softuni.fitlaunch.repository.WorkoutRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutService {
    private static final String BASE_IMAGES_PATH = "/images/";
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    private final ProgramWeekWorkoutRepository programWeekWorkoutRepository;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public WorkoutService(WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository, WorkoutExerciseRepository workoutExerciseRepository, ProgramWeekWorkoutRepository programWeekWorkoutRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.programWeekWorkoutRepository = programWeekWorkoutRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public Long createWorkout(WorkoutEntity workout) {

        WorkoutEntity newWorkout = workoutRepository.save(workout);
        return newWorkout.getId();

    }

    public Page<WorkoutDTO> getAllWorkouts(Pageable pageable) {
        return workoutRepository
                .findAll(pageable)
                .map(entity -> modelMapper.map(entity, WorkoutDTO.class));
    }

    public List<ExerciseDTO> getAllExercises() {
        return exerciseRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, ExerciseDTO.class))
                .toList();
    }

    public List<LevelEnum> getAllLevels() {
        return Arrays.stream(LevelEnum.values()).collect(Collectors.toList());
    }

//    public Optional<WorkoutDetailsDTO> getWorkoutDetails(Long workoutId) {
//        Optional<WorkoutDetailsDTO> workoutDetailsDTO = workoutRepository.findById(workoutId)
//                .map(this::mapAsDetails);
//
//        return workoutDetailsDTO;
//    }


    private WorkoutDetailsDTO mapAsDetails(ProgramWeekWorkoutDTO programWeekWorkoutDTO) {
        List<ProgramWorkoutExerciseDTO> exercisesDTO = programWeekWorkoutDTO.getExercises();
        return new WorkoutDetailsDTO(
                programWeekWorkoutDTO.getId(),
                programWeekWorkoutDTO.getName(),
                programWeekWorkoutDTO.getLevel(),
                programWeekWorkoutDTO.getDescription(),
                exercisesDTO,
                programWeekWorkoutDTO.getLikes(),
                programWeekWorkoutDTO.isHasStarted(),
                programWeekWorkoutDTO.isCompleted()
        );
    }

//    public void like(String username, Long workoutId) {
//        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
//        WorkoutEntity workoutEntity = workoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Workout not found"));
//
//        workoutEntity.getUsersLiked().add(userEntity);
//
//        workoutRepository.save(workoutEntity);
//    }
//
//    public void dislike(String username, Long workoutId) {
//        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
//        WorkoutEntity workoutEntity = workoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Workout not found"));
//
//        workoutEntity.getUsersLiked().remove(userEntity);
//
//        workoutRepository.save(workoutEntity);
//    }

//    public void startWorkout(Long workoutId, String username) {
//        WorkoutEntity workoutEntity = workoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Workout not found"));
//        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
//
//        workoutEntity.setHasStarted(true);
//        userEntity.getWorkoutsStarted().add(workoutEntity);
//
//        workoutRepository.save(workoutEntity);
//    }


//    @Transactional
//    public void completeWorkout(Long workoutId, String username) {
//        WorkoutEntity workoutEntity = workoutRepository.findById(workoutId).orElseThrow(() -> new RuntimeException("Workout not found"));
//        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
//
//
//        workoutEntity.setCompleted(true);
//        workoutEntity.setDateCompleted(String.valueOf(LocalDate.now()));
//        workoutEntity.setLikes(workoutEntity.getUsersLiked().size());
//        userEntity.getWorkoutsCompleted().add(workoutEntity);
//
//        workoutRepository.save(workoutEntity);
//        userRepository.save(userEntity);
//    }


    public void completeExercise(Long workoutId, Long exerciseId) {
        List<WorkoutExerciseEntity> workoutExercises = workoutExerciseRepository.findByWorkoutId(workoutId);
        for (WorkoutExerciseEntity workoutExercise : workoutExercises) {
            if(workoutExercise.getId().equals(exerciseId)) {
                workoutExercise.setCompleted(true);
                workoutExerciseRepository.save(workoutExercise);
                break;
            }
        }

    }
}
