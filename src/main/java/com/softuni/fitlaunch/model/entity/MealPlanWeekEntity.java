package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "meal_plan_weeks")
@Getter
@Setter
public class MealPlanWeekEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "week_id")
    private MealPlanEntity mealPlan;

    @OneToMany(mappedBy = "week", cascade = CascadeType.ALL)
    private Set<MealPlanDayEntity> weekDays = new HashSet<>();
}
