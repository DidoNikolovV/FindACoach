package com.softuni.fitlaunch.model.dto.workout;

import com.softuni.fitlaunch.model.dto.WorkoutExerciseDTO;
import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import com.softuni.fitlaunch.model.dto.week.DayDTO;
import com.softuni.fitlaunch.model.enums.LevelEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class WorkoutDTO {

    @NotNull
    private Long id;

    @NotNull
    private String author;

    @NotNull
    private String imgUrl;

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private LevelEnum level;

    @NotNull
    private String description;

    @NotNull
    private List<CommentCreationDTO> comments;

    @NotNull
    private Integer likes = 0;

    @NotNull
    private List<WorkoutExerciseDTO> exercises;


    @NotNull
    private boolean isCompleted = false;

    @NotNull
    private String dateCompleted;

    @NotNull
    private boolean hasStarted = false;

    @NotNull
    private List<DayDTO> day;

}
