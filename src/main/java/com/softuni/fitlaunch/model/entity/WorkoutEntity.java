package com.softuni.fitlaunch.model.entity;


import com.softuni.fitlaunch.model.enums.LevelEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@Entity
@Table(name = "workouts")
public class WorkoutEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private CoachEntity author;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LevelEnum level;

    @Column
    private String description;

    @Column
    private Integer likes = 0;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<WorkoutExerciseEntity> exercises = new ArrayList<>();

    @Column
    private String dateCompleted;

    @OneToMany(mappedBy = "workout", fetch = FetchType.EAGER, cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {


        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutEntity that = (WorkoutEntity) o;
        return Objects.equals(author, that.author) && Objects.equals(imgUrl, that.imgUrl) && Objects.equals(name, that.name) && level == that.level && Objects.equals(description, that.description) && Objects.equals(likes, that.likes) && Objects.equals(exercises, that.exercises) && Objects.equals(dateCompleted, that.dateCompleted) && Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, imgUrl, name, level, description, likes, exercises, dateCompleted, comments);
    }
}
