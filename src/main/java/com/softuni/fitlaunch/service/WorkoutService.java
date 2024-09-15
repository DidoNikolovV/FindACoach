package com.softuni.fitlaunch.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softuni.fitlaunch.model.dto.week.DayWorkoutsDTO;
import com.softuni.fitlaunch.model.dto.workout.ClientWorkoutDetails;
import com.softuni.fitlaunch.model.dto.workout.WorkoutAddDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutCreationDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDetailsDTO;
import com.softuni.fitlaunch.model.entity.CoachEntity;
import com.softuni.fitlaunch.model.entity.DayWorkoutsEntity;
import com.softuni.fitlaunch.model.entity.ExerciseEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.UserProgress;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.entity.WorkoutExercise;
import com.softuni.fitlaunch.model.entity.WorkoutExerciseEntity;
import com.softuni.fitlaunch.model.enums.LevelEnum;
import com.softuni.fitlaunch.repository.DayWorkoutsRepository;
import com.softuni.fitlaunch.repository.ProgramExerciseRepository;
import com.softuni.fitlaunch.repository.UserProgressRepository;
import com.softuni.fitlaunch.repository.WorkoutRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    @Value("${app.images.path}")
    private String BASE_IMAGES_PATH;

    private final UserProgressRepository userProgressRepository;
    private final WorkoutRepository workoutRepository;

    private final WorkoutExerciseService workoutExerciseService;
    private final ExerciseService exerciseService;

    private final CoachService coachService;

    private final ModelMapper modelMapper;

    private final DayWorkoutsRepository dayWorkoutsRepository;

    private final UserService userService;
    private final WeekService weekService;

    private final FileUpload fileUpload;

    private final ProgramExerciseRepository programExerciseRepository;


    public WorkoutService(UserProgressRepository userProgressRepository, WorkoutRepository workoutRepository, WorkoutExerciseService workoutExerciseService, ExerciseService exerciseService, CoachService coachService, ModelMapper modelMapper, DayWorkoutsRepository dayWorkoutsRepository, UserService userService, WeekService weekService, FileUpload fileUpload, ProgramExerciseRepository programExerciseRepository) {
        this.userProgressRepository = userProgressRepository;
        this.workoutRepository = workoutRepository;
        this.workoutExerciseService = workoutExerciseService;
        this.exerciseService = exerciseService;
        this.coachService = coachService;
        this.modelMapper = modelMapper;
        this.dayWorkoutsRepository = dayWorkoutsRepository;
        this.userService = userService;
        this.weekService = weekService;
        this.fileUpload = fileUpload;
        this.programExerciseRepository = programExerciseRepository;
    }

    public WorkoutDTO createWorkout(WorkoutCreationDTO workoutCreationDTO, String authorUsername) {
        WorkoutEntity workout = modelMapper.map(workoutCreationDTO, WorkoutEntity.class);
        CoachEntity author = coachService.getCoachEntityByUsername(authorUsername);
        workout.setAuthor(author);
        workout = workoutRepository.save(workout);

        return modelMapper.map(workout, WorkoutDTO.class);
    }

    public WorkoutDTO createWorkout(String name, String level, MultipartFile imgUrl, String authorUsername, List<String> json) {
        WorkoutCreationDTO workoutCreationDTO = new WorkoutCreationDTO(name, level, imgUrl);
        String picture = fileUpload.uploadFile(imgUrl);
        WorkoutEntity workout = modelMapper.map(workoutCreationDTO, WorkoutEntity.class);
        workout.setImgUrl(picture);
        CoachEntity author = coachService.getCoachEntityByUsername(authorUsername);
        workout.setAuthor(author);
        workout = workoutRepository.save(workout);
        mapAndProccessJsonExercises(json, workout);

        return modelMapper.map(workout, WorkoutDTO.class);
    }

    void mapAndProccessJsonExercises(List<String> json, WorkoutEntity workout) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<WorkoutExerciseEntity> exercises = new ArrayList<>();
        try {
            for (String exerciseJson : json) {
                WorkoutExerciseEntity exercise = objectMapper.readValue(exerciseJson, WorkoutExerciseEntity.class);
                exercises.add(exercise);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        exercises.forEach(exercise -> {
            ExerciseEntity dbExercise = exerciseService.getByName(exercise.getName());
            exercise.setVideoUrl(dbExercise.getVideoUrl());
            exercise.setMuscleGroup(dbExercise.getMuscleGroup());
            exercise.setWorkout(workout);
        });

        workoutExerciseService.saveAll(exercises);

    }


    public WorkoutDTO getWorkoutById(Long id) {
        WorkoutEntity workoutEntity = workoutRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Workout with id " + id + " does not exist"));
        return modelMapper.map(workoutEntity, WorkoutDTO.class);
    }

    public WorkoutEntity getWorkoutEntityById(Long id) {
        return workoutRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Workout with id " + id + " does not exist"));
    }

    public DayWorkoutsEntity getDayWorkout(Long workoutId, Long weekId, String dayName) {
        return dayWorkoutsRepository.findByNameAndWorkoutIdAndWeekId(dayName, workoutId, weekId).orElse(null);
    }

    public Page<WorkoutDTO> getAllWorkouts(Pageable pageable) {
        return workoutRepository
                .findAll(pageable)
                .map(entity -> modelMapper.map(entity, WorkoutDTO.class));
    }

    public List<WorkoutDTO> getAllWorkouts() {
        return workoutRepository
                .findAll().stream().map(entity -> modelMapper.map(entity, WorkoutDTO.class)).toList();
    }

    public List<WorkoutAddDTO> getAllWorkoutNames() {
        return workoutRepository
                .findAll().stream().map(entity -> modelMapper.map(entity, WorkoutAddDTO.class)).toList();
    }

    public void saveWorkout(WorkoutEntity workout) {
        workoutRepository.save(workout);
    }

    public List<LevelEnum> getAllLevels() {
        return Arrays.stream(LevelEnum.values()).collect(Collectors.toList());
    }

    public WorkoutDetailsDTO getWorkoutDetailsById(Long workoutId, String dayName) {
        WorkoutEntity workout = workoutRepository.findById(workoutId).orElse(null);

        return modelMapper.map(workout, WorkoutDetailsDTO.class);
    }

    public List<ClientWorkoutDetails> getCompletedWorkoutsForClient(String clientName) {
        List<UserProgress> completedWorkouts = userProgressRepository.findByUserUsernameAndWorkoutCompletedTrue(clientName);

        return completedWorkouts.stream()
                .map(userProgress -> modelMapper.map(userProgress.getWorkout(), ClientWorkoutDetails.class))
                .collect(Collectors.toList());
    }


    public void startWorkout(Long programId, Long workoutId, String username, Long weekId, String dayName) {
        UserEntity user = userService.getUserEntityByUsername(username);
        ProgramWeekEntity week = weekService.getWeekByNumber(weekId, programId);
        DayWorkoutsEntity dayWorkout = getDayWorkout(workoutId, week.getId(), dayName);

        UserProgress progress = userProgressRepository.findByUserIdAndWorkoutIdAndWeekIdAndProgramId(user.getId(), dayWorkout.getId(), week.getId(), programId)
                .orElse(new UserProgress());
        progress.setUser(user);
        progress.setWorkout(dayWorkout);
        progress.setWorkoutStarted(true);
        progress.setExerciseCompleted(false);
        progress.setWorkoutCompleted(false);
        progress.setWeekCompleted(false);
        userProgressRepository.save(progress);
    }

    public void completeWorkout(Long programId, Long workoutId, String username, Long weekNumber, String dayName) {
        UserEntity user = userService.getUserEntityByUsername(username);
        ProgramWeekEntity week = weekService.getWeekByNumber(weekNumber, programId);
        DayWorkoutsEntity dayWorkout = getDayWorkout(workoutId, week.getId(), dayName);

        UserProgress progress = userProgressRepository.findByUserIdAndWorkoutIdAndWeekIdAndProgramId(user.getId(), dayWorkout.getId(), week.getId(), programId)
                .orElse(new UserProgress());

        if (!progress.isWorkoutCompleted()) {
            progress.setWorkoutCompleted(true);
            userProgressRepository.save(progress);
        }
    }

    public boolean isWorkoutStarted(Long programId, String dayName, Long workoutId, Long weekId, String username) {
        UserEntity user = userService.getUserEntityByUsername(username);
        ProgramWeekEntity week = weekService.getWeekByNumber(weekId, programId);

        DayWorkoutsEntity dayWorkout = dayWorkoutsRepository.findByNameAndWorkoutIdAndWeekId(dayName, workoutId, week.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found for the specified week"));
        return userProgressRepository.findByUserIdAndWorkoutIdAndWeekIdAndProgramId(user.getId(), dayWorkout.getId(), week.getId(), programId)
                .map(UserProgress::isWorkoutStarted)
                .orElse(false);
    }

    // TODO
    public boolean isWorkoutCompleted(Long programId, Long workoutId, Long weekId, String dayName, String username) {
        UserEntity user = userService.getUserEntityByUsername(username);
        ProgramWeekEntity week = weekService.getWeekByNumber(weekId, programId);

        DayWorkoutsEntity dayWorkout = dayWorkoutsRepository.findByNameAndWorkoutIdAndWeekId(dayName, workoutId, week.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found for the specified week and day"));

        UserProgress progress = userProgressRepository.findByUserIdAndWorkoutIdAndWeekIdAndProgramId(user.getId(), dayWorkout.getId(), week.getId(), programId)
                .orElse(new UserProgress());

        return userProgressRepository.findByUserIdAndWorkoutIdAndWeekIdAndProgramId(user.getId(), dayWorkout.getId(), week.getId(), programId)
                .map(UserProgress::isWorkoutCompleted)
                .orElse(false);
    }


    public void like(Long workoutId, String username) {
        UserEntity user = userService.getUserEntityByUsername(username);

        boolean hasLiked = user.getWorkoutsLiked().stream().anyMatch(workoutEntity -> Objects.equals(workoutEntity.getId(), workoutId));

        if (!hasLiked) {
            WorkoutEntity workout = getWorkoutEntityById(workoutId);
            user.getWorkoutsLiked().add(workout);
            int oldLikes = workout.getLikes();
            workout.setLikes(oldLikes + 1);
            userService.saveUser(user);
        }

    }

    public void dislike(Long workoutId, String username) {
        UserEntity user = userService.getUserEntityByUsername(username);

        boolean hasLiked = user.getWorkoutsLiked().stream().anyMatch(workoutEntity -> Objects.equals(workoutEntity.getId(), workoutId));

        if (hasLiked) {
            WorkoutEntity workout = getWorkoutEntityById(workoutId);
            user.getWorkoutsLiked().remove(workout);
            int oldLikes = workout.getLikes();
            workout.setLikes(oldLikes - 1);
            userService.saveUser(user);
        }

    }

    public List<DayWorkoutsDTO> getAllByWorkoutIds(Long programId, Long week, List<WorkoutAddDTO> workoutAddDTO) {
        List<DayWorkoutsEntity> daysWorkouts = workoutAddDTO.stream().map(workout -> createDayWorkout(workout, week, programId)).toList();
        return daysWorkouts.stream().map(dayWorkoutsEntity -> modelMapper.map(dayWorkoutsEntity, DayWorkoutsDTO.class)).toList();
    }

    private DayWorkoutsEntity createDayWorkout(WorkoutAddDTO workout, Long weekId, Long programId) {
        ProgramWeekEntity week = weekService.getWeekByNumber(weekId, programId);
        WorkoutEntity workoutEntity = workoutRepository.findById(workout.getId()).get();
        DayWorkoutsEntity dayWorkoutsEntity = dayWorkoutsRepository.findByNameAndWeekId(workout.getName(), week.getId()).get();
        dayWorkoutsEntity.setWorkout(workoutEntity);
        dayWorkoutsRepository.save(dayWorkoutsEntity);
        return dayWorkoutsEntity;
    }

    public void completeExercise(Long programId, Long workoutId, String dayName, Long exerciseId, String username, Long weekNumber) {
        ProgramWeekEntity week = weekService.getWeekByNumber(weekNumber, programId);
        DayWorkoutsEntity dayWorkoutsEntity = dayWorkoutsRepository.findByWorkoutIdAndWeekId(workoutId, week.getId()).orElse(null);
        UserEntity user = userService.getUserEntityByUsername(username);
        WorkoutExercise workoutExercise = programExerciseRepository.findByExerciseIdAndWorkoutId(exerciseId, dayWorkoutsEntity.getId()).orElse(null);
        workoutExercise.setCompleted(true);
        workoutExercise.setUser(user);

        user.getCompletedExercises().add(workoutExercise);

        userService.saveUser(user);
        programExerciseRepository.save(workoutExercise);

    }
}
