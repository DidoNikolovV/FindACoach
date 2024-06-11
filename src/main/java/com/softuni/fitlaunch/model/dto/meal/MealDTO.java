package com.softuni.fitlaunch.model.dto.meal;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MealDTO {
    private Long id;
    private String name;
    private String description;
    private String type;
    private Double protein;
    private Double carbohydrates;
    private Double fats;
    private Double calories;
    private String imageUrl;

}
