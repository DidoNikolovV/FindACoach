package com.softuni.fitlaunch.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MealCreationDTO {
    private String name;
    private String description;
    private String type;
}
