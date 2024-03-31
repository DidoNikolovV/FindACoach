package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.Set;

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

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ImageEntity> image;

}
