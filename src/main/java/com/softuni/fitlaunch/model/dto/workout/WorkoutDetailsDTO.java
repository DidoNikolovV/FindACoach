package com.softuni.fitlaunch.model.dto.workout;

import com.softuni.fitlaunch.model.dto.program.ProgramWorkoutExerciseDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;

import java.util.List;

public class WorkoutDetailsDTO {

    private Long id;

    private String name;

    private String level;

    private String description;

    private String imgUrl;

    private List<ProgramWorkoutExerciseDTO> exercises;

    private Long likes;

    private List<UserDTO> usersLiked;

    private boolean hasStarted;

    private boolean isCompleted;

    public WorkoutDetailsDTO() {
    }

    public WorkoutDetailsDTO(Long id, String name, String level, String description, List<ProgramWorkoutExerciseDTO> exercises, Long likes, boolean hasStarted, boolean isCompleted) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.description = description;
        this.exercises = exercises;
        this.likes = likes;
        this.hasStarted = hasStarted;
        this.isCompleted = isCompleted;
    }

    public Long getId() {
        return id;
    }

    public WorkoutDetailsDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public WorkoutDetailsDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getLevel() {
        return level;
    }

    public WorkoutDetailsDTO setLevel(String level) {
        this.level = level;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public WorkoutDetailsDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public WorkoutDetailsDTO setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public List<ProgramWorkoutExerciseDTO> getExercises() {
        return exercises;
    }

    public WorkoutDetailsDTO setExercises(List<ProgramWorkoutExerciseDTO> exercises) {
        this.exercises = exercises;
        return this;
    }

    public Long getLikes() {
        return likes;
    }

    public WorkoutDetailsDTO setLikes(Long likes) {
        this.likes = likes;
        return this;
    }

    public List<UserDTO> getUsersLiked() {
        return usersLiked;
    }

    public WorkoutDetailsDTO setUsersLiked(List<UserDTO> usersLiked) {
        this.usersLiked = usersLiked;
        return this;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    public WorkoutDetailsDTO setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
        return this;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public WorkoutDetailsDTO setCompleted(boolean completed) {
        isCompleted = completed;
        return this;
    }
}