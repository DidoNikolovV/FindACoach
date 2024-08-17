package com.softuni.fitlaunch.service;

import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.entity.DayWorkoutsEntity;
import com.softuni.fitlaunch.model.entity.ExerciseEntity;
import com.softuni.fitlaunch.model.entity.ProgramEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.UserProgress;
import com.softuni.fitlaunch.model.entity.WorkoutEntity;
import com.softuni.fitlaunch.model.entity.WorkoutExerciseEntity;
import com.softuni.fitlaunch.repository.DayWorkoutsRepository;
import com.softuni.fitlaunch.repository.UserProgressRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class UserProgressServiceTest {
    @Mock
    private UserProgressRepository userProgressRepository;

    @Mock
    private UserService userService;

    @Mock
    private WeekService weekService;

    @Mock
    private DayWorkoutsRepository dayWorkoutsRepository;

    @InjectMocks
    private UserProgressService userProgressService;

    @Mock
    private ModelMapper modelMapper;

    public UserProgressServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getUserProgressForProgramIdAndWeekId_whenValidInput_thenReturnsUserProgressList() {
        Long userId = 1L;
        Long programId = 2L;
        Long weekId = 3L;

        List<UserProgress> expectedProgressList = List.of(new UserProgress(), new UserProgress());
        when(userProgressRepository.findByUserIdAndProgramIdAndWeekId(userId, programId, weekId)).thenReturn(expectedProgressList);

        List<UserProgress> result = userProgressService.getUserProgressForProgramIdAndWeekId(userId, programId, weekId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userProgressRepository, times(1)).findByUserIdAndProgramIdAndWeekId(userId, programId, weekId);
    }

    @Test
    void getByUserIdAndWorkoutIdAndWeekId_whenValidInput_thenReturnsUserProgress() {
        Long userId = 1L;
        Long workoutId = 2L;
        Long weekId = 3L;

        UserProgress expectedProgress = new UserProgress();
        when(userProgressRepository.findByUserIdAndWorkoutIdAndWeekId(userId, workoutId, weekId)).thenReturn(Optional.of(expectedProgress));

        UserProgress result = userProgressService.getByUserIdAndWorkoutIdAndWeekId(userId, workoutId, weekId);

        assertNotNull(result);
        verify(userProgressRepository, times(1)).findByUserIdAndWorkoutIdAndWeekId(userId, workoutId, weekId);
    }

    @Test
    void getByUserIdAndWorkoutIdAndWeekId_whenNotFound_thenThrowsResourceNotFoundException() {
        Long userId = 1L;
        Long workoutId = 2L;
        Long weekId = 3L;

        when(userProgressRepository.findByUserIdAndWorkoutIdAndWeekId(userId, workoutId, weekId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userProgressService.getByUserIdAndWorkoutIdAndWeekId(userId, workoutId, weekId));

        verify(userProgressRepository, times(1)).findByUserIdAndWorkoutIdAndWeekId(userId, workoutId, weekId);
    }

    @Test
    void saveUserProgress_whenNewProgressIsCreated_thenSavesUserProgress() {
        ProgramEntity program = new ProgramEntity();
        String username = "testUser";

        UserEntity user = new UserEntity();
        user.setUsername(username);

        ProgramWeekEntity week1 = new ProgramWeekEntity();
        ProgramWeekEntity week2 = new ProgramWeekEntity();
        List<ProgramWeekEntity> weeks = Arrays.asList(week1, week2);

        when(userService.getUserEntityByUsername(username)).thenReturn(user);

        userProgressService.saveUserProgress(program, username, weeks);

        verify(userService, times(1)).getUserEntityByUsername(username);
        verify(userProgressRepository, times(2)).save(any(UserProgress.class));
    }

    @Test
    void getByUserIdAndWorkoutIdAndWeekIdAndProgramId_whenValidInput_thenReturnsUserProgress() {
        Long userId = 1L;
        Long workoutId = 2L;
        Long weekId = 3L;
        Long programId = 4L;

        UserProgress expectedProgress = new UserProgress();
        when(userProgressRepository.findByUserIdAndWorkoutIdAndWeekIdAndProgramId(userId, workoutId, weekId, programId)).thenReturn(Optional.of(expectedProgress));

        UserProgress result = userProgressService.getByUserIdAndWorkoutIdAndWeekIdAndProgramId(userId, workoutId, weekId, programId);

        assertNotNull(result);
        verify(userProgressRepository, times(1)).findByUserIdAndWorkoutIdAndWeekIdAndProgramId(userId, workoutId, weekId, programId);
    }

    @Test
    void getByUserIdAndWorkoutIdAndWeekIdAndProgramId_whenNotFound_thenThrowsResourceNotFoundException() {
        Long userId = 1L;
        Long workoutId = 2L;
        Long weekId = 3L;
        Long programId = 4L;

        when(userProgressRepository.findByUserIdAndWorkoutIdAndWeekIdAndProgramId(userId, workoutId, weekId, programId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userProgressService.getByUserIdAndWorkoutIdAndWeekIdAndProgramId(userId, workoutId, weekId, programId));

        verify(userProgressRepository, times(1)).findByUserIdAndWorkoutIdAndWeekIdAndProgramId(userId, workoutId, weekId, programId);
    }

    @Test
    void saveUserProgress_whenValidInput_thenSavesProgress() {
        UserProgress userProgress = new UserProgress();

        userProgressService.saveUserProgress(userProgress);

        verify(userProgressRepository, times(1)).save(userProgress);
    }

    @Test
    void testGetAllExercisesForWorkout_whenWorkoutDetailsIsRequested_thenReturnListOfThem() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("testUser");

        ProgramWeekEntity week = new ProgramWeekEntity();
        week.setId(1L);
        week.setNumber(1);

        WorkoutExerciseEntity workoutExerciseEntity = new WorkoutExerciseEntity();
        workoutExerciseEntity.setReps(3);
        workoutExerciseEntity.setReps(3);
        workoutExerciseEntity.setName("Push up");

        WorkoutExerciseDTO workoutExerciseDto = new WorkoutExerciseDTO();
        workoutExerciseDto.setReps(3);
        workoutExerciseDto.setReps(3);
        workoutExerciseDto.setName("Push up");

        WorkoutEntity workout = new WorkoutEntity();
        workout.setExercises(new ArrayList<>());

        workout.getExercises().add(workoutExerciseEntity);

        DayWorkoutsEntity dayWorkoutsEntity = new DayWorkoutsEntity();
        dayWorkoutsEntity.setName("Monday");
        dayWorkoutsEntity.setId(1L);
        dayWorkoutsEntity.setWeek(week);
        dayWorkoutsEntity.setWorkout(workout);

        UserProgress userProgress = new UserProgress();
        userProgress.setWorkout(dayWorkoutsEntity);
        userProgress.setWeek(week);
        userProgress.setUser(user);

        when(userService.getUserEntityByUsername("testUser")).thenReturn(user);
        when(weekService.getWeekByNumber(1L, 1L)).thenReturn(week);
        when(dayWorkoutsRepository.findByNameAndWorkoutIdAndWeekId("Monday", 1L, 1L)).thenReturn(Optional.of(dayWorkoutsEntity));
        when(userProgressRepository.findByUserIdAndWorkoutIdAndWeekIdAndProgramId(1L, 1L, 1L, 1L)).thenReturn(Optional.of(userProgress));
        when(modelMapper.map(workoutExerciseEntity, WorkoutExerciseDTO.class)).thenReturn(workoutExerciseDto);

        List<WorkoutExerciseDTO> allExercisesForWorkout = userProgressService.getAllExercisesForWorkout(1L, 1L, 1L, "testUser", "Monday");

        assertThat(allExercisesForWorkout).isNotEmpty();

    }

}