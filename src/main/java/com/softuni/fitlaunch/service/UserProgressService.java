package com.softuni.fitlaunch.service;


import com.softuni.fitlaunch.model.entity.DayWorkoutsEntity;
import com.softuni.fitlaunch.model.entity.ProgramEntity;
import com.softuni.fitlaunch.model.entity.ProgramWeekEntity;
import com.softuni.fitlaunch.model.entity.UserEntity;
import com.softuni.fitlaunch.model.entity.UserProgress;
import com.softuni.fitlaunch.repository.UserProgressRepository;
import com.softuni.fitlaunch.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserProgressService {

    private final UserProgressRepository userProgressRepository;
    private final UserService userService;

    public UserProgressService(UserProgressRepository userProgressRepository, UserService userService) {
        this.userProgressRepository = userProgressRepository;
        this.userService = userService;
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

    public UserProgress getByUserIdAndWorkoutId(Long userId, Long workoutId) {
        return userProgressRepository.findByUserIdAndWorkoutId(userId, workoutId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    public void completeWeek(ProgramWeekEntity programWeek, UserEntity user) {
        UserProgress userProgress = new UserProgress();
        userProgress.setWeek(programWeek);
        userProgress.setUser(user);
        userProgress.setWeekCompleted(true);
        userProgressRepository.save(userProgress);
    }

    public void saveUserProgress(ProgramEntity program, String username, List<ProgramWeekEntity> weeks) {
        UserEntity user = userService.getUserEntityByUsername(username);

        for (ProgramWeekEntity week : weeks) {
            UserProgress progress = new UserProgress();
            progress.setUser(user);
            progress.setProgram(program);
            progress.setWeek(week);

            userProgressRepository.save(progress);
        }
    }


    public UserProgress getOrCreateUserProgress(UserEntity user, ProgramEntity program, ProgramWeekEntity week, DayWorkoutsEntity dayWorkout) {
        return userProgressRepository.findByUserIdAndWorkoutIdAndWeekIdAndProgramId(user.getId(), dayWorkout.getId(), week.getId(), program.getId())
                .orElseGet(() -> {
                    UserProgress newUserProgress = new UserProgress();
                    newUserProgress.setUser(user);
                    newUserProgress.setProgram(program);
                    newUserProgress.setWeek(week);
                    newUserProgress.setWorkout(dayWorkout);
                    return userProgressRepository.save(newUserProgress);
                });
    }

    public List<UserProgress> getUserProgressForProgramIdAndWeekId(Long userId, Long programId, Long weekId) {
        return userProgressRepository.findByUserIdAndProgramIdAndWeekId(userId, programId, weekId);
    }
}
