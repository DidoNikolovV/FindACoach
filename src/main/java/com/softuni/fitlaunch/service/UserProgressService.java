package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.entity.DayWorkoutsEntity;
import com.softuni.fitlaunch.model.entity.ProgramEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.UserProgress;
import com.softuni.fitlaunch.repository.DayWorkoutsRepository;
import com.softuni.fitlaunch.repository.UserProgressRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserProgressService {

    private final UserProgressRepository userProgressRepository;

    private final ModelMapper modelMapper;

    private final UserService userService;

    private final WeekService weekService;
    private final DayWorkoutsRepository dayWorkoutsRepository;

    public UserProgressService(UserProgressRepository userProgressRepository, ModelMapper modelMapper, UserService userService, WeekService weekService, DayWorkoutsRepository dayWorkoutsRepository) {
        this.userProgressRepository = userProgressRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.weekService = weekService;
        this.dayWorkoutsRepository = dayWorkoutsRepository;
    }

    public List<UserProgress> getUserProgressForProgram(String username, Long programId) {
        UserEntity user = userService.getUserEntityByUsername(username);
        return userProgressRepository.findByUserIdAndProgramId(user.getId(), programId);
    }


    public Map<Long, Boolean> getCompletedWorkouts(String username, Long programId) {
        List<UserProgress> userProgressList = getUserProgressForProgram(username, programId);
        return userProgressList.stream().collect(Collectors.toMap(
                userProgress -> userProgress.getWorkout().getId(),
                UserProgress::isWorkoutCompleted
        ));
    }

    public void saveUserProgress(ProgramEntity program, String username, List<ProgramWeekEntity> weeks) {
        UserEntity user = userService.getUserEntityByUsername(username);

        for (ProgramWeekEntity week : weeks) {
            UserProgress progress = createUserProgress(program, week, user);

            userProgressRepository.save(progress);
        }
    }

    private static UserProgress createUserProgress(ProgramEntity program, ProgramWeekEntity week, UserEntity user) {
        UserProgress progress = new UserProgress();
        progress.setUser(user);
        progress.setProgram(program);
        progress.setWeek(week);
        return progress;
    }

    public void saveUserProgress(UserProgress userProgress) {
        userProgressRepository.save(userProgress);
    }

    public List<WorkoutExerciseDTO> getAllExercisesForWorkout(Long programId, Long weekId, Long workoutId, String username, String dayName) {
        UserEntity user = userService.getUserEntityByUsername(username);
        ProgramWeekEntity week = weekService.getWeekByNumber(weekId, programId);
        DayWorkoutsEntity dayWorkoutsEntity = dayWorkoutsRepository.findByNameAndWorkoutIdAndWeekId(dayName, workoutId, week.getId()).orElseThrow(() -> new ResourceNotFoundException("Workout not found"));
        UserProgress userProgress = userProgressRepository.findByUserIdAndWorkoutIdAndWeekIdAndProgramId(user.getId(), dayWorkoutsEntity.getId(), week.getId(), programId).orElse(null);
        return userProgress.getWorkout().getWorkout().getExercises().stream().map(exercise -> modelMapper.map(exercise, WorkoutExerciseDTO.class)).toList();
    }


    public List<UserProgress> getUserProgressForProgramIdAndWeekId(Long userId, Long programId, Long weekId) {
        return userProgressRepository.findByUserIdAndProgramIdAndWeekId(userId, programId, weekId);
    }

    public UserProgress getByUserIdAndWorkoutIdAndWeekId(Long userId, Long workoutId, Long weekId) {
        return userProgressRepository.findByUserIdAndWorkoutIdAndWeekId(userId, workoutId, weekId).orElseThrow(() -> new ResourceNotFoundException("UserProgress not found"));
    }

    public UserProgress getByUserIdAndWorkoutIdAndWeekIdAndProgramId(Long userId, Long workoutId, Long weekId, Long programId) {
        return userProgressRepository.findByUserIdAndWorkoutIdAndWeekIdAndProgramId(userId, workoutId, weekId, programId).orElseThrow(() -> new ResourceNotFoundException("UserProgress not found"));
    }

    public void saveAll(List<UserProgress> userProgressList) {
        userProgressRepository.saveAll(userProgressList);
    }
}
