package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "meals")
@Getter
@Setter
public class MealEntity extends BaseEntity {

    private String name;
    private String description;
    private String type;

    @ManyToOne
    @JoinColumn(name = "meal_plan_id")
    private MealPlanEntity mealPlan;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private CoachEntity author;

    @Column
    private String image;
}
