package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "images")
@Getter
@Setter
public class ImageEntity extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "meal_id")
    private MealEntity meal;

    @Column
    private String title;

    @Column
    private String url;

}
