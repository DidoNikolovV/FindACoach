package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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

    @Column(name = "weight")
    private Double weight;

    @Column(name = "weight_goal")
    private Double weightGoal;

    @Column(name = "performance_goals")
    private String performanceGoals;

    @Column(name = "body_composition_goal")
    private String bodyCompositionGoal;

    @Column(name = "img_url")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private CoachEntity coach;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ScheduledWorkoutEntity> scheduledWorkouts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<ProgramEntity> completedPrograms = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<DailyMetricsEntity> dailyMetrics;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgressPicture> progressPictures = new ArrayList<>();

//    @OneToMany(mappedBy = "user")
//    private List<UserProgress> progress;
}
