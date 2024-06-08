package com.softuni.fitlaunch.model.dto;


import lombok.Data;

@Data
public class TopicCommentDTO {
    private Long id;
    private String profilePicture;
    private String authorUsername;
    private String message;
}
