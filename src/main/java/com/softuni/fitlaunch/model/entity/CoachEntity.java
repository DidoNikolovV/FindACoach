package com.softuni.fitlaunch.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "coaches")
public class CoachEntity extends BaseEntity {

    @Column(unique = true)
    private String username;

    @Email
    private String email;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(columnDefinition = "FLOAT DEFAULT 1.0")
    private Double rating;

    @Length(max = 255)
    private String description;

    @OneToMany
    private List<WorkoutEntity> workouts;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CertificateEntity> certificates;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private List<ProgramEntity> programs;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientEntity> clients;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<ScheduledWorkoutEntity> scheduledWorkouts;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<MealEntity> meals;

}
