package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "topics")
public class TopicEntity extends BaseEntity {
    private String title;
    private String content;
    private String author;
    private LocalDateTime date;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private List<CommentEntity> comments;
}
