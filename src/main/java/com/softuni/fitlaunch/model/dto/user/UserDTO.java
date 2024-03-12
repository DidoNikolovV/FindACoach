package com.softuni.fitlaunch.model.dto.user;

import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekWorkoutDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWorkoutExerciseDTO;
import com.softuni.fitlaunch.model.dto.workout.WorkoutDTO;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;
    private String username;

    private String email;

    private List<UserRoleDTO> roles;

    private String membership;


    private List<CommentCreationDTO> comments;


    private String workoutStarted;

    private List<WorkoutDTO> workoutsLiked;


    private List<ProgramWorkoutExerciseDTO> exercisesCompleted;

    private boolean activated = false;

    private String imgUrl;

    private UserTitleEnum title;


}