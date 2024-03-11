package com.softuni.fitlaunch.model.entity;


import com.softuni.fitlaunch.model.enums.LevelEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "workouts")
public class WorkoutEntity extends BaseEntity {

    @ManyToOne
    private UserEntity author;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LevelEnum level;

    @Column(nullable = false)
    private String description;


    @Column(nullable = false)
    private Integer likes = 0;


    @ManyToMany
    @JoinTable(
            name = "workouts_likes",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> usersLiked = new ArrayList<>();

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ExerciseEntity> exercises;


    @Column(nullable = false)
    private boolean isCompleted = false;

    @Column
    private String dateCompleted;

    @Column(nullable = false)
    private boolean hasStarted = false;

}
