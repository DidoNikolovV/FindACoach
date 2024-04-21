package com.softuni.fitlaunch.model.entity;


import com.softuni.fitlaunch.model.enums.DaysEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "meal_plan_week_days")
@Getter
@Setter
public class MealPlanDayEntity extends BaseEntity{

    private String name;

    @ManyToOne
    @JoinColumn(name = "week_id")
    private MealPlanWeekEntity week;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL)
    private Set<MealEntity> meals = new HashSet<>();
}
