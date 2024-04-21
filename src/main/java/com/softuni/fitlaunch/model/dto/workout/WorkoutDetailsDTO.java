package com.softuni.fitlaunch.model.dto.workout;

import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDetailsDTO {

    private Long id;

    private String name;

    private String level;

    private String description;

    private String imgUrl;

    private List<WorkoutExerciseDTO> exercises;

    private Long likes;

    private List<UserDTO> usersLiked;
}