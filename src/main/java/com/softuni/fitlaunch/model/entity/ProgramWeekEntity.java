package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "programs_weeks")
public class ProgramWeekEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "program_id")
    private ProgramEntity program;


    @OneToMany(mappedBy = "programWeek", cascade = CascadeType.ALL)
    private List<ProgramWeekWorkoutEntity> weekWorkouts;
}
