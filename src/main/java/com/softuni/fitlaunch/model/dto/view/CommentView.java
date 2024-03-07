package com.softuni.fitlaunch.model.dto.view;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentView {
    private Long id;
    private String authorUsername;
    private String message;
}
