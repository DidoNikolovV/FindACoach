package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "exercises")
@Getter
@Setter
public class ExerciseEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "target_muscle_group", nullable = false)
    private String targetMuscleGroup;

    @Column(name = "video_url")
    private String videoUrl;
}
