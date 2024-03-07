package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class CommentEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity author;

    @ManyToOne
    private ProgramWeekWorkoutEntity workout;

    @ManyToOne
    private ProgramEntity program;

    @ManyToOne
    private ProgramWeekEntity week;

    @Column(nullable = false)
    private String message;

    public Long getId() {
        return id;
    }

    public CommentEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public CommentEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommentEntity setMessage(String message) {
        this.message = message;
        return this;
    }

    public ProgramWeekWorkoutEntity getWorkout() {
        return workout;
    }

    public CommentEntity setWorkout(ProgramWeekWorkoutEntity workout) {
        this.workout = workout;
        return this;
    }

    public ProgramEntity getProgram() {
        return program;
    }

    public CommentEntity setProgram(ProgramEntity program) {
        this.program = program;
        return this;
    }

    public ProgramWeekEntity getWeek() {
        return week;
    }

    public CommentEntity setWeek(ProgramWeekEntity week) {
        this.week = week;
        return this;
    }
}
