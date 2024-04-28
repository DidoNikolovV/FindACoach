package com.softuni.fitlaunch.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "daily_weight")
@Getter
@Setter
public class DailyWeightEntity extends BaseEntity {
    private LocalDateTime dateTime;
    private Double weight;

    @ManyToOne
    @JoinColumn(name ="client_id")
    private ClientEntity client;
}
