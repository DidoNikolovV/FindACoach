package com.softuni.fitlaunch.model.dto.workout;

import com.softuni.fitlaunch.model.dto.ExerciseDTO;
import com.softuni.fitlaunch.model.enums.LevelEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public class CreateWorkoutDTO {

    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private LevelEnum level;

    @NotEmpty @Size(min = 5, max = 512)
    private String description;


    private MultipartFile imgUrl;

    @NotNull
    private List<ExerciseDTO> exercises;

    private List<Long> selectedExerciseIds;

    private List<Integer> sets;
    private List<Integer> reps;



    public CreateWorkoutDTO() {
    }

    public CreateWorkoutDTO(Long id, String name, LevelEnum level, String description, MultipartFile imgUrl, List<ExerciseDTO> exercises, List<Long> selectedExerciseIds, List<Integer> sets, List<Integer> reps) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.description = description;
        this.imgUrl = imgUrl;
        this.exercises = exercises;
        this.selectedExerciseIds = selectedExerciseIds;
        this.sets = sets;
        this.reps = reps;
    }

    public Long getId() {
        return id;
    }

    public CreateWorkoutDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreateWorkoutDTO setName(String name) {
        this.name = name;
        return this;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public CreateWorkoutDTO setLevel(LevelEnum level) {
        this.level = level;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateWorkoutDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public MultipartFile getImgUrl() {
        return imgUrl;
    }

    public CreateWorkoutDTO setImgUrl(MultipartFile imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public List<ExerciseDTO> getExercises() {
        return exercises;
    }

    public CreateWorkoutDTO setExercises(List<ExerciseDTO> exercises) {
        this.exercises = exercises;
        return this;
    }

    public List<Long> getSelectedExerciseIds() {
        return selectedExerciseIds;
    }

    public CreateWorkoutDTO setSelectedExerciseIds(List<Long> selectedExerciseIds) {
        this.selectedExerciseIds = selectedExerciseIds;
        return this;
    }

    public List<Integer> getSets() {
        return sets;
    }

    public CreateWorkoutDTO setSets(List<Integer> sets) {
        this.sets = sets;
        return this;
    }

    public List<Integer> getReps() {
        return reps;
    }

    public CreateWorkoutDTO setReps(List<Integer> reps) {
        this.reps = reps;
        return this;
    }
}
