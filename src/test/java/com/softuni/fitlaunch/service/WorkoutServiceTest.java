package com.softuni.fitlaunch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.softuni.fitlaunch.model.entity.WorkoutExerciseEntity;
import com.softuni.fitlaunch.model.enums.LevelEnum;
import com.softuni.fitlaunch.repository.DayWorkoutsRepository;
import com.softuni.fitlaunch.repository.UserProgressRepository;
import com.softuni.fitlaunch.repository.WorkoutExerciseRepository;
import com.softuni.fitlaunch.repository.WorkoutRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private CoachService coachService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private FileUpload fileUpload;

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private WeekService weekService;

    @Mock
    private UserProgressRepository userProgressRepository;

    @Mock
    private WorkoutExerciseService workoutExerciseService;

    @Mock
    private DayWorkoutsRepository dayWorkoutsRepository;

    @Mock
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Mock
    private UserService userService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private WorkoutService workoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(userService, workoutRepository);
    }

    @Test
    void createWorkout_whenValidInput_thenWorkoutIsCreatedAndSaved() {
        String name = "Full Body Workout";
        String level = "Beginner";
        MultipartFile imgUrl = mock(MultipartFile.class);
        String authorUsername = "coach123";
        List<String> json = List.of("{\"name\":\"Push Up\"}");

        WorkoutEntity workoutEntity = new WorkoutEntity();
        workoutEntity.setId(1L);
        workoutEntity.setImgUrl("image.jpg");

        CoachEntity coachEntity = new CoachEntity();
        coachEntity.setUsername(authorUsername);

        WorkoutDTO workoutDTO = new WorkoutDTO();
        workoutDTO.setId(1L);

        ExerciseEntity dbExercise = new ExerciseEntity();
        dbExercise.setName("Push Up");
        dbExercise.setVideoUrl("pushup.mp4");
        dbExercise.setMuscleGroup("Chest");

        when(fileUpload.uploadFile(imgUrl)).thenReturn("image.jpg");
        when(coachService.getCoachEntityByUsername(authorUsername)).thenReturn(coachEntity);
        when(modelMapper.map(any(WorkoutCreationDTO.class), eq(WorkoutEntity.class))).thenReturn(workoutEntity);
        when(workoutRepository.save(any(WorkoutEntity.class))).thenReturn(workoutEntity);
        when(modelMapper.map(any(WorkoutEntity.class), eq(WorkoutDTO.class))).thenReturn(workoutDTO);

        when(exerciseService.getByName("Push Up")).thenReturn(dbExercise);

        WorkoutDTO result = workoutService.createWorkout(name, level, imgUrl, authorUsername, json);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(fileUpload, times(1)).uploadFile(imgUrl);
        verify(coachService, times(1)).getCoachEntityByUsername(authorUsername);
        verify(modelMapper, times(1)).map(any(WorkoutCreationDTO.class), eq(WorkoutEntity.class));
        verify(workoutRepository, times(1)).save(workoutEntity);
        verify(workoutExerciseService, times(1)).saveAll(anyList());
        verify(modelMapper, times(1)).map(any(WorkoutEntity.class), eq(WorkoutDTO.class));
    }


    @Test
    void createWorkout_whenValidInput_thenWorkoutIsCreated() {
        WorkoutCreationDTO workoutCreationDTO = new WorkoutCreationDTO();
        CoachEntity coach = new CoachEntity();
        WorkoutEntity workoutEntity = new WorkoutEntity();
        WorkoutDTO workoutDTO = new WorkoutDTO();

        when(coachService.getCoachEntityByUsername(anyString())).thenReturn(coach);
        when(modelMapper.map(workoutCreationDTO, WorkoutEntity.class)).thenReturn(workoutEntity);
        when(workoutRepository.save(any(WorkoutEntity.class))).thenReturn(workoutEntity);
        when(modelMapper.map(any(WorkoutEntity.class), eq(WorkoutDTO.class))).thenReturn(workoutDTO);

        WorkoutDTO result = workoutService.createWorkout(workoutCreationDTO, "coachUsername");

        verify(workoutRepository, times(1)).save(workoutEntity);
        assertEquals(workoutDTO, result);
    }

    @Test
    void testGetAllWorkoutNames_whenNoErrors_thenReturnListOfWorkoutsAddDto() {
        List<WorkoutEntity> workouts = new ArrayList<>();
        WorkoutEntity workout = new WorkoutEntity();
        workout.setId(1L);
        workout.setName("Full Body");

        WorkoutAddDTO workoutAddDTO = new WorkoutAddDTO();
        workoutAddDTO.setName(workout.getName());
        workoutAddDTO.setId(1L);

        when(workoutRepository.findAll()).thenReturn(workouts);
        when(modelMapper.map(workout, WorkoutAddDTO.class)).thenReturn(workoutAddDTO);
        workoutService.getAllWorkoutNames();

        verify(workoutRepository, times(1)).findAll();
    }


    @Test
    void testGetWorkoutDetailsById_whenWorkoutExists_thenReturnWorkoutDetailsDto() {
        WorkoutEntity workout = new WorkoutEntity();
        workout.setId(1L);
        workout.setName("test");
        workout.setLevel(LevelEnum.BEGINNER);
        workout.setLikes(0);

        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));
        when(modelMapper.map(workout, WorkoutDetailsDTO.class)).thenReturn(any());

        workoutService.getWorkoutDetailsById(1L, "Monday");

        verify(workoutRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCompletedWorkoutsForClient_whenClientHasNotCompletedAny_thenReturnEmptyList() {
        String clientName = "test";
        when(userProgressRepository.findByUserUsernameAndWorkoutCompletedTrue(clientName)).thenReturn(new ArrayList<>());

        workoutService.getCompletedWorkoutsForClient(clientName);

        verify(userProgressRepository, times(1)).findByUserUsernameAndWorkoutCompletedTrue(clientName);

    }

    @Test
    void testCompletedWorkout_whenUserProgressDoesNotExist_thenNewProgressIsCreated() {
        Long workoutId = 1L;
        String username = "username";
        Long weekNumber = 1L;
        String dayName = "Monday";

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername(username);

        DayWorkoutsEntity dayWorkout = new DayWorkoutsEntity();

        when(userService.getUserEntityByUsername(username)).thenReturn(user);
        when(userProgressRepository.findByUserIdAndWorkoutId(user.getId(), workoutId)).thenReturn(Optional.empty());
        when(dayWorkoutsRepository.findByNameAndWorkoutIdAndWeekId(dayName, workoutId, weekNumber)).thenReturn(Optional.of(dayWorkout));

        workoutService.completedWorkout(workoutId, username, weekNumber, dayName);

        ArgumentCaptor<UserProgress> progressCaptor = ArgumentCaptor.forClass(UserProgress.class);
        verify(userProgressRepository).save(progressCaptor.capture());

        UserProgress savedProgress = progressCaptor.getValue();
        assertNotNull(savedProgress);
        assertEquals(user, savedProgress.getUser());
        assertTrue(savedProgress.isWorkoutCompleted());
        assertEquals(dayWorkout, savedProgress.getWorkout());
    }

    @Test
    void testStartWorkout_whenUserStartedWorkout_thenSaveStateToDatabase() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("test");

        WorkoutEntity workout = new WorkoutEntity();
        workout.setId(1L);
        workout.setName("Full Body");
        workout.setExercises(new ArrayList<>());

        ProgramWeekEntity weekEntity = new ProgramWeekEntity();
        weekEntity.setId(1L);
        weekEntity.setNumber(1);
        weekEntity.setDays(new ArrayList<>());

        DayWorkoutsEntity day = new DayWorkoutsEntity();
        day.setWorkout(workout);
        day.setWeek(weekEntity);
        day.setName("Monday");
        day.setUserProgress(new ArrayList<>());

        when(userService.getUserEntityByUsername("test")).thenReturn(user);
        when(dayWorkoutsRepository.findByNameAndWorkoutIdAndWeekId("Monday", 1L, 1L)).thenReturn(Optional.of(day));
        when(userProgressRepository.findByUserIdAndWorkoutId(1L, 1L)).thenReturn(any());

        workoutService.startWorkout(1L, "test", 1L, "Monday");

        verify(userProgressRepository, times(1)).save(any());
    }

    @Test
    void testCompletedWorkout_whenUserHasCompletedTheWorkout_thenSaveTheWorkoutAsCompletedForTheUser() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("test");

        WorkoutEntity workout = new WorkoutEntity();
        workout.setId(1L);
        workout.setName("Full Body");
        workout.setExercises(new ArrayList<>());

        ProgramWeekEntity weekEntity = new ProgramWeekEntity();
        weekEntity.setId(1L);
        weekEntity.setNumber(1);
        weekEntity.setDays(new ArrayList<>());

        DayWorkoutsEntity day = new DayWorkoutsEntity();
        day.setWorkout(workout);
        day.setWeek(weekEntity);
        day.setName("Monday");
        day.setUserProgress(new ArrayList<>());

        UserProgress userProgress = new UserProgress();
        userProgress.setUser(user);
        userProgress.setWorkout(day);
        userProgress.setWorkoutCompleted(false);

        when(userService.getUserEntityByUsername("test")).thenReturn(user);
        when(userProgressRepository.findByUserIdAndWorkoutId(1L, 1L)).thenReturn(Optional.of(userProgress));

        workoutService.completedWorkout(1L, "test", 1L, "Monday");

        verify(userProgressRepository, times(1)).save(any());
    }

    @Test
    void mapAndProccessJsonExercises_whenJsonIsValid_thenExercisesAreMappedAndSaved() throws JsonProcessingException {
        String jsonExercise = "{\"name\":\"Push Up\"}";
        WorkoutEntity workout = new WorkoutEntity();
        WorkoutExerciseEntity exerciseEntity = new WorkoutExerciseEntity();
        exerciseEntity.setName("Push Up");

        ExerciseEntity dbExercise = new ExerciseEntity();
        dbExercise.setName("Push Up");
        dbExercise.setVideoUrl("pushup.mp4");
        dbExercise.setMuscleGroup("Chest");

        when(objectMapper.readValue(jsonExercise, WorkoutExerciseEntity.class)).thenReturn(exerciseEntity);
        when(exerciseService.getByName("Push Up")).thenReturn(dbExercise);

        List<String> json = List.of(jsonExercise);

        workoutService.mapAndProccessJsonExercises(json, workout);

        assertEquals("pushup.mp4", dbExercise.getVideoUrl());
        assertEquals("Chest", dbExercise.getMuscleGroup());

        verify(exerciseService, times(1)).getByName("Push Up");
        verify(workoutExerciseService, times(1)).saveAll(anyList());
    }

    @Test
    void mapAndProccessJsonExercises_whenJsonProcessingException_thenRuntimeExceptionIsThrown() throws JsonProcessingException, JsonProcessingException {
        String invalidJson = "{\"name\":\"Push Up\"}";
        when(objectMapper.readValue(invalidJson, WorkoutExerciseEntity.class)).thenThrow(JsonProcessingException.class);

        List<String> json = List.of(invalidJson);

        assertThrows(RuntimeException.class, () -> workoutService.mapAndProccessJsonExercises(json, new WorkoutEntity()));

        verify(workoutExerciseService, never()).saveAll(anyList());
    }

    @Test
    void getWorkoutById_whenWorkoutExists_thenReturnWorkoutDTO() {
        WorkoutEntity workoutEntity = new WorkoutEntity();
        WorkoutDTO workoutDTO = new WorkoutDTO();

        when(workoutRepository.findById(anyLong())).thenReturn(Optional.of(workoutEntity));
        when(modelMapper.map(any(WorkoutEntity.class), eq(WorkoutDTO.class))).thenReturn(workoutDTO);

        WorkoutDTO result = workoutService.getWorkoutById(1L);

        assertEquals(workoutDTO, result);
        verify(workoutRepository, times(1)).findById(1L);
    }

    @Test
    void getWorkoutById_whenWorkoutDoesNotExist_thenThrowException() {
        when(workoutRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workoutService.getWorkoutById(1L));
        verify(workoutRepository, times(1)).findById(1L);
    }

    @Test
    void getAllWorkouts_whenCalled_thenReturnPageOfWorkoutDTOs() {
        WorkoutEntity workoutEntity = new WorkoutEntity();
        WorkoutDTO workoutDTO = new WorkoutDTO();
        Page<WorkoutEntity> workoutEntitiesPage = new PageImpl<>(Collections.singletonList(workoutEntity));

        when(workoutRepository.findAll(any(Pageable.class))).thenReturn(workoutEntitiesPage);
        when(modelMapper.map(any(WorkoutEntity.class), eq(WorkoutDTO.class))).thenReturn(workoutDTO);

        Page<WorkoutDTO> result = workoutService.getAllWorkouts(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        verify(workoutRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void like_whenWorkoutNotLiked_thenWorkoutIsLiked() {
        UserEntity user = new UserEntity();
        WorkoutEntity workout = new WorkoutEntity();
        workout.setLikes(0);
        workout.setId(1L);
        user.setWorkoutsLiked(new ArrayList<>());

        when(userService.getUserEntityByUsername(anyString())).thenReturn(user);
        when(workoutRepository.findById(anyLong())).thenReturn(Optional.of(workout));

        workoutService.like(1L, "username");

        assertTrue(user.getWorkoutsLiked().contains(workout));
        assertEquals(1, workout.getLikes());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void dislike_whenWorkoutLiked_thenWorkoutIsDisliked() {
        UserEntity user = new UserEntity();
        WorkoutEntity workout = new WorkoutEntity();
        user.setUsername("username");
        workout.setLikes(1);
        workout.setId(1L);
        List<WorkoutEntity> likedWorkouts = new ArrayList<>();
        likedWorkouts.add(workout);
        user.setWorkoutsLiked(likedWorkouts);

        when(userService.getUserEntityByUsername("username")).thenReturn(user);
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));

        workoutService.dislike(1L, "username");

        assertEquals(0, workout.getLikes());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void testIsWorkoutStarted_whenWorkoutIsNotStarted_thenReturnFalse() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("test");

        WorkoutEntity workout = new WorkoutEntity();
        workout.setId(1L);
        workout.setName("Full Body");

        ProgramWeekEntity weekEntity = new ProgramWeekEntity();
        weekEntity.setId(1L);
        weekEntity.setNumber(1);

        DayWorkoutsEntity day = new DayWorkoutsEntity();
        day.setName("Monday");
        day.setWorkout(workout);
        day.setWeek(weekEntity);

        UserProgress userProgress = new UserProgress();
        userProgress.setWorkoutStarted(false);

        when(userService.getUserEntityByUsername("test")).thenReturn(user);
        when(dayWorkoutsRepository.findByNameAndWorkoutIdAndWeekId("Monday", 1L, 1L))
                .thenReturn(Optional.of(day));

        when(userProgressRepository.findByUserIdAndWorkoutId(1L, 1L)).thenReturn(Optional.of(userProgress));

        boolean workoutStarted = workoutService.isWorkoutStarted("Monday", 1L, 1L, "test");

        assertFalse(workoutStarted);
    }

    @Test
    void testIsWorkoutCompleted_whenWorkoutIsNotCompleted_thenReturnFalse() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("test");

        WorkoutEntity workout = new WorkoutEntity();
        workout.setId(1L);
        workout.setName("Full Body");

        ProgramWeekEntity weekEntity = new ProgramWeekEntity();
        weekEntity.setId(1L);
        weekEntity.setNumber(1);

        DayWorkoutsEntity day = new DayWorkoutsEntity();
        day.setName("Monday");
        day.setWorkout(workout);
        day.setWeek(weekEntity);

        UserProgress userProgress = new UserProgress();
        userProgress.setWorkoutCompleted(false);

        when(userService.getUserEntityByUsername("test")).thenReturn(user);
        when(dayWorkoutsRepository.findByNameAndWorkoutIdAndWeekId("Monday", 1L, 1L))
                .thenReturn(Optional.of(day));

        when(userProgressRepository.findByUserIdAndWorkoutId(1L, 1L)).thenReturn(Optional.of(userProgress));

        boolean workoutCompleted = workoutService.isWorkoutCompleted(1L, 1L, "Monday", "test");

        assertFalse(workoutCompleted);
    }

    @Test
    void testCompletedExercise_whenUserCompleteExercise_thenSaveStateForExercise() {
        WorkoutExerciseEntity exerciseToBeCompleted = new WorkoutExerciseEntity();
        exerciseToBeCompleted.setCompleted(true);

        when(workoutExerciseRepository.findByIdAndWorkoutId(1L, 1L)).thenReturn(Optional.of(exerciseToBeCompleted));

        workoutService.completeExercise(1L, "Monday", 1L);
        verify(workoutExerciseService, times(1)).completeExercise(1L, 1L);
    }

    @Test
    void testCreateDayWorkout_whenCoachAttemptToCreateWorkout_thenCreateAndSave() {
        ProgramWeekEntity week = new ProgramWeekEntity();
        week.setId(1L);
        WorkoutEntity workout = new WorkoutEntity();
        workout.setId(1L);
        workout.setName("Full Body");
        DayWorkoutsEntity day = new DayWorkoutsEntity();
        day.setId(1L);
        day.setWorkout(workout);

        when(weekService.getWeekByNumber(1L, 1L)).thenReturn(week);
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));
        when(dayWorkoutsRepository.findByNameAndWeekId("Full Body", 1L)).thenReturn(Optional.of(day));

        List<WorkoutAddDTO> workoutDtos = new ArrayList<>();
        WorkoutAddDTO workoutDto = new WorkoutAddDTO();
        workoutDto.setName("Full Body");
        workoutDto.setId(1L);
        workoutDtos.add(workoutDto);

        workoutService.getAllByWorkoutIds(1L, 1L, workoutDtos);

        verify(weekService, times(1)).getWeekByNumber(1L, 1L);
        verify(workoutRepository, times(1)).findById(workout.getId());
        verify(dayWorkoutsRepository, times(1)).save(day);

    }

}