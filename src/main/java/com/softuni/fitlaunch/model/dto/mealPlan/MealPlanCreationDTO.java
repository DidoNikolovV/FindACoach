package com.softuni.fitlaunch.model.dto.mealPlan;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MealPlanCreationDTO {

    private String name;
    private String description;
    private List<Long> selectedMealIds;
    private Integer weeks;
}
