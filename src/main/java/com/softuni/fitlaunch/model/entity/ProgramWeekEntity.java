package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "weeks")
public class ProgramWeekEntity extends BaseEntity {

    @Column(name = "number", nullable = false)
    private int number;

    @OneToMany(mappedBy = "week", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DayWorkoutsEntity> days = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "program_id")
    private ProgramEntity program;

}
