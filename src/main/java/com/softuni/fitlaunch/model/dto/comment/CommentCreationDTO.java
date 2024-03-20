package com.softuni.fitlaunch.model.dto.comment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentCreationDTO {

    private String authorUsername;

    private Long programId;

    private Long workoutId;

    private String message;
}
