package com.softuni.fitlaunch.model.dto.mealPlan;


import com.softuni.fitlaunch.model.dto.meal.MealDTO;
import com.softuni.fitlaunch.model.entity.MealEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
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
    private List<MealDTO> meals;
}
