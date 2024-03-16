package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "clients")
public class ClientEntity extends BaseEntity {

    @Column(unique = true)
    private String username;

    @Email
    private String email;

    @Column(name = "img_url")
    private String imgUrl;

    private Double weight;

    private Double height;

    private String targetGoals;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private CoachEntity coach;

    @ManyToOne(cascade = CascadeType.ALL)
    private WorkoutEntity workoutStarted;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "program_workout_exercises_completed",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id"))
    private List<ExerciseEntity> programExercisesCompleted;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ScheduledWorkoutEntity> scheduledWorkouts;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ProgramEntity> completedPrograms;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "program_workouts_completed",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id"))
    private List<WorkoutEntity> completedWorkouts;

}
