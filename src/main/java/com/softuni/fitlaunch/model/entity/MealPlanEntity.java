package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "meal_plans")
@Getter
@Setter
public class MealPlanEntity extends BaseEntity {

    private String name;
    private String description;

    @OneToMany(mappedBy = "mealPlan", cascade = CascadeType.ALL)
    private List<MealEntity> meals;
}
