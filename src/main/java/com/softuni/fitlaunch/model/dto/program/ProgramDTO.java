package com.softuni.fitlaunch.model.dto.program;

import jakarta.validation.constraints.NotNull;

public class ProgramDTO {

    @NotNull
    private Long id;

    @NotNull
    private String imgUrl;

    @NotNull
    private String name;


    public Long getId() {
        return id;
    }

    public ProgramDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public ProgramDTO setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProgramDTO setName(String name) {
        this.name = name;
        return this;
    }
}
