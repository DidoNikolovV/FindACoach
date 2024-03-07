package com.softuni.fitlaunch.model.entity;


import com.softuni.fitlaunch.model.enums.LevelEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "programs_weeks_workouts")
@Getter
@Setter
public class ProgramWeekWorkoutEntity extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "program_week_id")
    private ProgramWeekEntity programWeek;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private ProgramEntity program;

    @Column(name = "has_started")
    private boolean hasStarted;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    private List<ProgramWorkoutExerciseEntity> exercises;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long likes;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LevelEnum level;

    @Column(nullable = false)
    private String description;

}
