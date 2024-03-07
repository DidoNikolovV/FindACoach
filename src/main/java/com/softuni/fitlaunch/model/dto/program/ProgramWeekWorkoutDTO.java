package com.softuni.fitlaunch.model.dto.program;

import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProgramWeekWorkoutDTO {

    private Long id;

    private String name;

    private Long programWeekId;

    private boolean hasStarted;

    private boolean isCompleted;

    private List<CommentCreationDTO> comments;

    private List<ProgramWorkoutExerciseDTO> exercises;

    private Long likes;

    private String level;

    private String description;
}
