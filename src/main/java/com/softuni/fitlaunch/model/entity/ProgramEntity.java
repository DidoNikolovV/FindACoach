package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "programs")
public class ProgramEntity extends BaseEntity {

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private CoachEntity coach;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramWeekEntity> weeks = new ArrayList<>();

}
