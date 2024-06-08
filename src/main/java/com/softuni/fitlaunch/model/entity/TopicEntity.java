package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "topics")
public class TopicEntity extends BaseEntity{

    private String title;
    private String content;
    private String author;
    private LocalDateTime date;
}
