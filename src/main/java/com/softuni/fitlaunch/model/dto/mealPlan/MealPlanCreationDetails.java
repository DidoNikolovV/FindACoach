package com.softuni.fitlaunch.model.dto.mealPlan;


import com.softuni.fitlaunch.model.entity.MealPlanWeekEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealPlanCreationDetails {
    private Long id;
    private String name;
    private String description;
    private List<MealPlanWeekEntity> weeks;
}
