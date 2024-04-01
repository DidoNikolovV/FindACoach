package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "meals")
@Getter
@Setter
public class MealEntity extends BaseEntity {


    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String type;

    @Column
    private Double calories;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private CoachEntity author;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private ImageEntity image;

}
