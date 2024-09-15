package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercises")
@Getter
@Setter
public class ExerciseEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "muscle_group", nullable = false)
    private String muscleGroup;

    @Column(name = "video_url")
    private String videoUrl;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    List<WorkoutExercise> workoutExerciseList = new ArrayList<>();
}
