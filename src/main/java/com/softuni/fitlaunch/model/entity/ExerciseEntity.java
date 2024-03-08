package com.softuni.fitlaunch.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "exercises")
public class ExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "video_url")
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "program_workout_id")
    private ProgramWeekWorkoutEntity program_week_workout;

//    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<WorkoutExerciseEntity> workoutExercise = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public ExerciseEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ExerciseEntity setName(String name) {
        this.name = name;
        return this;
    }


    public String getVideoUrl() {
        return videoUrl;
    }

    public ExerciseEntity setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public ProgramWeekWorkoutEntity getProgram_week_workout() {
        return program_week_workout;
    }

    public ExerciseEntity setProgram_week_workout(ProgramWeekWorkoutEntity program_week_workout) {
        this.program_week_workout = program_week_workout;
        return this;
    }
}
