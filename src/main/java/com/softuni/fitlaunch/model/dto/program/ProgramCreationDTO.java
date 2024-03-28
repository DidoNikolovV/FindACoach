package com.softuni.fitlaunch.model.dto.program;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProgramCreationDTO {

    private String name;

    private String description;

    private Long weeks;
}
