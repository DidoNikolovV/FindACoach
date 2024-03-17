package com.softuni.fitlaunch.model.dto.user;

import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
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
    private String username;

    private String email;

    private List<UserRoleDTO> roles;

    private String membership;

    private List<CommentCreationDTO> comments;

    private List<WorkoutDTO> workoutsLiked;

    private boolean isActivated;

    private String imgUrl;

    private UserTitleEnum title;

}