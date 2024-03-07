package com.softuni.fitlaunch.model.dto.program;

import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public class ProgramWeekWorkoutDTO {

    private Long id;
    @NotNull
    private String name;

    private Long programWeekId;

    @NotNull
    private boolean hasStarted;

    @NotNull
    private boolean isCompleted;

    private List<CommentCreationDTO> comments;

    private List<ProgramWorkoutExerciseDTO> exercises;

    private Long likes;

    private String level;

    @NotNull
    private String description;


    public Long getId() {
        return id;
    }

    public ProgramWeekWorkoutDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProgramWeekWorkoutDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Long getProgramWeekId() {
        return programWeekId;
    }

    public ProgramWeekWorkoutDTO setProgramWeekId(Long programWeekId) {
        this.programWeekId = programWeekId;
        return this;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    public ProgramWeekWorkoutDTO setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
        return this;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public ProgramWeekWorkoutDTO setCompleted(boolean completed) {
        isCompleted = completed;
        return this;
    }

    public List<CommentCreationDTO> getComments() {
        return comments;
    }

    public ProgramWeekWorkoutDTO setComments(List<CommentCreationDTO> comments) {
        this.comments = comments;
        return this;
    }

    public List<ProgramWorkoutExerciseDTO> getExercises() {
        return exercises;
    }

    public ProgramWeekWorkoutDTO setExercises(List<ProgramWorkoutExerciseDTO> exercises) {
        this.exercises = exercises;
        return this;
    }

    public Long getLikes() {
        return likes;
    }

    public ProgramWeekWorkoutDTO setLikes(Long likes) {
        this.likes = likes;
        return this;
    }

    public String getLevel() {
        return level;
    }

    public ProgramWeekWorkoutDTO setLevel(String level) {
        this.level = level;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProgramWeekWorkoutDTO setDescription(String description) {
        this.description = description;
        return this;
    }
}
