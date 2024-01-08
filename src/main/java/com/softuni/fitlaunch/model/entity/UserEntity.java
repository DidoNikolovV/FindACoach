package com.softuni.fitlaunch.model.entity;

import com.softuni.fitlaunch.model.dto.program.ProgramWeekWorkoutDTO;
import com.softuni.fitlaunch.model.enums.UserTitleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String username;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<UserRoleEntity> roles = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public UserProfileEntity userProfile;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String membership;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<CommentEntity> comments;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "program_workouts_started",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id"))
    private List<ProgramWeekWorkoutEntity> workoutsStarted;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "program_workouts_liked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id"))
    private List<ProgramWeekWorkoutEntity> workoutsLiked;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "program_workout_exercises_completed",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id"))
    private List<ProgramWorkoutExerciseEntity> programExercisesCompleted;


    private boolean activated = false;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "activation_code_expiration")
    private LocalDateTime activationCodeExpiration;

    @Enumerated(EnumType.STRING)
    private UserTitleEnum title;

    @Column(name = "img_url")
    private String imgUrl;

}



