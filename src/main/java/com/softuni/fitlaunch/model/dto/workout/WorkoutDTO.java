package com.softuni.fitlaunch.model.dto.workout;

import com.softuni.fitlaunch.model.dto.ExerciseDTO;
import com.softuni.fitlaunch.model.dto.comment.CommentCreationDTO;
import com.softuni.fitlaunch.model.dto.program.ProgramWeekDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.enums.LevelEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;


public class WorkoutDTO {

    @NotNull
    private Long id;

    @NotNull
    private UserDTO author;

    @NotNull
    private String imgUrl;

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private LevelEnum level;

    @NotNull
    private String description;

    @NotNull
    private List<WorkoutExerciseDTO> workoutExercises = new ArrayList<>();

    @NotNull
    private List<CommentCreationDTO> comments;

    @NotNull
    private Integer likes = 0;


    @NotNull
    private List<ExerciseDTO> exercises;


    @NotNull
    private boolean isCompleted = false;

    @NotNull
    private String dateCompleted;

    @NotNull
    private boolean hasStarted = false;

    @NotNull
    private ProgramWeekDTO programWeek;

    public Long getId() {
        return id;
    }

    public WorkoutDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public WorkoutDTO setAuthor(UserDTO author) {
        this.author = author;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public WorkoutDTO setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public String getName() {
        return name;
    }

    public WorkoutDTO setName(String name) {
        this.name = name;
        return this;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public WorkoutDTO setLevel(LevelEnum level) {
        this.level = level;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public WorkoutDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<WorkoutExerciseDTO> getWorkoutExercises() {
        return workoutExercises;
    }

    public WorkoutDTO setWorkoutExercises(List<WorkoutExerciseDTO> workoutExercises) {
        this.workoutExercises = workoutExercises;
        return this;
    }

    public List<CommentCreationDTO> getComments() {
        return comments;
    }

    public WorkoutDTO setComments(List<CommentCreationDTO> comments) {
        this.comments = comments;
        return this;
    }

    public Integer getLikes() {
        return likes;
    }

    public WorkoutDTO setLikes(Integer likes) {
        this.likes = likes;
        return this;
    }

    public List<ExerciseDTO> getExercises() {
        return exercises;
    }

    public WorkoutDTO setExercises(List<ExerciseDTO> exercises) {
        this.exercises = exercises;
        return this;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public WorkoutDTO setCompleted(boolean completed) {
        isCompleted = completed;
        return this;
    }

    public String getDateCompleted() {
        return dateCompleted;
    }

    public WorkoutDTO setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
        return this;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    public WorkoutDTO setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
        return this;
    }

    public ProgramWeekDTO getProgramWeek() {
        return programWeek;
    }

    public WorkoutDTO setProgramWeek(ProgramWeekDTO programWeek) {
        this.programWeek = programWeek;
        return this;
    }
}
