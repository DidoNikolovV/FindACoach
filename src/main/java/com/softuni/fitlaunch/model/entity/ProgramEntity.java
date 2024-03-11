package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


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

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;

}
