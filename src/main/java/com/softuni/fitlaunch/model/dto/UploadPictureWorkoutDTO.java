package com.softuni.fitlaunch.model.dto;

import com.softuni.fitlaunch.validation.anotations.FileAnnotation;
import org.springframework.web.multipart.MultipartFile;

public class UploadPictureWorkoutDTO {

    private long id;

    @FileAnnotation(contentTypes = {"image/png", "image/jpg"})
    private MultipartFile picture;


    public UploadPictureWorkoutDTO() {
    }

    public long getId() {
        return id;
    }

    public UploadPictureWorkoutDTO setId(long id) {
        this.id = id;
        return this;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public UploadPictureWorkoutDTO setPicture(MultipartFile picture) {
        this.picture = picture;
        return this;
    }
}
