package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "meal_plans")
@Getter
@Setter
public class MealPlanEntity extends BaseEntity {

    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "meal-plans_meals",
            joinColumns = @JoinColumn(name = "meal_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id")
    )
    private List<MealEntity> meals = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "coach_id")
    private CoachEntity coach;

    @OneToMany(mappedBy = "mealPlan", cascade = CascadeType.ALL)
    private Set<MealPlanWeekEntity> mealPlanWeeks = new HashSet<>();
}
