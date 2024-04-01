package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "images")
@Getter
@Setter
public class ImageEntity extends BaseEntity {


    @OneToOne(mappedBy = "image")
    private MealEntity meal;

    @Column
    private String title;

    @Column
    private String url;

}
