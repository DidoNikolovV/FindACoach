package com.softuni.fitlaunch.model.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TopicDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime date;
}
